package egovframework.mapper.member;

import egovframework.common.constants.BeanNames;
import egovframework.repository.jpa.entity.Member;
import egovframework.repository.jpa.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MemberRepository JPA 테스트
 *
 * <p>회원 관련 JPA Repository 기능을 테스트합니다. (Basic DataSource 사용)</p>
 *
 * <h2>테스트 항목</h2>
 * <ul>
 *   <li>회원 등록 (save)</li>
 *   <li>회원 조회 (findById, findByEmail)</li>
 *   <li>중복 체크 (existsByMemberId, existsByEmail)</li>
 *   <li>회원 정보 수정 (save - update)</li>
 *   <li>회원 삭제 (delete)</li>
 *   <li>목록 조회 (findByStatus, findByRole)</li>
 * </ul>
 *
 * @see MemberRepository
 * @see Member
 */
@SpringBootTest
@DisplayName("MemberRepository JPA 테스트 (Basic DataSource)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional(transactionManager = BeanNames.Basic_Transaction)  // 테스트 후 롤백
class MemberRepositoryJpaTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;
    private static final String TEST_MEMBER_ID = "jpaTestUser001";
    private static final String TEST_EMAIL = "jpatest001@test.com";

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .memberId(TEST_MEMBER_ID)
                .password("encodedPassword123")
                .memberName("JPA테스트유저")
                .email(TEST_EMAIL)
                .phone("010-1234-5678")
                .status("ACTIVE")
                .role("USER")
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("회원 등록 테스트 (save)")
    void saveTest() {
        System.out.println("========== [JPA] 회원 등록 테스트 ==========");
        System.out.println("등록할 회원 정보:");
        System.out.println("  - ID: " + testMember.getMemberId());
        System.out.println("  - 이름: " + testMember.getMemberName());
        System.out.println("  - 이메일: " + testMember.getEmail());
        System.out.println("  - 전화번호: " + testMember.getPhone());

        // when
        Member savedMember = memberRepository.saveAndFlush(testMember);

        // then
        System.out.println("SAVE 결과:");
        System.out.println("  - 저장된 ID: " + savedMember.getMemberId());
        System.out.println("  - 생성일시: " + savedMember.getCreatedAt());

        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getMemberId()).isEqualTo(TEST_MEMBER_ID);
        System.out.println("결과: 성공");
    }

    @Test
    @Order(2)
    @DisplayName("회원 ID로 조회 테스트 (findById)")
    void findByIdTest() {
        System.out.println("========== [JPA] 회원 ID로 조회 테스트 ==========");

        // given
        memberRepository.saveAndFlush(testMember);

        // when
        Optional<Member> foundMember = memberRepository.findById(TEST_MEMBER_ID);

        // then
        System.out.println("조회 결과:");
        assertThat(foundMember).isPresent();
        foundMember.ifPresent(member -> {
            System.out.println("  - ID: " + member.getMemberId());
            System.out.println("  - 이름: " + member.getMemberName());
            System.out.println("  - 이메일: " + member.getEmail());
            System.out.println("  - 상태: " + member.getStatus());
            System.out.println("  - 권한: " + member.getRole());
            System.out.println("  - 생성일: " + member.getCreatedAt());
        });

        assertThat(foundMember.get().getMemberId()).isEqualTo(TEST_MEMBER_ID);
        assertThat(foundMember.get().getMemberName()).isEqualTo("JPA테스트유저");
        System.out.println("결과: 성공");
    }

    @Test
    @Order(3)
    @DisplayName("이메일로 조회 테스트 (findByEmail)")
    void findByEmailTest() {
        System.out.println("========== [JPA] 이메일로 조회 테스트 ==========");

        // given
        memberRepository.saveAndFlush(testMember);

        // when
        Optional<Member> foundMember = memberRepository.findByEmail(TEST_EMAIL);

        // then
        System.out.println("이메일 [" + TEST_EMAIL + "]로 조회 결과:");
        assertThat(foundMember).isPresent();
        foundMember.ifPresent(member -> {
            System.out.println("  - ID: " + member.getMemberId());
            System.out.println("  - 이름: " + member.getMemberName());
        });

        assertThat(foundMember.get().getEmail()).isEqualTo(TEST_EMAIL);
        System.out.println("결과: 성공");
    }

    @Test
    @Order(4)
    @DisplayName("회원 ID 존재 여부 확인 테스트 (existsByMemberId)")
    void existsByMemberIdTest() {
        System.out.println("========== [JPA] 회원 ID 존재 여부 확인 테스트 ==========");

        // given
        memberRepository.saveAndFlush(testMember);

        // when
        boolean exists = memberRepository.existsByMemberId(TEST_MEMBER_ID);
        boolean notExists = memberRepository.existsByMemberId("nonexistent");

        // then
        System.out.println("ID [" + TEST_MEMBER_ID + "] 존재 여부: " + exists);
        System.out.println("ID [nonexistent] 존재 여부: " + notExists);

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
        System.out.println("결과: 성공");
    }

    @Test
    @Order(5)
    @DisplayName("이메일 존재 여부 확인 테스트 (existsByEmail)")
    void existsByEmailTest() {
        System.out.println("========== [JPA] 이메일 존재 여부 확인 테스트 ==========");

        // given
        memberRepository.saveAndFlush(testMember);

        // when
        boolean exists = memberRepository.existsByEmail(TEST_EMAIL);
        boolean notExists = memberRepository.existsByEmail("nonexistent@test.com");

        // then
        System.out.println("이메일 [" + TEST_EMAIL + "] 존재 여부: " + exists);
        System.out.println("이메일 [nonexistent@test.com] 존재 여부: " + notExists);

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
        System.out.println("결과: 성공");
    }

    @Test
    @Order(6)
    @DisplayName("회원 정보 수정 테스트 (save - update)")
    void updateTest() {
        System.out.println("========== [JPA] 회원 정보 수정 테스트 ==========");

        // given
        Member savedMember = memberRepository.saveAndFlush(testMember);

        // when
        savedMember.setMemberName("수정된이름");
        savedMember.setEmail("updated@test.com");
        savedMember.setPhone("010-9999-8888");
        Member updatedMember = memberRepository.save(savedMember);

        System.out.println("수정할 정보:");
        System.out.println("  - 이름: JPA테스트유저 -> 수정된이름");
        System.out.println("  - 이메일: " + TEST_EMAIL + " -> updated@test.com");
        System.out.println("  - 전화번호: 010-1234-5678 -> 010-9999-8888");

        // then
        System.out.println("수정 후 조회 결과:");
        System.out.println("  - 이름: " + updatedMember.getMemberName());
        System.out.println("  - 이메일: " + updatedMember.getEmail());
        System.out.println("  - 전화번호: " + updatedMember.getPhone());

        assertThat(updatedMember.getMemberName()).isEqualTo("수정된이름");
        assertThat(updatedMember.getEmail()).isEqualTo("updated@test.com");
        assertThat(updatedMember.getPhone()).isEqualTo("010-9999-8888");
        System.out.println("결과: 성공");
    }

    @Test
    @Order(7)
    @DisplayName("마지막 로그인 시간 업데이트 테스트")
    void updateLastLoginTest() {
        System.out.println("========== [JPA] 마지막 로그인 시간 업데이트 테스트 ==========");

        // given
        Member savedMember = memberRepository.saveAndFlush(testMember);

        // when
        LocalDateTime loginTime = LocalDateTime.now();
        savedMember.setLastLoginAt(loginTime);
        Member updatedMember = memberRepository.save(savedMember);

        // then
        System.out.println("마지막 로그인 시간: " + updatedMember.getLastLoginAt());

        assertThat(updatedMember.getLastLoginAt()).isNotNull();
        System.out.println("결과: 성공");
    }

    @Test
    @Order(8)
    @DisplayName("회원 상태 변경 테스트")
    void updateStatusTest() {
        System.out.println("========== [JPA] 회원 상태 변경 테스트 ==========");
        System.out.println("상태 변경: ACTIVE -> SUSPENDED");

        // given
        Member savedMember = memberRepository.saveAndFlush(testMember);

        // when
        savedMember.setStatus("SUSPENDED");
        Member updatedMember = memberRepository.save(savedMember);

        // then
        System.out.println("변경된 상태: " + updatedMember.getStatus());

        assertThat(updatedMember.getStatus()).isEqualTo("SUSPENDED");
        System.out.println("결과: 성공");
    }

    @Test
    @Order(9)
    @DisplayName("상태로 회원 목록 조회 테스트 (findByStatus)")
    void findByStatusTest() {
        System.out.println("========== [JPA] 상태로 회원 목록 조회 테스트 ==========");

        // given
        memberRepository.saveAndFlush(testMember);

        Member testMember2 = Member.builder()
                .memberId("jpaTestUser002")
                .password("encodedPassword123")
                .memberName("JPA테스트유저2")
                .email("jpatest002@test.com")
                .phone("010-2222-3333")
                .status("ACTIVE")
                .role("USER")
                .build();
        memberRepository.saveAndFlush(testMember2);

        // when
        List<Member> activeMembers = memberRepository.findByStatus("ACTIVE");

        // then
        System.out.println("ACTIVE 상태 회원 수: " + activeMembers.size());
        activeMembers.forEach(member ->
                System.out.println("  - " + member.getMemberId() + " / " + member.getMemberName())
        );

        assertThat(activeMembers).hasSizeGreaterThanOrEqualTo(2);
        System.out.println("결과: 성공");
    }

    @Test
    @Order(10)
    @DisplayName("권한으로 회원 목록 조회 테스트 (findByRole)")
    void findByRoleTest() {
        System.out.println("========== [JPA] 권한으로 회원 목록 조회 테스트 ==========");

        // given
        memberRepository.saveAndFlush(testMember);

        // when
        List<Member> userMembers = memberRepository.findByRole("USER");

        // then
        System.out.println("USER 권한 회원 수: " + userMembers.size());
        userMembers.forEach(member ->
                System.out.println("  - " + member.getMemberId() + " / " + member.getRole())
        );

        assertThat(userMembers).isNotEmpty();
        System.out.println("결과: 성공");
    }

    @Test
    @Order(11)
    @DisplayName("회원명으로 조회 테스트 (findByMemberName)")
    void findByMemberNameTest() {
        System.out.println("========== [JPA] 회원명으로 조회 테스트 ==========");

        // given
        memberRepository.saveAndFlush(testMember);

        // when
        List<Member> foundMembers = memberRepository.findByMemberName("JPA테스트유저");

        // then
        System.out.println("이름 [JPA테스트유저]로 조회 결과: " + foundMembers.size() + "건");
        foundMembers.forEach(member ->
                System.out.println("  - " + member.getMemberId() + " / " + member.getMemberName())
        );

        assertThat(foundMembers).isNotEmpty();
        assertThat(foundMembers.get(0).getMemberName()).isEqualTo("JPA테스트유저");
        System.out.println("결과: 성공");
    }

    @Test
    @Order(12)
    @DisplayName("회원 삭제 테스트 (delete)")
    void deleteTest() {
        System.out.println("========== [JPA] 회원 삭제 테스트 ==========");

        // given
        memberRepository.saveAndFlush(testMember);

        // when
        System.out.println("회원 [" + TEST_MEMBER_ID + "] 삭제 실행");
        memberRepository.deleteById(TEST_MEMBER_ID);
        Optional<Member> foundMember = memberRepository.findById(TEST_MEMBER_ID);

        // then
        System.out.println("삭제 후 조회 결과: " + (foundMember.isPresent() ? "존재" : "없음"));

        assertThat(foundMember).isEmpty();
        System.out.println("결과: 성공");
    }

    @Test
    @Order(13)
    @DisplayName("존재하지 않는 회원 조회시 Empty 반환 테스트")
    void findNonExistentMemberTest() {
        System.out.println("========== [JPA] 존재하지 않는 회원 조회 테스트 ==========");

        // when
        Optional<Member> foundMember = memberRepository.findById("nonexistent");

        // then
        System.out.println("ID [nonexistent] 조회 결과: " + (foundMember.isPresent() ? "존재" : "없음"));

        assertThat(foundMember).isEmpty();
        System.out.println("결과: 성공 (Empty 반환 확인)");
    }

    @Test
    @Order(14)
    @DisplayName("전체 회원 수 조회 테스트 (count)")
    void countTest() {
        System.out.println("========== [JPA] 전체 회원 수 조회 테스트 ==========");

        // given
        long beforeCount = memberRepository.count();
        memberRepository.saveAndFlush(testMember);

        // when
        long afterCount = memberRepository.count();

        // then
        System.out.println("등록 전 회원 수: " + beforeCount);
        System.out.println("등록 후 회원 수: " + afterCount);

        assertThat(afterCount).isEqualTo(beforeCount + 1);
        System.out.println("결과: 성공");
    }
}
