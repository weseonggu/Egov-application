/**
 * JPA 데이터 액세스 계층 패키지
 *
 * <h2>개요</h2>
 * <p>Spring Data JPA를 사용한 데이터베이스 접근을 담당하는 패키지입니다.
 * 주로 <b>CUD(Create/Update/Delete) 작업과 단순 조회</b>를 처리합니다.</p>
 *
 * <h2>패키지 구조</h2>
 * <pre>
 * egovframework.repository.jpa/
 * +-- entity/                           <- JPA Entity 클래스
 * |   +-- Member.java                   <- @Entity 어노테이션 사용
 * +-- MemberRepository.java             <- JpaRepository 인터페이스
 * </pre>
 *
 * <h2>Entity vs Repository 역할</h2>
 * <table border="1">
 *   <tr><th>구분</th><th>역할</th><th>예시</th></tr>
 *   <tr>
 *     <td><b>entity/</b></td>
 *     <td>JPA Entity - DB 테이블과 매핑되는 영속성 객체</td>
 *     <td>Member.java</td>
 *   </tr>
 *   <tr>
 *     <td><b>Repository</b></td>
 *     <td>Spring Data JPA Repository - CRUD 및 쿼리 메서드 제공</td>
 *     <td>MemberRepository.java</td>
 *   </tr>
 * </table>
 *
 * <h2>JPA 사용 권장 상황</h2>
 * <ul>
 *   <li><b>단순 CRUD</b>: save(), findById(), delete() 등</li>
 *   <li><b>영속성 컨텍스트 활용</b>: Dirty Checking을 통한 자동 업데이트</li>
 *   <li><b>연관관계 처리</b>: Cascade, Fetch 전략 활용</li>
 *   <li><b>쿼리 메서드</b>: findByEmail(), existsByMemberId() 등</li>
 * </ul>
 *
 * <h2>Entity 작성 규칙</h2>
 * <ul>
 *   <li>{@code @Entity}, {@code @Table} 어노테이션 필수</li>
 *   <li>{@code @Id}로 기본 키 지정</li>
 *   <li>Lombok 사용 시 {@code @Getter}, {@code @Setter}, {@code @Builder} 권장</li>
 * </ul>
 *
 * <h3>Entity 예시</h3>
 * <pre>{@code
 * @Entity
 * @Table(name = "TB_MEMBER")
 * @Getter @Setter @Builder
 * public class Member {
 *
 *     @Id
 *     @Column(name = "MEMBER_ID")
 *     private String memberId;
 *
 *     @Column(name = "MEMBER_NAME")
 *     private String memberName;
 *
 *     @CreationTimestamp
 *     @Column(name = "CREATED_AT", updatable = false)
 *     private LocalDateTime createdAt;
 * }
 * }</pre>
 *
 * <h2>Repository 작성 규칙</h2>
 * <ul>
 *   <li>{@code JpaRepository<Entity, ID>} 상속</li>
 *   <li>쿼리 메서드 네이밍 규칙 준수 (findBy, existsBy, countBy 등)</li>
 *   <li>복잡한 쿼리는 {@code @Query} 어노테이션 또는 MyBatis 활용</li>
 * </ul>
 *
 * <h3>Repository 예시</h3>
 * <pre>{@code
 * @Repository
 * public interface MemberRepository extends JpaRepository<Member, String> {
 *
 *     Optional<Member> findByEmail(String email);
 *
 *     boolean existsByMemberId(String memberId);
 *
 *     List<Member> findByStatus(String status);
 * }
 * }</pre>
 *
 * <h2>설정 클래스</h2>
 * <p>이 패키지는 {@link egovframework.config.jpa.BasicDBJpaConfig}에서
 * {@code @EnableJpaRepositories}를 통해 자동으로 스캔됩니다.</p>
 *
 * <h3>스캔 설정</h3>
 * <pre>{@code
 * @EnableJpaRepositories(
 *     basePackages = "egovframework.repository.jpa",
 *     entityManagerFactoryRef = "BasicDBJPAManager",
 *     transactionManagerRef = BeanNames.Basic_Transaction
 * )
 *
 * // Entity 스캔 설정
 * em.setPackagesToScan("egovframework.repository.jpa.entity");
 * }</pre>
 *
 * <h2>MyBatis와 함께 사용 시 주의사항</h2>
 * <p>동일 트랜잭션 내에서 JPA 저장 후 MyBatis 조회 시 flush 필요:</p>
 * <pre>{@code
 * memberRepository.saveAndFlush(member);  // 즉시 DB 반영
 * memberMapper.selectById(id);            // 정상 조회 가능
 * }</pre>
 *
 * @see egovframework.config.jpa.BasicDBJpaConfig JPA 설정 클래스
 * @see egovframework.repository.jpa.entity.Member Entity 클래스 예시
 * @see egovframework.repository.jpa.MemberRepository Repository 인터페이스 예시
 */
package egovframework.repository.jpa;
