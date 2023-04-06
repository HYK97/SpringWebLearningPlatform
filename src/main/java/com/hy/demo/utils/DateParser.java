package com.hy.demo.utils;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Component
public class DateParser {


    public DateParser() {

    }

    private LocalDate toLocalDate(String currentDate) {
        return LocalDate.parse(currentDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }


    public Timestamp startDate(String currentDate) {
        return Timestamp.valueOf(LocalDateTime.of(toLocalDate(currentDate), LocalTime.of(0, 0, 0)));

    }

    public Timestamp endDate(String currentDate) {
        return Timestamp.valueOf(LocalDateTime.of(toLocalDate(currentDate), LocalTime.of(23, 59, 59)));
    }


    public Timestamp startMonth(String currentDate) {
        return Timestamp.valueOf(LocalDateTime.of(toLocalDate(currentDate).getYear(), toLocalDate(currentDate).getMonth(), 1, 0, 0, 0));
    }

    public Timestamp endMonth(String currentDate) {
        return Timestamp.valueOf(LocalDateTime.of(toLocalDate(currentDate).getYear(), toLocalDate(currentDate).getMonth(), toLocalDate(currentDate).lengthOfMonth(), 23, 59, 59));
    }

    public Timestamp thisYearStart(String currentDate) {
        return Timestamp.valueOf(LocalDateTime.of(toLocalDate(currentDate).getYear(), 1, 1, 0, 0, 0));
    }

    public Timestamp thisYearEnd(String currentDate) {
        return Timestamp.valueOf(LocalDateTime.of(toLocalDate(currentDate).getYear(), 12, toLocalDate(currentDate).lengthOfMonth(), 23, 59, 59));
    }

    public Timestamp tenYearAgo(String currentDate) {
        return Timestamp.valueOf(LocalDateTime.of(toLocalDate(currentDate).minusYears(10).getYear(), 1, 1, 0, 0, 0));
    }

    public int getYear(String currentDate) {
        return toLocalDate(currentDate).getYear();
    }

    public String getMonth(String currentDate) {
        return toLocalDate(currentDate).format(DateTimeFormatter.ofPattern("MM"));
    }

    public int endDay(String currentDate) {
        return toLocalDate(currentDate).lengthOfMonth();
    }


}