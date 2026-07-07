package com.rentingframework.core.service;
 
import com.rentingframework.core.enums.RequestStatus;
import com.rentingframework.core.exception.ResourceNotFoundException;
import com.rentingframework.core.model.Group;
import com.rentingframework.core.model.Listing;
import com.rentingframework.core.model.Request;
import com.rentingframework.core.model.User;
import com.rentingframework.core.repository.ListingRepository;
import com.rentingframework.core.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import java.util.List;
 
@Service
@RequiredArgsConstructor
public class RequestService {
 
    private final RequestRepository requestRepository;
    private final ListingRepository listingRepository;
    private final UserService userService;
    private final GroupService groupService; // Serviço que desenharemos a seguir
 
    /**
     * Cria um novo pedido de reserva/locação/agendamento.
     * O status inicial é sempre PENDING, garantido pela entidade.
     */
    @Transactional
    public Request createRequest(Long requesterId, Long listingId) {
        if (requestRepository.existsByRequesterIdAndListingId(requesterId, listingId)) {
            throw new RuntimeException("Já existe uma solicitação ativa para este anúncio.");
        }
 
        User requester = userService.findById(requesterId);
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Anúncio não encontrado."));
 
        // Regra de sanidade: o dono não pode solicitar o próprio anúncio
        if (listing.getOwner().getId().equals(requesterId)) {
            throw new RuntimeException("O proprietário não pode criar uma solicitação para o próprio anúncio.");
        }
 
        Request request = new Request();
        request.setRequester(requester);
        request.setListing(listing);
        // O @PrePersist já garante que o status nasce como PENDING
        
        return requestRepository.save(request);
    }
 
    /**
     * Fluxo crítico do Framework: O aceite de um Request.
     * Transforma a solicitação em um vínculo real, criando/recuperando o Group.
     */
    @Transactional
    public Request acceptRequest(Long requestId) {
        Request request = findById(requestId);
 
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Apenas solicitações pendentes podem ser aceitas.");
        }
 
        request.setStatus(RequestStatus.ACCEPTED);
        requestRepository.save(request);
 
        Listing listing = request.getListing();
        
        // 1. O Core orquestra a criação ou recuperação do grupo vinculado ao ativo
        Group group = groupService.getOrCreateGroupForListing(listing.getId());
 
        // 2. A decisão arquitetural de ouro: O Dono entra no grupo (se já não estiver)
        groupService.addMemberToGroup(group.getId(), listing.getOwner().getId());
 
        // 3. O Interessado aprovado entra no grupo
        groupService.addMemberToGroup(group.getId(), request.getRequester().getId());
 
        return request;
    }
 
    /**
     * Rejeita a solicitação. O fluxo morre aqui.
     */
    @Transactional
    public Request rejectRequest(Long requestId) {
        Request request = findById(requestId);
 
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Apenas solicitações pendentes podem ser rejeitadas.");
        }
 
        request.setStatus(RequestStatus.REJECTED);
        return requestRepository.save(request);
    }
 
    @Transactional(readOnly = true)
    public Request findById(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada."));
    }
 
    @Transactional(readOnly = true)
    public List<Request> getPendingRequestsForOwner(Long ownerId) {
        return requestRepository.findByListing_Owner_IdAndStatus(ownerId, RequestStatus.PENDING);
    }
}