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
 *   <li>모든 Mapper 인터페이스는 {@code egovframework.mapper} 패키지 하위에 집중 관리</li>
 *   <li>도메인별로 하위 패키지 구성: mapper.member, mapper.product 등</li>
 *   <li>전자정부 프레임워크 표준 {@code @EgovMapper} 어노테이션 사용</li>
 * </ul>
 *
 * <h2>패키지 구조</h2>
 * <pre>
 * egovframework/
 * +-- mapper/                    <- Mapper 인터페이스 집중 관리
 * |   +-- member/
 * |   |   +-- MemberSignupMapper.java
 * |   +-- product/
 * |       +-- ProductMapper.java
 * +-- member/                    <- 도메인별 비즈니스 로직
 * |   +-- signup/
 * |       +-- controller/
 * |       +-- service/
 * |       +-- dto/
 * +-- config/
 * </pre>
 *
 * <h2>신규 DataSource 추가 시 MyBatis 설정 방법</h2>
 * <p>새로운 DB를 추가할 때는 별도의 설정 클래스를 생성합니다.</p>
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
 *     <b>2단계: 신규 설정 클래스 생성</b>
 *     <pre>
 * {@literal @}Configuration
 * {@literal @}MapperScan(
 *     basePackages = "egovframework.mapper.secondary",
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
 *         factory.setConfigLocation(
 *             resolver.getResource("classpath:/egovframework/sqlmap/secondary/mybatis-config.xml")
 *         );
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
 *     <b>3단계: Mapper XML 디렉토리 생성</b>
 *     <pre>
 * resources/egovframework/sqlmap/
 * +-- mybatis-config.xml           <- 기본 DB
 * +-- mappers/                     <- 기본 DB Mapper XML
 * +-- secondary/                   <- 신규 DB
 *     +-- mybatis-config.xml
 *     +-- mappers/
 *     </pre>
 *   </li>
 *   <li>
 *     <b>4단계: Mapper 인터페이스 패키지 분리</b>
 *     <pre>
 * egovframework/mapper/
 * +-- member/                      <- 기본 DB용
 * +-- secondary/                   <- 신규 DB용
 *     +-- log/
 *         +-- LogMapper.java
 *     </pre>
 *   </li>
 * </ol>
 *
 * <h2>중요 포인트</h2>
 * <p><b>DataSource : SqlSessionFactory : MapperScan = 1:1:1 관계</b></p>
 * <ul>
 *   <li>각 DB별로 별도의 SqlSessionFactory 빈 생성</li>
 *   <li>{@code @MapperScan}의 {@code sqlSessionFactoryRef}로 연결 지정</li>
 *   <li>Mapper 패키지를 분리하여 어떤 DB를 사용하는지 명확히 구분</li>
 * </ul>
 *
 * @see ApplicationDatasourceConfig DataSource 설정
 * @see TransactionConfig 트랜잭션 설정
 * @see BeanNames 빈 이름 상수
 */
@Configuration
@MapperScan(
    basePackages = "egovframework.mapper"
)
public class BasicDBConfigMapper {

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
