package egovframework;

import egovframework.common.constants.BeanNames;
import egovframework.mapper.member.MemberSignupMapper;
import egovframework.member.signup.dto.MemberVO;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.*;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 다중 DataSource 환경 테스트
 *
 * <h2>테스트 구성</h2>
 * <ul>
 *   <li>Basic DataSource: 기본 DB 연결 및 MemberSignupMapper 테스트</li>
 *   <li>Second DataSource: 보조 DB 연결 테스트</li>
 * </ul>
 *
 * @see BeanNames 빈 이름 상수
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EgovBootApplicationTests {

    // ============================================
    // Basic DataSource 관련 빈
    // ============================================
    @Autowired
    @Qualifier(BeanNames.BASIC_DATASOURCE)
    private DataSource basicDataSource;

    @Autowired
    @Qualifier(BeanNames.Basic_Sql_Session)
    private SqlSessionFactory basicSqlSessionFactory;

    @Autowired
    @Qualifier(BeanNames.Basic_Sql_Session_Template)
    private SqlSessionTemplate basicSqlSessionTemplate;

    @Autowired
    @Qualifier(BeanNames.Basic_Transaction)
    private PlatformTransactionManager basicTransactionManager;

    // ============================================
    // Second DataSource 관련 빈
    // ============================================
    @Autowired
    @Qualifier(BeanNames.SECOND_DATASOURCE)
    private DataSource secondDataSource;

    @Autowired
    @Qualifier(BeanNames.Second_Sql_Session)
    private SqlSessionFactory secondSqlSessionFactory;

    @Autowired
    @Qualifier(BeanNames.Second_Sql_Session_Template)
    private SqlSessionTemplate secondSqlSessionTemplate;

    @Autowired
    @Qualifier(BeanNames.Second_Transaction)
    private PlatformTransactionManager secondTransactionManager;

    // ============================================
    // Mapper (Basic DataSource 사용)
    // ============================================
    @Autowired
    private MemberSignupMapper memberSignupMapper;

    // ============================================
    // 테스트 데이터
    // ============================================
    private MemberVO testMember;
    private final String TEST_MEMBER_ID = "testuser001";
    private final String TEST_EMAIL = "testuser001@test.com";

    @BeforeEach
    void setUp() {
        testMember = MemberVO.builder()
                .memberId(TEST_MEMBER_ID)
                .password("encodedPassword123")
                .memberName("테스트유저")
                .email(TEST_EMAIL)
                .phone("010-1234-5678")
                .status("ACTIVE")
                .role("USER")
                .build();
    }

    // ============================================================
    // 1. Bean 생성 테스트 - 모든 필수 빈이 정상 생성되었는지 확인
    // ============================================================

    @Nested
    @DisplayName("1. Bean 생성 테스트")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class BeanCreationTests {

        @Test
        @Order(1)
        @DisplayName("Basic DataSource 빈 생성 확인")
        void basicDataSourceBeanTest() {
            System.out.println("========== Basic DataSource 빈 생성 테스트 ==========");
            System.out.println("DataSource: " + basicDataSource);
            System.out.println("DataSource Class: " + basicDataSource.getClass().getName());

            assertThat(basicDataSource).isNotNull();
            System.out.println("결과: 성공");
        }

        @Test
        @Order(2)
        @DisplayName("Basic SqlSessionFactory 빈 생성 확인")
        void basicSqlSessionFactoryBeanTest() {
            System.out.println("========== Basic SqlSessionFactory 빈 생성 테스트 ==========");
            System.out.println("SqlSessionFactory: " + basicSqlSessionFactory);

            assertThat(basicSqlSessionFactory).isNotNull();
            System.out.println("결과: 성공");
        }

        @Test
        @Order(3)
        @DisplayName("Basic SqlSessionTemplate 빈 생성 확인")
        void basicSqlSessionTemplateBeanTest() {
            System.out.println("========== Basic SqlSessionTemplate 빈 생성 테스트 ==========");
            System.out.println("SqlSessionTemplate: " + basicSqlSessionTemplate);

            assertThat(basicSqlSessionTemplate).isNotNull();
            System.out.println("결과: 성공");
        }

        @Test
        @Order(4)
        @DisplayName("Basic TransactionManager 빈 생성 확인")
        void basicTransactionManagerBeanTest() {
            System.out.println("========== Basic TransactionManager 빈 생성 테스트 ==========");
            System.out.println("TransactionManager: " + basicTransactionManager);
            System.out.println("TransactionManager Class: " + basicTransactionManager.getClass().getName());

            assertThat(basicTransactionManager).isNotNull();
            System.out.println("결과: 성공");
        }

        @Test
        @Order(5)
        @DisplayName("Second DataSource 빈 생성 확인")
        void secondDataSourceBeanTest() {
            System.out.println("========== Second DataSource 빈 생성 테스트 ==========");
            System.out.println("DataSource: " + secondDataSource);
            System.out.println("DataSource Class: " + secondDataSource.getClass().getName());

            assertThat(secondDataSource).isNotNull();
            System.out.println("결과: 성공");
        }

        @Test
        @Order(6)
        @DisplayName("Second SqlSessionFactory 빈 생성 확인")
        void secondSqlSessionFactoryBeanTest() {
            System.out.println("========== Second SqlSessionFactory 빈 생성 테스트 ==========");
            System.out.println("SqlSessionFactory: " + secondSqlSessionFactory);

            assertThat(secondSqlSessionFactory).isNotNull();
            System.out.println("결과: 성공");
        }

        @Test
        @Order(7)
        @DisplayName("Second SqlSessionTemplate 빈 생성 확인")
        void secondSqlSessionTemplateBeanTest() {
            System.out.println("========== Second SqlSessionTemplate 빈 생성 테스트 ==========");
            System.out.println("SqlSessionTemplate: " + secondSqlSessionTemplate);

            assertThat(secondSqlSessionTemplate).isNotNull();
            System.out.println("결과: 성공");
        }

        @Test
        @Order(8)
        @DisplayName("Second TransactionManager 빈 생성 확인")
        void secondTransactionManagerBeanTest() {
            System.out.println("========== Second TransactionManager 빈 생성 테스트 ==========");
            System.out.println("TransactionManager: " + secondTransactionManager);
            System.out.println("TransactionManager Class: " + secondTransactionManager.getClass().getName());

            assertThat(secondTransactionManager).isNotNull();
            System.out.println("결과: 성공");
        }

        @Test
        @Order(9)
        @DisplayName("MemberSignupMapper 빈 생성 확인")
        void memberSignupMapperBeanTest() {
            System.out.println("========== MemberSignupMapper 빈 생성 테스트 ==========");
            System.out.println("MemberSignupMapper: " + memberSignupMapper);

            assertThat(memberSignupMapper).isNotNull();
            System.out.println("결과: 성공");
        }

        @Test
        @Order(10)
        @DisplayName("두 DataSource가 서로 다른 인스턴스인지 확인")
        void dataSourcesAreDifferentTest() {
            System.out.println("========== DataSource 인스턴스 분리 테스트 ==========");
            System.out.println("Basic DataSource: " + System.identityHashCode(basicDataSource));
            System.out.println("Second DataSource: " + System.identityHashCode(secondDataSource));

            assertThat(basicDataSource).isNotSameAs(secondDataSource);
            System.out.println("결과: 성공 (서로 다른 인스턴스)");
        }
    }

    // ============================================================
    // 2. DB 연결 테스트 - 각 DataSource가 실제 DB에 연결되는지 확인
    // ============================================================

    @Nested
    @DisplayName("2. DB 연결 테스트")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class DatabaseConnectionTests {

        @Test
        @Order(1)
        @DisplayName("Basic DataSource DB 연결 테스트")
        void basicDataSourceConnectionTest() throws Exception {
            System.out.println("========== Basic DataSource DB 연결 테스트 ==========");

            try (Connection conn = basicDataSource.getConnection()) {
                assertThat(conn).isNotNull();
                assertThat(conn.isClosed()).isFalse();

                System.out.println("Connection: " + conn);
                System.out.println("Catalog: " + conn.getCatalog());
                System.out.println("Schema: " + conn.getSchema());
                System.out.println("AutoCommit: " + conn.getAutoCommit());

                // Oracle 연결 테스트 쿼리
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT 1 FROM DUAL")) {
                    assertThat(rs.next()).isTrue();
                    assertThat(rs.getInt(1)).isEqualTo(1);
                    System.out.println("SELECT 1 FROM DUAL 결과: " + rs.getInt(1));
                }

                System.out.println("결과: 성공");
            }
        }

        @Test
        @Order(2)
        @DisplayName("Second DataSource DB 연결 테스트")
        void secondDataSourceConnectionTest() throws Exception {
            System.out.println("========== Second DataSource DB 연결 테스트 ==========");

            try (Connection conn = secondDataSource.getConnection()) {
                assertThat(conn).isNotNull();
                assertThat(conn.isClosed()).isFalse();

                System.out.println("Connection: " + conn);
                System.out.println("Catalog: " + conn.getCatalog());
                System.out.println("Schema: " + conn.getSchema());
                System.out.println("AutoCommit: " + conn.getAutoCommit());

                // Oracle 연결 테스트 쿼리
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT 1 FROM DUAL")) {
                    assertThat(rs.next()).isTrue();
                    assertThat(rs.getInt(1)).isEqualTo(1);
                    System.out.println("SELECT 1 FROM DUAL 결과: " + rs.getInt(1));
                }

                System.out.println("결과: 성공");
            }
        }

        @Test
        @Order(3)
        @DisplayName("Basic SqlSessionTemplate으로 쿼리 실행 테스트")
        void basicSqlSessionTemplateQueryTest() {
            System.out.println("========== Basic SqlSessionTemplate 쿼리 테스트 ==========");

            Integer result = basicSqlSessionTemplate.selectOne(
                    "egovframework.mapper.member.MemberSignupMapper.testConnection"
            );

            System.out.println("쿼리 결과: " + result);
            assertThat(result).isEqualTo(1);
            System.out.println("결과: 성공");
        }

        @Test
        @Order(4)
        @DisplayName("Second SqlSessionTemplate으로 쿼리 실행 테스트")
        void secondSqlSessionTemplateQueryTest() {
            System.out.println("========== Second SqlSessionTemplate 쿼리 테스트 ==========");

            // Second DataSource도 동일한 Mapper XML을 사용하므로 동일한 쿼리 실행 가능
            Integer result = secondSqlSessionTemplate.selectOne(
                    "egovframework.mapper.member.MemberSignupMapper.testConnection"
            );

            System.out.println("쿼리 결과: " + result);
            assertThat(result).isEqualTo(1);
            System.out.println("결과: 성공");
        }
    }

    // ============================================================
    // 3. MemberSignupMapper 테스트 (Basic DataSource 사용)
    // ============================================================

    @Nested
    @DisplayName("3. MemberSignupMapper 테스트 (Basic DataSource)")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class MemberSignupMapperTests {

        @Test
        @Order(1)
        @DisplayName("회원 등록 테스트")
        void insertMemberTest() {
            System.out.println("========== 회원 등록 테스트 ==========");
            System.out.println("등록할 회원 정보:");
            System.out.println("  - ID: " + testMember.getMemberId());
            System.out.println("  - 이름: " + testMember.getMemberName());
            System.out.println("  - 이메일: " + testMember.getEmail());
            System.out.println("  - 전화번호: " + testMember.getPhone());

            int result = memberSignupMapper.insertMember(testMember);

            System.out.println("INSERT 결과: " + result + "건");
            assertThat(result).isEqualTo(1);
            System.out.println("결과: 성공");
        }

        @Test
        @Order(2)
        @DisplayName("회원 ID로 조회 테스트")
        void selectMemberByIdTest() {
            System.out.println("========== 회원 ID로 조회 테스트 ==========");

            // given
//            memberSignupMapper.insertMember(testMember);
//            System.out.println("테스트 데이터 INSERT 완료");

            // when
            MemberVO foundMember = memberSignupMapper.selectMemberById(TEST_MEMBER_ID);

            // then
            System.out.println("조회 결과:");
            System.out.println("  - ID: " + foundMember.getMemberId());
            System.out.println("  - 이름: " + foundMember.getMemberName());
            System.out.println("  - 이메일: " + foundMember.getEmail());
            System.out.println("  - 상태: " + foundMember.getStatus());
            System.out.println("  - 권한: " + foundMember.getRole());
            System.out.println("  - 생성일: " + foundMember.getCreatedAt());

            assertThat(foundMember).isNotNull();
            assertThat(foundMember.getMemberId()).isEqualTo(TEST_MEMBER_ID);
            assertThat(foundMember.getMemberName()).isEqualTo("테스트유저");
            assertThat(foundMember.getEmail()).isEqualTo(TEST_EMAIL);
            System.out.println("결과: 성공");
        }

        @Test
        @Order(3)
        @DisplayName("이메일로 조회 테스트")
        void selectMemberByEmailTest() {
            System.out.println("========== 이메일로 조회 테스트 ==========");

            // given
//            memberSignupMapper.insertMember(testMember);
//            System.out.println("테스트 데이터 INSERT 완료");

            // when
            MemberVO foundMember = memberSignupMapper.selectMemberByEmail(TEST_EMAIL);

            // then
            System.out.println("이메일 [" + TEST_EMAIL + "]로 조회 결과:");
            System.out.println("  - ID: " + foundMember.getMemberId());
            System.out.println("  - 이름: " + foundMember.getMemberName());

            assertThat(foundMember).isNotNull();
            assertThat(foundMember.getEmail()).isEqualTo(TEST_EMAIL);
            System.out.println("결과: 성공");
        }

        @Test
        @Order(4)
        @DisplayName("회원 ID 중복 체크 테스트")
        void checkMemberIdExistsTest() {
            System.out.println("========== 회원 ID 중복 체크 테스트 ==========");

            // given
//            memberSignupMapper.insertMember(testMember);
//            System.out.println("테스트 데이터 INSERT 완료");

            // when
            int existsResult = memberSignupMapper.checkMemberIdExists(TEST_MEMBER_ID);
            int notExistsResult = memberSignupMapper.checkMemberIdExists("nonexistent");

            // then
            System.out.println("ID [" + TEST_MEMBER_ID + "] 존재 여부: " + existsResult);
            System.out.println("ID [nonexistent] 존재 여부: " + notExistsResult);

            assertThat(existsResult).isEqualTo(1);
            assertThat(notExistsResult).isEqualTo(0);
            System.out.println("결과: 성공");
        }

        @Test
        @Order(5)
        @DisplayName("이메일 중복 체크 테스트")
        void checkEmailExistsTest() {
            System.out.println("========== 이메일 중복 체크 테스트 ==========");

            // given
//            memberSignupMapper.insertMember(testMember);
//            System.out.println("테스트 데이터 INSERT 완료");

            // when
            int existsResult = memberSignupMapper.checkEmailExists(TEST_EMAIL);
            int notExistsResult = memberSignupMapper.checkEmailExists("nonexistent@test.com");

            // then
            System.out.println("이메일 [" + TEST_EMAIL + "] 존재 여부: " + existsResult);
            System.out.println("이메일 [nonexistent@test.com] 존재 여부: " + notExistsResult);

            assertThat(existsResult).isEqualTo(1);
            assertThat(notExistsResult).isEqualTo(0);
            System.out.println("결과: 성공");
        }

        @Test
        @Order(6)
        @DisplayName("회원 정보 수정 테스트")
        void updateMemberTest() {
            System.out.println("========== 회원 정보 수정 테스트 ==========");

            // given
//            memberSignupMapper.insertMember(testMember);
//            System.out.println("테스트 데이터 INSERT 완료");

            MemberVO updateMember = MemberVO.builder()
                    .memberId(TEST_MEMBER_ID)
                    .memberName("수정된이름")
                    .email("updated@test.com")
                    .phone("010-9999-8888")
                    .build();

            System.out.println("수정할 정보:");
            System.out.println("  - 이름: 테스트유저 -> 수정된이름");
            System.out.println("  - 이메일: " + TEST_EMAIL + " -> updated@test.com");
            System.out.println("  - 전화번호: 010-1234-5678 -> 010-9999-8888");

            // when
            int result = memberSignupMapper.updateMember(updateMember);
            MemberVO foundMember = memberSignupMapper.selectMemberById(TEST_MEMBER_ID);

            // then
            System.out.println("UPDATE 결과: " + result + "건");
            System.out.println("수정 후 조회 결과:");
            System.out.println("  - 이름: " + foundMember.getMemberName());
            System.out.println("  - 이메일: " + foundMember.getEmail());
            System.out.println("  - 전화번호: " + foundMember.getPhone());

            assertThat(result).isEqualTo(1);
            assertThat(foundMember.getMemberName()).isEqualTo("수정된이름");
            assertThat(foundMember.getEmail()).isEqualTo("updated@test.com");
            assertThat(foundMember.getPhone()).isEqualTo("010-9999-8888");
            System.out.println("결과: 성공");
        }

        @Test
        @Order(7)
        @DisplayName("비밀번호 변경 테스트")
        void updatePasswordTest() {
            System.out.println("========== 비밀번호 변경 테스트 ==========");

            // given
//            memberSignupMapper.insertMember(testMember);
//            System.out.println("테스트 데이터 INSERT 완료");

            MemberVO passwordUpdate = MemberVO.builder()
                    .memberId(TEST_MEMBER_ID)
                    .password("newEncodedPassword456")
                    .build();

            System.out.println("비밀번호 변경: encodedPassword123 -> newEncodedPassword456");

            // when
            int result = memberSignupMapper.updatePassword(passwordUpdate);
            MemberVO foundMember = memberSignupMapper.selectMemberById(TEST_MEMBER_ID);

            // then
            System.out.println("UPDATE 결과: " + result + "건");
            System.out.println("변경된 비밀번호: " + foundMember.getPassword());

            assertThat(result).isEqualTo(1);
            assertThat(foundMember.getPassword()).isEqualTo("newEncodedPassword456");
            System.out.println("결과: 성공");
        }

        @Test
        @Order(8)
        @DisplayName("마지막 로그인 시간 업데이트 테스트")
        void updateLastLoginTest() {
            System.out.println("========== 마지막 로그인 시간 업데이트 테스트 ==========");

            // given
//            memberSignupMapper.insertMember(testMember);
//            System.out.println("테스트 데이터 INSERT 완료");

            // when
            int result = memberSignupMapper.updateLastLogin(TEST_MEMBER_ID);
            MemberVO foundMember = memberSignupMapper.selectMemberById(TEST_MEMBER_ID);

            // then
            System.out.println("UPDATE 결과: " + result + "건");
            System.out.println("마지막 로그인 시간: " + foundMember.getLastLoginAt());

            assertThat(result).isEqualTo(1);
            assertThat(foundMember.getLastLoginAt()).isNotNull();
            System.out.println("결과: 성공");
        }

        @Test
        @Order(9)
        @DisplayName("회원 상태 변경 테스트")
        void updateStatusTest() {
            System.out.println("========== 회원 상태 변경 테스트 ==========");

            // given
//            memberSignupMapper.insertMember(testMember);
//            System.out.println("테스트 데이터 INSERT 완료");

            System.out.println("상태 변경: ACTIVE -> SUSPENDED");

            // when
            int result = memberSignupMapper.updateStatus(TEST_MEMBER_ID, "SUSPENDED");
            MemberVO foundMember = memberSignupMapper.selectMemberById(TEST_MEMBER_ID);

            // then
            System.out.println("UPDATE 결과: " + result + "건");
            System.out.println("변경된 상태: " + foundMember.getStatus());

            assertThat(result).isEqualTo(1);
            assertThat(foundMember.getStatus()).isEqualTo("SUSPENDED");
            System.out.println("결과: 성공");
        }

        @Test
        @Order(10)
        @DisplayName("회원 삭제 테스트")
        void deleteMemberTest() {
            System.out.println("========== 회원 삭제 테스트 ==========");

            // given
//            memberSignupMapper.insertMember(testMember);
//            System.out.println("테스트 데이터 INSERT 완료");

            // when
            System.out.println("회원 [" + TEST_MEMBER_ID + "] 삭제 실행");
            int result = memberSignupMapper.deleteMember(TEST_MEMBER_ID);
            MemberVO foundMember = memberSignupMapper.selectMemberById(TEST_MEMBER_ID);

            // then
            System.out.println("DELETE 결과: " + result + "건");
            System.out.println("삭제 후 조회 결과: " + foundMember);

            assertThat(result).isEqualTo(1);
            assertThat(foundMember).isNull();
            System.out.println("결과: 성공");
        }

        @Test
        @Order(11)
        @DisplayName("존재하지 않는 회원 조회시 null 반환 테스트")
        void selectNonExistentMemberTest() {
            System.out.println("========== 존재하지 않는 회원 조회 테스트 ==========");

            // when
            MemberVO foundMember = memberSignupMapper.selectMemberById("nonexistent");

            // then
            System.out.println("ID [nonexistent] 조회 결과: " + foundMember);

            assertThat(foundMember).isNull();
            System.out.println("결과: 성공 (null 반환 확인)");
        }
    }

    // ============================================================
    // 4. 다중 DataSource 격리 테스트
    // ============================================================

    @Nested
    @DisplayName("4. 다중 DataSource 격리 테스트")
    class MultiDataSourceIsolationTests {

        @Test
        @DisplayName("Basic과 Second SqlSessionFactory가 서로 다른 인스턴스인지 확인")
        void sqlSessionFactoriesAreDifferentTest() {
            System.out.println("========== SqlSessionFactory 인스턴스 분리 테스트 ==========");
            System.out.println("Basic SqlSessionFactory: " + System.identityHashCode(basicSqlSessionFactory));
            System.out.println("Second SqlSessionFactory: " + System.identityHashCode(secondSqlSessionFactory));

            assertThat(basicSqlSessionFactory).isNotSameAs(secondSqlSessionFactory);
            System.out.println("결과: 성공 (서로 다른 인스턴스)");
        }

        @Test
        @DisplayName("Basic과 Second SqlSessionTemplate이 서로 다른 인스턴스인지 확인")
        void sqlSessionTemplatesAreDifferentTest() {
            System.out.println("========== SqlSessionTemplate 인스턴스 분리 테스트 ==========");
            System.out.println("Basic SqlSessionTemplate: " + System.identityHashCode(basicSqlSessionTemplate));
            System.out.println("Second SqlSessionTemplate: " + System.identityHashCode(secondSqlSessionTemplate));

            assertThat(basicSqlSessionTemplate).isNotSameAs(secondSqlSessionTemplate);
            System.out.println("결과: 성공 (서로 다른 인스턴스)");
        }

        @Test
        @DisplayName("Basic과 Second TransactionManager가 서로 다른 인스턴스인지 확인")
        void transactionManagersAreDifferentTest() {
            System.out.println("========== TransactionManager 인스턴스 분리 테스트 ==========");
            System.out.println("Basic TransactionManager: " + System.identityHashCode(basicTransactionManager));
            System.out.println("Second TransactionManager: " + System.identityHashCode(secondTransactionManager));

            assertThat(basicTransactionManager).isNotSameAs(secondTransactionManager);
            System.out.println("결과: 성공 (서로 다른 인스턴스)");
        }
    }
}
