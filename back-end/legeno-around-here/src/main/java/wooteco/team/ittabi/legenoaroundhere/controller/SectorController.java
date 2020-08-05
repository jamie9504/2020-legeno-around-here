package wooteco.team.ittabi.legenoaroundhere.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.service.SectorService;

@RestController
@RequestMapping("/sectors")
@AllArgsConstructor
public class SectorController {

    private final SectorService sectorService;

    @GetMapping("/{id}")
    public ResponseEntity<SectorResponse> findInUseSector(@PathVariable Long id) {
        SectorResponse sector = sectorService.findInUseSector(id);
        return ResponseEntity
            .ok(sector);
    }

    @GetMapping
    public ResponseEntity<List<SectorResponse>> findAllInUseSector() {
        List<SectorResponse> sectors = sectorService.findAllInUseSector();
        return ResponseEntity
            .ok(sectors);
    }
}
