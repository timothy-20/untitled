package com.timothy;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "TKLog4j2Config", category = ConfigurationFactory.CATEGORY)
@Order(50)
public class TKLog4j2Config extends ConfigurationFactory {
    private final ConfigurationBuilder<BuiltConfiguration> builder;
    private static final String CONFIGURATION_NAME = "TKLog4j2Config";
    private static final String CONSOLE_APPENDER_NAME = "ConsoleAppender";
    private static final String ROLLING_APPENDER_NAME = "RollingFileAppender";

    public TKLog4j2Config() {
        super();
        this.builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        this.builder.setStatusLevel(Level.DEBUG);
        this.builder.setConfigurationName(CONFIGURATION_NAME);
    }

    @Override
    public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
        this.builder.add(this.getConsoleAppenderBuilder());
        this.builder.add(this.getRollingLogAppenderBuilder());

        RootLoggerComponentBuilder rootLogger = this.builder.newRootLogger(Level.DEBUG);
        rootLogger.add(this.builder.newAppenderRef(CONSOLE_APPENDER_NAME));
        rootLogger.add(this.builder.newAppenderRef(ROLLING_APPENDER_NAME));
        rootLogger.addAttribute("additivity", false);
        this.builder.add(rootLogger);
        return this.builder.build(false);
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[] { "*" };
    }

    private AppenderComponentBuilder getConsoleAppenderBuilder() {
        // 콘솔 출력에 대한 설정
        AppenderComponentBuilder console = builder.newAppender(CONSOLE_APPENDER_NAME, "Console");
        console.addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
        // 기본 패턴 레이아웃 설정
        console.add(this.getDefaultPatternLayoutBuilder());

        // 동작할 로그 레벨 지정
        FilterComponentBuilder traceLevelFilter = this.builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.DENY);
        traceLevelFilter.addAttribute("level", Level.DEBUG);
        console.add(traceLevelFilter);
        return console;
    }

    private AppenderComponentBuilder getRollingLogAppenderBuilder() {
        // 롤링 로그 파일에 대한 설정
        AppenderComponentBuilder rollingLogFile = builder.newAppender(ROLLING_APPENDER_NAME, "RollingFile");
        rollingLogFile.addAttribute("fileName", "logs/record.log");
        rollingLogFile.addAttribute("filePattern", "logs/record-%d{yyyy-MM-dd}-%i.log");
        // 기본 패턴 레이아웃 설정
        rollingLogFile.add(this.getDefaultPatternLayoutBuilder());

        // 동작할 로그 레벨 지정
        FilterComponentBuilder infoLevelFilter = this.builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.DENY);
        infoLevelFilter.addAttribute("level", Level.INFO);
        rollingLogFile.add(infoLevelFilter);

        // 롤링 로그 파일에 대한 정책 설정
        ComponentBuilder<AppenderComponentBuilder> rollingLogPolicies = builder.newComponent("Policies");
        rollingLogPolicies.addComponent(builder.newComponent("CronTriggeringPolicy").addAttribute("schedule", "0 0 0 * * ?"));
        rollingLogPolicies.addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "1KB"));
        rollingLogFile.addComponent(rollingLogPolicies);

        // 롤링 로그 파일에 대한 저장 방식(전략) 설정
        ComponentBuilder<AppenderComponentBuilder> rollingLogStrategy = builder.newComponent("DefaultRolloverStrategy");
        rollingLogStrategy.addAttribute("max", "10");
        rollingLogStrategy.addAttribute("fileIndex", "min");
        rollingLogStrategy.addAttribute("compressionLevel", "9");
        rollingLogStrategy.addAttribute("compressionPattern", "logs/archived-record-%d{yyyy-MM-dd}-%i.zip");
        rollingLogFile.addComponent(rollingLogStrategy);
        return rollingLogFile;
    }

    private LayoutComponentBuilder getDefaultPatternLayoutBuilder() {
        // 로그 레이아웃 페턴에 대한 설정
        LayoutComponentBuilder patternLayoutBuilder = this.builder.newLayout("PatternLayout");
        patternLayoutBuilder.addAttribute("pattern", "%d{yyyy-MM-dd HH:mm:ss} %-5level %logger{36} - %msg%n");
        return patternLayoutBuilder;
    }
}
