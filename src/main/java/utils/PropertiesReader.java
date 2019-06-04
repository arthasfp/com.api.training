package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    public static final Logger logger = LoggerFactory.getLogger(PropertiesReader.class);

    public Properties getPropertiesByPath(final String resourcePath) {
        Properties properties = new Properties();
        InputStream inputStream;
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            inputStream = loader.getResourceAsStream(resourcePath);
            properties.load(inputStream);
        } catch (Exception e) {
            logger.debug("Failed to load property, error msg: {}", e.getMessage());
        }
        return properties;
    }

    public String resolvePropertyValue(final Properties properties, final String systemProperty, final String configProperty) {
        String varValue = System.getProperty(systemProperty);
        if (varValue == null) {
            varValue = properties.getProperty(configProperty);
        }
        return varValue;
    }
}
