package com.smtpbench.app;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ConfigLoaderTest {

    @Test
    void testConfigLoaderReadsProperties() throws Exception {
        ConfigLoader config = new ConfigLoader();
        assertNotNull(config.get("smtp.host"));
        assertNotNull(config.get("smtp.port"));
        assertNotNull(config.get("smtp.username"));
    }

    @Test
    void testConfigLoaderDefaultValue() throws Exception {
        ConfigLoader config = new ConfigLoader();
        int defaultValue = config.getInt("non.existent.key", 42);
        assertEquals(42, defaultValue);
    }

    @Test
    void testMissingPropertyReturnsNull() throws Exception {
        ConfigLoader config = new ConfigLoader();
        assertNull(config.get("non.existent.property"));
    }

    @Test
    void testGetIntReadsNumericProperty() throws Exception {
        ConfigLoader config = new ConfigLoader();
        int port = config.getInt("smtp.port", 0);
        assertTrue(port > 0, "SMTP port must be greater than zero");
    }

    @Test
    void testConfigLoaderDoesNotThrow() {
        assertDoesNotThrow(() -> new ConfigLoader());
    }
}
