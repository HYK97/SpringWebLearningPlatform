package com.hy.demo.Config.Dialect;

import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class CustomMysqlDialect extends MySQL5Dialect {
    public CustomMysqlDialect() {
        super();

        this.registerFunction("DATE_FORMAT", new StandardSQLFunction("DATE_FORMAT", StandardBasicTypes.STRING));
    }
}
