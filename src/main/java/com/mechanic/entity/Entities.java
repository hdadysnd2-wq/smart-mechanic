package com.mechanic.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ══════════════════════════════════════════════════════
 *  جدول أكواد الأعطال DTC في قاعدة البيانات
 * ══════════════════════════════════════════════════════
 */
@Entity
@Table(name = "dtc_codes",
        indexes = {
                @Index(name = "idx_dtc_code", columnList = "code", unique = true),
                @Index(name = "idx_dtc_severity", columnList = "severity"),
                @Index(name = "idx_dtc_category", columnList = "category")
        })
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class DtcCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // الكود القياسي: P0300, U0100 ...
    @Column(unique = true, nullable = false, length = 5)
    private String code;

    @Column(name = "short_description", nullable = false, length = 200)
    private String shortDescription;

    @Column(name = "detailed_description", columnDefinition = "TEXT")
    private String detailedDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private CategoryEnum category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private SeverityEnum severity;

    // قوائم مخزنة كـ JSON في PostgreSQL
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "possible_causes", columnDefinition = "jsonb")
    private List<String> possibleCauses;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "components_to_check", columnDefinition = "jsonb")
    private List<String> componentsToCheck;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "related_repair_step_ids", columnDefinition = "jsonb")
    private List<String> relatedRepairStepIds;

    @Column(name = "requires_special_tools")
    private boolean requiresSpecialTools;

    @Column(name = "average_repair_cost_usd")
    private double averageRepairCostUSD;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum CategoryEnum { POWERTRAIN, BODY, CHASSIS, NETWORK }
    public enum SeverityEnum { INFO, LOW, MEDIUM, HIGH, CRITICAL }
}


/**
 * ══════════════════════════════════════════════════════
 *  جدول أدلة الصيانة مع الخطوات بصيغة JSONB
 *  يُخزّن الخطوات كـ JSON لسهولة الإرسال للموبايل
 * ══════════════════════════════════════════════════════
 */
@Entity
@Table(name = "maintenance_manuals")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
class MaintenanceManualEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Enumerated(EnumType.STRING)
    @Column(name = "required_skill_level", length = 20)
    private SkillLevelEnum requiredSkillLevel;

    @Column(name = "estimated_time_minutes")
    private int estimatedTimeMinutes;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "required_tools", columnDefinition = "jsonb")
    private List<String> requiredTools;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "required_parts", columnDefinition = "jsonb")
    private List<Object> requiredParts;

    // ✅ الخطوات كاملة بصيغة JSONB — جاهزة للموبايل مباشرة
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "steps", columnDefinition = "jsonb")
    private List<Object> steps;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "programming_codes", columnDefinition = "jsonb")
    private List<Object> programmingCodes;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "safety_warnings", columnDefinition = "jsonb")
    private List<String> safetyWarnings;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "related_complaints", columnDefinition = "jsonb")
    private List<Object> relatedComplaints;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "applicable_vehicle_types", columnDefinition = "jsonb")
    private List<String> applicableVehicleTypes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum SkillLevelEnum { BEGINNER, INTERMEDIATE, ADVANCED, EXPERT }
}


/**
 * ══════════════════════════════════════════════════════
 *  جدول المستخدمين — للمصادقة JWT
 * ══════════════════════════════════════════════════════
 */
@Entity
@Table(name = "users")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 100)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleEnum role;

    @Column(name = "is_active")
    private boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum RoleEnum { ADMIN, TECHNICIAN, VIEWER }
}
