package com.universidad.mediconnect2.catalog;

import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name="specialties")
public class Specialty {
    @Id @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;
    @Column(nullable=false, unique=true)
    private String name;
    protected Specialty() {}
    public Specialty(String name) { this.name=name; }
    public UUID getId() { return id; }
    public String getName() { return name; }
}
