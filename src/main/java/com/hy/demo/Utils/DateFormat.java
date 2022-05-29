package com.hy.demo.Utils;

import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.StringTemplate;

import java.sql.Timestamp;

public interface DateFormat {
    StringTemplate getDayFormat(DateTimePath<Timestamp> date);

    StringTemplate getMonthFormat(DateTimePath<Timestamp> date);

    StringTemplate getYearFormat(DateTimePath<Timestamp> date);


}
