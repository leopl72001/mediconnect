package com.universidad.mediconnect2.admin;

import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name="offices")
public class Office {
    @Id @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;
    @Column(nullable=false)
    private String name;
    @Column(nullable=false)
    private String address;
    @Column(nullable=false)
    private String roomNumber;
    @Column(nullable=false)
    private boolean active;
    protected Office() {}
    public Office(String name,String address,String roomNumber,boolean active) {
        this.name=name; this.address=address; this.roomNumber=roomNumber; this.active=active;
    }
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getRoomNumber() { return roomNumber; }
    public boolean isActive() { return active; }
}
