package com.naome.template.owner;

import com.naome.template.platenumbers.PlateNumber;
import com.naome.template.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name="owner", indexes = {
        @Index(name = "idx_user_phonenumber_unq", columnList = "phoneNumber", unique = true),
        @Index(name = "idx_user_nationalid_unq", columnList = "nationalId", unique = true)
})
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    private String fullNames;
    private String nationalId;
    private String phoneNumber;
    private String address;

    @OneToMany(mappedBy = "currentOwner")
    private List<Vehicle> vehicles;

    @OneToMany(mappedBy = "owner")
    private List<PlateNumber> plateNumbers;

}
