package com.solarsoft.facelift.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;

    static {
        try {
            // Load config.properties file
            properties = new Properties();
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties file", e);
        }
    }

    // Get String value
    public static String get(String key) {
        return properties.getProperty(key);
    }

    // Get int value
    public static int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }
}
