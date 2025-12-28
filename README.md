# eGov Application

전자정부 프레임워크 기반 Spring Boot 애플리케이션입니다.

## 기술 스택

- Java 17
- Spring Boot 3.x
- eGovFrame 5.0
- MyBatis
- Oracle Database 21c XE

## 프로젝트 구조

```
krsi-ver-egov/
├── eGovapplication/       # Spring Boot 애플리케이션
├── docker-compose/        # Docker 설정
│   └── oracle/            # Oracle DB 컨테이너
└── README.md
```

## 빠른 시작

### 1. Oracle Database 실행

```bash
cd docker-compose/oracle

# 환경 변수 파일 생성
cp exampleEnv .env

# 컨테이너 시작 (초기화에 5-10분 소요)
docker-compose up -d

# 로그 확인
docker-compose logs -f
```

### 2. 애플리케이션 설정

```bash
cd eGovapplication

# 환경 변수 파일 생성
cp exampleEnv .env

# 필요시 .env 파일 수정
```

### 3. 애플리케이션 실행

```bash
# Maven으로 실행
mvn spring-boot:run

# 또는 빌드 후 실행
mvn clean package
java -jar target/eGovapplication-1.0.0.jar
```

## 환경 변수 설정

`eGovapplication/exampleEnv` 파일을 `.env`로 복사하여 사용합니다.

| 변수 | 설명 | 기본값 |
|------|------|--------|
| `DB_URL` | Oracle JDBC URL | jdbc:oracle:thin:@localhost:1521/XEPDB1 |
| `DB_USERNAME` | DB 사용자명 | krsi_user |
| `DB_PASSWORD` | DB 비밀번호 | krsi_pass |
| `APP_PORT` | 애플리케이션 포트 | 8080 |

## 접속 URL

| 서비스 | URL |
|--------|-----|
| 애플리케이션 | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| API Docs | http://localhost:8080/v3/api-docs |
| Health Check | http://localhost:8080/actuator/health |

## 개발 환경 요구사항

- JDK 17+
- Maven 3.8+
- Docker Desktop (Oracle DB용)
- IDE: IntelliJ IDEA 권장
