package com.cdw.helper.app.resident.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * ResidentDetails entity that encapsulates the data of a Resident
 */
@Data
@Entity
public class ResidentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String apartmentNumber;
}
