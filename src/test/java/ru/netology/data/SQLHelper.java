package ru.netology.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {

    private static final String URL =
            "jdbc:mysql://localhost:3306/app";

    private static final String USER = "app";
    private static final String PASSWORD = "pass";

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
        for (int i = 0; i < 10; i++) {
            String status = getLastPaymentStatus();

            if (expectedStatus.equals(status)) {
                return status;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

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
        for (int i = 0; i < 10; i++) {
            String status = getLastCreditStatus();

            if (expectedStatus.equals(status)) {
                return status;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return getLastCreditStatus();
    }

}
