/**
 * 트랜잭션 설정 패키지
 *
 * <p>JPA와 MyBatis를 통합 관리하는 트랜잭션 매니저를 설정합니다.</p>
 *
 * <h2>주요 클래스</h2>
 * <ul>
 *   <li>{@link egovframework.config.transaction.TransactionConfig} - TransactionManager Bean 설정</li>
 * </ul>
 *
 * <h2>트랜잭션 매니저 선택 기준</h2>
 * <table border="1">
 *   <tr><th>사용 기술</th><th>TransactionManager</th></tr>
 *   <tr><td>JPA + MyBatis</td><td>JpaTransactionManager</td></tr>
 *   <tr><td>MyBatis만</td><td>DataSourceTransactionManager</td></tr>
 * </table>
 *
 * @see egovframework.config 상위 패키지
 * @see egovframework.config.jpa JPA 설정
 * @see egovframework.config.mybatis MyBatis 설정
 */
package egovframework.config.transaction;
