package com.quickbase;

import com.quickbase.devint.ConcreteStatService;
import com.quickbase.devint.IStatService;
import com.quickbase.devint.TransformData;
import com.quickbase.devint.TransformDataImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.*;

public class MainTest {

    private Connection connection = null;
    private int population;
    private static final String SQL_QUERY1 = "Select population from City where CityId = ?";
    private static final String SQL_QUERY2 = "Select SUM(city.population) AS population from City, State" +
            " where City.StateId = State.StateId" +
            " and State.StateId = ?";

    private static final String SQL_QUERY3 = "Select SUM(city.population) AS population from City, State, Country" +
            " where City.StateId = State.StateId" +
            " and State.CountryId = Country.CountryId" +
            " and Country.CountryId = ?";

    private static final String SQL_QUERY4 = "Select SUM(city.population) AS population from City, State, Country" +
            " where City.StateId = State.StateId" +
            " and State.CountryId = Country.CountryId" +
            " GROUP BY Country.CountryId";

    private TransformData transformData;
    private IStatService iStatService;

    @Before
    public void setUp() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:resources/data/citystatecountry.db");
        transformData = new TransformDataImpl();
        iStatService = new ConcreteStatService();
    }

    @Test
    public void checkPopulationOfCityFromDb() {
        try (AutoCloseable statement  = connection.prepareStatement(SQL_QUERY1)) {
            ((PreparedStatement) statement).setInt(1, 163119);
            ResultSet resultSet = ((PreparedStatement) statement).executeQuery();
            while (resultSet.next())
                population = resultSet.getInt("population");

            assertEquals(5132794, population);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkPopulationOfStateFromDb() {
        try (AutoCloseable statement  = connection.prepareStatement(SQL_QUERY2)) {
            ((PreparedStatement) statement).setInt(1, 2);
            ResultSet resultSet = ((PreparedStatement) statement).executeQuery();
            while (resultSet.next())
                population = resultSet.getInt("population");

            assertEquals(729943, population);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkPopulationOfCountryFromDb() {
        try (AutoCloseable statement  = connection.prepareStatement(SQL_QUERY3)) {
            ((PreparedStatement) statement).setInt(1, 1);
            ResultSet resultSet = ((PreparedStatement) statement).executeQuery();
            while (resultSet.next())
                population = resultSet.getInt("population");

            assertEquals(311976362, population);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkListSizeFromDb(){
        try (AutoCloseable statement  = connection.createStatement()) {
            ResultSet resultSet = ((Statement) statement).executeQuery(SQL_QUERY4);
            int count = 0;
            while (resultSet.next())
                count++;
            assertEquals(16, count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkListSizeFromApi(){
        List<Pair<String, Integer>> pairs = iStatService.GetCountryPopulations();
        assertEquals(28, pairs.size());
    }

    @Test
    public void checkSimilarName() {
        assertEquals("U.S.A.", transformData.similarCountryName("United States of America"));
        assertEquals("U.S.A.", transformData.similarCountryName("U.S.A."));
        assertEquals("India", transformData.similarCountryName("India"));
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }
}