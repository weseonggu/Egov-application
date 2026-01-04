package egovframework.config.transaction;

import egovframework.common.constants.BeanNames;
import egovframework.config.database.ApplicationDatasourceConfig;
import egovframework.config.mybatis.BasicDBMybatisMapperConfig;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 트랜잭션 설정 - 기본 DataSource용
 *
 * <h2>@Transactional 어노테이션 기반 트랜잭션 관리</h2>
 *
 * <h3>사용방법</h3>
 * <pre>
 * {@literal @}Service
 * {@literal @}Transactional(readOnly = true)  // 클래스 레벨 기본값
 * public class MemberServiceImpl {
 *
 *     {@literal @}Transactional  // 쓰기 작업
 *     public void insert(MemberVO vo) { ... }
 *
 *     {@literal @}Transactional(rollbackFor = Exception.class)  // 롤백 조건 명시
 *     public void update(MemberVO vo) { ... }
 * }
 * </pre>
 *
 * <h3>주요 속성</h3>
 * <ul>
 *   <li>propagation: 전파 속성 (기본: REQUIRED)</li>
 *   <li>isolation: 격리 수준 (기본: DEFAULT)</li>
 *   <li>readOnly: 읽기 전용 최적화 (기본: false)</li>
 *   <li>rollbackFor: 롤백 대상 예외</li>
 *   <li>timeout: 타임아웃 (초)</li>
 * </ul>
 *
 * <h2>신규 DataSource 추가 시 트랜잭션 설정 방법</h2>
 * <p>새로운 DB를 추가할 때는 해당 DB의 TransactionManager를 추가합니다.</p>
 *
 * <ol>
 *   <li>
 *     <b>1단계: BeanNames에 상수 추가</b>
 *     <pre>
 * public static final String SECONDARY_TRANSACTION = "secondaryTransaction";
 *     </pre>
 *   </li>
 *   <li>
 *     <b>2단계: TransactionManager 빈 추가</b>
 *     <p>이 클래스에 새로운 빈을 추가하거나, 별도 설정 클래스를 생성합니다.</p>
 *     <pre>
 * {@literal @}Bean(name = BeanNames.SECONDARY_TRANSACTION)
 * public PlatformTransactionManager secondaryTransactionManager(
 *         {@literal @}Qualifier(BeanNames.SECONDARY_DATASOURCE) DataSource dataSource) {
 *     return new DataSourceTransactionManager(dataSource);
 * }
 *     </pre>
 *   </li>
 *   <li>
 *     <b>3단계: 서비스에서 transactionManager 지정</b>
 *     <pre>
 * // 기본 DB 사용 (transactionManager 생략 가능 - @Primary 적용됨)
 * {@literal @}Transactional
 * public void saveMember(MemberVO vo) { ... }
 *
 * // 신규 DB 사용 (transactionManager 명시 필요)
 * {@literal @}Transactional(transactionManager = BeanNames.SECONDARY_TRANSACTION)
 * public void saveLog(LogVO vo) { ... }
 *     </pre>
 *   </li>
 * </ol>
 *
 * <h2>중요 포인트</h2>
 * <p><b>DataSource : TransactionManager = 1:1 관계</b></p>
 * <ul>
 *   <li>각 DB별로 별도의 TransactionManager 생성</li>
 *   <li>기본 DB는 {@code @Primary} 지정하여 transactionManager 생략 가능</li>
 *   <li>추가 DB는 {@code @Transactional(transactionManager = "빈이름")} 명시 필요</li>
 * </ul>
 *
 * <h2>설정 클래스간 관계</h2>
 * <pre>
 * +----------------------------------+
 * |  ApplicationDatasourceConfig    | <- DataSource 생성
 * |  (1단계)                        |
 * +----------------+-----------------+
 *                  |
 *         +--------+--------+
 *         v                 v
 * +----------------+  +---------------------+
 * | BasicDB        |  | EgovConfig          |
 * | ConfigMapper   |  | Transaction         |
 * | (2단계)        |  | (2단계)             |
 * |                |  |                     |
 * | SqlSession     |  | Transaction         |
 * | Factory        |  | Manager             |
 * +----------------+  +---------------------+
 * </pre>
 *
 * @see ApplicationDatasourceConfig DataSource 설정
 * @see BasicDBMybatisMapperConfig MyBatis 설정
 * @see BeanNames 빈 이름 상수
 */
@Configuration
@EnableTransactionManagement
public class TransactionConfig {

    /**
     * 트랜잭션 매니저 빈 설정 - 기본 DataSource용 (JPA + MyBatis 공용)
     *
     * <p>JpaTransactionManager는 JPA와 MyBatis 모두 지원합니다.</p>
     * <ul>
     *   <li>JPA: EntityManager의 영속성 컨텍스트 관리 + flush/commit</li>
     *   <li>MyBatis: 동일 DataSource의 Connection 공유로 트랜잭션 참여</li>
     * </ul>
     *
     * @param entityManagerFactory JPA EntityManagerFactory
     * @return PlatformTransactionManager
     */
    @Bean(name = BeanNames.Basic_Transaction)
    public PlatformTransactionManager basicTransactionManager(
            @Qualifier("BasicDBJPAManager") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    /**
     * 트랜잭션 매니저 빈 설정 - Second DataSource용
     *
     * @param dataSource 데이터소스
     * @return PlatformTransactionManager
     */
    @Bean(name = BeanNames.Second_Transaction)
    public PlatformTransactionManager secondTransactionManager(
            @Qualifier(BeanNames.SECOND_DATASOURCE) DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
