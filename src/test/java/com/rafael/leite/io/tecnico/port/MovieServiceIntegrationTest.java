package com.rafael.leite.io.tecnico.port;

import com.rafael.leite.io.tecnico.ApiApplication;
import com.rafael.leite.io.tecnico.dto.ProducerDTO;
import com.rafael.leite.io.tecnico.dto.ProducerData;
import com.rafael.leite.io.tecnico.port.application.MovieService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ApiApplication.class)
@Transactional
public class MovieServiceIntegrationTest {

    @Autowired
    MovieService filmService;

    @Test
    public void testFinalImportedData() {
        ProducerDTO dtoMin = ProducerDTO.builder().producer("Joel Silver").interval(1).followingWin(1991).previousWin(1990).build();
        ProducerDTO dtoMax = ProducerDTO.builder().producer("Matthew Vaughn").interval(13).followingWin(2015).previousWin(2002).build();

        ProducerData producerResourceFromDataBase = filmService.getWinningProducersMinMaxIntervalRange();

        assertEquals(producerResourceFromDataBase.getMin().get(0).getProducer(), dtoMin.getProducer());
        assertEquals(producerResourceFromDataBase.getMin().get(0).getInterval(), dtoMin.getInterval());
        assertEquals(producerResourceFromDataBase.getMin().get(0).getPreviousWin(), dtoMin.getPreviousWin());
        assertEquals(producerResourceFromDataBase.getMin().get(0).getFollowingWin(), dtoMin.getFollowingWin());

        assertEquals(producerResourceFromDataBase.getMax().get(0).getProducer(), dtoMax.getProducer());
        assertEquals(producerResourceFromDataBase.getMax().get(0).getInterval(), dtoMax.getInterval());
        assertEquals(producerResourceFromDataBase.getMax().get(0).getPreviousWin(), dtoMax.getPreviousWin());
        assertEquals(producerResourceFromDataBase.getMax().get(0).getFollowingWin(), dtoMax.getFollowingWin());
    }

}