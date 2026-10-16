package com.spring.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.stereotype.Service;

import com.spring.ai.enums.AiAgent;



@Service
public class AiAgentService {

    private final ChatClient generalAgent;
    private final ChatClient codingAgent;
    private final ChatClient ragAgent;
    private final ChatClient mcpAgent;

    private final ChatMemory chatMemory;

    public AiAgentService(ChatClient.Builder builder) {

        this.chatMemory = MessageWindowChatMemory
                .builder()
                .maxMessages(20)
                .build();

        this.generalAgent = builder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();

        this.codingAgent = builder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();

        this.ragAgent = builder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();

        this.mcpAgent = builder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

    public ChatClient getAgent(AiAgent agent) {

        return switch (agent) {

            case GENERAL -> generalAgent;

            case CODING -> codingAgent;

            case RAG -> ragAgent;

            case MCP -> mcpAgent;
        };
    }

    public String getSystemPrompt(AiAgent agent) {

        return switch (agent) {

            case GENERAL -> """
                    You are a general AI assistant.
                    Provide clear, accurate and useful answers.
                    """;

            case CODING -> """
                    You are a senior Java and Spring Boot developer.
                    Provide production-ready code.
                    Explain technical concepts clearly.
                    """;

            case RAG -> """
                    You are a RAG-based AI assistant.
                    Answer questions using the available retrieved context.
                    If the information is unavailable, clearly say so.
                    """;

            case MCP -> """
                    You are an AI agent connected to external tools through MCP.
                    Use available tools when required.
                    Provide accurate and useful answers.
                    """;
        };
    }
}