package com.mechanic.repository;

import com.mechanic.entity.CarCatalogEntity;
import com.mechanic.entity.CarCatalogEntity.FuelTypeEnum;
import com.mechanic.entity.DtcCodeEntity;
import com.mechanic.entity.DtcCodeEntity.SeverityEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * مستودع بيانات السيارات
 */
@Repository
public interface CarCatalogRepository extends JpaRepository<CarCatalogEntity, String> {

    // البحث برقم الشاسيه
    Optional<CarCatalogEntity> findByVin(String vin);

    // التحقق من وجود الشاسيه
    boolean existsByVin(String vin);

    // البحث بالمصنّع والموديل
    List<CarCatalogEntity> findByManufacturerIgnoreCaseAndModelIgnoreCase(
            String manufacturer, String model);

    // البحث بنوع الوقود مع صفحات
    Page<CarCatalogEntity> findByFuelType(FuelTypeEnum fuelType, Pageable pageable);

    // البحث بالمصنّع مع صفحات
    Page<CarCatalogEntity> findByManufacturerIgnoreCase(String manufacturer, Pageable pageable);

    // البحث الشامل (Full Text Search)
    @Query("""
            SELECT c FROM CarCatalogEntity c
            WHERE LOWER(c.manufacturer) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(c.model) LIKE LOWER(CONCAT('%', :query, '%'))
               OR c.vin LIKE UPPER(CONCAT('%', :query, '%'))
            """)
    Page<CarCatalogEntity> searchCars(@Param("query") String query, Pageable pageable);

    // إحصائيات بنوع الوقود
    @Query("SELECT c.fuelType, COUNT(c) FROM CarCatalogEntity c GROUP BY c.fuelType")
    List<Object[]> countByFuelType();
}


/**
 * مستودع بيانات أكواد الأعطال
 */
@Repository
interface DtcCodeRepository extends JpaRepository<DtcCodeEntity, String> {

    Optional<DtcCodeEntity> findByCode(String code);

    boolean existsByCode(String code);

    List<DtcCodeEntity> findBySeverity(SeverityEnum severity);

    List<DtcCodeEntity> findByCategory(DtcCodeEntity.CategoryEnum category);

    // البحث في الوصف
    @Query("""
            SELECT d FROM DtcCodeEntity d
            WHERE LOWER(d.shortDescription) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR d.code LIKE UPPER(CONCAT('%', :keyword, '%'))
            """)
    List<DtcCodeEntity> searchByKeyword(@Param("keyword") String keyword);

    // الأكواد الحرجة فقط
    @Query("SELECT d FROM DtcCodeEntity d WHERE d.severity = 'CRITICAL' ORDER BY d.code")
    List<DtcCodeEntity> findAllCritical();
}
