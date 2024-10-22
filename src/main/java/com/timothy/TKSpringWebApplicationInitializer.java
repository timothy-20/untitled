package com.timothy;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class TKSpringWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
//    private static final String CHAR_ENCODING = "UTF-8";

    public TKSpringWebApplicationInitializer() {
        super();
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
