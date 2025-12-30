package egovframework.config;

import egovframework.common.constants.BeanNames;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 다중 DataSource 격리 테스트
 *
 * <p>Basic DataSource와 Second DataSource가 서로 독립적인 인스턴스로 생성되었는지 확인합니다.</p>
 *
 * <h2>테스트 항목</h2>
 * <ul>
 *   <li>DataSource 인스턴스 분리</li>
 *   <li>SqlSessionFactory 인스턴스 분리</li>
 *   <li>SqlSessionTemplate 인스턴스 분리</li>
 *   <li>TransactionManager 인스턴스 분리</li>
 * </ul>
 *
 * <h2>중요</h2>
 * <p>다중 DataSource 환경에서는 각 컴포넌트가 1:1:1 관계를 유지해야 합니다.</p>
 * <pre>
 * DataSource : SqlSessionFactory : TransactionManager = 1:1:1
 * </pre>
 *
 * @see BeanNames
 */
@SpringBootTest
@DisplayName("다중 DataSource 격리 테스트")
class MultiDataSourceIsolationTest {

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

    @Test
    @DisplayName("Basic과 Second DataSource가 서로 다른 인스턴스인지 확인")
    void dataSourcesAreDifferentTest() {
        System.out.println("========== DataSource 인스턴스 분리 테스트 ==========");
        System.out.println("Basic DataSource: " + System.identityHashCode(basicDataSource));
        System.out.println("Second DataSource: " + System.identityHashCode(secondDataSource));

        assertThat(basicDataSource).isNotSameAs(secondDataSource);
        System.out.println("결과: 성공 (서로 다른 인스턴스)");
    }

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
