package com.rafael.leite.io.tecnico.port.repository;

import com.rafael.leite.io.tecnico.domain.Films;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Films, String> {

    @Query(value = "SELECT * FROM FILMS WHERE IS_WINNER = TRUE", nativeQuery = true)
    List<Films> findAllIsWinner();
}
