package com.naz.organaiz.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Organisations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Organisation {
    @Id
    @Column(nullable = false, unique = true)
    private String orgId;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @ManyToMany(mappedBy = "organisations", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    @PrePersist
    public void generateUserId() {
        this.orgId = UUID.randomUUID().toString();
    }
}
