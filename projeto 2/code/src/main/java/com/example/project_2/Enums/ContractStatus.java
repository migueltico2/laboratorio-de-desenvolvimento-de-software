package com.example.project_2.Enums;

public enum ContractStatus {
    PENDING,
    APPROVED,
    REJECTED;

    public static ContractStatus fromString(String text) {
        try {
            return ContractStatus.valueOf(text.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("No constant with text " + text + " found", e);
        }
    }
}
