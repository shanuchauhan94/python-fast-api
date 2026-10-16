package com.spring.ai.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponseDTO {

    private Integer totalToken;

    private Integer promptToken;

    private String conversationId;

    private String modelUsed;

    private List<String> chatResponse;
}
