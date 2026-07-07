package com.expert.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.expert.api.dto.ExpertGroupResponseDTO;
import com.expert.api.dto.ParticipantSummaryDTO;
import com.expert.api.enums.UserRole;
import com.expert.api.model.ExpertGroup;
import com.expert.api.model.ExpertUser;
import com.rentingframework.core.dto.MessageResponseDTO;
import com.rentingframework.core.exception.ResourceNotFoundException;
import com.rentingframework.core.model.Group;
import com.rentingframework.core.model.Message;
import com.rentingframework.core.model.User;
import com.rentingframework.core.repository.GroupRepository;
import com.rentingframework.core.service.GroupService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpertGroupService {

    private final GroupService groupService;
    private final GroupRepository groupRepository;

    public ExpertGroupResponseDTO getGroupDtoByListingId(Long listingId) {
        Group group = groupRepository.findByListingId(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo não encontrado para este anúncio."));
        return convertToDto((ExpertGroup) group);
    }

    // Plural, same reasoning as Carpooling's getAllGroupsForDriver - a
    // client can genuinely engage more than one specialist at once,
    // unlike Housing's "at most one house" assumption.
    public List<ExpertGroupResponseDTO> getAllGroupsForClient(Long clientId) {
        return groupService.getUserGroups(clientId).stream()
                .map(g -> convertToDto((ExpertGroup) g))
                .collect(Collectors.toList());
    }

    public ExpertGroupResponseDTO updateEngagementNotes(Long groupId, String notes) {
        Group group = groupService.getGroupById(groupId);
        ExpertGroup expertGroup = (ExpertGroup) group;
        expertGroup.setEngagementNotes(notes);
        ExpertGroup saved = groupRepository.save(expertGroup);
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

    /**
     * Handles 0 or 1 members gracefully - a freshly published listing's
     * group starts with nobody in it (unlike Carpooling's day-1 poster
     * membership), so this can be called before any engagement has been
     * accepted yet, and client/specialist will just come back null.
     */
    private ExpertGroupResponseDTO convertToDto(ExpertGroup group) {
        ParticipantSummaryDTO clientDto = null;
        ParticipantSummaryDTO specialistDto = null;

        for (User member : group.getMembers()) {
            ExpertUser expertUser = (ExpertUser) member;
            ParticipantSummaryDTO dto = toParticipantSummary(expertUser);
            if (expertUser.getRole() == UserRole.CLIENT) {
                clientDto = dto;
            } else if (expertUser.getRole() == UserRole.SPECIALIST) {
                specialistDto = dto;
            }
        }

        return new ExpertGroupResponseDTO(
                group.getId(),
                group.getListing().getId(),
                group.getListing().getTitle(),
                group.getEngagementNotes(),
                clientDto,
                specialistDto
        );
    }

    private ParticipantSummaryDTO toParticipantSummary(ExpertUser user) {
        Integer xp = user.getReputationScore() != null ? user.getReputationScore() : 0;
        String specialty = user.getRole() == UserRole.SPECIALIST ? user.getSpecialty() : null;
        return new ParticipantSummaryDTO(user.getId(), user.getName(), user.getRole(), specialty, xp);
    }
}