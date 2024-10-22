package com.timothy;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.springframework.context.annotation.Bean;

public class TKLog4j2Config extends ConfigurationFactory {
    public TKLog4j2Config() {
        super();
    }

    @Override
    public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
        return null;
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[] { "*" };
    }

    public org.apache.logging.log4j.core.config.Configuration configureLog4j2() {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

        // 로그 레이아웃 페턴에 대한 설정
        LayoutComponentBuilder patternLayoutBuilder = builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%d{yyyy-MM-dd HH:mm:ss} %-5level %logger{36} - %msg%n");

        // 콘솔 출력에 대한 설정
        AppenderComponentBuilder console = builder.newAppender("stdout", "Console");
        console.addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
        console.add(patternLayoutBuilder);
        builder.add(console);

        // 로그 파일에 대한 설정
        AppenderComponentBuilder logFile = builder.newAppender("log", "File");
        logFile.addAttribute("fileName", "target/logging.log");

        logFile.add(patternLayoutBuilder);
        builder.add(logFile);

        // 롤링 로그 파일에 대한 설정
        AppenderComponentBuilder rollingLogFile = builder.newAppender("rollingLog", "RollingFile");

        //
        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(Level.INFO);
        rootLogger.add(builder.newAppenderRef("stdout"));
        builder.add(rootLogger);

        //
        LoggerComponentBuilder addtionalLogger = builder.newLogger("", Level.ERROR)


        return builder.build();
    }
}
