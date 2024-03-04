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
    private static final Integer MIN_INTERVAL = 1;

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
            if (years.size() > MIN_INTERVAL) {
                calculateMaxDistanceForProducer(years, producer);
            }
        }
    }

    public void calculateMaxDistanceForProducer(List<Integer> years, ProducersYearsDTO producer) {
        years.sort(Integer::compareTo);

        Integer[] minDistance = {Integer.MAX_VALUE};
        Integer[] maxDistance = {Integer.MIN_VALUE};
        Integer[] minStartYear = {0};
        Integer[] minEndYear = {0};
        Integer[] maxStartYear = {0};
        Integer[] maxEndYear = {0};

        years.stream()
                .reduce((prevYear, currYear) -> {
                    int distance = currYear - prevYear;

                    if (distance < minDistance[0]) {
                        minDistance[0] = distance;
                        minStartYear[0] = prevYear;
                        minEndYear[0] = currYear;
                    }

                    if (distance > maxDistance[0]) {
                        maxDistance[0] = distance;
                        maxStartYear[0] = prevYear;
                        maxEndYear[0] = currYear;
                    }

                    return currYear;
                });

        producer.setIntervalMax(maxDistance[0]);
        producer.setPreviousWinMax(maxStartYear[0]);
        producer.setFollowingWinMax(maxEndYear[0]);

        producer.setIntervalMin(minDistance[0]);
        producer.setPreviousWinMin(minStartYear[0]);
        producer.setFollowingWinMin(minEndYear[0]);
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
                .filter(film -> film.getYears().size() > MIN_INTERVAL)
                .mapToInt(ProducersYearsDTO::getIntervalMax)
                .max()
                .orElseThrow(null);

        return films.stream()
                .filter(film -> film.getYears().size() > MIN_INTERVAL && Objects.equals(film.getIntervalMax(), maxMaxDistance))
                .collect(Collectors.toList());
    }

    public List<ProducersYearsDTO> minProducers(List<ProducersYearsDTO> films){
        Integer minMaxDistance = films.stream()
                .filter(film -> film.getYears().size() > MIN_INTERVAL)
                .mapToInt(ProducersYearsDTO::getIntervalMin)
                .min()
                .orElseThrow(null);

        return films.stream()
                .filter(film -> film.getYears().size() > MIN_INTERVAL && Objects.equals(film.getIntervalMin(), minMaxDistance))
                .collect(Collectors.toList());
    }
}
