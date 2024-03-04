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

        Assertions.assertThat(producerResourceFromDataBase.getMin().get(0).getProducer()).isEqualTo(dtoMin.getProducer());
        Assertions.assertThat(producerResourceFromDataBase.getMax().get(0).getProducer()).isEqualTo(dtoMax.getProducer());
    }

}