package egovframework.config.database;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import egovframework.common.constants.BeanNames;
import egovframework.config.database.propertiesObject.DataBaseProperties;
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
 *     </pre>
 *   </li>
 *   <li>
 *     <b>4단계: DataSource Bean 추가</b>
 *     <pre>
 * {@literal @}Bean(name = BeanNames.SECONDARY_DATASOURCE)
 * public DataSource secondaryDataSource(
 *         {@literal @}Qualifier(BeanNames.SECONDARY_DATABASE_PROPERTIES) DataBaseProperties properties
 * ) {
 *     return createHikariDataSource(properties);
 * }
 *     </pre>
 *   </li>
 * </ol>
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
 * @see DataBaseProperties
 * @see BeanNames
 */
@Configuration(BeanNames.Application_Config_Datasource)
public class ApplicationConfigDatasource {

    private final DataBaseProperties basicDataBaseProperties;
    private final DataBaseProperties secondDataBaseProperties;

    public ApplicationConfigDatasource(
            @Qualifier(BeanNames.BASIC_DATABASE_PROPERTIES) DataBaseProperties basicDataBaseProperties,
            @Qualifier(BeanNames.SECOND_DATABASE_PROPERTIES) DataBaseProperties secondDataBaseProperties
    ) {
        this.basicDataBaseProperties = basicDataBaseProperties;
        this.secondDataBaseProperties = secondDataBaseProperties;
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
     * Second Oracle DataSource 생성
     *
     * @return HikariCP 기반 DataSource
     */
    @Bean(name = BeanNames.SECOND_DATASOURCE)
    public DataSource secondDataSource() {
        return createHikariDataSource(secondDataBaseProperties);
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
