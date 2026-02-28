package com.mechanic;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * ══════════════════════════════════════════════════════
 *  نقطة الانطلاق — Smart Mechanic API
 *
 *  بعد التشغيل:
 *  Swagger UI: http://localhost:8080/api/v1/swagger-ui
 *  API Docs:   http://localhost:8080/api/v1/api-docs
 * ══════════════════════════════════════════════════════
 */
@SpringBootApplication
@EnableJpaAuditing
@OpenAPIDefinition(
        info = @Info(
                title = "Smart Mechanic API",
                version = "1.0.0",
                description = """
                        نظام مساعد الميكانيكي الذكي — REST API
                        
                        يدعم:
                        • تشخيص الأعطال بالـ VIN + DTC Code
                        • كتالوج السيارات (CRUD كامل)
                        • قاعدة بيانات أكواد الأعطال
                        • خطوات الإصلاح بصيغة JSON للموبايل
                        • مصادقة JWT
                        """,
                contact = @Contact(name = "Smart Mechanic Team")
        )
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "أدخل الـ Token بالصيغة: Bearer {token}"
)
public class SmartMechanicApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartMechanicApiApplication.class, args);
        System.out.println("""
                
                ╔══════════════════════════════════════════════════════╗
                ║   🔧 Smart Mechanic API — تم التشغيل بنجاح ✅       ║
                ╠══════════════════════════════════════════════════════╣
                ║   Swagger UI  : http://localhost:8080/api/v1/swagger-ui  ║
                ║   API Docs    : http://localhost:8080/api/v1/api-docs    ║
                ║   H2 Console  : http://localhost:8080/api/v1/h2-console  ║
                ╚══════════════════════════════════════════════════════╝
                """);
    }
}
