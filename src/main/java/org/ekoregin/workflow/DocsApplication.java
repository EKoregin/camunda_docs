package org.ekoregin.workflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@EnableJdbcRepositories
public class DocsApplication {

  public static void main(String... args) {
    SpringApplication.run(DocsApplication.class, args);
  }
}