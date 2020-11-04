package wooteco.team.ittabi.legenoaroundhere.domain.sector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_ANOTHER_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_ANOTHER_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_PASSWORD;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

class SectorTest {

    private User user;
    private User admin;

    @BeforeEach
    void setUp() {
        user = makeUser(UserConstants.TEST_USER_EMAIL, UserConstants.TEST_USER_NICKNAME,
            UserConstants.TEST_USER_PASSWORD);
        admin = makeUser(UserConstants.TEST_ADMIN_EMAIL, UserConstants.TEST_ADMIN_NICKNAME,
            UserConstants.TEST_ADMIN_PASSWORD);
    }

    private User makeUser(String email, String nickname, String password) {
        return User.builder()
            .email(email)
            .nickname(nickname)
            .password(password)
            .build();
    }

    private Sector makeSector(String name, String description, User user, SectorState state) {
        return Sector.builder()
            .name(name)
            .description(description)
            .creator(user)
            .lastModifier(user)
            .state(state)
            .build();
    }

    @DisplayName("생성자 - 예외 발생, 파라미터에 널이 들어갈 경우")
    @Test
    void constructor_ParameterNull_thrownException() {
        Assertions.assertThatThrownBy(
            () -> makeSector(null, SectorConstants.TEST_SECTOR_DESCRIPTION, user,
                SectorState.PUBLISHED));
        Assertions.assertThatThrownBy(
            () -> makeSector(SectorConstants.TEST_SECTOR_NAME, null, user, SectorState.PUBLISHED));
        Assertions.assertThatThrownBy(
            () -> makeSector(
                SectorConstants.TEST_SECTOR_NAME, SectorConstants.TEST_SECTOR_DESCRIPTION, null,
                SectorState.PUBLISHED));
        Assertions.assertThatThrownBy(
            () -> makeSector(
                SectorConstants.TEST_SECTOR_NAME, SectorConstants.TEST_SECTOR_DESCRIPTION, user,
                null));
    }

    @DisplayName("Sector의 내용(name, description, lastModifier)을 Update한다.")
    @Test
    void update() {
        Sector sector
            = makeSector(SectorConstants.TEST_SECTOR_NAME, SectorConstants.TEST_SECTOR_DESCRIPTION,
            user, SectorState.PUBLISHED);
        Sector expectedSector
            = makeSector(
            SectorConstants.TEST_SECTOR_ANOTHER_NAME,
            SectorConstants.TEST_SECTOR_ANOTHER_DESCRIPTION, admin,
            SectorState.APPROVED);

        sector.update(expectedSector);

        assertThat(sector.getName()).isEqualTo(expectedSector.getName());
        assertThat(sector.getDescription()).isEqualTo(expectedSector.getDescription());
        assertThat(sector.getCreator().getEmail())
            .isNotEqualTo(expectedSector.getCreator().getEmail());
        assertThat(sector.getLastModifier().getEmail())
            .isEqualTo(expectedSector.getLastModifier().getEmail());
        assertThat(sector.getState()).isNotEqualTo(expectedSector.getState());
    }

    @DisplayName("Sector의 상태를 Update 한다.")
    @Test
    void setState_Success() {
        Sector sector
            = makeSector(SectorConstants.TEST_SECTOR_NAME, SectorConstants.TEST_SECTOR_DESCRIPTION,
            user, SectorState.PUBLISHED);

        sector.setState(SectorState.APPROVED, "삭제", user);

        assertThat(sector.getName()).isEqualToIgnoringCase(SectorConstants.TEST_SECTOR_NAME);
        assertThat(sector.getState()).isEqualTo(SectorState.APPROVED.getName());
    }
}
