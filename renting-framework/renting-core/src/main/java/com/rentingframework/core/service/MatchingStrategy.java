package com.rentingframework.core.service;
 
import com.rentingframework.core.dto.MatchResponseDTO;
 
/**
 * Fixed Hot Spot: O framework garante que toda decisão de alocação 
 * passa por um crivo de IA. A estratégia de como comparar 
 * um User (requester) com um Listing (ativo) é delegada.
 */
public interface MatchingStrategy {
    
    /**
     * Calcula o score de compatibilidade.
     * O Core chama isso dentro do RequestService (ou num Controller de Matching).
     */
    MatchResponseDTO calculateMatch(Long requesterId, Long listingId);
}