package com.aacreations.countryandcapitals;

import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainAccess {

    public static class Options {

        public static final String AFRICA = "Africa";
        public static final String ASIA= "Asia";
        public static final String EUROPE = "Europe";
        public static final String NORTH_AMERICA = "North America";
        public static final String OCEANA = "Oceana";
        public static final String SOUTH_AMERICA = "South America";
        public static final String ALL = "All Capitals";


        public static final String EXAM = "Exam";
        public static final String PRACTICE = "Practice";
        public static final String LEARN = "Learn";

    }

    static class RandomGenerator {

        private static final String TAG = "RandomGenerator";

        @NonNull
        public static <T> List<T> randomizeList(List<T> randomList, int questions) {
            // make a list have a particular amount of questions and randomize the list
            List<T> returnList = createNewList(randomList);
            int maxNumber = returnList.size() - questions;
            for (int i = 0; i<maxNumber; i++) {
                returnList.remove(getRandom(0,returnList.size() - 1));
            }
            Collections.shuffle(returnList);
            return returnList;
        }

        /**
         * gets a random number between a max and min
         * @param min the minimum number
         * @param max the maximum number
         * @return a random number between a max and min
         */
        public static int getRandom(int min, int max) {
            int number = (int)(Math.random()*(max-min+1)+min);
            return number;
        }

        @NonNull
        public static List<String> getRandomCapitals(@NonNull List<CountryCapital> completeList, String removeCapital) {
            List<String> allCapitals = new ArrayList<>();
            for (int i = 0; i < completeList.size(); i++) {
                allCapitals.add(completeList.get(i).getCapitalName());
            }

            allCapitals.remove(removeCapital);

            Log.d(TAG, "getRandomCapitals: " + completeList.size());
            Log.d(TAG, "getRandomCapitals: " + allCapitals.size());

            List<String> threeCapitals = new ArrayList<>();
//            Random rand = new Random();
//            int one = 10;
//            int two = 0;
//            int three = rand.nextInt(allCapitals.size());
            for (int i = 0; i < 3; i++) {
                // gets a random index
                int randomIndex = getRandom(0, allCapitals.size()-1);
//            int randomIndex = 0;
//            switch (i) {
//                case 0:
//                    randomIndex = one;
//                    break;
//                case 1:
//                    randomIndex = two;
//                    break;
//                case 2:
//                    randomIndex=rand.nextInt(allCapitals.size());
//                    break;
//            }

                // adds that to the three capitals
                threeCapitals.add(allCapitals.get(randomIndex));

                allCapitals.remove(randomIndex);
            }
            Log.d(TAG, "getRandomCapitals: " + threeCapitals.size());
            return threeCapitals;
        }

        @NonNull
        @Contract("_ -> new")
        private static <T> List<T> createNewList(List<T> currentList) {
            return new ArrayList<>(currentList);
        }

    }

    public static String toString(int str) {
        return toString("", str, "");
    }

    public static String toString(String start, int str) {
        return toString(start, str, "");
    }

    public static String toString(int str, String end) {
        return toString("", str, end);
    }

    public static String toString(String start, int str, String end) {
        return start + str + end;
    }

}
