package com.rafael.leite.io.tecnico.adapter;

import com.rafael.leite.io.tecnico.dto.ProducerData;
import com.rafael.leite.io.tecnico.port.application.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = { RequestMethod.GET }, maxAge = 3600)
public class MoviesControllerAdapter implements MovieUiPort {

   private final MovieService movieService;

    @Override
    public ResponseEntity<ProducerData> getWinningProducersMinMaxIntervalRange() {
        return ResponseEntity.status(HttpStatus.OK).body(movieService.getWinningProducersMinMaxIntervalRange());
    }
}
