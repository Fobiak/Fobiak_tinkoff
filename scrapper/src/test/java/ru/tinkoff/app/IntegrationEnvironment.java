package ru.tinkoff.app;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Testcontainers
@ActiveProfiles("test")
public class IntegrationEnvironment {
    public static final JdbcDatabaseContainer<?> POSTGRES_CONTAINER;

    protected static Database database;

    protected static final String DATABASE_NAME = "scrapper";
    protected static final String DATABASE_USERNAME = "postgres";
    protected static final String DATABASE_PASSWORD = "password";

    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:15")
                .withDatabaseName(DATABASE_NAME)
                .withUsername(DATABASE_USERNAME)
                .withPassword(DATABASE_PASSWORD);
        POSTGRES_CONTAINER.start();
        makeMigration(POSTGRES_CONTAINER);
    }

    private static void makeMigration(JdbcDatabaseContainer<?> container) {
        Path path = new File(".").toPath().toAbsolutePath().getParent()
                .resolve("src/main/resources/db/migrations");
        try (Connection connection = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword())) {
            database = new PostgresDatabase();
            database.setConnection(new JdbcConnection(connection));

            ResourceAccessor resourceAccessor = new DirectoryResourceAccessor(path);
            Liquibase liquibase = new Liquibase("master.xml", resourceAccessor, database);

            liquibase.update();
        } catch (SQLException | FileNotFoundException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
    }
}
