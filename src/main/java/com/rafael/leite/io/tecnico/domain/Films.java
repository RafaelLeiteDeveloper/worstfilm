package com.rafael.leite.io.tecnico.domain;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "Films")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Films {

    @Id
    @Column(name = "id", length = 23, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "year_")
    private Integer year;
    private String producers;
    private Boolean isWinner;
    private String title;
    private String studios;

}
