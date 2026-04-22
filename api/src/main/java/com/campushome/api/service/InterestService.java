package com.campushome.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.campushome.api.dto.InterestRequestDTO;
import com.campushome.api.dto.InterestResponseDTO;
import com.campushome.api.enums.InterestStatus;
import com.campushome.api.enums.UserRole;
import com.campushome.api.model.Advertisement;
import com.campushome.api.model.Interest;
import com.campushome.api.model.User;
import com.campushome.api.repository.AdvertisementRepository;
import com.campushome.api.repository.InterestRepository;
import com.campushome.api.repository.UserRepository;

@Service
public class InterestService {
    
    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdvertisementRepository adRepository;

    public InterestResponseDTO createInterest(InterestRequestDTO request){
        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Estudante não encontrado."));

        Advertisement ad = adRepository.findById(request.getAdId())
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado."));

        if (student.getRole() != UserRole.STUDENT) {
            throw new RuntimeException("Apenas usuários do tipo ESTUDANTE podem demonstrar interesse.");
        }

        if (interestRepository.existsByStudentIdAndAdvertisementId(request.getStudentId(), request.getAdId())) {
            throw new RuntimeException("Já demonstrou interesse neste anúncio.");
        }

        Interest interest = new Interest();
        interest.setStudent(student);
        interest.setAdvertisement(ad);
        interest.setStatus(InterestStatus.PENDING);

        Interest saved = interestRepository.save(interest);

        return convertToResponseDTO(saved);
    }

    public List<InterestResponseDTO> getInterestsByAd(Long adId){
        return interestRepository.findByAdvertisementId(adId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private InterestResponseDTO convertToResponseDTO(Interest interest){
        return new InterestResponseDTO(
                interest.getId(),
                interest.getStudent().getId(),
                interest.getStudent().getName(),
                interest.getAdvertisement().getId(),
                interest.getAdvertisement().getTitle(),
                interest.getStatus(),
                interest.getCreatedAt()
        );
    }
}
