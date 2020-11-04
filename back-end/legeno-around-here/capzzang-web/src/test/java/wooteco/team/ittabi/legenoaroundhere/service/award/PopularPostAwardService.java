package wooteco.team.ittabi.legenoaroundhere.service.award;

import static wooteco.team.ittabi.legenoaroundhere.domain.ranking.RankingCriteria.LAST_MONTH;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wooteco.team.ittabi.legenoaroundhere.domain.ranking.RankingCriteria;
import wooteco.team.ittabi.legenoaroundhere.dto.AwardResponse;
import wooteco.team.ittabi.legenoaroundhere.repository.PopularPostAwardRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.SectorRepository;
import wooteco.team.ittabi.legenoaroundhere.service.AreaService;
import wooteco.team.ittabi.legenoaroundhere.service.RankingService;

@Slf4j
@Service
@AllArgsConstructor
class PopularPostAwardService {

    private static final int RANKING_LIMIT = 3;    // 3등까지 수상
    private static final RankingCriteria RANKING_CRITERIA_FOR_AWARD = RankingCriteria.LAST_MONTH;    // 월간 수상

    private final PopularPostAwardRepository popularPostAwardRepository;
    private final SectorRepository sectorRepository;
    private final RankingService rankingService;
    private final AreaService areaService;

    List<AwardResponse> findPopularPostAwards(Long awardeeId) {
        return popularPostAwardRepository.findAllByAwardee_Id(awardeeId)
            .stream()
            .map(AwardResponse::of)
            .collect(Collectors.toList());
    }
}
