package com.quickbase.devint;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;


/**
 * This interface define the methods which are used to manipulate and
 * transform input data to modified data.
 * @author ankit
 */
public interface TransformData {
    /**
     * Merges the two input parameters such that if both parameters have same countryName
     * then entry from the databaseList is preferred.
     *
     * @param databaseList map of country and its corresponding population from the database.
     * @param apiList      list of immutable pair of country and its population from the API.
     * @return             A map of countries and its population from both the list.
     */
    Map<String, Integer> mergeListsOfPopulation(Map<String, Integer> databaseList, List<Pair<String, Integer>> apiList);
}
