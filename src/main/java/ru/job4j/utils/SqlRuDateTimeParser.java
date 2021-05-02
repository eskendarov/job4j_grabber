package ru.job4j.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * SqlRuDateTimeParser преобразовывает строку с датой в формат LocalDateTime
 *
 * @author Enver Eskendarov
 * @version 1.0 21/04/2021
 */
public class SqlRuDateTimeParser implements DateTimeParser {

    @Override
    public LocalDateTime parse(String parse) {
        final String[] splitDate = parse.split(", ");
        final String[] date = splitDate[0].split(" ");
        final Locale locale = Locale.getDefault();
        final LocalDate localDate
                // Парсинг месяцев Locale.RU в формате 'мес'
                = date.length == 3 ? LocalDate.parse(
                    String.format("%s %s %s",
                            date[0], getMonth(date[1]), date[2]
                    ),
                    DateTimeFormatter.ofPattern("d M yy", locale))
                // Преобразование в текущую дату (date = "сегодня")
                : date[0].startsWith("сегодня") ? LocalDate.now()
                // Преобразование в вчерашнюю дату (date = "вчера")
                : LocalDate.now().minusDays(1);
        final LocalTime localTime = LocalTime.parse(splitDate[1],
                DateTimeFormatter.ofPattern("HH:mm", locale)
        );
        return LocalDateTime.of(localDate, localTime);
    }

    private int getMonth(String month) {
        return List.of("янв", "фев", "мар", "апр", "май", "июн",
                "июл", "авг", "сен", "окт", "ноя", "дек").indexOf(month) + 1;
    }
}
