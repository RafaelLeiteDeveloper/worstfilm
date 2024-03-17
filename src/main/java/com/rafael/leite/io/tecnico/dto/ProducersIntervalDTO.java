package com.rafael.leite.io.tecnico.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProducersIntervalDTO {
    private Integer interval;
    private Integer previousWin;
    private Integer followingWin;

}
