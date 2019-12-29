package com.home.sample.utils;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class PropertyUtils {
    private static final String PROFILE = "spring.profiles.active";

    @Bean(name = "props")
    public Properties loadProperties() throws IOException {
        final PropertiesFactoryBean pfb = new PropertiesFactoryBean();
        final String envFile = getEnvironmentFileName(pfb);    
        pfb.setLocations(new ClassPathResource("/application.properties"), new ClassPathResource(envFile));
        pfb.afterPropertiesSet();
        return pfb.getObject();
    }

    private String getEnvironmentFileName(final PropertiesFactoryBean pfb) throws IOException {
        pfb.setLocation(new ClassPathResource("/application.properties"));
        pfb.afterPropertiesSet();
        final String env = pfb.getObject().getProperty(PROFILE);
        return "/application-" +env+ ".properties";
    }
}