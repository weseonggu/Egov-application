/**
 * 데이터 액세스 계층 - MyBatis + JPA 하이브리드 전략
 *
 * <h2>개요</h2>
 * <p>이 패키지는 데이터베이스 접근을 위한 Repository와 Mapper를 관리합니다.
 * <b>MyBatis와 JPA를 함께 사용</b>하여 각 기술의 장점을 활용합니다.</p>
 *
 * <h2>왜 두 기술을 함께 사용하는가?</h2>
 * <table border="1">
 *   <tr><th>기술</th><th>용도</th><th>선택 이유</th></tr>
 *   <tr>
 *     <td><b>MyBatis</b></td>
 *     <td>동적 쿼리, 복잡한 조회</td>
 *     <td>
 *       - XML 기반으로 동적 쿼리 작성이 직관적<br>
 *       - {@code <if>}, {@code <choose>}, {@code <foreach>} 등 강력한 동적 SQL 지원<br>
 *       - QueryDSL 대비 학습 비용이 낮음
 *     </td>
 *   </tr>
 *   <tr>
 *     <td><b>JPA</b></td>
 *     <td>CUD 작업 (Create/Update/Delete)</td>
 *     <td>
 *       - 영속성 컨텍스트를 통한 변경 감지 (Dirty Checking)<br>
 *       - {@code @Transactional} 어노테이션 기반 선언적 트랜잭션<br>
 *       - 단순 CRUD는 코드량 최소화
 *     </td>
 *   </tr>
 * </table>
 *
 * <h2>패키지 구조</h2>
 * <pre>
 * egovframework.repository/
 * +-- mybatis/                          <- MyBatis 관련 클래스
 * |   +-- dao/                          <- VO/DAO 클래스
 * |   |   +-- MemberVO.java
 * |   +-- mapper/                       <- Mapper 인터페이스
 * |       +-- MemberSignupMapper.java
 * +-- jpa/                              <- JPA 관련 클래스
 *     +-- entity/                       <- JPA Entity
 *     |   +-- Member.java
 *     +-- MemberRepository.java         <- JPA Repository
 * </pre>
 *
 * <h2>역할 분담 가이드</h2>
 * <pre>
 * ┌─────────────────────────────────────────────────────────────┐
 * │                    Service Layer                            │
 * │         @Transactional(transactionManager = "...")          │
 * ├─────────────────────────┬───────────────────────────────────┤
 * │       MyBatis           │             JPA                   │
 * │  (복잡한 조회 담당)      │      (CUD 작업 담당)              │
 * ├─────────────────────────┼───────────────────────────────────┤
 * │ - 동적 WHERE 조건       │ - save(), delete()                │
 * │ - 다중 테이블 JOIN      │ - 영속성 컨텍스트 활용             │
 * │ - 페이징 + 정렬         │ - Dirty Checking 자동 업데이트    │
 * │ - 통계/집계 쿼리        │ - Cascade 연관관계 처리           │
 * └─────────────────────────┴───────────────────────────────────┘
 * </pre>
 *
 * <h2>트랜잭션 통합</h2>
 * <p>{@code JpaTransactionManager}를 사용하여 <b>하나의 트랜잭션 안에서
 * MyBatis와 JPA를 함께 사용</b>할 수 있습니다.</p>
 *
 * <pre>{@code
 * @Service
 * @Transactional(transactionManager = BeanNames.Basic_Transaction)
 * public class MemberService {
 *
 *     @Autowired
 *     private MemberMapper memberMapper;      // MyBatis
 *
 *     @Autowired
 *     private MemberRepository memberRepository;  // JPA
 *
 *     // MyBatis로 복잡한 조회
 *     public List<MemberDTO> searchMembers(MemberSearchCondition condition) {
 *         return memberMapper.selectByCondition(condition);
 *     }
 *
 *     // JPA로 저장 (영속성 컨텍스트 활용)
 *     public void saveMember(MemberEntity member) {
 *         memberRepository.save(member);
 *     }
 *
 *     // 혼합 사용 - 예외 발생 시 둘 다 롤백
 *     public void complexOperation(MemberEntity entity, MemberDTO dto) {
 *         memberRepository.save(entity);      // JPA
 *         memberMapper.insertHistory(dto);    // MyBatis
 *         // 예외 발생 시 위 두 작업 모두 롤백됨
 *     }
 * }
 * }</pre>
 *
 * <h2>주의사항</h2>
 *
 * <h3>1. 영속성 컨텍스트와 MyBatis 동기화</h3>
 * <p>JPA로 저장 후 MyBatis로 조회하면 DB에 아직 반영되지 않을 수 있습니다.</p>
 * <pre>{@code
 * // 문제 상황
 * memberRepository.save(member);  // 영속성 컨텍스트에만 존재
 * memberMapper.selectById(id);    // DB에서 조회 -> 못 찾음!
 *
 * // 해결 방법
 * memberRepository.saveAndFlush(member);  // 즉시 DB 반영
 * memberMapper.selectById(id);            // 정상 조회
 * }</pre>
 *
 * <h3>2. 일관된 역할 분담</h3>
 * <p>팀 내에서 역할 분담 규칙을 명확히 정해야 합니다.</p>
 * <ul>
 *   <li><b>권장</b>: SELECT는 MyBatis, CUD는 JPA</li>
 *   <li><b>피해야 할 패턴</b>: 같은 엔티티를 MyBatis와 JPA 모두에서 UPDATE</li>
 * </ul>
 *
 * <h3>3. N+1 문제 인식</h3>
 * <p>JPA 연관관계 조회 시 N+1 문제가 발생할 수 있습니다.
 * 복잡한 조회는 MyBatis JOIN으로 해결하는 것이 효율적입니다.</p>
 *
 * <h2>QueryDSL 대신 MyBatis를 선택한 이유</h2>
 * <table border="1">
 *   <tr><th>항목</th><th>QueryDSL</th><th>MyBatis</th></tr>
 *   <tr>
 *     <td>학습 비용</td>
 *     <td>높음 (Q클래스 생성, 빌더 패턴)</td>
 *     <td>낮음 (SQL 그대로 사용)</td>
 *   </tr>
 *   <tr>
 *     <td>설정 복잡도</td>
 *     <td>높음 (APT 플러그인, 버전 호환성)</td>
 *     <td>낮음 (XML 매핑만 필요)</td>
 *   </tr>
 *   <tr>
 *     <td>동적 쿼리</td>
 *     <td>BooleanBuilder, BooleanExpression</td>
 *     <td>XML 태그로 직관적</td>
 *   </tr>
 *   <tr>
 *     <td>타입 안정성</td>
 *     <td>컴파일 타임 체크 (장점)</td>
 *     <td>런타임 체크</td>
 *   </tr>
 *   <tr>
 *     <td>네이티브 SQL</td>
 *     <td>제한적</td>
 *     <td>완전 지원</td>
 *   </tr>
 * </table>
 *
 * <h2>관련 설정 클래스</h2>
 * <ul>
 *   <li>{@link egovframework.config.mybatis.BasicDBMybatisMapperConfig} - MyBatis 설정</li>
 *   <li>{@link egovframework.config.jpa.BasicDBJpaConfig} - JPA 설정</li>
 *   <li>{@link egovframework.config.transaction.TransactionConfig} - 트랜잭션 설정</li>
 * </ul>
 *
 * @see egovframework.config.transaction.TransactionConfig
 * @see egovframework.config.mybatis.BasicDBMybatisMapperConfig
 * @see egovframework.config.jpa.BasicDBJpaConfig
 */
package egovframework.repository;
