package com.campushome.api.service;

import com.campushome.api.dto.MatchResponseDTO;
import com.campushome.api.model.HousingGroup;
import com.campushome.api.model.User;
import com.campushome.api.repository.HousingGroupRepository;
import com.campushome.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final ChatClient chatClient;
    private final HousingGroupRepository housingGroupRepository;
    private final UserRepository userRepository;

    public MatchResponseDTO calculateMatch(Long studentId, Long adId){
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Estudante não encontrado"));

        String residentsBios = housingGroupRepository.findByAdvertisementId(adId)
                .map(group -> group.getResidents().stream()
                        .map(r -> "Curso: " + r.getCourse() + ", Bio: " + r.getBio())
                        .collect(Collectors.joining(" | ")))
                .orElse("Ainda não há moradores nesta república.");

         String userPrompt = String.format(
            "Analise a compatibilidade entre um novo estudante e os moradores atuais de uma república.\n" +
            "Novo Estudante: Curso: %s, Bio: %s\n" +
            "Moradores Atuais: %s\n" +
            "Retorne um score de 0 a 100 e uma justificativa curta.",
            student.getCourse(), student.getBio(), residentsBios
        );

        return chatClient.prompt()
                .user(userPrompt)
                .call()
                .entity(new ParameterizedTypeReference<MatchResponseDTO>() {});
    }
}
