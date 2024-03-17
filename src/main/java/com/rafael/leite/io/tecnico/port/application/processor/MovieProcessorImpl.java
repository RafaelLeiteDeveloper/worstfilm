package com.rafael.leite.io.tecnico.port.application.processor;

import com.rafael.leite.io.tecnico.domain.Films;
import com.rafael.leite.io.tecnico.dto.ProducerDTO;
import com.rafael.leite.io.tecnico.dto.ProducerData;
import com.rafael.leite.io.tecnico.dto.ProducersIntervalDTO;
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

        for (int i = 0; i < years.size() - 1; i++) {

            int currentYear = years.get(i);
            int nextYear = years.get(i + 1);
            int interval = nextYear - currentYear;

            producer.getInterval().add(ProducersIntervalDTO.builder().
                    interval(interval).
                    followingWin(nextYear).
                    previousWin(currentYear).
                    build());
        }

        producer.setIntervalMax(getMaxInterval(producer.getInterval()));
        producer.setIntervalMin(getMinInterval(producer.getInterval()));
    }


    public List<ProducersIntervalDTO> getExtremeInterval(List<ProducersIntervalDTO> producers, Comparator<Integer> comparator) {
        Optional<Integer> extremeInterval = producers.stream()
                .map(ProducersIntervalDTO::getInterval)
                .min(comparator);

        if (extremeInterval.isEmpty()) {
            return Collections.emptyList();
        }

        final Integer extremeValue = extremeInterval.get();

        return producers.stream()
                .filter(p -> p.getInterval().equals(extremeValue))
                .collect(Collectors.toList());
    }

    public List<ProducersIntervalDTO> getMaxInterval(List<ProducersIntervalDTO> producers) {
        return getExtremeInterval(producers, Comparator.reverseOrder());
    }

    public List<ProducersIntervalDTO> getMinInterval(List<ProducersIntervalDTO> producers) {
        return getExtremeInterval(producers, Comparator.naturalOrder());
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
        producersMap.forEach((producer, years) -> updatedFilms.add(new ProducersYearsDTO(producer, years, new ArrayList<>(), new ArrayList<>(),new ArrayList<>())));

        return updatedFilms;
    }

    public ProducerData printUpdatedFilmsList(List<ProducersYearsDTO> films) {
        List<ProducerDTO> maxDistanceDTOS = new ArrayList<>();
        List<ProducerDTO> minDistanceDTOS = new ArrayList<>();;

        films.forEach(obj -> {
            obj.getIntervalMax().forEach(min ->{
                maxDistanceDTOS.add(ProducerDTO.builder().
                        producer(obj.getProducers()).
                        interval(min.getInterval()).
                        followingWin(min.getFollowingWin()).
                        previousWin(min.getPreviousWin()).
                        build());
            });
        });

        films.forEach(obj -> {
            obj.getIntervalMin().forEach(min ->{
                minDistanceDTOS.add(ProducerDTO.builder().
                        producer(obj.getProducers()).
                        interval(min.getInterval()).
                        followingWin(min.getFollowingWin()).
                        previousWin(min.getPreviousWin()).
                        build());
            });
        });

        return new ProducerData(getProducersWithMinInterval(minDistanceDTOS),getProducersWithMaxInterval(maxDistanceDTOS));
    }

    public List<ProducerDTO> getProducersWithMinInterval(List<ProducerDTO> producersList) {
        int minInterval = findMinInterval(producersList);
        return filterProducersByInterval(producersList, minInterval);
    }

    public List<ProducerDTO> getProducersWithMaxInterval(List<ProducerDTO> producersList) {
        int maxInterval = findMaxInterval(producersList);
        return filterProducersByInterval(producersList, maxInterval);
    }

    private int findMinInterval(List<ProducerDTO> producersList) {
        int min = Integer.MAX_VALUE;
        for (ProducerDTO producer : producersList) {
            min = Math.min(min, producer.getInterval());
        }
        return min;
    }

    private int findMaxInterval(List<ProducerDTO> producersList) {
        int max = Integer.MIN_VALUE;
        for (ProducerDTO producer : producersList) {
            max = Math.max(max, producer.getInterval());
        }
        return max;
    }

    private List<ProducerDTO> filterProducersByInterval(List<ProducerDTO> producersList, int interval) {
        List<ProducerDTO> filteredProducers = new ArrayList<>();
        for (ProducerDTO producer : producersList) {
            if (producer.getInterval() == interval) {
                filteredProducers.add(producer);
            }
        }
        return filteredProducers;
    }

}
