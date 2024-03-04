package com.rafael.leite.io.tecnico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProducersYearsDTO {
    private String producers;
    private List<Integer> years;
    private Integer interval;
    private Integer previousWin;
    private Integer followingWin;
}
