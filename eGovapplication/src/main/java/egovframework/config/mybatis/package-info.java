/**
 * MyBatis 설정 패키지
 *
 * <p>SqlSessionFactory, SqlSessionTemplate 및 Mapper 스캔을 설정합니다.</p>
 *
 * <h2>주요 클래스</h2>
 * <ul>
 *   <li>{@link egovframework.config.mybatis.BasicDBMybatisMapperConfig} - 기본 DB용 MyBatis 설정</li>
 * </ul>
 *
 * <h2>JPA와의 공존</h2>
 * <p>{@code JpaTransactionManager}를 사용하면 JPA와 MyBatis를
 * 같은 트랜잭션 내에서 함께 사용할 수 있습니다.</p>
 *
 * <h2>다중 DataSource 추가 시</h2>
 * <p>신규 DB를 추가할 경우, 이 패키지에 별도의 설정 클래스를 생성합니다.</p>
 * <pre>
 * // 예시: SecondDBMybatisMapperConfig.java
 * {@literal @}Configuration
 * {@literal @}MapperScan(
 *     basePackages = "egovframework.repository.mapper.second",
 *     sqlSessionFactoryRef = "secondSqlSession"
 * )
 * public class SecondDBMybatisMapperConfig {
 *     // SqlSessionFactory, SqlSessionTemplate 빈 정의
 * }
 * </pre>
 *
 * @see egovframework.config 상위 패키지
 * @see egovframework.config.jpa JPA 설정
 * @see egovframework.repository.mapper Mapper 인터페이스 위치
 */
package egovframework.config.mybatis;
