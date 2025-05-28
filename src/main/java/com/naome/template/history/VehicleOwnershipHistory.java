package com.naome.template.history;

import com.naome.template.owner.Owner;
import com.naome.template.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
public class VehicleOwnershipHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "from_owner_id", nullable = false)
    private Owner fromOwner;

    @ManyToOne
    @JoinColumn(name = "to_owner_id", nullable = false)
    private Owner toOwner;

    private String oldPlateNumber;

    private String newPlateNumber;

    @Column(name = "transfer_date", nullable = false)
    private Date transferDate;

    @Column(name = "purchase_price", nullable = false)
    private Double purchasePrice;
}