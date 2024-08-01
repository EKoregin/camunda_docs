package org.ekoregin.workflow.config;

import liquibase.integration.spring.SpringLiquibase;
import org.camunda.bpm.spring.boot.starter.configuration.Ordering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;

@Order(Ordering.DEFAULT_ORDER - 1)
@Configuration
public class LiquibaseConfiguration {

    @Autowired
    private DataSource dataSource;

    @Value("${spring.liquibase.change-log}")
    private String defaultLiquibaseChangelog;

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(defaultLiquibaseChangelog);
        return liquibase;
    }
}
