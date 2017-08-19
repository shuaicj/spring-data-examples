package shuaicj.example.persist.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Spring boot application.
 *
 * @author shuaicj 2017/02/03
 */
@SpringBootApplication
@EnableJpaRepositories("shuaicj.example.persist.jpa")
@EntityScan("shuaicj.example.persist.jpa")
@EnableJpaAuditing
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

