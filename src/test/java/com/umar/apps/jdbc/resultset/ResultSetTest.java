package com.umar.apps.jdbc.resultset;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

public class ResultSetTest {

    @Test
    public void when_table_is_populated_then_resultset_contains_object() {
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ex")) {
            String query = "SELECT id, coffee_name, supplier_id, price, sales, total from coffees";
            try (Statement statement = connection.createStatement()){
                var rs = statement.executeQuery(query);
                while (rs.next()) {
                    String coffeeName = rs.getString("coffee_name");
                    int supplierId = rs.getInt("supplier_id");
                    float price = rs.getFloat("price");
                    int sales = rs.getInt("sales");
                    int total = rs.getInt("total");
                    System.out.printf("%s, %d, %.2f, %d, %d\n", coffeeName, supplierId, price, sales, total);
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void display_cursor_holdability_support() throws SQLException {
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ex")) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            System.out.printf("ResultSet.HOLD_CURSORS_OVER_COMMIT = %d\n", ResultSet.HOLD_CURSORS_OVER_COMMIT);
            System.out.printf("ResultSet.CLOSE_CURSORS_AT_COMMIT = %d\n", ResultSet.CLOSE_CURSORS_AT_COMMIT);
            System.out.printf("Default Cursor Holdability = %s\n", databaseMetaData.getResultSetHoldability());
            System.out.printf("Supports HOLD_CURSORS_OVER_COMMIT ? = %s\n", databaseMetaData.supportsResultSetHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT));
            System.out.printf("Supports CLOSE_CURSORS_AT_COMMIT ? = %s\n", databaseMetaData.supportsResultSetHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT));

        }
    }

    @Test
    public void when_prices_modified_then_sql_update_modifies_prices() throws SQLException {
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ex")) {
            String query = "SELECT id, price from coffees WHERE id = 1";
            try(Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                ResultSet resultSet = statement.executeQuery(query);
                resultSet.next();
                float price = resultSet.getFloat("price");
                BigDecimal pricey = new BigDecimal(price);
                System.out.println(pricey.floatValue());
                Assertions.assertEquals(new BigDecimal("7.99").floatValue(), pricey.floatValue());
                resultSet.updateFloat("price", price * 1.2f);
                resultSet.updateRow();
            }
            //Revert to original price
            try(Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                ResultSet resultSet = statement.executeQuery(query);
                resultSet.next();
                float price = resultSet.getFloat("price");
                Assertions.assertEquals(new BigDecimal("9.59").floatValue(), new BigDecimal(price).floatValue());
                resultSet.updateFloat("price", 7.99f);
                resultSet.updateRow();
            }
        }
    }

    @Test
    public void when_xml_then_saved_success() throws SQLException, ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(ClassLoader.getSystemClassLoader().getResourceAsStream("rss-the-coffee-break-blog.xml"));
        System.out.println(Thread.currentThread().getContextClassLoader());
        //document =  builder.parse(Thread.currentThread().getContextClassLoader().getResource("rss-the-coffee-break-blog.xml").getFile());
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();

        Node titleElement = (Node) xPath.evaluate("/rss/channel/title[1]", document, XPathConstants.NODE);
        if(null == titleElement) {
            System.out.println("Unable to retrieve title element");
            return;
        }
        String titleString = titleElement.getTextContent().trim().toLowerCase().replaceAll("\\s+", "_");
        System.out.println(titleString);
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ex")) {
            String insertQuery = "INSERT INTO rss_feeds (rss_name, rss_feed) VALUES (?, ?)";
            var pstmt = connection.prepareStatement(insertQuery);
            pstmt.setString(1, titleString);
            System.out.println("Creating SQLXML object with PostgreSQL");
            var sqlXML = connection.createSQLXML();
            DOMResult dom = sqlXML.setResult(DOMResult.class);
            dom.setNode(document);
            pstmt.setSQLXML(2, sqlXML);
            pstmt.executeUpdate();
            pstmt = connection.prepareStatement("DELETE FROM rss_feeds");
            pstmt.execute();
        }
    }
}
