/**
 * JPA 설정 패키지
 *
 * <p>Spring Data JPA의 EntityManagerFactory와 Repository 스캔을 설정합니다.</p>
 *
 * <h2>주요 클래스</h2>
 * <ul>
 *   <li>{@link egovframework.config.jpa.BasicDBJpaConfig} - 기본 DB용 JPA 설정</li>
 * </ul>
 *
 * <h2>MyBatis와의 공존</h2>
 * <p>동일한 DataSource와 {@code JpaTransactionManager}를 사용하여
 * JPA와 MyBatis를 같은 트랜잭션 내에서 함께 사용할 수 있습니다.</p>
 *
 * @see egovframework.config 상위 패키지
 * @see egovframework.config.mybatis MyBatis 설정
 * @see egovframework.repository.jpa JPA Repository 위치
 */
package egovframework.config.jpa;
