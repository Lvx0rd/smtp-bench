package com.smtpbench.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final String CONFIG_FILE = "configuration.properties";
    private final Properties properties = new Properties();

    public ConfigLoader() throws IOException {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IOException("File di configurazione non trovato: " + CONFIG_FILE);
            }
            properties.load(input);
        }
        checkRequiredProperties();
    }

    private void checkRequiredProperties() {
        String[] required = {
            "smtp.host", "smtp.port", "smtp.username", "smtp.password"
        };
        for (String key : required) {
            String value = properties.getProperty(key);
            if (value == null || value.trim().isEmpty()) {
                System.err.println("WARNING: La property obbligatoria '" + key + "' è mancante o vuota!");
            }
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
