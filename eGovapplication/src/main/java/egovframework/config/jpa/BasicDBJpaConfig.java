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
 * <h3>설정 항목</h3>
 * <ul>
 *   <li><b>basePackages</b>: JPA Repository 인터페이스 스캔 경로</li>
 *   <li><b>entityManagerFactoryRef</b>: EntityManagerFactory 빈 이름</li>
 *   <li><b>transactionManagerRef</b>: 트랜잭션 매니저 빈 이름 (MyBatis와 공유)</li>
 * </ul>
 *
 * <h3>다중 데이터베이스 구성 시 주의사항</h3>
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
 *     // ...
 * }
 * }</pre>
 *
 * <h3>패키지 구조 권장사항</h3>
 * <pre>
 * egovframework/
 * └── repository/
 *     ├── jpa/          ← 기본 DB용 JPA Repository, Entity
 *     ├── secondjpa/    ← 두번째 DB용 JPA Repository, Entity
 *     └── thirdjpa/     ← 세번째 DB용 JPA Repository, Entity
 * </pre>
 *
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
    @Bean(name = "BasicDBJPAManager")
    public LocalContainerEntityManagerFactoryBean dataEntityManager(
            @Qualifier(BeanNames.BASIC_DATASOURCE) DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dataSource);
        em.setPackagesToScan("egovframework.repository.jpa"); // jpa 엔티티 패키지
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        
        // 추가 jpa설덩 테이블 정보 업데이트 쿼리 출력 등등
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        em.setJpaPropertyMap(properties);

        return em;
    }
}
