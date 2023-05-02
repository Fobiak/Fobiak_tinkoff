package ru.tinkoff.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TestDataTable extends IntegrationEnvironment {

    @Test
    void database__startMigration_databaseRun() {
        assertNotNull(database);
        try (Connection connection = DriverManager.getConnection(POSTGRES_CONTAINER.getJdbcUrl(), POSTGRES_CONTAINER.getUsername(), POSTGRES_CONTAINER.getPassword())) {
            ResultSet resultSet = connection.getMetaData().getTables(null, null,
                    "%", new String[]{"TABLE"});
            Set<String> tableName = new HashSet<>();
            while (resultSet.next()) {
                tableName.add(resultSet.getString("TABLE_NAME"));
            }
            assertAll(
                    () -> assertTrue(tableName.contains("link")),
                    () -> assertTrue(tableName.contains("client")),
                    () -> assertTrue(tableName.contains("user_links"))
            );

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
