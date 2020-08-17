package wooteco.team.ittabi.legenoaroundhere;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LegenoAroundHereApplication {

    private static final String APPLICATION_LOCATIONS = "spring.config.location="
        + "classpath:application.properties,"
        + "classpath:application-local.properties,"
        + "/app/config/back/profile.yml,"
        + "/app/config/back/setting.properties";

    public static void main(String[] args) {
        new SpringApplicationBuilder(LegenoAroundHereApplication.class)
            .properties(APPLICATION_LOCATIONS)
            .run(args);
    }
}
