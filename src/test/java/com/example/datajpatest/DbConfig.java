package com.example.datajpatest;

import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class DbConfig {
  @Bean
  @ConfigurationProperties("app.db")
  public DataSource dataSource() {
    return DataSourceBuilder.create().build();
  }
}
