package com.rentingframework.core.dto;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
/**
 * A trimmed-down User reference — just enough to render "who's in this
 * group" without pulling in an app's full profile DTO (course, cpfCnpj,
 * etc., which core doesn't know about anyway).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryDTO {
    private Long id;
    private String name;
    private Integer reputationScore;
}
