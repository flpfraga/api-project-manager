package com.fraga.projectManager.data.entity;

import com.fraga.projectManager.data.enums.EStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EStatus status;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate expectedEndDate;
    private LocalDate realEndDate;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "project_member",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private Set<Member> projectManager;

    public void upStatus() {
        this.setStatus(this.status.getNext());
    }

    public Boolean isInForbidenDeleteStatus() {
        return this.status == EStatus.STARTED ||
                this.status == EStatus.IN_PROGRESS ||
                this.status == EStatus.COMPLETED;
    }

    public Long countActiveAllocationsByMember(Member member, Set<Project> validProjects) {
        return validProjects.stream()
                .filter(project -> project.getProjectManager().contains(member))
                .count();
    }

    public void removeExistingMembers(Set<Member> members) {
        if (this.projectManager != null) {
            members.removeAll(projectManager);
        }
    }

    public void setStatus(EStatus status) {
        this.status = status;
        if (EStatus.CANCELLED.equals(status) || EStatus.COMPLETED.equals(status)) {
            this.realEndDate = LocalDate.now();
        }
    }

}
