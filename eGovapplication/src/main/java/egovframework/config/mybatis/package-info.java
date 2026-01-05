/**
 * MyBatis 설정 패키지
 *
 * <p>SqlSessionFactory, SqlSessionTemplate 및 Mapper 스캔을 설정합니다.</p>
 *
 * <h2>주요 클래스</h2>
 * <ul>
 *   <li>{@link egovframework.config.mybatis.BasicDBMybatisMapperConfig} - 기본 DB용 MyBatis 설정</li>
 *   <li>{@link egovframework.config.mybatis.SecondDBMybatisMapperConfig} - Second DB용 MyBatis 설정</li>
 * </ul>
 *
 * <h2>JPA와의 공존</h2>
 * <p>{@code JpaTransactionManager}를 사용하면 JPA와 MyBatis를
 * 같은 트랜잭션 내에서 함께 사용할 수 있습니다.</p>
 *
 * @see egovframework.config 상위 패키지
 * @see egovframework.config.jpa JPA 설정
 * @see egovframework.repository.mapper Mapper 인터페이스 위치
 */
package egovframework.config.mybatis;
