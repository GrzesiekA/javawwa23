package com.example.demo.service;

import com.example.demo.entity.PricePerDay;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
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


    public PricePerDay statisticsSecondVersion() throws IOException {
        return readPrices()
                .stream()
                .sorted(new Comparator<PricePerDay>() {
                    @Override
                    public int compare(PricePerDay t1, PricePerDay t2) {
                        return t1.getMidQualityPrice().compareTo(t2.getMidQualityPrice());
                    }
                })
                .findFirst()
                .get();
    }

    public PricePerDay statisticsThirdVersion() throws IOException {
        return readPrices()
                .stream()
                .sorted((t1, t2) -> t1.getMidQualityPrice().compareTo(t2.getMidQualityPrice()))
                .findFirst()
                .get();
    }

    public PricePerDay statisticsFourthVersion() throws IOException {
        return readPrices()
                .stream()
                .sorted(Comparator.comparing(PricePerDay::getMidQualityPrice))
                .findFirst()
                .get();
    }


    public Map<String, Optional<PricePerDay>> statisticsForCountry() throws IOException {
        return readPrices().stream()
                .collect(Collectors.groupingBy(PricePerDay::getState,
                        Collectors.minBy(Comparator.comparing(PricePerDay::getMidQualityPrice))));
    }


    public Map<LocalDate, Optional<PricePerDay>> lowerPriceForDay() throws IOException {
        return readPrices().stream()
                .filter(p -> p.getLowQualityPrice() != null)
                .collect(Collectors.groupingBy(PricePerDay::getDate,
                        Collectors.minBy(Comparator.comparing(PricePerDay::getLowQualityPrice))));
    }

//    public Map<LocalDate, Optional<PricePerDay>> lowerPriceForMonth() throws IOException {
//        return readPrices().stream()
//                .filter(p -> p.getLowQualityPrice() != null)
//                .collect(Collectors.groupingBy(PricePerDay::getDate,
//                        Collectors.minBy(Comparator.comparing(PricePerDay::getLowQualityPrice))))
//                .
//
//    }


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
}


