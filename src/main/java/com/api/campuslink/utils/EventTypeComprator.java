package com.api.campuslink.utils;

import com.api.campuslink.models.entities.EventType;

import java.util.Comparator;

public class EventTypeComprator implements Comparator<EventType> {

    @Override
    public int compare(EventType e1, EventType e2) {

        // Extract alphanumeric components
        String code1 = e1.getEventCode();
        String code2 = e2.getEventCode();

        // Split into prefix and numeric suffix
        String prefix1 = code1.replaceAll("\\d", ""); // Extract non-numeric part
        String prefix2 = code2.replaceAll("\\d", "");
        int numeric1 = Integer.parseInt(code1.replaceAll("\\D", "0")); // Extract numeric part
        int numeric2 = Integer.parseInt(code2.replaceAll("\\D", "0"));

        // Compare prefixes
        int prefixComparison = prefix1.compareTo(prefix2);
        if (prefixComparison != 0) {
            return prefixComparison; // If prefixes are different, sort by prefix
        }

        // Compare numeric suffixes
        return Integer.compare(numeric1, numeric2);
    }
}
