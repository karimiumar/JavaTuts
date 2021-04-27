package com.umar.apps.jdbc.transactions.psql;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLTransactionIsolationLevelSupportTest {

    //PostgreSQL doesn't support NONE
    @Test
    public void given_psql_driver_when_supports_TransactionIsolationLevel_NONE_then_success() throws SQLException {
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ex")) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            Assertions.assertFalse(databaseMetaData.supportsTransactionIsolationLevel(Connection.TRANSACTION_NONE));
        }
    }

    @Test
    public void given_psql_driver_when_supports_TransactionIsolationLevel_TRANSACTION_READ_COMMITTED_then_success() throws SQLException {
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ex")) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            Assertions.assertTrue(databaseMetaData.supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_COMMITTED));
        }
    }

    //PostgreSQL doesn't support TRANSACTION_READ_UNCOMMITTED
    @Test
    public void given_psql_driver_when_supports_TransactionIsolationLevel_TRANSACTION_READ_UNCOMMITTED_then_success() throws SQLException {
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ex")) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            Assertions.assertFalse(databaseMetaData.supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_UNCOMMITTED));
        }
    }

    //PostgreSQL doesn't support TRANSACTION_REPEATABLE_READ
    @Test
    public void given_psql_driver_when_supports_TransactionIsolationLevel_TRANSACTION_REPEATABLE_READ_then_success() throws SQLException {
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ex")) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            Assertions.assertFalse(databaseMetaData.supportsTransactionIsolationLevel(Connection.TRANSACTION_REPEATABLE_READ));
        }
    }

    @Test
    public void given_psql_driver_when_supports_TransactionIsolationLevel_TRANSACTION_SERIALIZABLE_then_success() throws SQLException {
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ex")) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            Assertions.assertTrue(databaseMetaData.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE));
        }
    }
}
