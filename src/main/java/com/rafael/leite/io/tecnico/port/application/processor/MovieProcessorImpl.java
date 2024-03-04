package com.rafael.leite.io.tecnico.port.application.processor;

import com.rafael.leite.io.tecnico.domain.Films;
import com.rafael.leite.io.tecnico.dto.ProducerDTO;
import com.rafael.leite.io.tecnico.dto.ProducerData;
import com.rafael.leite.io.tecnico.dto.ProducersYearsDTO;
import com.rafael.leite.io.tecnico.port.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MovieProcessorImpl implements MovieProcessor {

    private final MovieRepository movieRepository;

    @Override
    public ProducerData findMinAndMax() {
        List<Films> films = movieRepository.findAllIsWinner();
        List<ProducersYearsDTO> updatedFilms = this.separateFilmsWithMultipleProducers(films);
        this.calculateMaxDistanceEachProducer(updatedFilms);
        return this.printUpdatedFilmsList(updatedFilms);
    }


    public List<ProducersYearsDTO> separateFilmsWithMultipleProducers(List<Films> films) {
        Map<String, List<Integer>> producersMap = this.createProducersMap(films);
        return createProducersYearsDTOList(producersMap);
    }

    public void calculateMaxDistanceEachProducer(List<ProducersYearsDTO> films) {
        for (ProducersYearsDTO producer : films) {
            List<Integer> years = producer.getYears();
            if (years.size() > 1) {
                calculateMaxDistanceForProducer(years, producer);
            }
        }
    }

    public void calculateMaxDistanceForProducer(List<Integer> years, ProducersYearsDTO producer) {
        years.sort(Integer::compareTo);

        Integer minDistance = Integer.MAX_VALUE; // Inicializa com o maior valor possível
        Integer maxDistance = Integer.MIN_VALUE; // Inicializa com o menor valor possível
        Integer minStartYear = 0;
        Integer minEndYear = 0;
        Integer maxStartYear = 0;
        Integer maxEndYear = 0;

        for (int i = 1; i < years.size(); i++) {
            int prevYear = years.get(i - 1);
            int currYear = years.get(i);
            int distance = currYear - prevYear;

            if (distance < minDistance) {
                minDistance = distance;
                minStartYear = prevYear;
                minEndYear = currYear;
            }

            if (distance > maxDistance) {
                maxDistance = distance;
                maxStartYear = prevYear;
                maxEndYear = currYear;
            }
        }

        producer.setIntervalMax(maxDistance);
        producer.setPreviousWinMax(maxStartYear);
        producer.setFollowingWinMax(maxEndYear);

        producer.setIntervalMin(minDistance);
        producer.setPreviousWinMin(minStartYear);
        producer.setFollowingWinMin(minEndYear);
    }


    public  Map<String, List<Integer>> createProducersMap(List<Films> films) {
        Map<String, List<Integer>> producersMap = new HashMap<>();

        films.forEach(film -> {
            String[] producers = film.getProducers().split(", | and |, and ");
            for (String producer : producers) {
                String producerName = producer.trim().replace("and", "").trim();
                producersMap.computeIfAbsent(producerName, k -> new ArrayList<>()).add(film.getYear());
            }
        });

        return producersMap;
    }

    public  List<ProducersYearsDTO> createProducersYearsDTOList(Map<String, List<Integer>> producersMap) {
        List<ProducersYearsDTO> updatedFilms = new ArrayList<>();
        producersMap.forEach((producer, years) -> updatedFilms.add(new ProducersYearsDTO(producer, years, 0, 0, 0, 0, 0, 0)));

        return updatedFilms;
    }

    public ProducerData printUpdatedFilmsList(List<ProducersYearsDTO> films) {
        List<ProducersYearsDTO> maxDistanceDTOS = this.maxProducer(films);
        List<ProducersYearsDTO> minDistanceDTOS = this.minProducers(films);

        return new ProducerData(
                minDistanceDTOS.stream()
                        .map(producer -> new ProducerDTO(producer.getProducers(), producer.getIntervalMin(), producer.getPreviousWinMin(), producer.getFollowingWinMin()))
                        .collect(Collectors.toList()),
                maxDistanceDTOS.stream()
                        .map(producer -> new ProducerDTO(producer.getProducers(), producer.getIntervalMax(), producer.getPreviousWinMax(), producer.getFollowingWinMax()))
                        .collect(Collectors.toList())
        );
    }

    public List<ProducersYearsDTO> maxProducer(List<ProducersYearsDTO> films){
        Integer maxMaxDistance = films.stream()
                .filter(film -> film.getYears().size() > 1)
                .mapToInt(ProducersYearsDTO::getIntervalMax)
                .max()
                .orElseThrow(null);

        return films.stream()
                .filter(film -> film.getYears().size() > 1 && Objects.equals(film.getIntervalMax(), maxMaxDistance))
                .collect(Collectors.toList());
    }

    public List<ProducersYearsDTO> minProducers(List<ProducersYearsDTO> films){
        Integer minMaxDistance = films.stream()
                .filter(film -> film.getYears().size() > 1)
                .mapToInt(ProducersYearsDTO::getIntervalMin)
                .min()
                .orElseThrow(null);

        return films.stream()
                .filter(film -> film.getYears().size() >= 1 && Objects.equals(film.getIntervalMin(), minMaxDistance))
                .collect(Collectors.toList());
    }
}
