package com.campushome.api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.campushome.api.dto.AdRequestDTO;
import com.campushome.api.dto.AdResponseDTO;
import com.campushome.api.enums.UserRole;
import com.campushome.api.model.Advertisement;
import com.campushome.api.model.User;
import com.campushome.api.repository.AdvertisementRepository;
import com.campushome.api.repository.UserRepository;

@Service
public class AdService {

    @Autowired
    private AdvertisementRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    public AdResponseDTO publish(AdRequestDTO request){
        User owner = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("Usuário Não Encontrado."));

        if(owner.getRole() != UserRole.OWNER){
            throw new RuntimeException("Apenas usuários do tipo DONO podem publicar anúncios.");
        }

        Advertisement ad = new Advertisement();
        ad.setTitle(request.getTitle());
        ad.setDescription(request.getDescription());
        ad.setPrice(request.getPrice());
        ad.setNeighborhood(request.getNeighborhood());
        ad.setAddress(request.getAddress());
        ad.setOwner(owner);

        Advertisement savedAd = adRepository.save(ad);

        return new AdResponseDTO(
            savedAd.getId(), 
            savedAd.getTitle(), 
            savedAd.getPrice(), 
            savedAd.getNeighborhood(), 
            owner.getName(),
            savedAd.isActive()
        );
    }

    public List<AdResponseDTO> searchAds(String neighborhood, BigDecimal maxPrice){
        List<Advertisement> ads;

        if(neighborhood != null && maxPrice != null){
            ads = adRepository.findByActiveTrueAndNeighborhoodAndPriceLessThanEqual(neighborhood, maxPrice);
        } else if (neighborhood != null) {
            ads = adRepository.findByActiveTrueAndNeighborhood(neighborhood);
        } else if (maxPrice != null) {
            ads = adRepository.findByActiveTrueAndPriceLessThanEqual(maxPrice);
        } else {
            ads = adRepository.findByActiveTrue();
        }

        return ads.stream().map(ad -> new AdResponseDTO(
            ad.getId(), 
            ad.getTitle(), 
            ad.getPrice(),
            ad.getNeighborhood(),
            ad.getOwner().getName(),
            ad.isActive()
        )).collect(Collectors.toList());
    }

    public void toggleAdStatus(Long adId, Long ownerId){
        Advertisement ad = adRepository.findById(adId)
            .orElseThrow(() -> new RuntimeException("Anúncio não encontrado."));

        if (!ad.getOwner().getId().equals(ownerId)) {
            throw new RuntimeException("Você não tem permissão para alterar este anúncio.");
        }

        ad.setActive(!ad.isActive());
        adRepository.save(ad);
    }
}
