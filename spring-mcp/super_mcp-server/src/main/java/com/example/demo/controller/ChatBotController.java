package com.example.demo.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.enums.AiAgent;
import com.example.demo.model.ChatResponseDTO;
import com.example.demo.service.AiAgentService;


@RequestMapping(path = "/ai")
@RestController 
public class ChatBotController {

    private final AiAgentService aiAgentService;
    private ListOutputConverter converter = new ListOutputConverter(new DefaultConversionService());
    private final SyncMcpToolCallbackProvider mcpTools;

    public ChatBotController(AiAgentService aiAgentService, SyncMcpToolCallbackProvider mcpTools){
        this.aiAgentService = aiAgentService;
        this.mcpTools = mcpTools;
    }

@GetMapping(path = "/chat")
public ResponseEntity<ChatResponseDTO> chat(@RequestParam  String message, 
                                            @RequestParam AiAgent agent,
                                            @RequestParam(required = false) String conversationId) {

    List<String> response = new LinkedList<>();

    // Create ID only for a new conversation 
    final String finalConversationId = (conversationId == null || conversationId.isBlank()) 
        ? UUID.randomUUID().toString() 
        : conversationId;

    // Get selected AI agent
        ChatClient chatClient =
                aiAgentService.getAgent(agent);

        // Get agent-specific system prompt
        String systemPrompt =
                aiAgentService.getSystemPrompt(agent);

     // Send request to selected agent
    ChatResponse chatResponse = chatClient
            .prompt()
            .system(systemPrompt)
            .user(message)
            .tools(mcpTools) // pass all tool available
            .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, finalConversationId))
            .call()
            .chatResponse();

      // Get AI response
      String text = chatResponse .getResult() .getOutput() .getText();

      if (text != null && !text.isBlank()) {
            response = converter.convert(text.trim());
        }

    if (response.isEmpty()) {
            response.add("The AI model returned an empty response.");
        }    

     // Metadata
    String model = chatResponse.getMetadata().getModel(); 
    Integer promptToken = chatResponse.getMetadata().getUsage().getPromptTokens(); 
    Integer totalToken = chatResponse.getMetadata().getUsage().getTotalTokens();
    
    return ResponseEntity.ok(ChatResponseDTO.builder() 
            .totalToken(totalToken)
            .promptToken(promptToken) 
            .conversationId(finalConversationId) 
            .modelUsed(model) 
            .chatResponse(response) 
            .build() );

}

}
