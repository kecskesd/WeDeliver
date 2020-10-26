package reminderserver.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;

@EntityScan(basePackages = "reminderserver")
@EnableJpaRepositories(basePackages = "reminderserver" )
@SpringBootApplication( scanBasePackages = "reminderserver")

public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

    }

}
