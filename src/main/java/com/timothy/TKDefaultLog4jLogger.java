package com.timothy;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import java.util.Map;

public class TKDefaultLog4jLogger implements TKLog4jLogger{
    public static final String CONSOLE_APPENDER_NAME = "TKDefaultLogConsoleAppender";
    public static final String ROLLING_APPENDER_NAME = "TKDefaultLogRollingFileAppender";
    private final ConfigurationBuilder<BuiltConfiguration> builder;

    public TKDefaultLog4jLogger(final ConfigurationBuilder<BuiltConfiguration> builder) {
        super();
        this.builder = builder;
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @Override
    public Level getBaseLevel() {
        return Level.DEBUG;
    }

    @Override
    public AppenderComponentBuilder[] getAppenderComponentBuilders() {
        return new AppenderComponentBuilder[] {
                this.getConsoleAppenderBuilder(),
                this.getRollingLogAppenderBuilder()
        };
    }

    @Override
    public AppenderRefComponentBuilder[] getAppenderRefComponentBuilders() {
        return new AppenderRefComponentBuilder[] {
                this.builder.newAppenderRef(TKDefaultLog4jLogger.CONSOLE_APPENDER_NAME),
                this.builder.newAppenderRef(TKDefaultLog4jLogger.ROLLING_APPENDER_NAME)
        };
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    protected AppenderComponentBuilder getConsoleAppenderBuilder() {
        // 콘솔 출력에 대한 설정
        AppenderComponentBuilder console = this.builder.newAppender(TKDefaultLog4jLogger.CONSOLE_APPENDER_NAME, "Console");
        console.addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
        // 패턴 레이아웃 설정
        console.add(this.getStyledPatternLayoutBuilder());

        // 동작할 로그 레벨 지정
        FilterComponentBuilder traceLevelFilter = this.builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.DENY);
        traceLevelFilter.addAttribute("level", Level.DEBUG);
        console.add(traceLevelFilter);
        return console;
    }

    protected AppenderComponentBuilder getRollingLogAppenderBuilder() {
        // 롤링 로그 파일에 대한 설정
        AppenderComponentBuilder rollingLogFile = this.builder.newAppender(TKDefaultLog4jLogger.ROLLING_APPENDER_NAME, "RollingFile");
        rollingLogFile.addAttribute("fileName", "logs/record.log");
        rollingLogFile.addAttribute("filePattern", "logs/record-%d{yyyy-MM-dd}-%i.log.gz");
        // 패턴 레이아웃 설정
        rollingLogFile.add(this.getDefaultPatternLayoutBuilder());

        // 동작할 로그 레벨 지정
        FilterComponentBuilder infoLevelFilter = this.builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.DENY);
        infoLevelFilter.addAttribute("level", Level.INFO);
        rollingLogFile.add(infoLevelFilter);

        // 롤링 로그 파일에 대한 정책 설정
        ComponentBuilder<AppenderComponentBuilder> rollingLogPolicies = this.builder.newComponent("Policies");
        rollingLogPolicies.addComponent(this.builder.newComponent("CronTriggeringPolicy").addAttribute("schedule", "0 0 0 * * ?"));
        rollingLogPolicies.addComponent(this.builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "100MB"));
        rollingLogFile.addComponent(rollingLogPolicies);

        // 롤링 로그 파일에 대한 저장 방식(전략) 설정
        ComponentBuilder<AppenderComponentBuilder> rollingLogStrategy = this.builder.newComponent("DefaultRolloverStrategy");
        rollingLogStrategy.addAttribute("max", "10");
        rollingLogStrategy.addAttribute("fileIndex", "min");
        rollingLogStrategy.addAttribute("compressionLevel", "9");
        rollingLogStrategy.addAttribute("tempCompressedFilePattern", "logs/archived-record-%d{yyyy-MM-dd}-%i.zip");
        rollingLogFile.addComponent(rollingLogStrategy);
        return rollingLogFile;
    }

    protected LayoutComponentBuilder getDefaultPatternLayoutBuilder() {
        LayoutComponentBuilder patternLayoutBuilder = this.builder.newLayout("PatternLayout");
        patternLayoutBuilder.addAttribute("pattern", "[%date{yyyy-MM-dd HH:mm:ss}] %-5level - %class %method:%line - %msg%n");
        return patternLayoutBuilder;
    }

    protected LayoutComponentBuilder getStyledPatternLayoutBuilder() {
        LayoutComponentBuilder patternLayoutBuilder = this.builder.newLayout("PatternLayout");
        patternLayoutBuilder.addAttribute("pattern", "[%style{%date{yyyy-MM-dd HH:mm:ss}}{underline}] %highlight{%-5level}{FATAL=red, ERROR=yellow, WARN=green, INFO=blue, DEBUG=black} - %class %method:%line - %msg%n");
        return patternLayoutBuilder;
    }
}