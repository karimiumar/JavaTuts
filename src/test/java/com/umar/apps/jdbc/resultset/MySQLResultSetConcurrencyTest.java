package com.umar.apps.jdbc.resultset;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.*;

public class MySQLResultSetConcurrencyTest {

    @Test
    public void given_mysql_driver_when_supports_TYPE_FORWARD_ONLY_and_CONCUR_READ_ONLY_then_success() throws SQLException {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind")) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            Assertions.assertTrue(databaseMetaData.supportsResultSetConcurrency(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY));
        }
    }

    @Test
    public void given_mysql_driver_when_supports_TYPE_FORWARD_ONLY_and_CONCUR_UPDATABLE_then_success() throws SQLException {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind")) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            Assertions.assertTrue(databaseMetaData.supportsResultSetConcurrency(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE));
        }
    }

    @Test
    public void given_mysql_driver_when_supports_TYPE_SCROLL_INSENSITIVE_and_CONCUR_READ_ONLY_then_success() throws SQLException {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind")) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            Assertions.assertTrue(databaseMetaData.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY));
        }
    }

    @Test
    public void given_mysql_driver_when_supports_TYPE_SCROLL_INSENSITIVE_and_CONCUR_UPDATABLE_then_success() throws SQLException {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", "admin", "K@tyusha1010")) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            Assertions.assertTrue(databaseMetaData.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE));
        }
    }

    @Test
    public void given_mysql_driver_when_supports_TYPE_SCROLL_SENSITIVE_and_CONCUR_READ_ONLY_then_success() throws SQLException {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", "admin", "K@tyusha1010")) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            Assertions.assertTrue(databaseMetaData.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY));
        }
    }

    @Test
    public void given_mysql_driver_when_supports_TYPE_SCROLL_SENSITIVE_and_CONCUR_UPDATABLE_then_success() throws SQLException {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", "admin", "K@tyusha1010")) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            Assertions.assertTrue(databaseMetaData.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE));
        }
    }

    @Test
    public void when_TYPE_SCROLL_SENSITIVE_and_CONCUR_UPDATABLE_then_prices_modified() throws SQLException {
        float [] coffeePricesInDB = new float[] {9.99f, 9.99f, 10.99f, 10.99f};
        int currentIndex = 0;
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", "admin", "K@tyusha1010")) {
            Statement updatableStatement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet updateResultSet = updatableStatement.executeQuery("SELECT * FROM coffees");
            while (updateResultSet.next()) {
                float f = updateResultSet.getFloat("price");
                Assertions.assertEquals(BigDecimal.valueOf(f).floatValue(), coffeePricesInDB[currentIndex++]);
                updateResultSet.updateFloat("price", f * 1.2f);
                updateResultSet.updateRow();
            }
            currentIndex = 0;
            Statement readOnlyStmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet readOnlyResultSet = readOnlyStmt.executeQuery("SELECT * FROM coffees");
            while (readOnlyResultSet.next()) {
                float f = readOnlyResultSet.getFloat("price");
                Assertions.assertTrue(BigDecimal.valueOf(f).floatValue() > coffeePricesInDB[currentIndex++]);
                Assertions.assertThrows(Exception.class, () -> readOnlyResultSet.updateFloat("price", f * 1.2f));
                Assertions.assertThrows(Exception.class, readOnlyResultSet::updateRow);
            }

            //revert original values
            updateResultSet = updatableStatement.executeQuery("SELECT * FROM coffees");
            while (updateResultSet.next()) {
                float f = updateResultSet.getFloat("price");
                updateResultSet.updateFloat("price", f / 1.2f);
                updateResultSet.updateRow();
            }
        }
    }
}
