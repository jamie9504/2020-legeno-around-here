package wooteco.team.ittabi.legenoaroundhere.config;

import java.util.ArrayList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final String CONTROLLER_PACKAGE_NAME = "wooteco.team.ittabi.legenoaroundhere.controller";
    private static final String DEFAULT_TITLE = "Legeno Around Here ";
    private static final String ALL_ANT_PATTERN_FORMAT = "/**%s**";

    private String groupName;

    @Bean
    public Docket areaApiDocket() {
        groupName = UrlPathConstants.AREAS;
        return getDocket(groupName,
            String.format(ALL_ANT_PATTERN_FORMAT, UrlPathConstants.AREAS_PATH_WITH_SLASH));
    }

    @Bean
    public Docket awardApiDocket() {
        groupName = UrlPathConstants.AWARDS;
        return getDocket(groupName,
            String.format(ALL_ANT_PATTERN_FORMAT, UrlPathConstants.AWARDS_PATH_WITH_SLASH));
    }

    @Bean
    public Docket commentApiDocket() {
        groupName = UrlPathConstants.COMMENTS;
        return getDocket(groupName,
            String.format(ALL_ANT_PATTERN_FORMAT, UrlPathConstants.COMMENTS_PATH_WITH_SLASH));
    }

    @Bean
    public Docket commentReportApiDocket() {
        groupName = UrlPathConstants.COMMENT_REPORTS;
        return getDocket(groupName,
            String
                .format(ALL_ANT_PATTERN_FORMAT, UrlPathConstants.COMMENT_REPORTS_PATH_WITH_SLASH));
    }

    @Bean
    public Docket imageApiDocket() {
        groupName = UrlPathConstants.IMAGES;
        return getDocket(groupName,
            String.format(ALL_ANT_PATTERN_FORMAT, UrlPathConstants.IMAGES_PATH_WITH_SLASH));
    }

    @Bean
    public Docket mailAuthApiDocket() {
        groupName = UrlPathConstants.MAIL_AUTH;
        return getDocket(groupName,
            String.format(ALL_ANT_PATTERN_FORMAT, UrlPathConstants.MAIL_AUTH_PATH_WITH_SLASH));
    }

    @Bean
    public Docket meApiDocket() {
        groupName = UrlPathConstants.ME;
        return getDocket(groupName,
            String.format(ALL_ANT_PATTERN_FORMAT, UrlPathConstants.ME_PATH_WITH_SLASH));
    }

    @Bean
    public Docket noticeApiDocket() {
        groupName = UrlPathConstants.NOTIFICATION;
        return getDocket(groupName, String.format(ALL_ANT_PATTERN_FORMAT,
            UrlPathConstants.NOTIFICATION_PATH_WITH_SLASH));
    }

    @Bean
    public Docket postApiDocket() {
        groupName = UrlPathConstants.POSTS;
        return getDocket(groupName,
            String.format(ALL_ANT_PATTERN_FORMAT, UrlPathConstants.POSTS_PATH_WITH_SLASH));
    }

    @Bean
    public Docket postReportApiDocket() {
        groupName = UrlPathConstants.POST_REPORTS;
        return getDocket(groupName,
            String.format(ALL_ANT_PATTERN_FORMAT, UrlPathConstants.POST_REPORTS_PATH_WITH_SLASH));
    }

    @Bean
    public Docket profileApiDocket() {
        groupName = UrlPathConstants.PROFILE;
        return getDocket(groupName,
            String.format(ALL_ANT_PATTERN_FORMAT, UrlPathConstants.PROFILE_PATH_WITH_SLASH));
    }

    @Bean
    public Docket rankingApiDocket() {
        groupName = UrlPathConstants.RANKING;
        return getDocket(groupName,
            String.format(ALL_ANT_PATTERN_FORMAT, UrlPathConstants.RANKING_PATH_WITH_SLASH));
    }

    @Bean
    public Docket sectorApiDocket() {
        groupName = UrlPathConstants.SECTORS;
        return getDocket(groupName,
            String.format(ALL_ANT_PATTERN_FORMAT, UrlPathConstants.SECTORS_PATH_WITH_SLASH));
    }

    @Bean
    public Docket userApiDocket() {
        groupName = UrlPathConstants.USERS;
        return getDocket(groupName,
            String.format(ALL_ANT_PATTERN_FORMAT, UrlPathConstants.USERS_PATH_WITH_SLASH));
    }

    @Bean
    public Docket userReportApiDocket() {
        groupName = UrlPathConstants.USER_REPORTS;
        return getDocket(groupName,
            String.format(ALL_ANT_PATTERN_FORMAT, UrlPathConstants.USER_REPORTS_PATH_WITH_SLASH));
    }

    @Bean
    public Docket zzangApiDocket() {
        groupName = UrlPathConstants.ZZANGS;
        return getDocket(groupName,
            String.format(ALL_ANT_PATTERN_FORMAT, UrlPathConstants.ZZANGS_PATH_WITH_SLASH));
    }

    @Bean
    public Docket userImageApiDocket() {
        groupName = "userImages";
        return getDocket(groupName, "/user-images/**");
    }

    @Bean
    public Docket joinApiDocket() {
        groupName = "join";
        return getDocket(groupName, "/join/**");
    }

    @Bean
    public Docket loginApiDocket() {
        groupName = "login";
        return getDocket(groupName, "/login/**");
    }

    private Docket getDocket(String groupName, String groupUrl) {
        return new Docket(DocumentationType.SWAGGER_2)
            .useDefaultResponseMessages(false)
            .groupName(groupName)
            .select()
            .apis(RequestHandlerSelectors
                .basePackage(CONTROLLER_PACKAGE_NAME))
            .paths(PathSelectors.ant(groupUrl))
            .build()
            .apiInfo(apiInfo(DEFAULT_TITLE + groupName, groupName));
    }

    private ApiInfo apiInfo(String title, String version) {
        return new ApiInfo(
            title,
            "이따비의 팀 프로젝트 Legeno Around Here의 API Docs",
            version,
            "www.capzzang.co.kr",
            new Contact("Conetact Me", "www.capzzang.co.kr", "aegis1920@gmail.com"),
            "Ittabi Licenses",
            "www.capzzang.co.kr",
            new ArrayList<>()
        );
    }
}
