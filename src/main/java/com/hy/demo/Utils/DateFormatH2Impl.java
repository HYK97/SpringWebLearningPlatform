package com.hy.demo.Utils;


import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@Primary
public class DateFormatH2Impl implements DateFormat {


    @Override
    public StringTemplate getDayFormat(DateTimePath<Timestamp> date) {
        return Expressions.stringTemplate(
                "FORMATDATETIME({0}, 'Y-MM-dd')"
                , date);
    }

    @Override
    public StringTemplate getMonthFormat(DateTimePath<Timestamp> date) {
        return Expressions.stringTemplate(
                "FORMATDATETIME({0}, 'Y-MM')"
                , date);
    }

    @Override
    public StringTemplate getYearFormat(DateTimePath<Timestamp> date) {
        return Expressions.stringTemplate(
                "FORMATDATETIME({0}, 'Y')"
                , date);
    }
}
