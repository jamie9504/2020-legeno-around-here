package wooteco.team.ittabi.legenoaroundhere.service.award;

import static wooteco.team.ittabi.legenoaroundhere.domain.ranking.RankingCriteria.LAST_MONTH;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.award.PopularityPostCreatorAward;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.ranking.RankingCriteria;
import wooteco.team.ittabi.legenoaroundhere.domain.util.AwardNameMaker;
import wooteco.team.ittabi.legenoaroundhere.dto.AwardResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostSearchRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.RankingRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorSimpleResponse;
import wooteco.team.ittabi.legenoaroundhere.repository.PopularityPostCreatorAwardRepository;
import wooteco.team.ittabi.legenoaroundhere.service.AreaService;
import wooteco.team.ittabi.legenoaroundhere.service.RankingService;
import wooteco.team.ittabi.legenoaroundhere.service.SectorService;

@Slf4j
@Service
@AllArgsConstructor
public class PopularPostAwardService {

    private final PopularityPostCreatorAwardRepository popularPostAwardRepository;
    private final RankingService rankingService;
    private final SectorService sectorService;
    private final AreaService areaService;

    List<AwardResponse> findPopularPostAwards(Long awardeeId) {
        return popularPostAwardRepository.findAllByAwardee_Id(awardeeId)
            .stream()
            .map(AwardResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public void createPopularPostAwards(LocalDateTime awardingTime) {
        List<SectorSimpleResponse> allAvailableSectors = sectorService.findAllAvailableSectors();
        List<Area> allAreas = areaService.findAllAreas();

        allAvailableSectors.forEach(sector ->
            allAreas.forEach(area ->
                createAwardAt(area, sector.getId(), 5, awardingTime, LAST_MONTH))
        );
    }

    /**
     * @param rankLimit : 몇위까지 가져올지. ex) Top3 를 원할 경우 rankLimit = 3
     */
    private void createAwardAt(Area area, Long sectorId, int rankLimit,
        LocalDateTime awardingTime, RankingCriteria rankingCriteria) {
        RankingRequest rankingRequest = new RankingRequest(LAST_MONTH.getCriteriaName());
        PostSearchRequest postSearchRequest = new PostSearchRequest(area.getId(),
            String.valueOf(sectorId));

        List<Post> posts = rankingService
            .searchRanking(postSearchRequest, rankingRequest, rankLimit);
        List<PostWithRank> postWithRankList = makePostWithRankingList(posts);

        postWithRankList.forEach(postWithRank -> givePopularPostAward(
            postWithRank.getPost(),
            postWithRank.getRank(),
            area,
            rankingCriteria,
            rankingCriteria.getStartDate(awardingTime).toLocalDate(),
            rankingCriteria.getEndDate(awardingTime).toLocalDate()
        ));
    }

    private List<PostWithRank> makePostWithRankingList(List<Post> postsSortedByMonthlyRanking) {
        List<PostWithRank> postWithRankList = new ArrayList<>();

        if (postsSortedByMonthlyRanking.isEmpty()) {
            return Collections.unmodifiableList(postWithRankList);
        }
        for (int index = 0; index < postsSortedByMonthlyRanking.size(); index++) {
            Post post = postsSortedByMonthlyRanking.get(index);
            int rank = index + 1;

            postWithRankList.add(new PostWithRank(rank, post));
        }
        return Collections.unmodifiableList(postWithRankList);
    }

    private void givePopularPostAward(Post popularPost, int ranking, Area targetArea,
        RankingCriteria rankingCriteria,
        LocalDate startDate, LocalDate endDate) {
        PopularityPostCreatorAward award = PopularityPostCreatorAward.builder()
            .name(AwardNameMaker.makePopularPostAwardName(
                popularPost, ranking, targetArea, startDate, endDate, rankingCriteria))
            .awardee(popularPost.getCreator())
            .post(popularPost)
            .ranking(ranking)
            .startDate(startDate)
            .endDate(endDate)
            .build();
        popularPostAwardRepository.save(award);

        log.info("인기글 상 수여 : {}", award.getName());
    }

    private static class PostWithRank {

        private int rank;
        private Post post;

        PostWithRank(int rank, Post post) {
            this.rank = rank;
            this.post = post;
        }

        int getRank() {
            return rank;
        }

        Post getPost() {
            return post;
        }
    }
}