package com.carpooling.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carpooling.api.dto.BookingRequestDTO;
import com.carpooling.api.dto.BookingResponseDTO;
import com.carpooling.api.enums.BookingStatus;
import com.carpooling.api.enums.TaskType;
import com.carpooling.api.model.Booking;
import com.carpooling.api.model.CarUser;
import com.carpooling.api.model.VehicleListing;
import com.carpooling.api.repository.BookingRepository;
import com.carpooling.api.repository.CarUserRepository;
import com.carpooling.api.repository.VehicleListingRepository;
import com.rentingframework.core.exception.ResourceNotFoundException;
import com.rentingframework.core.model.Group;
import com.rentingframework.core.repository.GroupRepository;

import lombok.RequiredArgsConstructor;

/**
 * No core equivalent - this is the piece that actually enforces "one
 * driver at a time." createBooking() is the only place that check runs;
 * everything else is plain CRUD around it.
 */
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final VehicleListingRepository vehicleListingRepository;
    private final CarUserRepository carUserRepository;
    private final GroupRepository groupRepository;
    private final CarpoolingTaskService taskService;

    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO request) {
        CarUser driver = carUserRepository.findById(request.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException("Motorista não encontrado."));

        VehicleListing listing = vehicleListingRepository.findById(request.getVehicleListingId())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado."));

        // Only pool members can book the car - you have to have joined
        // (had a Request accepted) before you can reserve a time slot.
        Group group = groupRepository.findByListingId(listing.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Grupo não encontrado para este veículo."));
        boolean isMember = group.getMembers().stream().anyMatch(m -> m.getId().equals(driver.getId()));
        if (!isMember) {
            throw new RuntimeException("Apenas participantes do grupo podem reservar este veículo.");
        }

        // The actual exclusivity rule - reject if any other non-cancelled
        // booking on this vehicle overlaps the requested window.
        List<Booking> overlapping = bookingRepository.findOverlappingBookings(
                listing.getId(), request.getStartTime(), request.getEndTime());
        if (!overlapping.isEmpty()) {
            throw new RuntimeException("Já existe uma reserva conflitante para este período.");
        }

        Booking booking = new Booking();
        booking.setDriver(driver);
        booking.setVehicleListing(listing);
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setOdometerStart(listing.getMileage());

        Booking saved = bookingRepository.save(booking);
        return toResponseDto(saved);
    }

    /**
     * Marks the booking done and auto-generates the two follow-up tasks -
     * this is the "tasks come from an event, not a manual click" variable
     * point we designed. Both are assigned straight to the driver who
     * just finished, unlike Housing's manually-created, initially
     * unassigned chores.
     */
    @Transactional
    public BookingResponseDTO completeBooking(Long bookingId, Integer odometerEnd) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada."));

        booking.setStatus(BookingStatus.COMPLETED);
        booking.setOdometerEnd(odometerEnd);
        Booking saved = bookingRepository.save(booking);

        Long listingId = saved.getVehicleListing().getId();
        Long driverId = saved.getDriver().getId();

        taskService.addTask(listingId, "Reabasteça o tanque após o uso.", TaskType.REFUEL, driverId);
        taskService.addTask(listingId, "Registre a quilometragem final da viagem.", TaskType.MILEAGE_LOG, driverId);

        return toResponseDto(saved);
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada."));
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    public List<BookingResponseDTO> listByVehicle(Long vehicleListingId) {
        return bookingRepository.findByVehicleListingIdOrderByStartTimeAsc(vehicleListingId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<BookingResponseDTO> listByDriver(Long driverId) {
        return bookingRepository.findByDriverId(driverId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private BookingResponseDTO toResponseDto(Booking booking) {
        return new BookingResponseDTO(
            booking.getId(),
            booking.getDriver().getId(),
            booking.getDriver().getName(),
            booking.getVehicleListing().getId(),
            booking.getStartTime(),
            booking.getEndTime(),
            booking.getStatus(),
            booking.getOdometerStart(),
            booking.getOdometerEnd()
        );
    }
}