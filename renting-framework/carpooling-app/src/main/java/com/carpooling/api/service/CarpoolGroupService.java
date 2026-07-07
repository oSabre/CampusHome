package com.carpooling.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.carpooling.api.dto.CarpoolGroupResponseDTO;
import com.carpooling.api.dto.DriverSummaryDTO;
import com.carpooling.api.model.CarUser;
import com.carpooling.api.model.CarpoolGroup;
import com.rentingframework.core.dto.MessageResponseDTO;
import com.rentingframework.core.exception.ResourceNotFoundException;
import com.rentingframework.core.model.Group;
import com.rentingframework.core.model.Message;
import com.rentingframework.core.repository.GroupRepository;
import com.rentingframework.core.service.GroupService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarpoolGroupService {

    private final GroupService groupService;
    private final GroupRepository groupRepository;

    public CarpoolGroupResponseDTO getGroupDtoByListingId(Long listingId) {
        Group group = groupRepository.findByListingId(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo não encontrado para este veículo."));
        return convertToDto((CarpoolGroup) group);
    }

    public CarpoolGroupResponseDTO getGroupDtoByDriverId(Long driverId) {
        List<Group> groups = groupService.getUserGroups(driverId);
        // Unlike Housing's "at most one group per student" assumption,
        // a driver can genuinely be in multiple pools at once - so this
        // returns every group the driver belongs to, not just the first.
        return groups.isEmpty() ? null : convertToDto((CarpoolGroup) groups.get(0));
    }

    public List<CarpoolGroupResponseDTO> getAllGroupsForDriver(Long driverId) {
        return groupService.getUserGroups(driverId).stream()
                .map(g -> convertToDto((CarpoolGroup) g))
                .collect(Collectors.toList());
    }

    public CarpoolGroupResponseDTO updateUsagePolicy(Long groupId, String newPolicy) {
        Group group = groupService.getGroupById(groupId);
        CarpoolGroup carpoolGroup = (CarpoolGroup) group;
        carpoolGroup.setUsagePolicy(newPolicy);
        CarpoolGroup saved = groupRepository.save(carpoolGroup);
        return convertToDto(saved);
    }

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

    private CarpoolGroupResponseDTO convertToDto(CarpoolGroup group) {
        List<DriverSummaryDTO> members = group.getMembers().stream()
                .map(member -> {
                    CarUser driver = (CarUser) member;
                    Integer xp = driver.getReputationScore() != null ? driver.getReputationScore() : 0;
                    return new DriverSummaryDTO(driver.getId(), driver.getName(), driver.getDriverLicenseNumber(), xp);
                })
                .collect(Collectors.toList());

        return new CarpoolGroupResponseDTO(
                group.getId(),
                group.getListing().getId(),
                group.getListing().getTitle(),
                group.getUsagePolicy(),
                members
        );
    }
}