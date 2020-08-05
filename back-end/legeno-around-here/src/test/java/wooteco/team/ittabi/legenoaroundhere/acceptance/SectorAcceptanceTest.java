package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_ADMIN_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_ADMIN_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_ADMIN_PASSWORD;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_SECTOR_ANOTHER_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_SECTOR_ANOTHER_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_SECTOR_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_SECTOR_NAME;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.SectorState;
import wooteco.team.ittabi.legenoaroundhere.dto.AdminSectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/init-table.sql")
public class SectorAcceptanceTest {

    @LocalServerPort
    public int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * Feature: 부문 관리
     * <p>
     * Scenario: 관리자가 부문을 관리한다.
     * <p>
     * Given 관리자가 로그인 되어있다.
     * <p>
     * When 부문을 등록한다. Then 부문이 등록되었다.
     * <p>
     * When 부문을 조회한다. Then 부문이 조회된다.
     * <p>
     * When 부문을 수정한다. Then 부문이 수정된다.
     * <p>
     * Given 부문을 추가로 등록한다. When 사용중인 부문을 전체 조회한다. Then 사용중인 부문이 전체 조회된다.
     * <p>
     * When 부문을 삭제한다. Then 부문이 삭제되었다. 전체 조회시 조회된다. 사용중인 건만 조회시 조회되지 않는다.
     */
    @DisplayName("관리자의 부문 관리")
    @Test
    void manageSector_admin() {
        // 관리자 로그인
        createAdmin(TEST_ADMIN_EMAIL, TEST_ADMIN_NICKNAME, TEST_ADMIN_PASSWORD);
        TokenResponse tokenResponse = login(TEST_ADMIN_EMAIL, TEST_ADMIN_PASSWORD);
        String accessToken = tokenResponse.getAccessToken();

        // 부문 등록
        Long id = createSector(accessToken, TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);

        // 사용 중인 부문 조회
        SectorResponse sectorResponse = findInUseSector(accessToken, id);
        assertThat(sectorResponse.getId()).isEqualTo(id);
        assertThat(sectorResponse.getName()).isEqualToIgnoringCase(TEST_SECTOR_NAME);
        assertThat(sectorResponse.getDescription()).isEqualTo(TEST_SECTOR_DESCRIPTION);
        assertThat(sectorResponse.getCreator()).isNotNull();

        // Admin을 위한 부문 조회(상세)
        AdminSectorResponse adminSectorResponse = findSector(accessToken, id);
        assertThat(adminSectorResponse.getId()).isEqualTo(id);
        assertThat(adminSectorResponse.getName()).isEqualToIgnoringCase(TEST_SECTOR_NAME);
        assertThat(adminSectorResponse.getDescription()).isEqualTo(TEST_SECTOR_DESCRIPTION);
        assertThat(adminSectorResponse.getCreator()).isNotNull();
        assertThat(adminSectorResponse.getLastModifier()).isNotNull();
        assertThat(adminSectorResponse.getState()).isEqualTo(SectorState.PUBLISHED.name());

        // 부문 수정
        updateSector(accessToken, id, TEST_SECTOR_ANOTHER_NAME, TEST_SECTOR_ANOTHER_DESCRIPTION);

        // 사용 중인 부문 조회
        sectorResponse = findInUseSector(accessToken, id);
        assertThat(sectorResponse.getId()).isEqualTo(id);
        assertThat(sectorResponse.getName()).isEqualToIgnoringCase(TEST_SECTOR_ANOTHER_NAME);
        assertThat(sectorResponse.getDescription()).isEqualTo(TEST_SECTOR_ANOTHER_DESCRIPTION);
        assertThat(sectorResponse.getCreator()).isNotNull();

        // Admin을 위한 부문 조회(상세)
        adminSectorResponse = findSector(accessToken, id);
        assertThat(adminSectorResponse.getId()).isEqualTo(id);
        assertThat(adminSectorResponse.getName())
            .isEqualToIgnoringCase(TEST_SECTOR_ANOTHER_NAME);
        assertThat(adminSectorResponse.getDescription())
            .isEqualTo(TEST_SECTOR_ANOTHER_DESCRIPTION);
        assertThat(adminSectorResponse.getCreator()).isNotNull();
        assertThat(adminSectorResponse.getLastModifier()).isNotNull();
        assertThat(adminSectorResponse.getState()).isEqualTo(SectorState.PUBLISHED.name());

        // 부문 전체 조회 + 사용하는 부문 전체 조회
        Long anotherId = createSector(accessToken, TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);
        List<AdminSectorResponse> adminSectorResponses = findAllSector(accessToken);
        List<SectorResponse> sectorResponses = findAllInUseSector(accessToken);
        assertThat(adminSectorResponses).hasSize(2);
        assertThat(sectorResponses).hasSize(2);

        // 부문 삭제
        deleteSector(accessToken, id);
        assertThatThrownBy(() -> findInUseSector(accessToken, id));
        adminSectorResponse = findSector(accessToken, id);
        assertThat(adminSectorResponse.getState()).isEqualTo(SectorState.DELETED.name());

        adminSectorResponses = findAllSector(accessToken);
        sectorResponses = findAllInUseSector(accessToken);
        assertThat(adminSectorResponses).hasSize(2);
        assertThat(sectorResponses).hasSize(1);

        deleteSector(accessToken, anotherId);
        adminSectorResponses = findAllSector(accessToken);
        sectorResponses = findAllInUseSector(accessToken);
        assertThat(adminSectorResponses).hasSize(2);
        assertThat(sectorResponses).hasSize(0);
    }

    private String createAdmin(String email, String nickname, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("nickname", nickname);
        params.put("password", password);

        return given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/joinAdmin")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .header("Location");
    }

    private TokenResponse login(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/login")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract().as(TokenResponse.class);
    }

    private Long createSector(String accessToken, String sectorName, String sectorDescription) {
        Map<String, String> params = new HashMap<>();
        params.put("name", sectorName);
        params.put("description", sectorDescription);

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

    private Long getIdFromUrl(String location) {
        int lastIndex = location.lastIndexOf("/");
        return Long.valueOf(location.substring(lastIndex + 1));
    }

    private AdminSectorResponse findSector(String accessToken, Long id) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/admin/sectors/" + id)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(AdminSectorResponse.class);
    }

    private SectorResponse findInUseSector(String accessToken, Long id) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/sectors/" + id)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(SectorResponse.class);
    }

    private void updateSector(String accessToken, Long id,
        String sectorName, String sectorDescription) {
        Map<String, String> params = new HashMap<>();
        params.put("name", sectorName);
        params.put("description", sectorDescription);

        given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .put("/admin/sectors/" + id)
            .then()
            .statusCode(HttpStatus.OK.value());
    }

    private List<AdminSectorResponse> findAllSector(String accessToken) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/admin/sectors")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList(".", AdminSectorResponse.class);
    }

    private List<SectorResponse> findAllInUseSector(String accessToken) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/sectors")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList(".", SectorResponse.class);
    }

    private void deleteSector(String accessToken, Long id) {
        given()
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .delete("/admin/sectors/" + id)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
