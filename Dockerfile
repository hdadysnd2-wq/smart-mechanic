# ══════════════════════════════════════════════════════
#  Dockerfile — بناء وتشغيل Smart Mechanic API
#  Multi-stage build لتقليل حجم الـ Image
# ══════════════════════════════════════════════════════

# ── المرحلة 1: بناء التطبيق ───────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# نسخ ملفات Maven أولاً (استخدام Cache)
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .

# تحميل الاعتمادات
RUN ./mvnw dependency:go-offline -B

# نسخ الكود وبناء التطبيق
COPY src/ src/
RUN ./mvnw clean package -DskipTests -B

# ── المرحلة 2: تشغيل التطبيق ──────────────────────────
FROM eclipse-temurin:21-jre-alpine AS runner

WORKDIR /app

# نسخ الـ JAR من مرحلة البناء فقط
COPY --from=builder /app/target/smart-mechanic-api-1.0.0.jar app.jar

# إنشاء مستخدم غير جذر للأمان
RUN addgroup -S mechanic && adduser -S mechanic -G mechanic
USER mechanic

# المنفذ
EXPOSE 8080

# تشغيل التطبيق
ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]
