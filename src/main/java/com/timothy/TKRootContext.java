package com.timothy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Controller;

import java.io.IOException;


@ComponentScan(
        basePackages = "com.timothy",
        excludeFilters = { @ComponentScan.Filter(Controller.class) }
)
@Configuration
public class TKRootContext {
    public TKRootContext() {
        super();
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() throws IOException {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setFileEncoding("UTF-8");

        // classpath 상에 존재하는 리소스 등록
        MutablePropertySources propertySources = new MutablePropertySources();
        PropertySource<?> applicationProperties = new ResourcePropertySource(new ClassPathResource("application.properties"));
        propertySources.addLast(applicationProperties);
        configurer.setPropertySources(propertySources);
        return configurer;
    }
}
