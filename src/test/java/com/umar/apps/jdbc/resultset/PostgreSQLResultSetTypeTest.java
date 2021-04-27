package com.umar.apps.jdbc.resultset;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class PostgreSQLResultSetTypeTest {
    private static final Logger LOG = LoggerFactory.getLogger(PostgreSQLResultSetTypeTest.class);

    @BeforeAll
    public static void setup() throws SQLException {
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ex")) {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            statement.addBatch("INSERT INTO foo(text) VALUES ('foo')");
            statement.addBatch("INSERT INTO foo(text) VALUES ('bar')");
            statement.addBatch("INSERT INTO foo(text) VALUES ('baz')");
            statement.addBatch("INSERT INTO bar(text) VALUES ('foo')");
            statement.addBatch("INSERT INTO bar(text) VALUES ('bar')");
            statement.addBatch("INSERT INTO bar(text) VALUES ('baz')");
            statement.executeBatch();
            connection.commit();
        }
    }

    @AfterAll
    public static void teardown() throws SQLException {
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ex")) {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            statement.execute("DELETE FROM foo");
            statement.execute("DELETE FROM bar");
            connection.commit();
        }
    }

    @Test
    public void given_connection_when_ResultSet_TYPE_FORWARD_ONLY_then_ResultSet_cannot_be_scrolled() throws SQLException {
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ex")) {
            Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM foo");
            while (resultSet.next()){
                Assertions.assertThrows(SQLException.class, resultSet::first); //Operation not allowed for a result set of type ResultSet.TYPE_FORWARD_ONLY
                //LOG.warn(resultSet.getString("text"));
                Assertions.assertThrows(SQLException.class, resultSet::beforeFirst);//throws exception as the ResultSet type is TYPE_FORWARD_ONLY
                Assertions.assertThrows(SQLException.class, resultSet::afterLast);//throws exception as the ResultSet type is TYPE_FORWARD_ONLY
                Assertions.assertThrows(SQLException.class, resultSet::deleteRow);//throws exception as the ResultSet type is CONCUR_READ_ONLY
                Assertions.assertThrows(SQLException.class, resultSet::updateRow);//throws exception as the ResultSet type is CONCUR_READ_ONLY
                Assertions.assertThrows(SQLException.class, resultSet::moveToInsertRow);//throws exception as the ResultSet type is CONCUR_READ_ONLY
                Assertions.assertThrows(SQLException.class, resultSet::moveToCurrentRow);//throws exception as the ResultSet type is CONCUR_READ_ONLY
                Assertions.assertThrows(SQLException.class, () -> resultSet.absolute(2));//throws exception as the ResultSet type is TYPE_FORWARD_ONLY
            }
        }
    }

    //Given Test case gives same result is TYPE_SCROLL_SENSITIVE
    @Test
    public void given_connection_when_ResultSet_TYPE_SCROLL_INSENSITIVE_then_data_modified_wont_be_visible() throws Exception {
        List<String> foos = List.of("foo", "bar", "baz");
        List<String> dfoos = new ArrayList<>();
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ex");
        connection.setAutoCommit(false);
        Statement insertStatement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        insertStatement.setFetchSize(1);
        LOG.warn("Statement's fetchSize() {}:", insertStatement.getFetchSize());
        ResultSet updateResultSet = insertStatement.executeQuery("SELECT * FROM foo");
        updateResultSet.moveToInsertRow();
        updateResultSet.updateString("text", "buzz");
        updateResultSet.insertRow();
        updateResultSet.beforeFirst();
        connection.commit();
        //The ResultSet when iterated shouldn't contain "buzz".
        while (updateResultSet.next()) {
            dfoos.add(updateResultSet.getString("text"));
        }
        LOG.warn("ResultSet CURSOR {}:", updateResultSet.getCursorName());
        connection.close();
        //Assertions.assertEquals(3, dfoos.size());
        Assertions.assertTrue(dfoos.containsAll(foos));
        //Assertions.assertFalse(dfoos.contains("buzz"));
    }


    /**
     *
     * This test case demonstrated that given ResultSet.TYPE_SCROLL_SENSITIVE & ResultSet.CONCUR_UPDATABLE
     * when ResultSet is created using SELECT query and a new record is updated/added by the given ResultSet
     * object then the ResultSet object when iterated contains the newly created/updated row.
     * Note: the use of ResultSet#beforeFirst(); method called immediately after ResultSet#insertRow();
     * This is needed otherwise unexpected results can occur.
     */
    @Test
    public void given_connection_when_ResultSet_TYPE_SCROLL_SENSITIVE_then_data_modified_is_visible() throws Exception {
        List<String> bars = List.of("foo", "bar", "baz", "buzz");
        List<String> dbars = new ArrayList<>();
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ex");
        connection.setAutoCommit(false);
        Statement insertStatement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet updateResultSet = insertStatement.executeQuery("SELECT * FROM bar");
        updateResultSet.moveToInsertRow();
        updateResultSet.updateString("text", "buzz");
        updateResultSet.insertRow();
        updateResultSet.beforeFirst();
        connection.commit();
        //The ResultSet when iterated should contain "buzz".
        while (updateResultSet.next()) {
            dbars.add(updateResultSet.getString("text"));
        }
        connection.close();
        Assertions.assertEquals(4, dbars.size());
        Assertions.assertTrue(dbars.containsAll(bars));
    }
}
