package com.example.demo.service;

import com.example.demo.entity.PricePerDay;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {

    private final Resource resourceFile;

    public FileService(@Value("classpath:marijuana-street-price-clean.csv") Resource resourceFile) {
        this.resourceFile = resourceFile;
    }

    public String fileContent() throws IOException {
        return Files.readString(resourceFile.getFile().toPath());
    }

    public List<PricePerDay> readPrices() throws IOException {
        List<String> strings = Files.readAllLines(resourceFile.getFile().toPath());
        return strings.stream()
                .filter(line -> line != null)
                .map(line -> line.split(","))
                .map(table -> new PricePerDay(
                        table[0],
                        getPriceBigDecimal(table[1]),
                        getPriceBigDecimal(table[3]),
                        getPriceBigDecimal(table[5]),
                        getDate(table[7])
                ))
                .collect(Collectors.toList());
    }

    private BigDecimal getPriceBigDecimal(String t) {
        if (t == null) {
            return null;
        }

        try {
            return new BigDecimal(t);
        } catch (NumberFormatException e) {
            return null;
        }

    }

    private LocalDate getDate(String s) {
        if (s == null) {
            return null;
        }
        return LocalDate.parse(s);
    }

    public PricePerDay statistics() throws IOException {
        return readPrices()
                .stream()
                .sorted(new PriceComparator())
                .findFirst()
                .get();
    }

    public class PriceComparator implements Comparator<PricePerDay> {

        @Override
        public int compare(PricePerDay t1, PricePerDay t2) {
            return (t1.getMidQualityPrice().compareTo(t2.getMidQualityPrice()));
        }
    }

//    public List<PricePerDay> readPrices2() throws IOException {
//        CsvMapper mapper = new CsvMapper();
////        CsvSchema schema = mapper.schemaFor(PricePerDay.class);
//
//        CsvSchema schema = CsvSchema.builder()
//                .addColumn("state")
//                .addColumn("highQualityPrice")
//                .addColumn("")
//                .addColumn("midQualityPrice")
//                .addColumn(Column.)
//                .addColumn("lowQualityPrice")
//                .addColumn("")
//                .addColumn("date")
//                .build();
//
//        mapper.registerModule(new JavaTimeModule());
//
//        MappingIterator<PricePerDay> it = mapper.readerFor(PricePerDay.class).with(schema)
//                .readValues(fileContent());
//
//        return it.readAll();
//    }


}


