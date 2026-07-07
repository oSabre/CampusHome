package com.expert.api.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expert.api.dto.SessionRequestDTO;
import com.expert.api.dto.SessionResponseDTO;
import com.expert.api.enums.SessionStatus;
import com.expert.api.enums.TaskType;
import com.expert.api.model.ExpertListing;
import com.expert.api.model.ExpertUser;
import com.expert.api.model.Session;
import com.expert.api.repository.ExpertListingRepository;
import com.expert.api.repository.ExpertUserRepository;
import com.expert.api.repository.SessionRepository;
import com.rentingframework.core.exception.ResourceNotFoundException;
import com.rentingframework.core.model.Group;
import com.rentingframework.core.repository.GroupRepository;

import lombok.RequiredArgsConstructor;

/**
 * No Housing equivalent, mirrors BookingService's shape - but the
 * exclusivity check here scopes by specialist (across every listing
 * they've posted), not by listing, per the design decision made when
 * SessionRepository was built.
 */
@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final ExpertListingRepository expertListingRepository;
    private final ExpertUserRepository expertUserRepository;
    private final GroupRepository groupRepository;
    private final ExpertTaskService taskService;

    @Transactional
    public SessionResponseDTO createSession(SessionRequestDTO request) {
        ExpertUser client = expertUserRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));

        ExpertListing listing = expertListingRepository.findById(request.getExpertListingId())
                .orElseThrow(() -> new ResourceNotFoundException("Anúncio não encontrado."));

        // Only an accepted client can book a session - mirrors
        // BookingService's "only pool members can book" check.
        Group group = groupRepository.findByListingId(listing.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Grupo não encontrado para este anúncio."));
        boolean isMember = group.getMembers().stream().anyMatch(m -> m.getId().equals(client.getId()));
        if (!isMember) {
            throw new RuntimeException("Apenas o cliente com engajamento aceito pode agendar uma sessão.");
        }

        // Scoped by specialist (listing owner), not by listing - the one
        // real difference from Booking's overlap-check.
        Long specialistId = listing.getOwner().getId();
        List<Session> overlapping = sessionRepository.findOverlappingSessionsForSpecialist(
                specialistId, request.getStartTime(), request.getEndTime());
        if (!overlapping.isEmpty()) {
            throw new RuntimeException("O especialista já possui uma sessão marcada nesse período.");
        }

        Session session = new Session();
        session.setClient(client);
        session.setExpertListing(listing);
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());

        Session saved = sessionRepository.save(session);
        return toResponseDto(saved);
    }

    /**
     * Marks the session done, computes the invoice from hourlyRate x
     * actual duration, and auto-generates the two follow-up tasks -
     * same "tasks come from an event" pattern as BookingService, both
     * assigned to the specialist (the operationally responsible party
     * for session admin and billing).
     */
    @Transactional
    public SessionResponseDTO completeSession(Long sessionId, String notes) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Sessão não encontrada."));

        session.setStatus(SessionStatus.COMPLETED);
        session.setNotes(notes);

        double hours = Duration.between(session.getStartTime(), session.getEndTime()).toMinutes() / 60.0;
        BigDecimal invoiceAmount = session.getExpertListing().getHourlyRate()
                .multiply(BigDecimal.valueOf(hours));
        session.setInvoiceAmount(invoiceAmount);

        Session saved = sessionRepository.save(session);

        Long listingId = saved.getExpertListing().getId();
        Long specialistId = saved.getExpertListing().getOwner().getId();

        taskService.addTask(listingId, "Escreva as notas da sessão.", TaskType.SESSION_NOTES, specialistId);
        taskService.addTask(listingId, "Emita a fatura para o cliente.", TaskType.INVOICE, specialistId);

        return toResponseDto(saved);
    }

    @Transactional
    public void cancelSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Sessão não encontrada."));
        session.setStatus(SessionStatus.CANCELLED);
        sessionRepository.save(session);
    }

    public List<SessionResponseDTO> listByListing(Long expertListingId) {
        return sessionRepository.findByExpertListingIdOrderByStartTimeAsc(expertListingId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<SessionResponseDTO> listByClient(Long clientId) {
        return sessionRepository.findByClientId(clientId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private SessionResponseDTO toResponseDto(Session session) {
        return new SessionResponseDTO(
            session.getId(),
            session.getClient().getId(),
            session.getClient().getName(),
            session.getExpertListing().getId(),
            session.getExpertListing().getOwner().getName(),
            session.getStartTime(),
            session.getEndTime(),
            session.getStatus(),
            session.getNotes(),
            session.getInvoiceAmount()
        );
    }
}