package model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Series {

    O_100("100 greatest", "1"),
    EAST("East", "e\\d"),
    MIDLANDS("Midlands", "m\\d"),
    NORTH_EAST("North-east", "ne\\d"),
    NORTH_WEST("North-west", "nw\\d"),
    SOUTH_WEST("South-west", "sw\\d"),
    SOUTH("South", "s\\d"),
    WALES("Wales", "w\\d"),
    YORKSHIRE("Yorkshire", "y\\d"),
    A_100("another 100", "a1"),;

    private String displayName;
    private Pattern pattern;

    Series(String displayName,
           String pattern) {

        this.displayName = displayName;
        this.pattern = Pattern.compile(pattern);
    }

    public static Series codeToSeries(String rawCode) {

        Series result = null;

        for (Series series : Series.values()) {
            if (series.codeMatches(rawCode)) {
                result = series;
            }
        }

        if (result == null) {
            throw new RuntimeException("Cant map code '" + rawCode + "'");
        }

        return result;
    }

    private boolean codeMatches(String rawCode) {

        boolean result = false;

        Matcher matcher = pattern.matcher(rawCode);

        if (matcher.matches()) {
            result = true;
        }

        return result;
    }

    public String getDisplayName() {
        return displayName;
    }

}
