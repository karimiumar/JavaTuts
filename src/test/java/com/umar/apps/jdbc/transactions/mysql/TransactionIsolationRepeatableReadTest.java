package com.umar.apps.jdbc.transactions.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TransactionIsolationRepeatableReadTest {

    public static void main(String[] args) {
        given_when_IsolationLevel_REPEATABLE_READ_then_first_transaction_dont_see_dirty_data();
    }

    /**
     * This is the most restrictive isolation level. The transaction holds read locks on all rows it references
     * and writes locks on all rows it inserts, updates, or deletes. Since other transaction cannot read, update
     * or delete these rows, consequently it avoids non-repeatable read.
     *
     * Sometimes phantom reads may occur.
     */
    public static void given_when_IsolationLevel_REPEATABLE_READ_then_first_transaction_dont_see_dirty_data() {
        new Thread(() -> {
            try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind")){
                connection.setAutoCommit(false);
                connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
                PreparedStatement pstmt1 = connection.prepareStatement("SELECT text FROM repeatable_read");
                ResultSet resultSet1 = pstmt1.executeQuery();
                if(resultSet1.next()) {
                    System.out.println(Thread.currentThread().getName() + "->" + resultSet1.getString("text"));
                }
                Thread.sleep(1000);
                PreparedStatement pstmt2 = connection.prepareStatement("SELECT text FROM repeatable_read");
                ResultSet resultSet2 = pstmt2.executeQuery();
                if(resultSet2.next()) {
                    System.out.println(Thread.currentThread().getName() + "->" + resultSet2.getString("text"));
                }
                Thread.sleep(1000);
                PreparedStatement pstmt3 = connection.prepareStatement("UPDATE repeatable_read SET text = ? WHERE text = ?");
                pstmt3.setString(1,"2nd row");
                pstmt3.setString(2, "second row");
                boolean result = pstmt3.execute();
                System.out.println(Thread.currentThread().getName() + "->" + result);
                connection.commit();
            }catch (Exception e) {
                e.printStackTrace();
            }

        }).start();

        new Thread(() -> {
            try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind")){
                connection.setAutoCommit(false);
                PreparedStatement pstmt = connection.prepareStatement("INSERT INTO repeatable_read(text) values(?)");
                pstmt.setString(1, "first row");
                pstmt.executeUpdate();
                pstmt.setString(1, "second row");
                pstmt.executeUpdate();
                System.out.println(Thread.currentThread().getName() + "-> Two rows added to repeatable_read");
                connection.commit();
                pstmt.execute("DELETE FROM repeatable_read");
                Thread.sleep(2000);
                connection.commit();
                System.out.println(Thread.currentThread().getName() + "-> All records deleted");
            }catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
