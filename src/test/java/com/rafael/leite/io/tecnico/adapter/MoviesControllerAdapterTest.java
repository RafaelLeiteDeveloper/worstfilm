package com.rafael.leite.io.tecnico.adapter;

import com.rafael.leite.io.tecnico.dto.ProducerDTO;
import com.rafael.leite.io.tecnico.dto.ProducerData;
import com.rafael.leite.io.tecnico.port.application.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class MoviesControllerAdapterTest {

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MoviesControllerAdapter moviesControllerAdapter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindWinnersIntervallMinMax() {
        ProducerDTO minProducer1 = new ProducerDTO("Joel Silver", 1, 1990, 1991);
        ProducerDTO maxProducer1 = new ProducerDTO("Matthew Vaughn", 13, 2002, 2015);
        List<ProducerDTO> minProducers = Collections.singletonList(minProducer1);
        List<ProducerDTO> maxProducers = Collections.singletonList(maxProducer1);
        ProducerData expectedProducerData = new ProducerData(minProducers, maxProducers);
        ResponseEntity<ProducerData> expectedResponseEntity = ResponseEntity.status(HttpStatus.OK).body(expectedProducerData);
        when(movieService.getWinningProducersMinMaxIntervalRange()).thenReturn(expectedProducerData);
        ResponseEntity<ProducerData> actualResponseEntity = moviesControllerAdapter.getWinningProducersMinMaxIntervalRange();
        assertEquals(expectedResponseEntity.getStatusCode(), actualResponseEntity.getStatusCode());
        assertEquals(expectedResponseEntity.getBody(), actualResponseEntity.getBody());
    }
}
