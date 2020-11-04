package wooteco.team.ittabi.legenoaroundhere.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AwardService {

    private final PopularPostAwardRepository popularPostAwardRepository;
    private final SectorRepository sectorRepository;
    private final RankingService rankingService;
    private final AreaService areaService;

    @Transactional
    void createPopularPostAwards(LocalDateTime awardingTime) {
        List<Sector> allAvailableSectors = sectorRepository.findAllByStateIn(
            SectorState.getAllAvailable());
        List<Area> allAreas = areaService.findAllAreas();

        allAvailableSectors.forEach(sector ->
            allAreas.forEach(area -> createAwardAt(
                area, sector.getId(), RANKING_LIMIT, awardingTime, RANKING_CRITERIA_FOR_AWARD))
        );
    }

    private void createAwardAt(Area area, Long sectorId, int rankingLimit,
        LocalDateTime awardingTime, RankingCriteria rankingCriteria) {
        RankingRequest rankingRequest = new RankingRequest(
            RankingCriteria.LAST_MONTH.getCriteriaName());
        PostSearchRequest postSearchRequest = new PostSearchRequest(area.getId(),
            String.valueOf(sectorId));

        List<Post> posts = rankingService
            .searchRanking(postSearchRequest, rankingRequest, rankingLimit);
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
        PopularPostAward award = PopularPostAward.builder()
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
