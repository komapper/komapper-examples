package org.komapper.example.formatter

import org.springframework.format.Formatter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class LocalDateTimeFormatter : Formatter<LocalDateTime> {
    override fun print(temporal: LocalDateTime, locale: Locale): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return formatter.format(temporal)
    }

    override fun parse(text: String, locale: Locale): LocalDateTime {
        return LocalDateTime.parse(text)
    }
}
