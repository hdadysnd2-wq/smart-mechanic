# 🔧 Smart Mechanic API — دليل التشغيل الكامل

## 🗂️ هيكل المشروع
```
smart-mechanic-api/
├── src/main/java/com/mechanic/
│   ├── SmartMechanicApiApplication.java   ← نقطة الانطلاق
│   ├── controller/   ← REST Endpoints
│   ├── entity/       ← جداول قاعدة البيانات
│   ├── repository/   ← تعاملات DB
│   ├── dto/          ← Request & Response
│   ├── security/     ← JWT + Spring Security
│   └── exception/    ← معالجة الأخطاء
├── src/main/resources/
│   └── application.yml   ← الإعدادات
├── docker-compose.yml    ← تشغيل بـ Docker
├── Dockerfile
└── pom.xml
```

---

## 🚀 طريقة 1: التشغيل بـ Docker (الأسهل)

### المتطلبات
- Docker Desktop مثبّت

### الخطوات
```bash
# 1. ادخل مجلد المشروع
cd smart-mechanic-api

# 2. شغّل كل الخدمات بأمر واحد
docker-compose up -d

# 3. تحقق من التشغيل
docker-compose ps
```

### الروابط بعد التشغيل
| الخدمة | الرابط | البيانات |
|--------|--------|---------|
| 🔧 API | http://localhost:8080/api/v1 | — |
| 📖 Swagger UI | http://localhost:8080/api/v1/swagger-ui | — |
| 🗄️ pgAdmin | http://localhost:5050 | admin@mechanic.com / Admin@123 |
| 🐘 PostgreSQL | localhost:5432 | mechanic_user / mechanic_pass |

### إيقاف الخدمات
```bash
docker-compose down           # إيقاف فقط
docker-compose down -v        # إيقاف + حذف البيانات
```

---

## 🛠️ طريقة 2: التشغيل بدون Docker

### المتطلبات
- Java 21 JDK
- Maven 3.9+
- PostgreSQL 14+ مثبّت

### إعداد قاعدة البيانات
```sql
-- في psql أو pgAdmin
CREATE DATABASE smart_mechanic_db;
CREATE USER mechanic_user WITH PASSWORD 'mechanic_pass';
GRANT ALL PRIVILEGES ON DATABASE smart_mechanic_db TO mechanic_user;
```

### تشغيل التطبيق
```bash
# في وضع التطوير (H2 بدل PostgreSQL)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# في وضع الإنتاج (PostgreSQL)
./mvnw spring-boot:run

# أو بناء JAR أولاً ثم تشغيله
./mvnw clean package -DskipTests
java -jar target/smart-mechanic-api-1.0.0.jar
```

---

## 🔑 استخدام الـ API

### 1. تسجيل الدخول والحصول على Token
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@mechanic.com",
    "password": "Admin@123"
  }'
```
**الرد:**
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1...",
    "tokenType": "Bearer",
    "expiresIn": 86400000
  }
}
```

### 2. تشخيص عطل السيارة
```bash
curl -X POST http://localhost:8080/api/v1/diagnose \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1..." \
  -d '{
    "vin": "1HGCM82633A123456",
    "dtcCode": "P0300"
  }'
```

### 3. البحث عن سيارة برقم الشاسيه
```bash
curl -X GET http://localhost:8080/api/v1/cars/vin/1HGCM82633A123456 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1..."
```

### 4. تفاصيل كود العطل
```bash
curl -X GET http://localhost:8080/api/v1/dtc/P0300 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1..."
```

---

## 📋 قائمة جميع الـ Endpoints

### المصادقة (بدون توكن)
| Method | Endpoint | الوظيفة |
|--------|----------|---------|
| POST | /auth/login | تسجيل الدخول |
| POST | /auth/register | إنشاء حساب |
| POST | /auth/refresh | تجديد التوكن |

### التشخيص (TECHNICIAN+)
| Method | Endpoint | الوظيفة |
|--------|----------|---------|
| POST | /diagnose | تشخيص عطل كامل |
| GET | /diagnose/steps/{id} | خطوات JSON للموبايل |
| GET | /diagnose/complaints/{code} | تحليل الشكاوى |

### كتالوج السيارات
| Method | Endpoint | الصلاحية | الوظيفة |
|--------|----------|---------|---------|
| GET | /cars | VIEWER+ | قائمة السيارات |
| GET | /cars/{id} | VIEWER+ | تفاصيل سيارة |
| GET | /cars/vin/{vin} | VIEWER+ | بحث بالشاسيه |
| GET | /cars/search?q= | VIEWER+ | بحث شامل |
| POST | /cars | ADMIN | إضافة سيارة |
| PUT | /cars/{id} | ADMIN | تحديث سيارة |
| DELETE | /cars/{id} | ADMIN | حذف سيارة |

### أكواد الأعطال
| Method | Endpoint | الوظيفة |
|--------|----------|---------|
| GET | /dtc/{code} | تفاصيل كود (P0300) |
| GET | /dtc | قائمة الأكواد |
| GET | /dtc/search?q= | بحث |
| GET | /dtc/critical | الأكواد الحرجة |

---

## 🔐 الصلاحيات
| الدور | يستطيع |
|-------|-------|
| ADMIN | كل شيء |
| TECHNICIAN | التشخيص + القراءة |
| VIEWER | القراءة فقط |

---

## 🌍 متغيرات البيئة
```bash
DB_USERNAME=mechanic_user
DB_PASSWORD=mechanic_pass
JWT_SECRET=your-256-bit-secret-key
SPRING_PROFILES_ACTIVE=prod
```
