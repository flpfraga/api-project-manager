package com.fraga.projectManager.data.enums;

import lombok.Getter;

import java.util.Set;

@Getter
public enum EStatus {
    IN_ANALISIS("In analisis", "DO_ANALISIS"),
    DO_ANALISIS("Do analisis", "APROVED_ANALISIS"),
    APROVED_ANALISIS("Aprove analisis", "STARTED"),
    STARTED("Started", "PLANNED"),
    PLANNED("Planned", "IN_PROGRESS"),
    IN_PROGRESS("In Progress", "COMPLETED"),
    COMPLETED("Completed", null),
    CANCELLED("Cancelled", null);

    private final String status;
    private final String next;

    EStatus(String status, String next) {
        this.status = status;
        this.next = next;
    }

    public EStatus getNext() {
        return EStatus.valueOf(next.toUpperCase());
    }

    public static Set<EStatus> notFinalStatus() {
        return Set.of(IN_ANALISIS, DO_ANALISIS, APROVED_ANALISIS, STARTED, PLANNED, IN_PROGRESS);
    }

    public static Set<EStatus> finalStatus() {
        return Set.of(COMPLETED, CANCELLED);
    }

}
