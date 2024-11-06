package com.timothy;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class TKSpringWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
//    private static final String CHAR_ENCODING = "UTF-8";

    public TKSpringWebApplicationInitializer() {
        super();
    }

    @Override
    public void onStartup(@NonNull ServletContext servletContext) throws ServletException {
        // log4j2 로거 사용 설정
        TKLog4jConfiguration log4jConfig = new TKLog4jConfiguration();
        Configurator.initialize(log4jConfig.getConfiguration(null, null));

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
