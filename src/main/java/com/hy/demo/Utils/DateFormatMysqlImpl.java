package com.hy.demo.Utils;


import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class DateFormatMysqlImpl implements DateFormat {


    @Override
    public StringTemplate getDayFormat(DateTimePath<Timestamp> date) {
        return Expressions.stringTemplate(
                "DATE_FORMAT({0}, '%Y-%m-%d')"
                , date);
    }

    @Override
    public StringTemplate getMonthFormat(DateTimePath<Timestamp> date) {
        return Expressions.stringTemplate(
                "DATE_FORMAT({0}, '%Y-%m')"
                , date);
    }

    @Override
    public StringTemplate getYearFormat(DateTimePath<Timestamp> date) {
        return Expressions.stringTemplate(
                "DATE_FORMAT({0}, '%Y')"
                , date);
    }

}
