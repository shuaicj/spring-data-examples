package shuaicj.example.persist.hive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * Spring boot application.
 *
 * @author shuaicj 2017/04/12
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // @Bean
    // public DataSource dataSource() {
    //     return DataSourceBuilder.create()
    //             .driverClassName("org.apache.hive.jdbc.HiveDriver")
    //             .url("jdbc:hive2://localhost:10000/default")
    //             .username("hive")
    //             .password("hive")
    //             .build();
    // }
}

