package com.timothy;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.*;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "TKLog4jConfiguration", category = ConfigurationFactory.CATEGORY)
@Order(50)
public class TKLog4jConfiguration extends ConfigurationFactory {
    public static final String CONFIGURATION_NAME = "TKLog4jConfiguration";
    protected final ConfigurationBuilder<BuiltConfiguration> builder;
    protected final TKLog4jLogger[] loggers;

    public TKLog4jConfiguration() {
        super();
        this.builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        this.builder.setStatusLevel(Level.DEBUG);
        this.builder.setConfigurationName(CONFIGURATION_NAME);
        this.loggers = new TKLog4jLogger[] {
                new TKDefaultLog4jLogger(this.builder),
                new TKAccessLog4jLogger(this.builder)
        };
    }

    @Override
    public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
        for (TKLog4jLogger logger : loggers) {
            for (AppenderComponentBuilder appender : logger.getAppenderComponentBuilders()) {
                this.builder.add(appender);
            }

            if (logger.isRoot()) {
                RootLoggerComponentBuilder rootLogger = this.builder.newRootLogger(logger.getBaseLevel());

                for (AppenderRefComponentBuilder appenderRef : logger.getAppenderRefComponentBuilders()) {
                    rootLogger.add(appenderRef);
                }

                logger.getAttributes().forEach(rootLogger::addAttribute);
                this.builder.add(rootLogger);

            } else {
                LoggerComponentBuilder customLogger = this.builder.newLogger(logger.getClass().getName(), logger.getBaseLevel());

                for (AppenderRefComponentBuilder appenderRef : logger.getAppenderRefComponentBuilders()) {
                    customLogger.add(appenderRef);
                }

                logger.getAttributes().forEach(customLogger::addAttribute);
                customLogger.addAttribute("additivity", false);
                this.builder.add(customLogger);
            }
        }

        return this.builder.build(false);
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[] { "*" };
    }
}
