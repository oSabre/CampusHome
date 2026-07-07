package com.rentingframework.core.service;

import com.rentingframework.core.dto.GroupResponseDTO;
import com.rentingframework.core.dto.MessageResponseDTO;
import com.rentingframework.core.dto.UserSummaryDTO;
import com.rentingframework.core.exception.ResourceNotFoundException;
import com.rentingframework.core.model.Group;
import com.rentingframework.core.model.Listing;
import com.rentingframework.core.model.Message;
import com.rentingframework.core.model.User;
import com.rentingframework.core.repository.GroupRepository;
import com.rentingframework.core.repository.ListingRepository;
import com.rentingframework.core.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final MessageRepository messageRepository;
    private final ListingRepository listingRepository;
    private final UserService userService;

    /**
     * Tenta buscar o grupo do anúncio. Se for a primeira pessoa sendo aceita
     * (ou seja, o grupo ainda não existe), ele cria e salva o grupo vazio.
     *
     * WARNING for every app built on this: the fallback below can only ever
     * instantiate the plain core Group, never an app's subclass (e.g.
     * HousingGroup) — core has no knowledge of subclasses. If this fallback
     * fires, the row is saved with no matching row in the app's own table,
     * and any later cast to the app's Group subclass throws
     * ClassCastException. Each app MUST pre-create its own Group subclass
     * at Listing-creation time (not wait for the first accepted Request) so
     * this fallback never actually runs in practice. Treat this path as a
     * safety net for a bug elsewhere, not a supported way to create groups.
     */
    @Transactional
    public Group getOrCreateGroupForListing(Long listingId) {
        return groupRepository.findByListingId(listingId)
                .orElseGet(() -> {
                    Listing listing = listingRepository.findById(listingId)
                            .orElseThrow(() -> new ResourceNotFoundException("Anúncio não encontrado."));
                    
                    Group newGroup = new Group();
                    newGroup.setListing(listing);
                    // O @PrePersist da entidade cuidará do createdAt
                    return groupRepository.save(newGroup);
                });
    }

    /**
     * Busca um grupo pelo próprio ID do grupo (não o ID do anúncio/listing).
     * Existe separado de getOrCreateGroupForListing porque os dois IDs não
     * são intercambiáveis — qualquer serviço que já tenha o groupId em mãos
     * (como TaskService) deve usar este método, não o outro.
     */
    @Transactional(readOnly = true)
    public Group getGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo não encontrado."));
    }

    /**
     * Adiciona um usuário ao grupo. A entidade Group já possui a lógica 
     * interna para evitar duplicação (não adiciona se já estiver na lista).
     */
    @Transactional
    public void addMemberToGroup(Long groupId, Long userId) {
        Group group = getGroupById(groupId);
        User user = userService.findById(userId);

        group.addMember(user);
        groupRepository.save(group);
    }

    /**
     * Registra uma nova mensagem no fórum/chat do ativo.
     */
    @Transactional
    public Message sendMessage(Long groupId, Long senderId, String content) {
        Group group = getGroupById(groupId);
        User sender = userService.findById(senderId);

        // Barreira de segurança: Apenas membros podem interagir no grupo
        if (!group.getMembers().contains(sender)) {
            throw new RuntimeException("Acesso negado: Apenas membros do grupo podem enviar mensagens.");
        }

        Message message = new Message();
        message.setGroup(group);
        message.setSender(sender);
        message.setContent(content);

        return messageRepository.save(message);
    }

    /**
     * Recupera a thread de mensagens de um ativo específico,
     * garantindo a ordem cronológica para o frontend.
     */
    @Transactional(readOnly = true)
    public List<Message> getGroupMessages(Long groupId) {
        return messageRepository.findByGroupIdOrderBySentAtAsc(groupId);
    }

    /**
     * Mesma coisa que getGroupMessages, mas já convertida para o DTO —
     * use esse método nos controllers, o outro é mais para uso interno
     * do core (ex: futuras regras que precisem da entidade completa).
     */
    @Transactional(readOnly = true)
    public List<MessageResponseDTO> getGroupMessageDtos(Long groupId) {
        return getGroupMessages(groupId).stream()
                .map(this::toMessageDto)
                .collect(Collectors.toList());
    }

    /**
     * Versão em DTO de um Group — cobre só a parte genérica (membros,
     * listing, data de criação). Se a subclasse do app tiver campos
     * extras (regras da casa, log de manutenção), o app monta o DTO
     * dele próprio em volta desse, em vez de estender este método.
     */
    @Transactional(readOnly = true)
    public GroupResponseDTO getGroupDto(Long groupId) {
        Group group = getGroupById(groupId);
        return toGroupResponseDto(group);
    }

    private GroupResponseDTO toGroupResponseDto(Group group) {
        List<UserSummaryDTO> memberDtos = group.getMembers().stream()
                .map(this::toUserSummaryDto)
                .collect(Collectors.toList());

        return new GroupResponseDTO(
                group.getId(),
                group.getListing() != null ? group.getListing().getId() : null,
                memberDtos,
                group.getCreatedAt()
        );
    }

    private UserSummaryDTO toUserSummaryDto(User user) {
        return new UserSummaryDTO(user.getId(), user.getName(), user.getReputationScore());
    }

    private MessageResponseDTO toMessageDto(Message message) {
        return new MessageResponseDTO(
                message.getId(),
                message.getGroup().getId(),
                message.getSender().getId(),
                message.getSender().getName(),
                message.getContent(),
                message.getSentAt()
        );
    }
    
    /**
     * Permite que o frontend liste todos os grupos/chats ativos de um usuário.
     * Útil para a tela inicial do usuário (Meus Imóveis / Minhas Caronas).
     */
    @Transactional(readOnly = true)
    public List<Group> getUserGroups(Long userId) {
        return groupRepository.findByMembers_Id(userId);
    }
}