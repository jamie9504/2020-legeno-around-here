package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.SectorState;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.AdminSectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotUniqueException;
import wooteco.team.ittabi.legenoaroundhere.repository.SectorRepository;

@Service
@AllArgsConstructor
public class SectorService {

    private final SectorRepository sectorRepository;

    @Transactional
    public SectorResponse createSector(SectorRequest sectorRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            Sector sector = sectorRepository
                .save(sectorRequest.toSector(user, SectorState.PUBLISHED));
            return SectorResponse.of(sector);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new NotUniqueException(
                    "Sector 이름 [" + sectorRequest.getName() + "](이)가 Unique하지 않습니다.");
            }
            throw e;
        }
    }

    public SectorResponse findInUseSector(Long id) {
        Sector sector = findUsedSectorBy(id);
        return SectorResponse.of(sector);
    }

    private Sector findUsedSectorBy(Long id) {
        Sector sector = findSectorBy(id);
        if (sector.isNotUsed()) {
            throw new NotExistsException(id + "에 해당하는 Sector가 존재하지 않습니다.");
        }
        return sector;
    }

    private Sector findSectorBy(Long id) {
        return sectorRepository.findById(id)
            .orElseThrow(() -> new NotExistsException(id + "에 해당하는 Sector가 존재하지 않습니다."));
    }

    public List<SectorResponse> findAllInUseSector() {
        List<Sector> sectors = sectorRepository.findAll()
            .stream()
            .filter(Sector::isUsed)
            .collect(Collectors.toList());

        return SectorResponse.listOf(sectors);
    }

    @Transactional
    public void updateSector(Long id, SectorRequest sectorRequest) {
        Sector sector = findUsedSectorBy(id);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        sector.update(sectorRequest.toSector(user, SectorState.PUBLISHED));
    }

    @Transactional
    public void deleteSector(Long id) {
        Sector sector = findUsedSectorBy(id);
        sector.setState(SectorState.DELETED);
    }

    public AdminSectorResponse findSector(Long id) {
        Sector sector = findSectorBy(id);
        return AdminSectorResponse.of(sector);
    }

    public List<AdminSectorResponse> findAllSector() {
        List<Sector> sectors = sectorRepository.findAll();
        return AdminSectorResponse.listOf(sectors);
    }
}
