package com.rafael.leite.io.tecnico.port.application.file;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.rafael.leite.io.tecnico.domain.Films;
import com.rafael.leite.io.tecnico.port.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import java.io.FileReader;
import java.io.IOException;

import static com.rafael.leite.io.tecnico.port.application.Messages.HEADER_IS_NOT_NULL;
import static java.util.Objects.requireNonNull;

@Log4j2
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "upload.database", havingValue = "csv")
public class FileLoadCsv implements FileLoad {

    private final MovieRepository filmRepository;

    @Value("${file.name}")
    private String filename;

    @Override
    public void runDatabaseLoad() {
        log.info("Start load file...");
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filename))
                .withCSVParser(new CSVParserBuilder().withSeparator(';')
                                                     .build())
                .build()) {

            String[] header = reader.readNext();
            requireNonNull(header, HEADER_IS_NOT_NULL);
            reader.forEach(this::saveFile);

        log.info("process load file end.");

        } catch (IOException | CsvValidationException e) {
            log.error("File cannot be processed");
            throw new RuntimeException(e);
        }
    }

    private void saveFile(String[] nextLine){
        this.filmRepository.save(
                Films.builder()
                .year(Integer.parseInt(nextLine[0]))
                .title(nextLine[1])
                .studios(nextLine[2])
                .producers(nextLine[3])
                .isWinner("yes".equals(nextLine[4]))
                .build()
        );
    }

}