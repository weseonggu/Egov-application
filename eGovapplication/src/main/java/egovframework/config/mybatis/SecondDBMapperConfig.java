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
 * MyBatis Mapper 설정 - Second DataSource용
 *
 * <h2>Mapper 인터페이스 관리 정책</h2>
 * <ul>
 *   <li>Second DB용 Mapper 인터페이스는 {@code egovframework.mapper.second} 패키지 하위에 관리</li>
 *   <li>도메인별로 하위 패키지 구성: mapper.second.log, mapper.second.audit 등</li>
 * </ul>
 *
 * <h2>패키지 구조</h2>
 * <pre>
 * egovframework/
 * +-- mapper/
 * |   +-- second/                  <- Second DB Mapper 인터페이스
 * |       +-- log/
 * |       |   +-- LogMapper.java
 * |       +-- audit/
 * |           +-- AuditMapper.java
 * </pre>
 *
 * <h2>중요 포인트</h2>
 * <p><b>DataSource : SqlSessionFactory : MapperScan = 1:1:1 관계</b></p>
 * <ul>
 *   <li>Second DB용 SqlSessionFactory 빈 생성</li>
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
    basePackages = "egovframework.mapper.second",
    sqlSessionFactoryRef = BeanNames.Second_Sql_Session
)
public class SecondDBMapperConfig {

    /**
     * SqlSessionFactory 빈 설정
     *
     * @param dataSource 데이터소스
     * @return SqlSessionFactoryBean
     * @throws IOException 리소스 로딩 실패시
     */
    @Bean(name = BeanNames.Second_Sql_Session)
    public SqlSessionFactoryBean sqlSessionFactory(@Qualifier(BeanNames.SECOND_DATASOURCE) DataSource dataSource) throws IOException {
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
    @Bean(name = BeanNames.Second_Sql_Session_Template)
    public SqlSessionTemplate sqlSession(@Qualifier(BeanNames.Second_Sql_Session) SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}