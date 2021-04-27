package com.umar.apps.jdbc.transactions.mysql;

import java.sql.*;

public class TransactionIsolationReadCommittedTest {

    /**
     * This isolation level guarantees that any data read is committed at the moment it is read.
     * Thus it does not allows dirty read. The transaction holds a read or write lock on the current row,
     * and thus prevent other transactions from reading, updating or deleting it
     */
    public static void given_when_IsolationLevel_READ_COMMITTED_then_first_transaction_sees_only_committed_data()  {
        new Thread(() -> {
            try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind")) {
                connection.setAutoCommit(false);
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                System.out.println(Thread.currentThread().getName() + ":Transaction Isolation Level:" + connection.getTransactionIsolation());
                PreparedStatement pstmt1 = connection.prepareStatement("SELECT COUNT(*) from shippers WHERE country_region = ?");
                pstmt1.setString(1, "USA");
                ResultSet rs1 = pstmt1.executeQuery();
                rs1.next();
                System.out.println(Thread.currentThread().getName() + "->" + rs1.getInt(1) + " records found for country_region = 'USA'"); //3 records
                Thread.sleep(2000);
                PreparedStatement pstmt2 = connection.prepareStatement("SELECT COUNT(*) from shippers WHERE country_region = ?");
                pstmt2.setString(1, "USA");
                ResultSet rs2 = pstmt2.executeQuery();
                rs2.next();
                System.out.println(Thread.currentThread().getName() + "->" + rs2.getInt(1) + " records found for country_region = 'USA'"); //3 records. Although there are 2 rows now.
                connection.commit();
            } catch (SQLException | InterruptedException throwables) {
                throwables.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind")) {
                connection.setAutoCommit(false);
                System.out.println(Thread.currentThread().getName() + ":Transaction Isolation Level:" + connection.getTransactionIsolation());
                PreparedStatement pstmt1 = connection.prepareStatement("UPDATE shippers SET country_region=? WHERE id = ?");
                pstmt1.setString(1, "US");
                pstmt1.setInt(2, 2);
                pstmt1.execute();
                connection.commit();
                System.out.println(Thread.currentThread().getName() + "->" + " One row updated");
                Thread.sleep(2000);
                //Restore original value
                PreparedStatement pstmt2 = connection.prepareStatement("UPDATE shippers SET country_region=? WHERE id = ?");
                pstmt2.setString(1, "USA");
                pstmt2.setInt(2, 2);
                pstmt2.execute();
                connection.commit();
            } catch (SQLException | InterruptedException throwables) {
                throwables.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        given_when_IsolationLevel_READ_COMMITTED_then_first_transaction_sees_only_committed_data();
    }
}
