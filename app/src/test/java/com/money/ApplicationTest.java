package com.money;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class ApplicationTest {

    @Inject
    EmbeddedServer server;

    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = server.getApplicationContext().getBean(DataSource.class).getConnection();
    }

    @Test
    public void smokeTest() throws SQLException, InterruptedException {
        try (HttpClient client = server.getApplicationContext().createBean(HttpClient.class, server.getURL())) {
            assertEquals(HttpStatus.ACCEPTED, client.toBlocking().exchange(HttpRequest.POST("/api/v1/transfer/ABC/XYZ/100", "")).status());
        }

        TimeUnit.SECONDS.sleep(1);

        ResultSet transferCount = connection.prepareStatement("select COUNT(*) from TRANSFER").executeQuery();
        ResultSet account = connection.prepareStatement("select * from ACCOUNT").executeQuery();

        transferCount.next();
        account.next();

        Assertions.assertEquals(3, transferCount.getInt(1));
        Assertions.assertEquals(0, account.getInt(2));

        account.next();

        Assertions.assertEquals(200, account.getInt(2));
    }

}
