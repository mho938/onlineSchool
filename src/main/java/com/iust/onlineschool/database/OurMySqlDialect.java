package com.iust.onlineschool.database;

import org.hibernate.dialect.MySQLDialect;

/**
 * Created by mohsen.oloumi on 01/03/2016.
 */
public class OurMySqlDialect extends MySQLDialect {
    @Override
    public String getTableTypeString() {
        return " DEFAULT CHARSET=utf8";
    }
}