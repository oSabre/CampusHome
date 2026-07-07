package com.rentingframework.core.dto;
 
import java.time.LocalDateTime;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDTO {
    private Long id;
    private Long groupId;
    private Long senderId;
    private String senderName;
    private String content;
    private LocalDateTime sentAt;
}