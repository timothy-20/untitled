package com.timothy;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class TKSpringWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
//    private static final String CHAR_ENCODING = "UTF-8";

    public TKSpringWebApplicationInitializer() {
        super();
    }

    @Override
    public void onStartup(@NonNull ServletContext servletContext) throws ServletException {
        TKLog4j2Config log4j2Config = new TKLog4j2Config();
        Configurator.initialize(log4j2Config.getConfiguration(null, null));

        super.onStartup(servletContext);
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] {
                TKServletContext.class
        };
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] {
                TKRootContext.class,
                TKDatasourceContext.class
        };
    }

    @NonNull
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

//    @Override
//    protected Filter[] getServletFilters() {
//        final CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
//        encodingFilter.setEncoding(CHAR_ENCODING);
//        encodingFilter.setForceEncoding(true);
//        return new Filter[] {
//                encodingFilter
//        };
//    }
}
