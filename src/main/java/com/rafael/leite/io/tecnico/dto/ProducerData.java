package com.rafael.leite.io.tecnico.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProducerData {
    private List<ProducerDTO> min;
    private List<ProducerDTO> max;
}
