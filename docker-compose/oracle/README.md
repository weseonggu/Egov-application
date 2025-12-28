# Oracle Database Docker Compose

로컬 개발용 Oracle Database 21c XE 환경입니다.

## 빠른 시작

```bash
# 1. 환경 변수 파일 생성 (최초 1회)
cp exampleEnv .env

# 2. 필요시 .env 파일 수정 (비밀번호 등)

# 3. 컨테이너 시작
docker-compose up -d

# 로그 확인 (초기화에 5-10분 소요)
docker-compose logs -f

# 컨테이너 중지
docker-compose down

# 컨테이너 및 데이터 삭제
docker-compose down -v
```

## 접속 정보

| 항목 | 값 |
|------|-----|
| **Host** | localhost |
| **Port** | 1521 |
| **SID** | XE |
| **Service Name** | XEPDB1 |
| **SYS/SYSTEM 비밀번호** | oracle123 |
| **애플리케이션 사용자** | krsi_user |
| **애플리케이션 비밀번호** | krsi_pass |

## JDBC 연결 문자열

```properties
# PDB 연결 (권장)
jdbc:oracle:thin:@localhost:1521/XEPDB1

# CDB 연결
jdbc:oracle:thin:@localhost:1521:XE
```

## Spring Boot 설정 예시

```yaml
spring:
  datasource:
    driver-class-name: oracle.jdbc.OracleDriver
    url: jdbc:oracle:thin:@localhost:1521/XEPDB1
    username: krsi_user
    password: krsi_pass
```

## SQL*Plus 접속

```bash
# 컨테이너 내부 접속
docker exec -it oracle-21c-xe sqlplus sys/oracle123@XE as sysdba

# 또는 일반 사용자로
docker exec -it oracle-21c-xe sqlplus krsi_user/krsi_pass@XEPDB1
```

## 이미지 버전 옵션

| 이미지 | 버전 | 특징 |
|--------|------|------|
| `gvenzl/oracle-xe:21-slim` | 21c XE | 권장, 경량 (약 2GB) |
| `gvenzl/oracle-xe:21` | 21c XE | 전체 버전 (약 6GB) |
| `gvenzl/oracle-free:23-slim` | 23c Free | 최신 버전 |
| `gvenzl/oracle-xe:18-slim` | 18c XE | 레거시 호환 |

## 초기화 스크립트

`init-scripts/` 폴더의 `.sql` 파일은 컨테이너 최초 실행 시 자동으로 실행됩니다.

- 알파벳 순서로 실행
- 파일명에 번호 접두사 권장 (예: `01_create_schema.sql`)

## 환경 변수 설정

`exampleEnv` 파일을 `.env`로 복사하여 사용합니다. `.env` 파일은 Git에서 제외됩니다.

| 변수 | 설명 | 기본값 |
|------|------|--------|
| `ORACLE_PASSWORD` | SYS/SYSTEM 비밀번호 | oracle123 |
| `APP_USER` | 애플리케이션 DB 사용자 | krsi_user |
| `APP_USER_PASSWORD` | 애플리케이션 DB 비밀번호 | krsi_pass |

## 트러블슈팅

### 메모리 부족
Oracle XE는 최소 2GB RAM이 필요합니다. Docker Desktop 설정에서 메모리를 4GB 이상으로 설정하세요.

### 시작 시간이 오래 걸림
최초 실행 시 5-10분 정도 소요됩니다. `docker-compose logs -f`로 진행 상황을 확인하세요.

### 데이터 초기화
```bash
docker-compose down -v
docker-compose up -d
```
