package com.campushome.api.controller;

import com.campushome.api.dto.HousingGroupResponseDTO;
import com.campushome.api.service.HousingGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/housing-groups")
public class HousingGroupController {
    
    @Autowired
    private HousingGroupService housingGroupService;

    @GetMapping("/advertisement/{adId}")
    public ResponseEntity<HousingGroupResponseDTO> getByAdvertisementId(@PathVariable Long adId) {
        HousingGroupResponseDTO dashboard = housingGroupService.getGroupDtoByAdvertisementId(adId);
        return ResponseEntity.ok(dashboard);
    }

    @PutMapping("/{id}/rules")
    public ResponseEntity<Void> updateRules(@PathVariable Long id, @RequestBody String newRules) {
        housingGroupService.updateRules(id, newRules);
        return ResponseEntity.noContent().build();
    }
}
