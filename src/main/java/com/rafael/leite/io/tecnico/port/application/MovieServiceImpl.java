package com.rafael.leite.io.tecnico.port.application;

import com.rafael.leite.io.tecnico.dto.ProducerData;
import com.rafael.leite.io.tecnico.port.application.processor.MovieProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieProcessor movieProcessor;

    @Override
    public ProducerData getWinningProducersMinMaxIntervalRange() {
        return movieProcessor.findMinAndMax();
    }

}
