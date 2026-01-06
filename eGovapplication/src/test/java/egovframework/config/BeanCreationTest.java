package egovframework.config;

import egovframework.common.constants.BeanNames;
import egovframework.repository.mybatis.mapper.MemberSignupMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.*;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Bean 생성 테스트
 *
 * <p>DataSource 환경에서 모든 필수 빈이 정상적으로 생성되었는지 확인합니다.</p>
 *
 * <h2>테스트 대상 빈</h2>
 * <ul>
 *   <li>Basic DataSource 관련: DataSource, SqlSessionFactory, SqlSessionTemplate, TransactionManager</li>
 *   <li>Mapper: MemberSignupMapper</li>
 * </ul>
 *
 * @see BeanNames
 */
@SpringBootTest
@DisplayName("Bean 생성 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BeanCreationTest {

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
    // Mapper
    // ============================================
    @Autowired
    private MemberSignupMapper memberSignupMapper;

    // ============================================
    // Basic DataSource 빈 테스트
    // ============================================

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

    // ============================================
    // Mapper 빈 테스트
    // ============================================

    @Test
    @Order(5)
    @DisplayName("MemberSignupMapper 빈 생성 확인")
    void memberSignupMapperBeanTest() {
        System.out.println("========== MemberSignupMapper 빈 생성 테스트 ==========");
        System.out.println("MemberSignupMapper: " + memberSignupMapper);

        assertThat(memberSignupMapper).isNotNull();
        System.out.println("결과: 성공");
    }
}
