package com.quickbase;

import com.quickbase.devint.ConcreteStatService;
import com.quickbase.devint.DBManager;
import com.quickbase.devint.DBManagerImpl;
import com.quickbase.devint.TransformDataImpl;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * The main method of the executable JAR generated from this repository. This is to let you
 * execute something from the command-line or IDE for the purposes of demonstration, but you can choose
 * to demonstrate in a different way (e.g. if you're using a framework)
 */
public class Main {
    public static void main( String args[] ) {
        System.out.println("Starting.");
        System.out.print("Getting DB Connection...");

        DBManager dbm = new DBManagerImpl();
        Connection c = dbm.getConnection();

        // country and its population from the database.
        Map<String, Integer> databasePopulationList = dbm.getPopulationByCountry(c);

        // country and its population from API
        List<Pair<String, Integer>> apiPopulationList = new ConcreteStatService().GetCountryPopulations();

        // final list of countries and its populations
        // between the databasePopulationList and apiPopulationList.
        Map<String, Integer> finalPopulationList =
                new TransformDataImpl().mergeListsOfPopulation(databasePopulationList, apiPopulationList);

        for(Map.Entry<String, Integer> entry: finalPopulationList.entrySet())
            System.out.println(entry.getKey() + " - " + entry.getValue());
    }
}