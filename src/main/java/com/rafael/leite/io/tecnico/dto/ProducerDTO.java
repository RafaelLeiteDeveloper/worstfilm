package com.rafael.leite.io.tecnico.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProducerDTO {
    String producer;
    int interval;
    int previousWin;
    int followingWin;
}
