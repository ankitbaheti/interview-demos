package com.quickbase.devint;

import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements the TransformData Interface.
 * @author ankit
 */
public class TransformDataImpl implements TransformData {

    /**
     * Merges the two input parameters such that if both parameters have same countryName
     * then entry from the databaseList is preferred.
     *
     * @param databaseList map of country and its corresponding population from the database.
     * @param apiList      list of immutable pair of country and its population from the API.
     * @return             A map of countries and its population from both the list.
     */
    @Override
    public Map<String, Integer> mergeListsOfPopulation(Map<String, Integer> databaseList, List<Pair<String, Integer>> apiList) {

        Map<String, Integer> finalPopulationList = new HashMap<>();

        for(Map.Entry<String,Integer> entry: databaseList.entrySet()){
            String countryName = similarCountryName(entry.getKey());
            finalPopulationList.put(countryName, databaseList.get(entry.getKey()));
        }

        for(Pair<String, Integer> pair: apiList){
            String countryName = similarCountryName(pair.getKey());
            if(!finalPopulationList.containsKey(countryName)){
                finalPopulationList.put(countryName, pair.getValue());
            }
        }

        return finalPopulationList;
    }

    /**
     * If the name of the country is in different form, then it return a common name.
     * @param countryName name of a country
     * @return a common name for that country.
     */
    private String similarCountryName(String countryName) {
        if(countryName.equals("United States of America") || countryName.equals("U.S.A."))
            return "U.S.A.";
        return countryName;
    }
}
