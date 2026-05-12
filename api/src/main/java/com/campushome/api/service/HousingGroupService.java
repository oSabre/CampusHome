package com.campushome.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campushome.api.dto.HousingGroupResponseDTO;
import com.campushome.api.dto.ResidentDTO;
import com.campushome.api.model.Advertisement;
import com.campushome.api.model.HousingGroup;
import com.campushome.api.model.User;
import com.campushome.api.repository.HousingGroupRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HousingGroupService {
    
    private final HousingGroupRepository housingGroupRepository;

    @Transactional
    public void addResidentToGroup(Advertisement ad, User student){
        HousingGroup group = housingGroupRepository.findByAdvertisementId(ad.getId())
                .orElseGet(() -> {
                    HousingGroup newGroup = new HousingGroup();
                    newGroup.setAdvertisement(ad);
                    newGroup.setRules("Bem-vindos! Respeitem o espaço.");
                    return newGroup;
                });

        group.addResident(student);

        housingGroupRepository.save(group);
    }

    public HousingGroup getGroupDetailsByAd(Long adId) {
        return housingGroupRepository.findByAdvertisementId(adId)
                .orElseThrow(() -> new RuntimeException("Grupo de moradia não encontrado para este anúncio."));
    }

    @Transactional
    public HousingGroup updateRules(Long adId, String newRules) {
        HousingGroup group = getGroupDetailsByAd(adId);
        group.setRules(newRules);
        return housingGroupRepository.save(group);
    }

    private HousingGroupResponseDTO convertToDto(HousingGroup group) {
        // CORREÇÃO: Usar o método convertToResidentDTO que você criou lá embaixo!
        List<ResidentDTO> residentDtos = group.getResidents().stream()
            .map(this::convertToResidentDTO) // <--- Chama o método certo
            .collect(Collectors.toList());

        HousingGroupResponseDTO dto = new HousingGroupResponseDTO();
        dto.setId(group.getId());
        dto.setAdvertisementId(group.getAdvertisement().getId());
        dto.setAdvertisementTitle(group.getAdvertisement().getTitle());
        dto.setHouseRules(group.getRules());
        dto.setResidents(residentDtos);

        return dto;
    }

    public HousingGroupResponseDTO getGroupDtoByAdvertisementId(Long adId) {
        HousingGroup group = housingGroupRepository.findByAdvertisementId(adId)
            .orElseThrow(() -> new RuntimeException("Dashboard não encontrado para este anúncio"));
        
        return convertToDto(group);
    }

    public HousingGroupResponseDTO getGroupDtoByStudentId(Long studentId) {
        // 1. Tenta achar o grupo pelo ID do residente
        return housingGroupRepository.findByResidents_Id(studentId)
                .map(group -> convertToDto(group)) // Se achou, converte para DTO
                .orElse(null); // Se não achou, retorna null (o Controller tratará como 204)
    }

    @Transactional
    public void updateRulesById(Long groupId, String newRules) {
        HousingGroup group = housingGroupRepository.findById(groupId)
            .orElseThrow(() -> new RuntimeException("Grupo não encontrado"));
        group.setRules(newRules);
        housingGroupRepository.save(group);
    }

    private ResidentDTO convertToResidentDTO(User user) {
        ResidentDTO dto = new ResidentDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setCourse(user.getCourse());
        
        // IMPORTANTE: Se o xp no User for null, passe 0 para o DTO
        dto.setXp(user.getXp() != null ? user.getXp() : 0); 
        
        return dto;
    }

}
