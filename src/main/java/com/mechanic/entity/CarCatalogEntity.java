package com.mechanic.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ══════════════════════════════════════════════════════
 *  جدول السيارات في قاعدة البيانات
 *  يُترجم CarCatalog Record إلى JPA Entity
 * ══════════════════════════════════════════════════════
 */
@Entity
@Table(name = "car_catalog",
        indexes = {
                @Index(name = "idx_car_vin", columnList = "vin", unique = true),
                @Index(name = "idx_car_manufacturer", columnList = "manufacturer"),
                @Index(name = "idx_car_fuel_type", columnList = "fuel_type")
        })
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class CarCatalogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false, length = 17)
    private String vin;

    @Column(nullable = false, length = 50)
    private String manufacturer;

    @Column(nullable = false, length = 50)
    private String model;

    @Column(nullable = false)
    private int year;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type", nullable = false, length = 20)
    private FuelTypeEnum fuelType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transmission", length = 20)
    private TransmissionEnum transmission;

    @Column(name = "engine_displacement_cc")
    private double engineDisplacementCC;

    @Column(name = "horse_power")
    private int horsePower;

    @Column(name = "obd_protocol", length = 20)
    private String obdProtocol;

    // قائمة وحدات ECU مخزنة كـ JSON
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "car_ecu_modules",
            joinColumns = @JoinColumn(name = "car_id"))
    @Column(name = "module_name")
    private List<String> ecuModules;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "car_maintenance_notes",
            joinColumns = @JoinColumn(name = "car_id"))
    @Column(name = "note", length = 500)
    private List<String> maintenanceNotes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum FuelTypeEnum {
        GASOLINE, DIESEL, ELECTRIC, HYBRID, PLUG_IN_HYBRID
    }

    public enum TransmissionEnum {
        MANUAL, AUTOMATIC, CVT, DCT, SINGLE_SPEED
    }
}
