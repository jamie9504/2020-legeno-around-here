package wooteco.team.ittabi.legenoaroundhere.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.report.CommentReport;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {

}
