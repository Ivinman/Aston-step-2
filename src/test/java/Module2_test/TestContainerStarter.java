package Module2_test;

import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Properties;

public class TestContainerStarter {
    @SuppressWarnings("resource")
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("Prod")
                    .withUsername("postgres")
                    .withPassword("root");

    public static Properties starWithProperties() {
        postgres.start();
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        properties.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        properties.setProperty("hibernate.connection.username", postgres.getUsername());
        properties.setProperty("hibernate.connection.password", postgres.getPassword());
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        return properties;
    }

    public static void closeContainer() {
        postgres.close();
    }
}
