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

        Integer[] maxDistance = {0};
        Integer[] startYear = {0};
        Integer[] endYear = {0};

        years.stream()
                .reduce((prevYear, currYear) -> {
                    int distance = currYear - prevYear;
                    if (distance > maxDistance[0]) {
                        maxDistance[0] = distance;
                        startYear[0] = prevYear;
                        endYear[0] = currYear;
                    }
                    return currYear;
                });

        producer.setInterval(maxDistance[0]);
        producer.setPreviousWin(startYear[0]);
        producer.setFollowingWin(endYear[0]);
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
        producersMap.forEach((producer, years) -> updatedFilms.add(new ProducersYearsDTO(producer, years, 0, 0, 0)));

        return updatedFilms;
    }

    public ProducerData printUpdatedFilmsList(List<ProducersYearsDTO> films) {
        List<ProducersYearsDTO> maxDistanceDTOS = this.maxProducer(films);
        List<ProducersYearsDTO> minDistanceDTOS = this.minProducers(films);

        return new ProducerData(
                minDistanceDTOS.stream()
                        .map(producer -> new ProducerDTO(producer.getProducers(), producer.getInterval(), producer.getPreviousWin(), producer.getFollowingWin()))
                        .collect(Collectors.toList()),
                maxDistanceDTOS.stream()
                        .map(producer -> new ProducerDTO(producer.getProducers(), producer.getInterval(), producer.getPreviousWin(), producer.getFollowingWin()))
                        .collect(Collectors.toList())
        );
    }

    public List<ProducersYearsDTO> maxProducer(List<ProducersYearsDTO> films){
        Integer maxMaxDistance = films.stream()
                .filter(film -> film.getYears().size() > 1)
                .mapToInt(ProducersYearsDTO::getInterval)
                .max()
                .orElseThrow(null);

        return films.stream()
                .filter(film -> film.getYears().size() > 1 && Objects.equals(film.getInterval(), maxMaxDistance))
                .collect(Collectors.toList());
    }

    public List<ProducersYearsDTO> minProducers(List<ProducersYearsDTO> films){
        Integer minMaxDistance = films.stream()
                .filter(film -> film.getYears().size() > 1)
                .mapToInt(ProducersYearsDTO::getInterval)
                .min()
                .orElseThrow(null);

        return films.stream()
                .filter(film -> film.getYears().size() > 1 && Objects.equals(film.getInterval(), minMaxDistance))
                .collect(Collectors.toList());
    }
}
