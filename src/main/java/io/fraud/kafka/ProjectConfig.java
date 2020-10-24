package io.fraud.kafka;

import org.aeonbits.owner.Config;

import static org.aeonbits.owner.Config.*;

@Sources({"classpath:config.properties"})
public interface ProjectConfig extends Config {
    @Key("app")
    String app();
    @Key("${app}.dbHost")
    String dbHost();

    @Key("${app}.dbPort")
    int dbPort();

    @Key("${app}.dbName")
    String dbName();

    @Key("${app}.dbUser")
    String dbUser();

    @Key("${app}.dbPassword")
    String dbPassword();

    @Key("${app}.kafkaBrokers")
    String kafkaBrokers();

    @Key("legitTopic")
    String legitTopic();

    @Key("fraudTopic")
    String fraudTopic();

    @Key("queuingTopic")
    String queuingTopic();
}
