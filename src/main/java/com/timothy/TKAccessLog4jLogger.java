package com.timothy;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import java.util.Map;

//logger.info("Current working directory: {}", System.getProperty("user.dir"));
public class TKAccessLog4jLogger implements TKLog4jLogger {
    public static final String ROLLING_APPENDER_NAME = "TKAccessLogRollingFileAppender";
    private final ConfigurationBuilder<BuiltConfiguration> builder;

    public TKAccessLog4jLogger(ConfigurationBuilder<BuiltConfiguration> builder) {
        super();
        this.builder = builder;
    }

    @Override
    public boolean isRoot() {
        return false;
    }

    @Override
    public Level getBaseLevel() {
        return Level.INFO;
    }

    @Override
    public AppenderComponentBuilder[] getAppenderComponentBuilders() {
        return new AppenderComponentBuilder[] {
                this.getRollingLogAppenderBuilder()
        };
    }

    @Override
    public AppenderRefComponentBuilder[] getAppenderRefComponentBuilders() {
        return new AppenderRefComponentBuilder[] {
                this.builder.newAppenderRef(TKAccessLog4jLogger.ROLLING_APPENDER_NAME)
        };
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    protected AppenderComponentBuilder getRollingLogAppenderBuilder() {
        // 롤링 로그 파일에 대한 설정
        AppenderComponentBuilder rollingLogFile = this.builder.newAppender(TKAccessLog4jLogger.ROLLING_APPENDER_NAME, "RollingFile");
        rollingLogFile.addAttribute("fileName", "logs/access/access.log");
        rollingLogFile.addAttribute("filePattern", "logs/access/access-%d{yyyy-MM-dd}-%i.log.gz");
        // 패턴 레이아웃 설정
        rollingLogFile.add(this.getPatternLayoutBuilder());

        // 동작할 로그 레벨 지정
        FilterComponentBuilder infoLevelFilter = this.builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.DENY);
        infoLevelFilter.addAttribute("level", Level.INFO);
        rollingLogFile.add(infoLevelFilter);

        // 롤링 로그 파일에 대한 정책 설정
        ComponentBuilder<AppenderComponentBuilder> rollingLogPolicies = this.builder.newComponent("Policies");
        rollingLogPolicies.addComponent(this.builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "300MB"));
        rollingLogFile.addComponent(rollingLogPolicies);

        // 롤링 로그 파일에 대한 저장 방식(전략) 설정
        ComponentBuilder<AppenderComponentBuilder> rollingLogStrategy = this.builder.newComponent("DefaultRolloverStrategy");
        rollingLogStrategy.addAttribute("max", "20");
        rollingLogStrategy.addAttribute("fileIndex", "min");
        rollingLogStrategy.addAttribute("compressionLevel", "9");
        rollingLogStrategy.addAttribute("tempCompressedFilePattern", "logs/access/archived-access-%d{yyyy-MM-dd}-%i.zip");
        rollingLogFile.addComponent(rollingLogStrategy);
        return rollingLogFile;
    }

    protected LayoutComponentBuilder getPatternLayoutBuilder() {
        LayoutComponentBuilder patternLayoutBuilder = this.builder.newLayout("PatternLayout");
        patternLayoutBuilder.addAttribute("pattern", "[%date{yyyy-MM-dd HH:mm:ss}, %nano] %-5level - %msg%n");
        return patternLayoutBuilder;
    }
}