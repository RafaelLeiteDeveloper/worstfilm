package com.rafael.leite.io.tecnico.application.processor;

import com.rafael.leite.io.tecnico.dto.ProducerData;
import com.rafael.leite.io.tecnico.port.application.processor.MovieProcessorImpl;
import com.rafael.leite.io.tecnico.port.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MovieProcessorImplIntegrationTest {

    @Autowired
    private MovieProcessorImpl movieProcessor;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void testFindMinAndMax() {
        ProducerData producerData = movieProcessor.findMinAndMax();
        assertNotNull(producerData);
    }
}
