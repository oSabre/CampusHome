package com.campushome.api.controller;

import com.campushome.api.dto.HousingGroupResponseDTO;
import com.campushome.api.service.HousingGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/housing-groups")
@CrossOrigin(origins = "*")
public class HousingGroupController {
    
    @Autowired
    private HousingGroupService housingGroupService;

    @GetMapping("/advertisement/{adId}")
    public ResponseEntity<HousingGroupResponseDTO> getByAdvertisementId(@PathVariable Long adId) {
        HousingGroupResponseDTO dashboard = housingGroupService.getGroupDtoByAdvertisementId(adId);
        return ResponseEntity.ok(dashboard);
    }

    @PutMapping("/{id}/rules")
    public ResponseEntity<Void> updateRules(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String newRules = body.get("rules");
        housingGroupService.updateRulesById(id, newRules);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<HousingGroupResponseDTO> getByStudentId(@PathVariable Long studentId) {
        HousingGroupResponseDTO response = housingGroupService.getGroupDtoByStudentId(studentId);
    
        if (response == null) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        
        return ResponseEntity.ok(response);
    }
    
}
