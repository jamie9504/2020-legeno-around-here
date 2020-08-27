package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.CommentConstants.TEST_COMMENT_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_EMPTY_IMAGES;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_POST_REPORT_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_POST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_PASSWORD;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.team.ittabi.legenoaroundhere.dto.PostCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.ReportCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;

public class ReportAcceptanceTest extends AcceptanceTest {

    private String adminToken;
    private String accessToken;
    private Long sectorId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        accessToken = tokenResponse.getAccessToken();
        adminToken = getCreateAdminToken();

        sectorId = createSector(adminToken, TEST_SECTOR_NAME);
    }

    /**
     * Feature: 글 신고 하기
     * <p>
     * Scenario: 신고 버튼을 누르고, 신고 내용을 작성한다.
     * <p>
     * Given 글이 등록되어 있다.
     * <p>
     * When 신고 버튼을 누르고 신고 내용을 작성한다. Then 글을 신고하는데 성공했다.
     */
    @DisplayName("글 신고하기")
    @Test
    void ReportPost() {
        String postLocation = createPostWithoutImage(accessToken);
        Long postId = getIdFromUrl(postLocation);

        // 글 신고하기
        createPostReport(accessToken, postId);
    }

    /**
     * Feature: 댓글 신고 하기
     * <p>
     * Scenario: 신고 버튼을 누르고, 신고 내용을 작성한다.
     * <p>
     * Given 댓글이 등록되어 있다.
     * <p>
     * When 신고 버튼을 누르고 신고 내용을 작성한다. Then 댓글을 신고하는데 성공했다.
     */
    @DisplayName("댓글 신고하기")
    @Test
    void ReportComment() {
        String postLocation = createPostWithoutImage(accessToken);
        Long postId = getIdFromUrl(postLocation);

        String commentLocation = createComment(postId, accessToken);
        Long commentId = getIdFromUrl(commentLocation);

        // 댓글 신고하기
        createCommentReport(accessToken, commentId);
    }

    /**
     * Feature: 사용자 신고 하기
     * <p>
     * Scenario: 신고 버튼을 누르고, 신고 내용을 작성한다.
     * <p>
     * Given 사용자가 등록되어 있다.
     * <p>
     * When 신고 버튼을 누르고 신고 내용을 작성한다. Then 사용자를 신고하는데 성공했다.
     */
    @DisplayName("사용자 신고하기")
    @Test
    void ReportUser() {
        // 사용자 신고하기
        createUserReport(accessToken, TEST_USER_ID);
    }


    private String getCreateAdminToken() {
        TokenResponse tokenResponse = login(TEST_ADMIN_EMAIL, TEST_ADMIN_PASSWORD);
        return tokenResponse.getAccessToken();
    }

    private Long createSector(String accessToken, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("description", TEST_SECTOR_DESCRIPTION);

        String location = given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/admin/sectors")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .header("Location");

        return getIdFromUrl(location);
    }

    private String createPostWithoutImage(String accessToken) {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);

        return given()
            .log().all()
            .header("X-AUTH-TOKEN", accessToken)
            .body(postCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/posts")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .header("Location");
    }

    private void createPostReport(String accessToken, Long postId) {
        ReportCreateRequest reportCreateRequest
            = new ReportCreateRequest(postId, TEST_POST_REPORT_WRITING);

        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(reportCreateRequest)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .post("/post-reports/")
            .then()
            .statusCode(HttpStatus.CREATED.value());
    }

    private String createComment(Long postId, String accessToken) {
        Map<String, String> params = new HashMap<>();
        params.put("writing", TEST_COMMENT_WRITING);

        return given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .post("/posts/" + postId + "/comments")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .header("Location");
    }


    private void createCommentReport(String accessToken, Long commentId) {
        ReportCreateRequest reportCreateRequest
            = new ReportCreateRequest(commentId, TEST_POST_REPORT_WRITING);

        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(reportCreateRequest)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .post("/comment-reports/")
            .then()
            .statusCode(HttpStatus.CREATED.value());
    }

    private void createUserReport(String accessToken, Long userId) {
        ReportCreateRequest reportCreateRequest
            = new ReportCreateRequest(userId, TEST_POST_REPORT_WRITING);

        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(reportCreateRequest)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .post("/user-reports/")
            .then()
            .statusCode(HttpStatus.CREATED.value());
    }
}
