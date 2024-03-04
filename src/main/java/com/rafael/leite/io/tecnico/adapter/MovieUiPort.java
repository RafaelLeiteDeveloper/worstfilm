package com.rafael.leite.io.tecnico.adapter;

import com.rafael.leite.io.tecnico.dto.ProducerData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface MovieUiPort {

    @GetMapping(path = "/search/winners/interval/min/max")
    ResponseEntity<ProducerData> getWinningProducersMinMaxIntervalRange();
}
