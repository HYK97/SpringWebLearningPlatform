package com.hy.demo.config.dialect;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class CustomH2Dialect extends H2Dialect {
    public CustomH2Dialect() {
        super();

        this.registerFunction("FORMATDATETIME", new StandardSQLFunction("FORMATDATETIME", StandardBasicTypes.STRING));
    }
}
