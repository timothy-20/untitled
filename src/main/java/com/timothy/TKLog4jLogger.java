package com.timothy;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.AppenderRefComponentBuilder;

import java.util.Map;

public interface TKLog4jLogger {
    boolean isRoot();
    Level getBaseLevel();
    AppenderComponentBuilder[] getAppenderComponentBuilders();
    AppenderRefComponentBuilder[] getAppenderRefComponentBuilders();
    Map<String, Object> getAttributes();
}
