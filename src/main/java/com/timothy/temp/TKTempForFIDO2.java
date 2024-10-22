package com.timothy.temp;

import java.util.concurrent.CompletableFuture;

public class TKTempForFIDO2 {

}

class TKCredential {
    private String id;
    private String type;

    public TKCredential(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public static CompletableFuture<Boolean> isConditionalMediationAvailable() {
        return CompletableFuture.supplyAsync(() -> {

            return false;
        });
    }

    public static CompletableFuture<Void> willRequestConditionalCreation() {
//        return CompletableFuture.supplyAsync(() -> {
//
//        });

        return CompletableFuture.runAsync(() -> {

        });
    }
}

enum TKTokenBindingStatus {
    Present("present"),
    Supported("supported");

    private final String value;

    TKTokenBindingStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}

class TKTokenBinding {
    private final TKTokenBindingStatus status;
    private String id;

    public TKTokenBinding(TKTokenBindingStatus status) {
        this.status = status;
    }

    public TKTokenBindingStatus getStatus() {
        return this.status;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

class TKCollectedClientData {
    private final String type;
    private final String challenge;
    private final String origin;
    private boolean crossOrigin;
    private TKTokenBinding tokenBinding;

    public TKCollectedClientData(String type,
                                 String challenge,
                                 String origin) {
        this.type = type;
        this.challenge = challenge;
        this.origin = origin;
        this.crossOrigin = false;
        this.tokenBinding = null;
    }

    public String getType() {
        return this.type;
    }

    public String getChallenge() {
        return this.challenge;
    }

    public String getOrigin() {
        return this.origin;
    }

    public boolean isCrossOrigin() {
        return this.crossOrigin;
    }

    public TKTokenBinding getTokenBinding() {
        return this.tokenBinding;
    }

    public void setCrossOrigin(boolean crossOrigin) {
        this.crossOrigin = crossOrigin;
    }

    public void setTokenBinding( TKTokenBinding tokenBinding) {
        this.tokenBinding = tokenBinding;
    }
}