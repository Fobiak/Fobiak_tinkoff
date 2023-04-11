import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class IntegrationEnvironment {
    static final PostgreSQLContainer<?> POSTGRES_CONTAINER;

    private static final Path MASTER_PATH = new File("../migrations").toPath();

    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:15-alpine");
        POSTGRES_CONTAINER.start();

        try (Connection connection = getConnection()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new liquibase.Liquibase("master.xml",
                    new DirectoryResourceAccessor(MASTER_PATH), database);
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (FileNotFoundException | LiquibaseException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                POSTGRES_CONTAINER.getJdbcUrl(),
                POSTGRES_CONTAINER.getUsername(),
                POSTGRES_CONTAINER.getPassword()
        );
    }
}
