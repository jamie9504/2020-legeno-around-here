package wooteco.team.ittabi.legenoaroundhere.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_PASSWORD;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Email;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Nickname;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Password;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.exception.UserInputException;

public class PostTest {

    private User user = new User(
        new Email(TEST_EMAIL),
        new Nickname(TEST_NICKNAME),
        new Password(TEST_PASSWORD)
    );

    @DisplayName("길이 검증 - 예외 발생")
    @Test
    void validateLength_OverLength_ThrownException() {
        String overLengthInput = "aaaaaaaaaaaaaaaaaaaaa";
        assertThatThrownBy(() -> new Post(user, overLengthInput))
            .isInstanceOf(UserInputException.class);
    }

    @DisplayName("같은 상태인지 확인")
    @Test
    void isSameState_SameState_True() {
        Post post = new Post(user, "Hello!");
        post.setState(State.DELETED);

        assertThat(post.isSameState(State.DELETED)).isTrue();
    }

    @DisplayName("다른 상태인지 확인")
    @Test
    void isNotSameState_DifferentState_True() {
        Post post = new Post(user, "Hello!");
        post.setState(State.DELETED);

        assertThat(post.isNotSameState(State.PUBLISHED)).isTrue();
    }
}
