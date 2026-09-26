package ru.netology.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.time.Duration;

import static org.awaitility.Awaitility.await;

public class SQLHelper {

    private static final String URL =
            System.getProperty("db.url", "jdbc:mysql://localhost:3306/app");

    private static final String USER =
            System.getProperty("db.user", "app");

    private static final String PASSWORD =
            System.getProperty("db.password", "pass");

    private SQLHelper() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static String getLastPaymentStatus() {
        String query = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";

        try (Connection connection = getConnection();
             var statement = connection.createStatement();
             var resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                return resultSet.getString("status");
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String waitForPaymentStatus(String expectedStatus) {
        await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofMillis(500))
                .until(() -> expectedStatus.equals(getLastPaymentStatus()));

        return getLastPaymentStatus();
    }

    public static String getLastCreditStatus() {
        String query =
                "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";

        try (Connection connection = getConnection();
             var statement = connection.createStatement();
             var resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                return resultSet.getString("status");
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String waitForCreditStatus(String expectedStatus) {
        await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofMillis(500))
                .until(() -> expectedStatus.equals(getLastCreditStatus()));

        return getLastCreditStatus();
    }

}
