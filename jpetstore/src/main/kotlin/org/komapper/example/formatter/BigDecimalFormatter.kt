package org.komapper.example.formatter

import org.springframework.format.Formatter
import org.springframework.format.number.NumberStyleFormatter
import java.math.BigDecimal
import java.text.ParseException
import java.util.Locale

class BigDecimalFormatter : Formatter<BigDecimal> {
    private val formatter: NumberStyleFormatter = NumberStyleFormatter("$#,##0.00")

    override fun print(value: BigDecimal, locale: Locale): String {
        return formatter.print(value, locale)
    }

    @Throws(ParseException::class)
    override fun parse(text: String, locale: Locale): BigDecimal {
        return formatter.parse(text, locale) as BigDecimal
    }
}
