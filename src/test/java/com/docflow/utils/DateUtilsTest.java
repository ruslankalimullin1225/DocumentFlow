package com.docflow.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @Test
    void testFormatDate() {
        LocalDate date = LocalDate.of(2025, 11, 30);
        String formatted = DateUtils.formatDate(date);
        assertEquals("30.11.2025", formatted);
    }

    @Test
    void testFormatDateNull() {
        String formatted = DateUtils.formatDate(null);
        assertEquals("", formatted);
    }

    @Test
    void testParseDate() {
        String dateString = "30.11.2025";
        LocalDate parsed = DateUtils.parseDate(dateString);
        assertEquals(LocalDate.of(2025, 11, 30), parsed);
    }

    @Test
    void testParseDateISO() {
        String dateString = "2025-11-30";
        LocalDate parsed = DateUtils.parseDate(dateString);
        assertEquals(LocalDate.of(2025, 11, 30), parsed);
    }

    @Test
    void testParseDateInvalid() {
        String dateString = "invalid-date";
        LocalDate parsed = DateUtils.parseDate(dateString);
        assertNotNull(parsed);
        assertEquals(LocalDate.now(), parsed);
    }

    @Test
    void testParseDateNull() {
        LocalDate parsed = DateUtils.parseDate(null);
        assertNotNull(parsed);
        assertEquals(LocalDate.now(), parsed);
    }

    @Test
    void testParseDateEmpty() {
        LocalDate parsed = DateUtils.parseDate("");
        assertNotNull(parsed);
        assertEquals(LocalDate.now(), parsed);
    }

    @Test
    void testIsValidDate() {
        assertTrue(DateUtils.isValidDate("30.11.2025"));
        assertTrue(DateUtils.isValidDate("2025-11-30"));
        assertFalse(DateUtils.isValidDate("invalid-date"));
        assertFalse(DateUtils.isValidDate(null));
        assertFalse(DateUtils.isValidDate(""));
    }

    @Test
    void testRoundTripConversion() {
        LocalDate originalDate = LocalDate.of(2025, 11, 30);
        String formatted = DateUtils.formatDate(originalDate);
        LocalDate parsed = DateUtils.parseDate(formatted);
        assertEquals(originalDate, parsed);
    }
}
