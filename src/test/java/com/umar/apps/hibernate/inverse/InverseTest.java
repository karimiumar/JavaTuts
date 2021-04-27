package com.umar.apps.hibernate.inverse;

import org.hibernate.Session;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InverseTest {

    private static final String PERSISTENCE_UNIT_NAME = "postCommentPU";
    private static EntityManagerFactory emf;
    private EntityManager em;
    private static final Logger LOGGER = Logger.getLogger(InverseTest.class.getName());
    @BeforeAll
    public static void setup() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
    }

    @BeforeEach
    public void beforeEach() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    @AfterEach
    public void afterEach() {
        if(null != em) {
            if(em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
            em.close();
        }
    }

    @AfterAll
    public static void tearDown() {
        if(null != emf){
            emf.close();
        }
    }

    //@Test
    public void fetchStockAndStockDailyRecordsForIBM() {
        List<Stock> stocks =
                em
                .createQuery("SELECT s FROM com.umar.apps.hibernate.inverse.Stock s WHERE s.id=:id", Stock.class)
                .setParameter("id", 2L)
                .getResultList();
        Assertions.assertFalse(stocks.isEmpty());
        stocks.forEach(s -> LOGGER.info(s.getStockDailyRecords().toString()));
    }


    //Given test throws NotYetImplementedException: Collections having FK in secondary table
    //Hibernate requires a Secondary table in order to manage the INVERSE=FALSE from Parent to Child
    //when <join-column/> has following <join-column table="STOCK_DAILY_RECORD" name="STOCK_ID"/>
    //Remove table attribute from the <join-column/> to remove the exception.
    //This is called a Unidirectional @OneToMany with @JoinColumn. No third table is needed to manage relationships.
    @Test
    public void whenStockIsRelationshipOwner_MappedByNotSetInOneToManyThenTwoInsertsAndOneUpdate_UsingJPA() {
        var stock = new Stock();
        stock.setStockCode("SAP");
        stock.setStockName("Sapient");
        var sdr = new StockDailyRecord();
        sdr.setPriceOpen(new BigDecimal("342.45"));
        sdr.setPriceClose(new BigDecimal("242.45"));
        sdr.setPriceChange(new BigDecimal("-100.00"));
        sdr.setVolume(120000);
        sdr.setDate(LocalDate.now());
        sdr.setStock(stock);
        stock.addStockDailyRecord(sdr);
        em.persist(stock);
        //em.persist(sdr);

        stock.removeStockDailyRecord(sdr);
        em.remove(stock);
        //em.remove(sdr);
    }

    //@Test
    public void whenStockIsRelationshipOwner_InverseIsFalseThenTwoInsertsAndOneUpdate_UsingHibernate() {
        var session = em.unwrap(Session.class);
        session.beginTransaction();
        var stock = new Stock();
        stock.setStockCode("SAP");
        stock.setStockName("Sapient");
        var sdr = new StockDailyRecord();
        sdr.setPriceOpen(new BigDecimal("342.45"));
        sdr.setPriceClose(new BigDecimal("242.45"));
        sdr.setPriceChange(new BigDecimal("-100.00"));
        sdr.setVolume(120000);
        sdr.setDate(LocalDate.now());
        sdr.setStock(stock);
        stock.addStockDailyRecord(sdr);
        session.save(stock);
        session.save(sdr);
        session.getTransaction().commit();
    }

    //@Test
    public void whenStockDailyRecordIsRelationshipOwner_InverseIsTrueThenTwoInsertsAndOneUpdate_UsingHibernate() {
        var session = em.unwrap(Session.class);
        session.beginTransaction();
        var stock = new Stock();
        stock.setStockCode("SAP");
        stock.setStockName("Sapient");
        var sdr = new StockDailyRecord();
        sdr.setPriceOpen(new BigDecimal("342.45"));
        sdr.setPriceClose(new BigDecimal("242.45"));
        sdr.setPriceChange(new BigDecimal("-100.00"));
        sdr.setVolume(120000);
        sdr.setDate(LocalDate.now());
        sdr.setStock(stock);
        stock.addStockDailyRecord(sdr);
        session.save(stock);
        session.save(sdr);
        session.getTransaction().commit();
    }
}
