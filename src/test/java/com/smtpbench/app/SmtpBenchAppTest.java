package com.smtpbench.app;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SmtpBenchAppTest {

    @Test
    void testGenerateBodyLength() {
        String body = SmtpBenchApp.generateBody(100);
        assertEquals(100, body.length(), "Il corpo deve avere la lunghezza richiesta");
    }

    @Test
    void testGenerateBodyContent() {
        String body = SmtpBenchApp.generateBody(50);
        assertTrue(body.chars().allMatch(c -> c == 'A'), "Il corpo deve essere composto solo da 'A'");
    }

    @Test
    void testGenerateBodyZero() {
        String body = SmtpBenchApp.generateBody(0);
        assertEquals(0, body.length(), "Il corpo deve essere vuoto se size=0");
    }

    @Test
    void testGenerateBodyLargeSize() {
        assertDoesNotThrow(() -> SmtpBenchApp.generateBody(10_000));
    }

    @Test
    void testGenerateBodyNegative() {
        String body = SmtpBenchApp.generateBody(-5);
        assertEquals(0, body.length(), "Il corpo deve essere vuoto se size è negativo");
    }

    @Test
    void testGenerateBodyDeterministic() {
        String body1 = SmtpBenchApp.generateBody(15);
        String body2 = SmtpBenchApp.generateBody(15);
        assertEquals(body1, body2, "generateBody deve essere deterministico");
    }
}
