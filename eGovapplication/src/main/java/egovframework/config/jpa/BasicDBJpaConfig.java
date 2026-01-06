package egovframework.config.jpa;

import egovframework.common.constants.BeanNames;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * 기본 데이터베이스 JPA 설정 클래스.
 *
 * <p>Spring Data JPA를 사용하여 기본 데이터소스에 대한 JPA Repository를 구성합니다.
 * MyBatis와 함께 사용할 수 있으며, 동일한 트랜잭션 매니저를 공유합니다.</p>
 *
 * <h2>스캔 대상 패키지</h2>
 * <table border="1">
 *   <tr><th>설정 항목</th><th>대상 패키지</th><th>설명</th></tr>
 *   <tr>
 *     <td>{@code basePackages}</td>
 *     <td>{@code egovframework.repository.jpa}</td>
 *     <td>JPA Repository 인터페이스 스캔</td>
 *   </tr>
 *   <tr>
 *     <td>{@code setPackagesToScan}</td>
 *     <td>{@code egovframework.repository.jpa.entity}</td>
 *     <td>JPA Entity 클래스 스캔</td>
 *   </tr>
 * </table>
 *
 * <h2>패키지 구조</h2>
 * <pre>
 * egovframework/repository/jpa/
 * +-- entity/                           <- JPA Entity (@Entity)
 * |   +-- Member.java
 * +-- MemberRepository.java             <- JPA Repository (JpaRepository 상속)
 * </pre>
 *
 * <h2>다중 데이터베이스 구성</h2>
 * <p>추가 데이터베이스에 대한 JPA 설정이 필요한 경우, 별도의 설정 클래스를 생성하고
 * <b>반드시 다른 패키지 경로를 지정</b>해야 합니다.</p>
 *
 * <pre>{@code
 * // 예시: 추가 DB용 JPA 설정
 * @Configuration
 * @EnableJpaRepositories(
 *     basePackages = "egovframework.repository.secondjpa",  // 별도 패키지 필수
 *     entityManagerFactoryRef = "SecondDBJPAManager",
 *     transactionManagerRef = BeanNames.Second_Transaction
 * )
 * public class SecondDBJpaConfig {
 *
 *     @Bean(name = "SecondDBJPAManager")
 *     public LocalContainerEntityManagerFactoryBean dataEntityManager(...) {
 *         em.setPackagesToScan("egovframework.repository.secondjpa.entity");
 *         // ...
 *     }
 * }
 * }</pre>
 *
 * <h2>다중 DB 패키지 구조 권장사항</h2>
 * <pre>
 * egovframework/repository/
 * +-- jpa/                              <- 기본 DB용
 * |   +-- entity/
 * |   |   +-- Member.java
 * |   +-- MemberRepository.java
 * +-- secondjpa/                        <- 두번째 DB용
 * |   +-- entity/
 * |   |   +-- Log.java
 * |   +-- LogRepository.java
 * +-- thirdjpa/                         <- 세번째 DB용
 *     +-- entity/
 *     +-- ...
 * </pre>
 *
 * @see egovframework.repository.jpa JPA Repository 패키지 (스캔 대상)
 * @see egovframework.repository.jpa.entity.Member Entity 클래스 예시
 * @see egovframework.repository.jpa.MemberRepository Repository 인터페이스 예시
 * @see org.springframework.data.jpa.repository.config.EnableJpaRepositories
 * @see org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "egovframework.repository.jpa",
        entityManagerFactoryRef = "BasicDBJPAManager",
        transactionManagerRef = BeanNames.Basic_Transaction
)
public class BasicDBJpaConfig {

    /**
     * 기본 데이터베이스용 EntityManagerFactory 빈 생성.
     *
     * <p>JPA Entity 관리를 위한 EntityManagerFactory를 구성합니다.
     * Hibernate를 JPA 구현체로 사용하며, 개발 환경에 맞는 설정이 적용되어 있습니다.</p>
     *
     * <h3>Hibernate 설정 옵션</h3>
     * <table border="1">
     *   <tr><th>속성</th><th>값</th><th>설명</th></tr>
     *   <tr><td>hibernate.hbm2ddl.auto</td><td>update</td><td>Entity 변경 시 테이블 자동 업데이트 (운영환경에서는 validate 또는 none 권장)</td></tr>
     *   <tr><td>hibernate.show_sql</td><td>true</td><td>실행되는 SQL 콘솔 출력</td></tr>
     *   <tr><td>hibernate.format_sql</td><td>true</td><td>SQL 포맷팅하여 출력</td></tr>
     * </table>
     *
     * <h3>운영환경 설정 권장사항</h3>
     * <pre>{@code
     * properties.put("hibernate.hbm2ddl.auto", "validate"); // 또는 "none"
     * properties.put("hibernate.show_sql", "false");
     * }</pre>
     *
     * @param dataSource 기본 데이터소스 (BeanNames.BASIC_DATASOURCE)
     * @return EntityManagerFactory 빈
     */
    @Bean(name = "BasicDBJPAManager")
    public LocalContainerEntityManagerFactoryBean dataEntityManager(
            @Qualifier(BeanNames.BASIC_DATASOURCE) DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dataSource);
        em.setPackagesToScan("egovframework.repository.jpa.entity"); // JPA Entity 패키지
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        // Hibernate JPA 설정
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");   // Entity 변경 시 테이블 자동 업데이트
        properties.put("hibernate.show_sql", "true");         // SQL 출력
        properties.put("hibernate.format_sql", "true");       // SQL 포맷팅
        em.setJpaPropertyMap(properties);

        return em;
    }
}
