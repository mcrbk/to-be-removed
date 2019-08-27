package com.money;

import com.money.dao.impl.DefaultDAOManager;
import com.money.service.TransferService;
import com.money.service.TransferUpdateException;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.h2.jdbcx.JdbcDataSource;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Factory
public class Configuration {

    private ExecutorService executorService;

    @Inject
    @Bean
    public TransferService transferService(DataSource dataSource, @Named("transferExecutor") ExecutorService executorService) {
        return new TransferServiceExecutor(() -> new DefaultDAOManager(dataSource), executorService);
    }

    @Bean
    @Named("transferExecutor")
    public ExecutorService executorService() {
        executorService = Executors.newFixedThreadPool(10, init(Executors.defaultThreadFactory()));
        return executorService;
    }

    @Bean
    public DataSource dataSource() {
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL("jdbc:h2:file:~/test;AUTO_SERVER=TRUE;INIT=RUNSCRIPT FROM 'classpath:database.sql'");
        jdbcDataSource.setUser("sa");
        jdbcDataSource.setPassword("sa");
        return jdbcDataSource;
    }

    /*
     * This part needs improvements
     * */
    private ThreadFactory init(ThreadFactory threadFactory) {
        return runnable -> {
            Thread thread = threadFactory.newThread(runnable);
            thread.setUncaughtExceptionHandler((x, t) -> {
                if (t instanceof TransferUpdateException) {
                    executorService.submit(runnable);
                }
            });
            return thread;
        };
    }
}
