package ru.sks.service;

import ru.sks.exception.FileHandlingException;
import ru.sks.model.Sock;
import ru.sks.repository.SockRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchUploadService {
    private final SockRepository repository;

    @Transactional
    public void uploadBatch(MultipartFile file) throws FileHandlingException{
        try (InputStream is = file.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);
            List<CSVRecord> records = csvParser.getRecords();
            for (CSVRecord record : records) {
                String color = record.get("color");
                int cottonPart = Integer.parseInt(record.get("cotton_part"));
                int quantity = Integer.parseInt(record.get("quantity"));
                Sock sock = new Sock(null, color, cottonPart, quantity);
                repository.save(sock);
            }
        } catch (IOException e) {
            throw new FileHandlingException(e.getMessage());
        }
    }
}
