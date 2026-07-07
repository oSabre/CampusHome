package com.campushome.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.campushome.api.dto.HousingGroupResponseDTO;
import com.campushome.api.dto.ResidentDTO;
import com.campushome.api.model.CampusUser;
import com.campushome.api.model.HousingGroup;
import com.rentingframework.core.dto.MessageResponseDTO;
import com.rentingframework.core.exception.ResourceNotFoundException;
import com.rentingframework.core.model.Group;
import com.rentingframework.core.model.Message;
import com.rentingframework.core.repository.GroupRepository;
import com.rentingframework.core.service.GroupService;

import lombok.RequiredArgsConstructor;

/**
 * GroupRepository is injected directly for the same reason as in
 * HousingMatchingStrategy: core's GroupService.getOrCreateGroupForListing()
 * creates a group as a side effect, which is wrong for a plain "does a
 * group already exist for this listing" read, and there's also no core
 * wrapper for saving an already-loaded, app-mutated Group back down.
 */
@Service
@RequiredArgsConstructor
public class HousingGroupService {

    private final GroupService groupService;
    private final GroupRepository groupRepository;

    public HousingGroupResponseDTO getGroupDtoByListingId(Long listingId) {
        Group group = groupRepository.findByListingId(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo não encontrado para este anúncio."));
        return convertToDto((HousingGroup) group);
    }

    public HousingGroupResponseDTO getGroupDtoByStudentId(Long studentId) {
        List<Group> groups = groupService.getUserGroups(studentId);
        if (groups.isEmpty()) {
            return null; // Controller trata como 204, igual ao original
        }
        // "No máximo uma república por estudante" é uma regra desta app,
        // não uma garantia do core — core.getUserGroups() pode retornar
        // vários grupos para outras apps (ex: um motorista em duas caronas).
        return convertToDto((HousingGroup) groups.get(0));
    }

    public HousingGroupResponseDTO updateRules(Long groupId, String newRules) {
        Group group = groupService.getGroupById(groupId);
        HousingGroup housingGroup = (HousingGroup) group;
        housingGroup.setRules(newRules);
        HousingGroup saved = groupRepository.save(housingGroup);
        return convertToDto(saved);
    }

    // Message is never subclassed per app, so building the DTO here
    // manually (rather than routing back through a core method) doesn't
    // risk losing any fields — same reasoning as core's own private
    // toMessageDto(), just not worth adding a second core entry point for.
    public MessageResponseDTO sendMessage(Long groupId, Long senderId, String content) {
        Message message = groupService.sendMessage(groupId, senderId, content);
        return new MessageResponseDTO(
                message.getId(),
                message.getGroup().getId(),
                message.getSender().getId(),
                message.getSender().getName(),
                message.getContent(),
                message.getSentAt()
        );
    }

    public List<MessageResponseDTO> getMessages(Long groupId) {
        return groupService.getGroupMessageDtos(groupId);
    }

    private HousingGroupResponseDTO convertToDto(HousingGroup group) {
        List<ResidentDTO> residents = group.getMembers().stream()
                .map(member -> {
                    CampusUser resident = (CampusUser) member;
                    Integer xp = resident.getReputationScore() != null ? resident.getReputationScore() : 0;
                    return new ResidentDTO(resident.getId(), resident.getName(), resident.getCourse(), xp);
                })
                .collect(Collectors.toList());

        return new HousingGroupResponseDTO(
                group.getId(),
                group.getListing().getId(),
                group.getListing().getTitle(),
                group.getRules(),
                residents
        );
    }
}