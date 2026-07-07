package com.carpooling.api.service;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.carpooling.api.model.Booking;
import com.carpooling.api.model.CarUser;
import com.carpooling.api.repository.BookingRepository;
import com.rentingframework.core.dto.MatchResponseDTO;
import com.rentingframework.core.model.User;
import com.rentingframework.core.repository.GroupRepository;
import com.rentingframework.core.service.MatchingStrategy;
import com.rentingframework.core.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Same fixed LLM-matching mechanism as HousingMatchingStrategy, completely
 * different prompt content: instead of course/bio compatibility, this
 * compares the requester's stated availability (bio, repurposed here to
 * hold a freeform schedule/need description) against the pool's actual
 * existing bookings, pulled from BookingRepository for factual grounding
 * rather than leaving the LLM to guess from bios alone.
 */
@Service
@RequiredArgsConstructor
public class CarpoolMatchingStrategy implements MatchingStrategy {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("EEE dd/MM HH:mm");

    private final ChatClient chatClient;
    private final UserService userService;
    private final GroupRepository groupRepository;
    private final BookingRepository bookingRepository;

    @Override
    public MatchResponseDTO calculateMatch(Long requesterId, Long listingId) {
        User requesterBase = userService.findById(requesterId);
        CarUser requester = (CarUser) requesterBase;

        String currentDrivers = groupRepository.findByListingId(listingId)
                .map(group -> group.getMembers().stream()
                        .map(m -> ((CarUser) m).getName() + " (habilitado há " + ((CarUser) m).getYearsLicensed() + " anos)")
                        .collect(Collectors.joining(", ")))
                .orElse("Nenhum motorista no grupo ainda.");

        String existingSchedule = bookingRepository.findByVehicleListingIdOrderByStartTimeAsc(listingId).stream()
                .map(this::describeBooking)
                .collect(Collectors.joining(" | "));
        if (existingSchedule.isBlank()) {
            existingSchedule = "Nenhuma reserva registrada ainda - agenda completamente livre.";
        }

        String userPrompt = String.format(
            "Analise a compatibilidade entre um novo motorista e um grupo de compartilhamento de veículo.\n" +
            "Novo Motorista: Habilitado há %d anos. Disponibilidade/necessidade declarada: %s\n" +
            "Motoristas atuais do grupo: %s\n" +
            "Agenda de reservas existente: %s\n" +
            "Avalie se a disponibilidade declarada do novo motorista parece compatível com os horários " +
            "já ocupados (evitando conflitos) e com a experiência dos demais motoristas.\n" +
            "Retorne um score de 0 a 100 e uma justificativa curta.",
            requester.getYearsLicensed() != null ? requester.getYearsLicensed() : 0,
            requester.getBio(),
            currentDrivers,
            existingSchedule
        );

        return chatClient.prompt()
                .user(userPrompt)
                .call()
                .entity(new ParameterizedTypeReference<MatchResponseDTO>() {});
    }

    private String describeBooking(Booking booking) {
        return booking.getStartTime().format(FORMAT) + "-" + booking.getEndTime().format(FORMAT)
                + " (" + booking.getStatus() + ")";
    }
}