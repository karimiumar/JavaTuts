package com.umar.apps.jdbc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.*;

public class RowIdValidityTest {

    @Test
    public void rowIdLifeTimeTest() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ex", "umar", "K@tyusha1010")){
            DatabaseMetaData dbMetaData = connection.getMetaData();
            Assertions.assertThrows(SQLFeatureNotSupportedException.class, dbMetaData::getRowIdLifetime);
            /*RowIdLifetime rowIdLifetime = dbMetaData.getRowIdLifetime();
            switch (rowIdLifetime) {
                case ROWID_UNSUPPORTED -> System.out.println("ROWID not supported");
                case ROWID_VALID_FOREVER -> System.out.println("ROWID has unlimited lifetime");
                case ROWID_VALID_SESSION -> System.out.println("ROWID has lifetime that is valid for atleast the containing session");
                case ROWID_VALID_TRANSACTION -> System.out.println("ROWID has lifetime that is valid for atleast the containing transaction");
                case ROWID_VALID_OTHER -> System.out.println("ROWID has indeterminate lifetime");
            }*/
            Assertions.assertTrue(connection.isValid(0));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
