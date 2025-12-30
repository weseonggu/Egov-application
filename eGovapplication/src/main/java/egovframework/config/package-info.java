/**
 * 애플리케이션 설정 Bean(Configuration Bean) 패키지
 *
 * <h2>패키지 구조</h2>
 * <pre>
 * config/
 * ├── database/                    - DataSource 설정
 * │   ├── ApplicationDatasourceConfig.java
 * │   └── propertiesObject/        - DB 접속 정보 클래스
 * │       ├── DataBaseProperties.java
 * │       └── BasicDataBaseProperties.java
 * ├── mybatis/                     - MyBatis 설정
 * │   └── BasicDBConfigMapper.java
 * ├── security/                    - Spring Security 설정
 * │   └── SecurityConfig.java
 * ├── swagger/                     - OpenAPI/Swagger 설정
 * │   └── SwaggerConfig.java
 * └── transaction/                 - 트랜잭션 설정
 *     └── TransactionConfig.java
 * </pre>
 *
 * <h2>주요 패키지 설명</h2>
 *
 * <h3>1. database - DataSource 설정</h3>
 * <table border="1">
 *   <tr><th>클래스</th><th>Bean 이름</th><th>설명</th></tr>
 *   <tr>
 *     <td>{@link egovframework.config.database.ApplicationDatasourceConfig}</td>
 *     <td>basicDataSource</td>
 *     <td>HikariCP 기반 DataSource 생성</td>
 *   </tr>
 *   <tr>
 *     <td>{@link egovframework.config.database.propertiesObject.BasicDataBaseProperties}</td>
 *     <td>basicDatabaseProperties</td>
 *     <td>application.properties의 DB 접속 정보 바인딩</td>
 *   </tr>
 * </table>
 *
 * <h3>2. mybatis - MyBatis 설정</h3>
 * <table border="1">
 *   <tr><th>클래스</th><th>Bean 이름</th><th>설명</th></tr>
 *   <tr>
 *     <td rowspan="2">{@link egovframework.config.mybatis.BasicDBConfigMapper}</td>
 *     <td>basicSqlSession</td>
 *     <td>SqlSessionFactory - MyBatis 설정 및 Mapper XML 로드</td>
 *   </tr>
 *   <tr>
 *     <td>basicSqlSessionTemplate</td>
 *     <td>SqlSessionTemplate - 실제 SQL 실행 담당</td>
 *   </tr>
 * </table>
 * <p>{@code @MapperScan}으로 {@code egovframework.mapper} 패키지의 Mapper 인터페이스를 스캔</p>
 *
 * <h3>3. security - Spring Security 설정</h3>
 * <table border="1">
 *   <tr><th>클래스</th><th>Bean 이름</th><th>설명</th></tr>
 *   <tr>
 *     <td rowspan="2">{@link egovframework.config.security.SecurityConfig}</td>
 *     <td>passwordEncoder</td>
 *     <td>BCryptPasswordEncoder - 비밀번호 암호화</td>
 *   </tr>
 *   <tr>
 *     <td>filterChain</td>
 *     <td>SecurityFilterChain - URL 접근 권한 설정</td>
 *   </tr>
 * </table>
 *
 * <h3>4. swagger - API 문서 설정</h3>
 * <table border="1">
 *   <tr><th>클래스</th><th>Bean 이름</th><th>설명</th></tr>
 *   <tr>
 *     <td>{@link egovframework.config.swagger.SwaggerConfig}</td>
 *     <td>openAPI</td>
 *     <td>OpenAPI 3.0 문서 설정 (Swagger UI)</td>
 *   </tr>
 * </table>
 * <p>접근 URL: {@code /swagger-ui/index.html}</p>
 *
 * <h3>5. transaction - 트랜잭션 설정</h3>
 * <table border="1">
 *   <tr><th>클래스</th><th>Bean 이름</th><th>설명</th></tr>
 *   <tr>
 *     <td>{@link egovframework.config.transaction.TransactionConfig}</td>
 *     <td>basicTransaction</td>
 *     <td>DataSourceTransactionManager - @Transactional 지원</td>
 *   </tr>
 * </table>
 *
 * <h2>Bean 의존성 흐름</h2>
 * <pre>
 * application.properties
 *         │
 *         ▼
 * BasicDataBaseProperties (DB 접속 정보 바인딩)
 *         │
 *         ▼
 * ApplicationDatasourceConfig (DataSource: basicDataSource)
 *         │
 *    ┌────┴────┐
 *    ▼         ▼
 * BasicDBConfigMapper    TransactionConfig
 * - basicSqlSession      - basicTransaction
 * - basicSqlSessionTemplate
 *
 * SecurityConfig         SwaggerConfig
 * - passwordEncoder      - openAPI
 * - filterChain
 * </pre>
 *
 * <h2>신규 DB 추가 시 수정 필요 파일</h2>
 * <ol>
 *   <li>{@code application.properties} - 신규 DB 접속 정보 추가</li>
 *   <li>{@link egovframework.common.constants.BeanNames} - Bean 이름 상수 추가</li>
 *   <li>{@code database/propertiesObject/} - 신규 Properties 클래스 생성</li>
 *   <li>{@link egovframework.config.database.ApplicationDatasourceConfig} - 신규 DataSource Bean 추가</li>
 *   <li>{@code mybatis/} - 신규 Mapper 설정 클래스 생성</li>
 *   <li>{@link egovframework.config.transaction.TransactionConfig} - 신규 TransactionManager Bean 추가</li>
 * </ol>
 *
 * @see egovframework.common.constants.BeanNames Bean 이름 상수
 */
package egovframework.config;
