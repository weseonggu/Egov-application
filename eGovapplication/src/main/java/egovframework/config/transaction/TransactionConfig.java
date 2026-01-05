package egovframework.config.transaction;

import egovframework.common.constants.BeanNames;
import egovframework.config.database.ApplicationDatasourceConfig;
import egovframework.config.mybatis.BasicDBMybatisMapperConfig;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 트랜잭션 설정 - JPA + MyBatis 공용
 *
 * <h2>트랜잭션 매니저 종류</h2>
 * <table border="1">
 *   <tr><th>트랜잭션 매니저</th><th>용도</th><th>지원 기술</th></tr>
 *   <tr>
 *     <td>JpaTransactionManager</td>
 *     <td>JPA 사용 DB</td>
 *     <td>JPA + MyBatis 모두 지원</td>
 *   </tr>
 *   <tr>
 *     <td>DataSourceTransactionManager</td>
 *     <td>MyBatis만 사용 DB</td>
 *     <td>MyBatis만 지원</td>
 *   </tr>
 * </table>
 *
 * <h2>JpaTransactionManager vs DataSourceTransactionManager</h2>
 * <ul>
 *   <li><b>JpaTransactionManager</b>: EntityManager + Connection 관리 → JPA 영속성 컨텍스트 지원</li>
 *   <li><b>DataSourceTransactionManager</b>: Connection만 관리 → JPA 사용 불가</li>
 *   <li><b>중요</b>: JpaTransactionManager는 같은 DataSource를 사용하는 MyBatis도 트랜잭션 관리 가능</li>
 * </ul>
 *
 * <h2>@Transactional 어노테이션 기반 트랜잭션 관리</h2>
 *
 * <h3>사용방법</h3>
 * <pre>
 * {@literal @}Service
 * {@literal @}Transactional(transactionManager = BeanNames.Basic_Transaction)
 * public class MemberServiceImpl {
 *
 *     // JPA 사용
 *     public void saveWithJpa(Member member) {
 *         memberRepository.save(member);
 *     }
 *
 *     // MyBatis 사용 - 같은 트랜잭션에서 동작
 *     public void saveWithMyBatis(MemberVO vo) {
 *         memberMapper.insertMember(vo);
 *     }
 *
 *     // JPA + MyBatis 혼용 - 예외 시 둘 다 롤백
 *     public void saveWithBoth(Member member, MemberVO vo) {
 *         memberRepository.save(member);
 *         memberMapper.insertMember(vo);
 *     }
 * }
 * </pre>
 *
 * <h3>주요 속성</h3>
 * <ul>
 *   <li>transactionManager: 사용할 트랜잭션 매니저 지정</li>
 *   <li>propagation: 전파 속성 (기본: REQUIRED)</li>
 *   <li>isolation: 격리 수준 (기본: DEFAULT)</li>
 *   <li>readOnly: 읽기 전용 최적화 (기본: false)</li>
 *   <li>rollbackFor: 롤백 대상 예외</li>
 *   <li>timeout: 타임아웃 (초)</li>
 * </ul>
 *
 * <h2>신규 DataSource 추가 시 트랜잭션 설정 방법</h2>
 * <p>새로운 DB를 추가할 때는 <b>JPA 사용 여부에 따라</b> 적절한 TransactionManager를 선택합니다.</p>
 *
 * <ol>
 *   <li>
 *     <b>1단계: BeanNames에 상수 추가</b>
 *     <pre>
 * public static final String THIRD_TRANSACTION = "thirdTransaction";
 *     </pre>
 *   </li>
 *   <li>
 *     <b>2단계: TransactionManager 빈 추가 (JPA 사용 여부에 따라 선택)</b>
 *     <pre>
 * // JPA를 사용하는 DB → JpaTransactionManager
 * {@literal @}Bean(name = BeanNames.THIRD_TRANSACTION)
 * public PlatformTransactionManager thirdTransactionManager(
 *         {@literal @}Qualifier("ThirdDBJPAManager") EntityManagerFactory emf) {
 *     return new JpaTransactionManager(emf);
 * }
 *
 * // MyBatis만 사용하는 DB → DataSourceTransactionManager
 * {@literal @}Bean(name = BeanNames.THIRD_TRANSACTION)
 * public PlatformTransactionManager thirdTransactionManager(
 *         {@literal @}Qualifier(BeanNames.THIRD_DATASOURCE) DataSource dataSource) {
 *     return new DataSourceTransactionManager(dataSource);
 * }
 *     </pre>
 *   </li>
 *   <li>
 *     <b>3단계: 서비스에서 transactionManager 지정</b>
 *     <pre>
 * {@literal @}Transactional(transactionManager = BeanNames.THIRD_TRANSACTION)
 * public void saveToThirdDB(Entity entity) { ... }
 *     </pre>
 *   </li>
 * </ol>
 *
 * <h2>중요 포인트</h2>
 * <p><b>DataSource : TransactionManager = 1:1 관계</b></p>
 * <ul>
 *   <li>각 DB별로 별도의 TransactionManager 생성</li>
 *   <li>JPA 사용 DB → JpaTransactionManager (MyBatis도 함께 사용 가능)</li>
 *   <li>MyBatis만 사용 DB → DataSourceTransactionManager</li>
 *   <li>다중 DB 환경에서는 {@code @Transactional(transactionManager = "빈이름")} 명시 필요</li>
 * </ul>
 *
 * <h2>설정 클래스간 관계</h2>
 * <pre>
 * +----------------------------------+
 * |  ApplicationDatasourceConfig    | <- DataSource 생성
 * +----------------+-----------------+
 *                  |
 *     +------------+------------+
 *     v            v            v
 * +--------+  +----------+  +-------------+
 * | MyBatis |  |   JPA    |  | Transaction |
 * | Config  |  |  Config  |  |   Config    |
 * +--------+  +----------+  +-------------+
 *     |            |              |
 *     v            v              v
 * SqlSession   EntityMgr    JpaTransaction
 * Factory      Factory         Manager
 * </pre>
 *
 * @see ApplicationDatasourceConfig DataSource 설정
 * @see BasicDBMybatisMapperConfig MyBatis 설정
 * @see egovframework.config.jpa.BasicDBJpaConfig JPA 설정
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

}
