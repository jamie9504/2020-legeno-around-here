package wooteco.team.ittabi.legenoaroundhere.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.post.image.PostImage;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

}
