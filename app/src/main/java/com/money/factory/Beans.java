package com.money.factory;

import com.money.dao.DAOManagerFactory;
import com.money.dao.impl.DefaultDAOManager;
import com.money.service.TransferService;
import com.money.service.TransferTaskFactory;
import com.money.service.impl.DefaultTaskFactory;
import com.money.service.impl.TransferServiceImpl;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

import javax.inject.Inject;
import javax.sql.DataSource;

@Factory
public class Beans {

    @Inject
    @Bean
    public TransferService transferMoneyService(TransferTaskFactory transferTaskFactory, DAOManagerFactory managerFactory) {
        return new TransferServiceImpl(transferTaskFactory, managerFactory);
    }

    @Bean
    public DAOManagerFactory daoManagerFactory(DataSource source) {
        return () -> new DefaultDAOManager(source);
    }

    @Bean
    public TransferTaskFactory transferTaskFactory() {
        return new DefaultTaskFactory();
    }
}
