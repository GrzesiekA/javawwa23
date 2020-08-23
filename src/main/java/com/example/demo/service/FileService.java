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
import java.math.BigInteger;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                        parseFromString(BigDecimal.ZERO, table[1], BigDecimal::new),
                        parseFromString(BigDecimal.ZERO, table[3], BigDecimal::new),
                        parseFromString(BigDecimal.ZERO, table[5], BigDecimal::new),
                        getDate(table[7]),
                        parseFromString(BigInteger.ONE, table[2], BigInteger::new),
                        parseFromString(BigInteger.ONE, table[4], BigInteger::new),
                        parseFromString(BigInteger.ONE, table[6], BigInteger::new)
                ))
                .collect(Collectors.toList());
    }

//    public List<PricePerDay> readPrices2() throws IOException {
//        List<String> strings = Files.readAllLines(resourceFile.getFile().toPath());
//        return strings.stream()
//                .filter(line -> line != null)
//                .map(line -> line.split(","))
//                .map(table -> new PricePerDay(
//                        table[0],
//                        parseFromString2(BigDecimal.ZERO, () -> new BigDecimal(table[1])),
//                        parseFromString(BigDecimal.ZERO, () -> new BigDecimal(table[3])),
//                        parseFromString(BigDecimal.ZERO, () -> new BigDecimal(table[5])),
//                        getDate(table[7]),
//                        parseFromString(BigInteger.ONE, table[2], BigInteger::new),
//                        parseFromString(table[4]),
//                        parseFromString(table[6])
//                ))
//                .collect(Collectors.toList());
//    }


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

    public Map<String, Optional<PricePerDay>> lowerPriceForMonth() throws IOException {
        return readPrices().stream()
                .filter(p -> p.getLowQualityPrice() != null)
                .collect(Collectors.groupingBy(p -> p.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Collectors.minBy(Comparator.comparing(PricePerDay::getLowQualityPrice))));

    }

//    public BigDecimal sumLowPrices() throws IOException {
//        return readPrices().stream().
//                flatMap(p -> Stream.of(
//                        p.getHighQualityPrice().multiply(new BigDecimal(p.getHighCount())),
//                        p.getMidQualityPrice().multiply(new BigDecimal(p.getMidCount())),
//                        p.getLowQualityPrice().multiply(new BigDecimal(p.getLowCount()))
//                ))
//                .reduce(BigDecimal.ZERO,
//                        BigDecimal::add);
//    }

    public BigDecimal sumLowPrices() throws IOException {

        return readPrices().stream().
                flatMap(p -> Stream.of(
                        p.getHighQualityPrice().multiply(new BigDecimal(p.getHighCount())),
                        p.getMidQualityPrice().multiply(new BigDecimal(p.getMidCount())),
                        p.getLowQualityPrice().multiply(new BigDecimal(p.getLowCount()))
                ))
                .reduce(BigDecimal.ZERO,
                        BigDecimal::add);
    }

    public Map<LocalDate, BigDecimal> sumAllPricesPerDay() throws IOException {

        return readPrices().stream()
                .collect(Collectors.groupingBy(
                        PricePerDay::getDate,
                        Collectors.flatMapping(
                                p -> Stream.of(
                                        p.getHighQualityPrice().multiply(new BigDecimal(p.getHighCount())),
                                        p.getMidQualityPrice().multiply(new BigDecimal(p.getMidCount())),
                                        p.getLowQualityPrice().multiply(new BigDecimal(p.getLowCount()))
                                ),
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));
    }


//    public List<PricePerDay> readPrices() throws IOException {
//        CsvMapper mapper = new CsvMapper();
//        CsvSchema schema = mapper.schemaFor(PricePerDay.class);
//
//        mapper.registerModule(new JavaTimeModule());
//
//        MappingIterator<PricePerDay> it = mapper.readerFor(PricePerDay.class).with(schema)
//                .readValues(fileContent());
//
//        return it.readAll();
//    }


    private <T extends Number> T parseFromString(T defaultValue, String s, Function<String, T> creator) {
        if (s == null) return defaultValue;

        try {
            return creator.apply(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private <T extends Number> T parseFromString2(T defaultValue, Supplier<T> supplier) {

        try {
            return supplier.get();
        } catch (NumberFormatException | NullPointerException e) {
            return defaultValue;
        }
    }

    /*zamiana na metodę parseFromString*/
//    private BigDecimal getPriceBigDecimal(String t) {
//        if (t == null) {
//            return BigDecimal.ZERO;
//        }
//
//        try {
//            return new BigDecimal(t);
//        } catch (NumberFormatException e) {
//            return BigDecimal.ZERO;
//        }
//    }
//
//    private Integer getCount(String t) {
//        if (t == null) {
//            return 1;
//        }
//
//        try {
//            return Integer.parseInt(t);
//        } catch (NumberFormatException e) {
//            return 1;
//        }
//    }

    private LocalDate getDate(String s) {
        if (s == null) {
            return null;
        }
        return LocalDate.parse(s);
    }
}


