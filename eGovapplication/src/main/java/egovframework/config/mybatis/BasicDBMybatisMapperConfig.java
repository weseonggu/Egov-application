package egovframework.config.mybatis;

import egovframework.common.constants.BeanNames;
import egovframework.config.database.ApplicationDatasourceConfig;
import egovframework.config.transaction.TransactionConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * MyBatis Mapper 설정 - 기본 DataSource용
 *
 * <h2>Mapper 인터페이스 관리 정책</h2>
 * <ul>
 *   <li>모든 Mapper 인터페이스는 {@code egovframework.repository.mybatis.mapper} 패키지 하위에 집중 관리</li>
 *   <li>VO/DAO 클래스는 {@code egovframework.repository.mybatis.dao} 패키지에 관리</li>
 *   <li>전자정부 프레임워크 표준 {@code @EgovMapper} 어노테이션 사용</li>
 * </ul>
 *
 * <h2>패키지 구조</h2>
 * <pre>
 * egovframework/
 * +-- repository/                       <- 데이터 접근 계층 집중 관리
 * |   +-- mybatis/                      <- MyBatis 관련 클래스
 * |   |   +-- dao/                      <- VO/DAO 클래스
 * |   |   |   +-- MemberVO.java
 * |   |   +-- mapper/                   <- Mapper 인터페이스 (@MapperScan 대상)
 * |   |       +-- MemberSignupMapper.java
 * |   +-- jpa/                          <- JPA 관련 클래스
 * |       +-- entity/                   <- JPA Entity
 * |       |   +-- Member.java
 * |       +-- MemberRepository.java
 * +-- application/                      <- 도메인별 비즈니스 로직
 * |   +-- member/
 * |       +-- signup/
 * |           +-- controller/
 * |           +-- service/
 * |           +-- dto/
 * +-- config/
 * </pre>
 *
 * <h2>sqlSessionFactoryRef 설정 (필수)</h2>
 * <p>다중 DataSource 환경에서는 {@code @MapperScan}에 반드시 {@code sqlSessionFactoryRef}를 지정해야 합니다.</p>
 *
 * <h3>왜 필요한가?</h3>
 * <ul>
 *   <li>여러 개의 {@code SqlSessionFactory} 빈이 존재할 때 Spring이 어떤 것을 선택할지 모호해짐</li>
 *   <li>{@code sqlSessionFactoryRef}를 명시하지 않으면 {@code NoUniqueBeanDefinitionException} 발생</li>
 *   <li>각 Mapper가 어떤 DB를 사용하는지 명확하게 문서화하는 역할</li>
 * </ul>
 *
 * <h3>설정 예시</h3>
 * <pre>
 * // 다중 DataSource 환경에서 필수 설정
 * {@literal @}MapperScan(
 *     basePackages = "egovframework.repository.mybatis.mapper",
 *     sqlSessionFactoryRef = BeanNames.Basic_Sql_Session  // 필수!
 * )
 * </pre>
 *
 * <h3>에러 발생 케이스</h3>
 * <pre>
 * // ❌ sqlSessionFactoryRef 미지정 시 에러 발생
 * {@literal @}MapperScan(basePackages = "egovframework.repository.mybatis.mapper")
 *
 * // 에러 메시지:
 * // NoUniqueBeanDefinitionException: expected single matching bean
 * // but found 2: basicSqlSession, secondSqlSession
 * </pre>
 *
 * <h2>신규 DataSource 추가 시 MyBatis 설정 방법</h2>
 * <p>새로운 DB를 추가할 때는 별도의 설정 클래스를 생성합니다.</p>
 *
 * <h3>권장 사항: 별도 경로 및 설정 파일 분리</h3>
 * <p><b>신규 DataSource는 반드시 별도의 mybatis-config.xml과 mapper XML 경로를 사용하세요.</b></p>
 * <ul>
 *   <li>동일한 설정 파일을 공유하면 Type Alias, 플러그인 등에서 충돌 발생 가능</li>
 *   <li>DB별로 다른 설정(캐시, 타임아웃 등)이 필요할 수 있음</li>
 *   <li>유지보수 시 어떤 설정이 어떤 DB에 적용되는지 명확함</li>
 *   <li>Mapper XML이 중복 스캔되어 예기치 않은 동작 방지</li>
 * </ul>
 *
 * <ol>
 *   <li>
 *     <b>1단계: BeanNames에 상수 추가</b>
 *     <pre>
 * public static final String SECONDARY_SQL_SESSION = "secondarySqlSession";
 * public static final String SECONDARY_SQL_SESSION_TEMPLATE = "secondarySqlSessionTemplate";
 *     </pre>
 *   </li>
 *   <li>
 *     <b>2단계: 신규 설정 클래스 생성 (별도 경로 사용)</b>
 *     <pre>
 * {@literal @}Configuration
 * {@literal @}MapperScan(
 *     basePackages = "egovframework.repository.secondmybatis.mapper",  // 별도 패키지!
 *     sqlSessionFactoryRef = BeanNames.SECONDARY_SQL_SESSION
 * )
 * public class SecondaryDBConfigMapper {
 *
 *     {@literal @}Bean(name = BeanNames.SECONDARY_SQL_SESSION)
 *     public SqlSessionFactoryBean sqlSessionFactory(
 *             {@literal @}Qualifier(BeanNames.SECONDARY_DATASOURCE) DataSource dataSource
 *     ) throws IOException {
 *         PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
 *         SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
 *         factory.setDataSource(dataSource);
 *
 *         // ⚠️ 중요: 별도의 mybatis-config.xml 사용 권장
 *         factory.setConfigLocation(
 *             resolver.getResource("classpath:/egovframework/sqlmap/secondary/mybatis-config.xml")
 *         );
 *
 *         // ⚠️ 중요: 별도의 mapper XML 경로 사용 권장
 *         factory.setMapperLocations(
 *             resolver.getResources("classpath:/egovframework/sqlmap/secondary/mappers/**{@literal /}*.xml")
 *         );
 *         return factory;
 *     }
 *
 *     {@literal @}Bean(name = BeanNames.SECONDARY_SQL_SESSION_TEMPLATE)
 *     public SqlSessionTemplate sqlSession(
 *             {@literal @}Qualifier(BeanNames.SECONDARY_SQL_SESSION) SqlSessionFactory sqlSessionFactory
 *     ) {
 *         return new SqlSessionTemplate(sqlSessionFactory);
 *     }
 * }
 *     </pre>
 *   </li>
 *   <li>
 *     <b>3단계: Mapper XML 디렉토리 분리 (권장 구조)</b>
 *     <pre>
 * resources/egovframework/sqlmap/
 * +-- mybatis-config.xml           <- 기본 DB 전용
 * +-- mappers/                     <- 기본 DB Mapper XML
 * |   +-- member/
 * |       +-- MemberMapper.xml
 * +-- secondary/                   <- 신규 DB 전용 (완전 분리)
 *     +-- mybatis-config.xml       <- 신규 DB 전용 설정
 *     +-- mappers/                 <- 신규 DB Mapper XML
 *         +-- log/
 *             +-- LogMapper.xml
 *     </pre>
 *   </li>
 *   <li>
 *     <b>4단계: Mapper 인터페이스 패키지 분리</b>
 *     <pre>
 * egovframework/repository/
 * +-- mybatis/                     <- 기본 DB용
 * |   +-- dao/
 * |   |   +-- MemberVO.java
 * |   +-- mapper/                  <- @MapperScan 대상
 * |       +-- MemberSignupMapper.java
 * +-- secondmybatis/               <- 신규 DB용
 *     +-- dao/
 *     |   +-- LogVO.java
 *     +-- mapper/                  <- SecondaryDBConfigMapper가 스캔
 *         +-- LogMapper.java
 *     </pre>
 *   </li>
 * </ol>
 *
 * <h2>중요 포인트</h2>
 * <p><b>DataSource : SqlSessionFactory : MapperScan = 1:1:1 관계</b></p>
 * <ul>
 *   <li>각 DB별로 별도의 SqlSessionFactory 빈 생성</li>
 *   <li>{@code @MapperScan}의 {@code sqlSessionFactoryRef}로 연결 지정 (필수)</li>
 *   <li>Mapper 패키지를 분리하여 어떤 DB를 사용하는지 명확히 구분</li>
 *   <li><b>mybatis-config.xml과 mapper XML 경로도 DB별로 분리 권장</b></li>
 * </ul>
 *
 * <h2>경로 분리를 권장하는 이유</h2>
 * <table border="1">
 *   <tr><th>항목</th><th>경로 공유 시 문제점</th><th>분리 시 장점</th></tr>
 *   <tr>
 *     <td>Type Alias</td>
 *     <td>동일 alias가 다른 클래스에 매핑될 수 있음</td>
 *     <td>DB별 독립적인 alias 정의 가능</td>
 *   </tr>
 *   <tr>
 *     <td>Mapper XML</td>
 *     <td>여러 SqlSessionFactory에서 중복 로드</td>
 *     <td>필요한 Mapper만 정확히 로드</td>
 *   </tr>
 *   <tr>
 *     <td>캐시 설정</td>
 *     <td>모든 DB에 동일한 캐시 정책 적용</td>
 *     <td>DB 특성에 맞는 캐시 설정</td>
 *   </tr>
 *   <tr>
 *     <td>플러그인</td>
 *     <td>특정 DB에만 필요한 플러그인이 전체 적용</td>
 *     <td>DB별 필요한 플러그인만 적용</td>
 *   </tr>
 *   <tr>
 *     <td>유지보수</td>
 *     <td>설정 변경 시 영향 범위 파악 어려움</td>
 *     <td>명확한 책임 분리</td>
 *   </tr>
 * </table>
 *
 * @see egovframework.repository.mybatis MyBatis Repository 패키지 (스캔 대상)
 * @see egovframework.repository.mybatis.mapper.MemberSignupMapper Mapper 인터페이스 예시
 * @see egovframework.repository.mybatis.dao.MemberVO VO 클래스 예시
 * @see ApplicationDatasourceConfig DataSource 설정
 * @see TransactionConfig 트랜잭션 설정
 * @see BeanNames 빈 이름 상수
 */
@Configuration
@MapperScan(
    basePackages = "egovframework.repository.mybatis.mapper",
    sqlSessionFactoryRef = BeanNames.Basic_Sql_Session
)
public class BasicDBMybatisMapperConfig {

    /**
     * SqlSessionFactory 빈 설정
     *
     * @param dataSource 데이터소스
     * @return SqlSessionFactoryBean
     * @throws IOException 리소스 로딩 실패시
     */
    @Bean(name = BeanNames.Basic_Sql_Session)
    public SqlSessionFactoryBean sqlSessionFactory(@Qualifier(BeanNames.BASIC_DATASOURCE) DataSource dataSource) throws IOException {
        PathMatchingResourcePatternResolver pmrpr = new PathMatchingResourcePatternResolver();
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        // MyBatis 설정 파일
        sqlSessionFactoryBean.setConfigLocation(
            pmrpr.getResource("classpath:/egovframework/sqlmap/mybatis-config.xml")
        );

        // Mapper XML 위치 - 하위 폴더 전체 스캔
        sqlSessionFactoryBean.setMapperLocations(
            pmrpr.getResources("classpath:/egovframework/sqlmap/mappers/**/*.xml")
        );

        return sqlSessionFactoryBean;
    }

    /**
     * SqlSessionTemplate 빈 설정 (MyBatis 세션 관리)
     *
     * @param sqlSessionFactory SqlSessionFactory
     * @return SqlSessionTemplate
     */
    @Bean(name = BeanNames.Basic_Sql_Session_Template)
    public SqlSessionTemplate sqlSession(@Qualifier(BeanNames.Basic_Sql_Session) SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
