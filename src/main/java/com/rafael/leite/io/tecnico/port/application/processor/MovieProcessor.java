package com.rafael.leite.io.tecnico.port.application.processor;

import com.rafael.leite.io.tecnico.dto.ProducerData;

public interface MovieProcessor {
    ProducerData findMinAndMax();
}
