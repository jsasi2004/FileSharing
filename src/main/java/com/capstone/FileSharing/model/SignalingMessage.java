package com.capstone.FileSharing.model;

public class SignalingMessage {

    private String type;    // "offer", "answer", or "ice"
    private String sdp;     // Session Description Protocol data (offer/answer)
    private String candidate; // ICE candidate string
    private String code;    // Unique session code

    public SignalingMessage() {
    }

    public SignalingMessage(String type, String sdp, String candidate, String code) {
        this.type = type;
        this.sdp = sdp;
        this.candidate = candidate;
        this.code = code;
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSdp() {
        return sdp;
    }

    public void setSdp(String sdp) {
        this.sdp = sdp;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
