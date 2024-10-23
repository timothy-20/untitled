package com.timothy;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

import java.io.IOException;

@Plugin(name = "TKLog4j2Config", category = ConfigurationFactory.CATEGORY)
@Order(50)
public class TKLog4j2Config extends ConfigurationFactory {
    public TKLog4j2Config() {
        super();
    }

    @Override
    public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

        // 로그 레이아웃 페턴에 대한 설정
        LayoutComponentBuilder patternLayoutBuilder = builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%d{yyyy-MM-dd HH:mm:ss} %-5level %logger{36} - %msg%n");

        // 콘솔 출력에 대한 설정
        AppenderComponentBuilder console = builder.newAppender("stdout", "Console");
        console.addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
        console.add(patternLayoutBuilder);
        builder.add(console);

//        // 롤링 로그 파일에 대한 설정
//        AppenderComponentBuilder rollingLogFile = builder.newAppender("log", "RollingFile");
//        rollingLogFile.addAttribute("fileName", "logs/record.log");
//        rollingLogFile.addAttribute("filePattern", "logs/record-%d{yyyy-MM-dd}-%i.log");
//        rollingLogFile.add(patternLayoutBuilder);
//
//        // 롤링 로그 파일에 대한 정책 설정
//        ComponentBuilder<AppenderComponentBuilder> rollingLogPolicies = builder.newComponent("Policies");
//        rollingLogPolicies.addComponent(builder.newComponent("CronTriggeringPolicy").addAttribute("schedule", "0 0 0 * * ?"));
//        rollingLogPolicies.addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "100MB"));
//        rollingLogFile.addComponent(rollingLogPolicies);
//
//        // 롤링 로그 파일에 대한 저장 방식(전략) 설정
//        ComponentBuilder<AppenderComponentBuilder> rollingLogStrategy = builder.newComponent("DefaultRolloverStrategy");
//        rollingLogStrategy.addAttribute("max", "10");
//        rollingLogStrategy.addAttribute("fileIndex", "min");
//        rollingLogStrategy.addAttribute("compressionLevel", "9");
//        rollingLogStrategy.addAttribute("compressionPattern", "logs/archived-record-%d{yyyy-MM-dd}-%i.zip");
//        rollingLogFile.addComponent(rollingLogStrategy);
//        builder.add(rollingLogFile);

        // 콘솔에 INFO 수준 로그가 기록되도록 루트 로거 구성
        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(Level.INFO);
        rootLogger.add(builder.newAppenderRef("stdout"));
        builder.add(rootLogger);

        // 롤링 로그에 DEBUG 수준 로그가 기록되도록 추가 로거 구성
//        LoggerComponentBuilder debugLogger = builder.newLogger("", Level.DEBUG);
//        debugLogger.add(builder.newAppenderRef("log"));
//        builder.add(debugLogger);

        try {
            builder.writeXmlConfiguration(System.out);

        } catch (IOException ie) {
            System.out.println(" ");
            return null;
        }

        System.out.println(" ");
        return builder.build();
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[] { "*" };
    }
}
