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

    private Integer intervalMax;
    private Integer previousWinMax;
    private Integer followingWinMax;

    private Integer intervalMin;
    private Integer previousWinMin;
    private Integer followingWinMin;
}
