package com.umar.apps.jdbc.transactions.mysql;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.concurrent.*;

public class TransactionIsolationReadUncommittedTest {

    public static void main(String[] args) {
        given_when_IsolationLevel_READ_UNCOMMITTED_then_first_transaction_sees_dirty_data();
    }

    /**
     * This test will show that the first transaction can see some 'dirty data'
     * on the second transaction, although it hasn’t finished yet.
     *
     * Read Uncommitted is the lowest isolation level. In this level, one transaction may read
     * not yet committed changes made by other transaction, thereby allowing dirty reads.
     * In this level, transactions are not isolated from each other.
     */
    public static void given_when_IsolationLevel_READ_UNCOMMITTED_then_first_transaction_sees_dirty_data() {
        new Thread(() -> {
            try{
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind");
                connection.setAutoCommit(false);
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                PreparedStatement pstmt1 = connection.prepareStatement("SELECT company FROM customers WHERE id = 1");
                ResultSet resultSet1 = pstmt1.executeQuery();
                resultSet1.next();
                System.out.println(Thread.currentThread().getName() + "->" + resultSet1.getString(1));
                Thread.sleep(2000);
                PreparedStatement pstmt2 = connection.prepareStatement("SELECT company FROM customers WHERE id = 1");
                ResultSet resultSet2 = pstmt2.executeQuery();
                resultSet2.next();
                System.out.println(Thread.currentThread().getName() + "->" + resultSet2.getString(1));
            }catch (Exception e) {
                e.printStackTrace();
            }

        }).start();

        new Thread(() -> {
            try{
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind");
                connection.setAutoCommit(false);
                PreparedStatement pstmt = connection.prepareStatement("UPDATE customers SET company = ? WHERE id = 1");
                pstmt.setString(1, "Company AA");
                Thread.sleep(1000);
                pstmt.executeUpdate();
                System.out.println(Thread.currentThread().getName() + "-> set company to 'Company AA where id is 1");
                //do not commit
            }catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    //This test may fail when UpdateCallable is executed prior to ReadCallable by the ExecutorService
    @Test
    public void when_IsolationLevel_READ_UNCOMMITTED_then_first_transaction_sees_dirty_data() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<String> readFuture = executorService.submit(new ReadCallable());
        Future<Integer> updateFuture = executorService.submit(new UpdateCallable());
        Assertions.assertEquals("Company AA", readFuture.get());
        updateFuture.get();
    }
}


class ReadCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind")) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            PreparedStatement pstmt1 = connection.prepareStatement("SELECT company FROM customers WHERE id = 1");
            ResultSet resultSet1 = pstmt1.executeQuery();
            resultSet1.next();
            System.out.println(Thread.currentThread().getName() + "->" + resultSet1.getString(1));
            PreparedStatement pstmt2 = connection.prepareStatement("SELECT company FROM customers WHERE id = 1");
            ResultSet resultSet2 = pstmt2.executeQuery();
            resultSet2.next();
            String company = resultSet2.getString(1);
            System.out.println(Thread.currentThread().getName() + "->" + company);
            return company;
        }
    }
}

class UpdateCallable implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind")) {
            connection.setAutoCommit(false);
            PreparedStatement pstmt = connection.prepareStatement("UPDATE customers SET company = ? WHERE id = 1");
            pstmt.setString(1, "Company AA");
            int result = pstmt.executeUpdate();
            System.out.println(Thread.currentThread().getName() + "-> set company to 'Company AA where id is 1");
            return result;
        }
    }
}