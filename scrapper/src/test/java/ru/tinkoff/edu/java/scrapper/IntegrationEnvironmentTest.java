package ru.tinkoff.edu.java.scrapper;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class IntegrationEnvironmentTest extends IntegrationEnvironment {
    private static final String GET_ALL_TABLES_QUERY = """
            select table_name\s
            from information_schema.tables\s
            where table_schema='public' and table_catalog = current_database()
            """;

    @SneakyThrows
    @Test
    void when_ContainerStarted_Expect_TablesCreated() {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(GET_ALL_TABLES_QUERY);
        Assertions.assertTrue(getTableNames(result)
                .containsAll(List.of("link", "chat", "subscription", "databasechangelog", "databasechangeloglock")));
    }

    private List<String> getTableNames(ResultSet result) throws SQLException {
        List<String> tableNames = new ArrayList<>();
        while (result.next()) {
            tableNames.add(result.getString("TABLE_NAME"));
        }
        return tableNames;
    }
}
