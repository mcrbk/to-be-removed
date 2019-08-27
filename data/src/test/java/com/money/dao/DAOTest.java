package com.money.dao;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;

public class DAOTest {

    protected JdbcDataSource jdbcDataSource;

    @BeforeEach
    void setUp() {
        jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL("jdbc:h2:mem:testdb;INIT=RUNSCRIPT FROM 'classpath:beforeTestRun.sql'");
        jdbcDataSource.setUser("sa");
        jdbcDataSource.setPassword("sa");
    }
}
