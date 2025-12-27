-- ============================================
-- KRSI Application 초기 스키마 설정
-- 이 스크립트는 컨테이너 최초 실행 시 자동 실행됩니다.
-- ============================================

-- 테이블스페이스 생성 (선택사항)
-- CREATE TABLESPACE krsi_data
--   DATAFILE '/opt/oracle/oradata/XE/krsi_data.dbf'
--   SIZE 100M AUTOEXTEND ON NEXT 50M MAXSIZE UNLIMITED;

-- 예제 테이블 생성
CREATE TABLE sample_table (
    id NUMBER PRIMARY KEY,
    name VARCHAR2(100) NOT NULL,
    description VARCHAR2(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 시퀀스 생성 (eGovFrame ID Generator 용)
CREATE SEQUENCE sample_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- 샘플 데이터
INSERT INTO sample_table (id, name, description) VALUES (sample_seq.NEXTVAL, 'Sample 1', 'Test data 1');
INSERT INTO sample_table (id, name, description) VALUES (sample_seq.NEXTVAL, 'Sample 2', 'Test data 2');

COMMIT;
