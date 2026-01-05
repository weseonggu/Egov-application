package egovframework.config.database;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import egovframework.common.constants.BeanNames;
import egovframework.config.database.propertiesObject.DataBaseProperties;
import egovframework.config.mybatis.BasicDBMybatisMapperConfig;
import egovframework.config.transaction.TransactionConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 애플리케이션 DataSource 설정 클래스
 *
 * <h2>사용방법</h2>
 * <p>application.properties의 설정값을 로딩하여 HikariCP DataSource를 생성합니다.</p>
 *
 * <h2>설정 클래스 간 의존 관계</h2>
 * <pre>
 * ┌──────────────────────────────────────┐
 * │  ApplicationDatasourceConfig (1단계) │ ← 현재 클래스: DataSource 생성
 * └─────────────────┬────────────────────┘
 *                   │
 *          +────────+────────+
 *          ↓                 ↓
 * ┌─────────────────┐  ┌─────────────────┐
 * │ BasicDBConfig   │  │ TransactionConfig│
 * │ Mapper (2단계)  │  │ (2단계)         │
 * │                 │  │                 │
 * │ - SqlSession    │  │ - Transaction   │
 * │   Factory       │  │   Manager       │
 * │ - MapperScan    │  │                 │
 * └─────────────────┘  └─────────────────┘
 * </pre>
 *
 * <h2>신규 DataSource 추가 방법</h2>
 * <ol>
 *   <li>
 *     <b>1단계: application.properties에 설정 추가</b>
 *     <pre>
 * # Secondary DataSource 설정
 * spring.secondary.datasource.driver-class-name=oracle.jdbc.OracleDriver
 * spring.secondary.datasource.url=${DB_SECONDARY_URL}
 * spring.secondary.datasource.username=${DB_SECONDARY_USERNAME}
 * spring.secondary.datasource.password=${DB_SECONDARY_PASSWORD}
 *     </pre>
 *   </li>
 *   <li>
 *     <b>2단계: Properties 구현체 생성</b>
 *     <pre>
 * {@literal @}Getter
 * {@literal @}Setter
 * {@literal @}Component(BeanNames.SECONDARY_DATABASE_PROPERTIES)
 * {@literal @}ConfigurationProperties(prefix = "spring.secondary.datasource")
 * public class SecondaryDataBaseProperties implements DataBaseProperties {
 *     private String driverClassName;
 *     private String url;
 *     private String username;
 *     private String password;
 * }
 *     </pre>
 *   </li>
 *   <li>
 *     <b>3단계: BeanNames에 상수 추가</b>
 *     <pre>
 * public static final String SECONDARY_DATABASE_PROPERTIES = "secondaryDatabaseProperties";
 * public static final String SECONDARY_DATASOURCE = "secondaryDataSource";
 * public static final String SECONDARY_TRANSACTION = "secondaryTransaction";
 * public static final String Secondary_Sql_Session = "secondarySqlSession";
 * public static final String Secondary_Sql_Session_Template = "secondarySqlSessionTemplate";
 *     </pre>
 *   </li>
 *   <li>
 *     <b>4단계: DataSource Bean 추가 (이 클래스)</b>
 *     <pre>
 * {@literal @}Bean(name = BeanNames.SECONDARY_DATASOURCE)
 * public DataSource secondaryDataSource(
 *         {@literal @}Qualifier(BeanNames.SECONDARY_DATABASE_PROPERTIES) DataBaseProperties properties
 * ) {
 *     return createHikariDataSource(properties);
 * }
 *     </pre>
 *   </li>
 *   <li>
 *     <b>5단계: MyBatis 설정 추가</b>
 *     <p>{@link BasicDBMybatisMapperConfig}를 참고하여 새 Mapper 설정 클래스 생성</p>
 *     <pre>
 * {@literal @}Configuration
 * {@literal @}MapperScan(
 *     basePackages = "egovframework.mapper.secondary",
 *     sqlSessionFactoryRef = BeanNames.Secondary_Sql_Session
 * )
 * public class SecondaryDBConfigMapper {
 *     // SqlSessionFactory, SqlSessionTemplate 빈 정의
 * }
 *     </pre>
 *   </li>
 *   <li>
 *     <b>6단계: 트랜잭션 매니저 추가</b>
 *     <p>{@link TransactionConfig}에 새 DataSource용 TransactionManager 추가</p>
 *     <pre>
 * {@literal @}Bean(name = BeanNames.SECONDARY_TRANSACTION)
 * public PlatformTransactionManager secondaryTransactionManager(
 *         {@literal @}Qualifier(BeanNames.SECONDARY_DATASOURCE) DataSource dataSource) {
 *     return new DataSourceTransactionManager(dataSource);
 * }
 *     </pre>
 *   </li>
 * </ol>
 *
 * <h2>중요: 1:1:1 관계 유지</h2>
 * <pre>
 * DataSource : SqlSessionFactory : MapperScan = 1:1:1
 * DataSource : TransactionManager = 1:1
 * </pre>
 *
 * <h2>HikariCP 주요 설정</h2>
 * <ul>
 *   <li>maximumPoolSize: 최대 커넥션 수 (기본값: 10)</li>
 *   <li>minimumIdle: 최소 유휴 커넥션 수</li>
 *   <li>connectionTimeout: 커넥션 획득 대기 시간 (ms)</li>
 *   <li>idleTimeout: 유휴 커넥션 유지 시간 (ms)</li>
 *   <li>maxLifetime: 커넥션 최대 수명 (ms)</li>
 * </ul>
 *
 * @see TransactionConfig 트랜잭션 매니저 설정
 * @see BasicDBMybatisMapperConfig MyBatis SqlSession 설정
 * @see DataBaseProperties 데이터베이스 연결 정보 인터페이스
 * @see BeanNames 빈 이름 상수
 */
@Configuration(BeanNames.Application_Config_Datasource)
public class ApplicationDatasourceConfig {

    private final DataBaseProperties basicDataBaseProperties;

    public ApplicationDatasourceConfig(
            @Qualifier(BeanNames.BASIC_DATABASE_PROPERTIES) DataBaseProperties basicDataBaseProperties
    ) {
        this.basicDataBaseProperties = basicDataBaseProperties;
    }

    /**
     * 기본 Oracle DataSource 생성
     *
     * @return HikariCP 기반 DataSource
     */
    @Primary
    @Bean(name = BeanNames.BASIC_DATASOURCE)
    public DataSource dataSource() {
        return createHikariDataSource(basicDataBaseProperties);
    }

    /**
     * HikariCP DataSource 생성 공통 메서드
     *
     * @param properties 데이터베이스 설정 프로퍼티
     * @return 설정된 HikariDataSource
     */
    private DataSource createHikariDataSource(DataBaseProperties properties) {
        HikariConfig config = new HikariConfig();

        // 기본 연결 정보
        config.setDriverClassName(properties.getDriverClassName());
        config.setJdbcUrl(properties.getUrl());
        config.setUsername(properties.getUsername());
        config.setPassword(properties.getPassword());

        // 커넥션 풀 설정
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        // 커넥션 유효성 검사 (Oracle)
        config.setConnectionTestQuery("SELECT 1 FROM DUAL");

        return new HikariDataSource(config);
    }

}
