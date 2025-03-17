package com.pck.genai.dto;

public class ChatRequest {
    private String model = "gpt-4-turbo";
    private ChatMessage[] messages;
    private double temperature = 0.7;
    private int max_tokens = 200;
    private double top_p = 1.0;
    private double frequency_penalty = 0.0;
    private double presence_penalty = 0.0;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public ChatMessage[] getMessages() {
        return messages;
    }

    public void setMessages(ChatMessage[] messages) {
        this.messages = messages;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getMax_tokens() {
        return max_tokens;
    }

    public void setMax_tokens(int max_tokens) {
        this.max_tokens = max_tokens;
    }

    public double getTop_p() {
        return top_p;
    }

    public void setTop_p(double top_p) {
        this.top_p = top_p;
    }

    public double getFrequency_penalty() {
        return frequency_penalty;
    }

    public void setFrequency_penalty(double frequency_penalty) {
        this.frequency_penalty = frequency_penalty;
    }

    public double getPresence_penalty() {
        return presence_penalty;
    }

    public void setPresence_penalty(double presence_penalty) {
        this.presence_penalty = presence_penalty;
    }
}
