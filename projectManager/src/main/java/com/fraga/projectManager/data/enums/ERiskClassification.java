package com.fraga.projectManager.data.enums;

import lombok.Getter;

@Getter
public enum ERiskClassification {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private final String classification;

    ERiskClassification(String classification) {
        this.classification = classification;
    }

}
