package com.poc.rest_api_poc.model;

public enum FilterDelimeter {
    EQ("eq"),
    SW("sw"),
    EW("ew"),
    CONTAINS("contains");

    final String delimeter;

    FilterDelimeter(String delimeter) {
        this.delimeter = delimeter;
    }

    public static FilterDelimeter convertToFilterDelimeter(String delimiter) {
        for (FilterDelimeter filter : FilterDelimeter.values()) {
            if (filter.delimeter.equalsIgnoreCase(delimiter)) {
                return filter;
            }
        }
        throw new IllegalArgumentException("Unknown filter delimiter: " + delimiter);
    }
}
