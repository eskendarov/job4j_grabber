package ru.job4j.utils;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SqlTimeParserTest {

    @Test
    public void parseDate() {
        LocalDateTime res1 = new SqlTimeParser().parse("сегодня, 04:23");
        LocalDateTime res2 = new SqlTimeParser().parse("вчера, 12:05");
        LocalDateTime res3 = new SqlTimeParser().parse("2 дек 19, 00:20");
        LocalDateTime exp1 = LocalDateTime.of(
                LocalDate.now(), LocalTime.of(4, 23));
        LocalDateTime exp2 = LocalDateTime.of(
                LocalDate.now().minusDays(1), LocalTime.of(12, 5));
        LocalDateTime exp3 = LocalDateTime.of(
                LocalDate.of(2019, 12, 2), LocalTime.of(0, 20));
        Assert.assertEquals(res1, exp1);
        Assert.assertEquals(res2, exp2);
        Assert.assertEquals(res3, exp3);
    }
}
