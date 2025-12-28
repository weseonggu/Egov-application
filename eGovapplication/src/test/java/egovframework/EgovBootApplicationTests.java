package egovframework;

import egovframework.mapper.member.MemberSignupMapper;
import egovframework.member.signup.dto.MemberVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MemberSignupMapper 테스트
 */
@SpringBootTest
//@Transactional
class EgovBootApplicationTests {

    @Autowired
    private MemberSignupMapper memberSignupMapper;

    private MemberVO testMember;
    private final String TEST_MEMBER_ID = "testuser001";
    private final String TEST_EMAIL = "testuser001@test.com";

    @BeforeEach
    void setUp() {
        // 테스트용 회원 데이터 생성
        testMember = MemberVO.builder()
                .memberId(TEST_MEMBER_ID)
                .password("encodedPassword123")
                .memberName("테스트유저")
                .email(TEST_EMAIL)
                .phone("010-1234-5678")
                .status("ACTIVE")
                .role("USER")
                .build();
    }

    // tearDown 주석처리 - DB에 데이터 남기려면 비활성화
    // @AfterEach
    // void tearDown() {
    //     try {
    //         memberSignupMapper.deleteMember(TEST_MEMBER_ID);
    //     } catch (Exception ignored) {
    //     }
    // }

    @Test
    @DisplayName("스프링 컨텍스트 로드 테스트")
    void contextLoads() {
        System.out.println("========== 컨텍스트 로드 테스트 ==========");
        System.out.println("memberSignupMapper: " + memberSignupMapper);
        assertThat(memberSignupMapper).isNotNull();
        System.out.println("결과: 성공");
    }

    @Test
    @DisplayName("회원 등록 테스트")
    void insertMemberTest() {
        System.out.println("========== 회원 등록 테스트 ==========");
        System.out.println("등록할 회원 정보:");
        System.out.println("  - ID: " + testMember.getMemberId());
        System.out.println("  - 이름: " + testMember.getMemberName());
        System.out.println("  - 이메일: " + testMember.getEmail());
        System.out.println("  - 전화번호: " + testMember.getPhone());

        // when
        int result = memberSignupMapper.insertMember(testMember);

        // then
        System.out.println("INSERT 결과: " + result + "건");
        assertThat(result).isEqualTo(1);
        System.out.println("결과: 성공");
    }

    @Test
    @DisplayName("회원 ID로 조회 테스트")
    void selectMemberByIdTest() {
        System.out.println("========== 회원 ID로 조회 테스트 ==========");

        // given
        memberSignupMapper.insertMember(testMember);
        System.out.println("테스트 데이터 INSERT 완료");

        // when
        MemberVO foundMember = memberSignupMapper.selectMemberById(TEST_MEMBER_ID);

        // then
        System.out.println("조회 결과:");
        System.out.println("  - ID: " + foundMember.getMemberId());
        System.out.println("  - 이름: " + foundMember.getMemberName());
        System.out.println("  - 이메일: " + foundMember.getEmail());
        System.out.println("  - 상태: " + foundMember.getStatus());
        System.out.println("  - 권한: " + foundMember.getRole());
        System.out.println("  - 생성일: " + foundMember.getCreatedAt());

        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getMemberId()).isEqualTo(TEST_MEMBER_ID);
        assertThat(foundMember.getMemberName()).isEqualTo("테스트유저");
        assertThat(foundMember.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(foundMember.getStatus()).isEqualTo("ACTIVE");
        assertThat(foundMember.getRole()).isEqualTo("USER");
        System.out.println("결과: 성공");
    }

    @Test
    @DisplayName("이메일로 조회 테스트")
    void selectMemberByEmailTest() {
        System.out.println("========== 이메일로 조회 테스트 ==========");

        // given
        memberSignupMapper.insertMember(testMember);
        System.out.println("테스트 데이터 INSERT 완료");

        // when
        MemberVO foundMember = memberSignupMapper.selectMemberByEmail(TEST_EMAIL);

        // then
        System.out.println("이메일 [" + TEST_EMAIL + "]로 조회 결과:");
        System.out.println("  - ID: " + foundMember.getMemberId());
        System.out.println("  - 이름: " + foundMember.getMemberName());

        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(foundMember.getMemberId()).isEqualTo(TEST_MEMBER_ID);
        System.out.println("결과: 성공");
    }

    @Test
    @DisplayName("회원 ID 중복 체크 테스트")
    void checkMemberIdExistsTest() {
        System.out.println("========== 회원 ID 중복 체크 테스트 ==========");

        // given
        memberSignupMapper.insertMember(testMember);
        System.out.println("테스트 데이터 INSERT 완료");

        // when
        int existsResult = memberSignupMapper.checkMemberIdExists(TEST_MEMBER_ID);
        int notExistsResult = memberSignupMapper.checkMemberIdExists("nonexistent");

        // then
        System.out.println("ID [" + TEST_MEMBER_ID + "] 존재 여부: " + existsResult);
        System.out.println("ID [nonexistent] 존재 여부: " + notExistsResult);

        assertThat(existsResult).isEqualTo(1);
        assertThat(notExistsResult).isEqualTo(0);
        System.out.println("결과: 성공");
    }

    @Test
    @DisplayName("이메일 중복 체크 테스트")
    void checkEmailExistsTest() {
        System.out.println("========== 이메일 중복 체크 테스트 ==========");

        // given
        memberSignupMapper.insertMember(testMember);
        System.out.println("테스트 데이터 INSERT 완료");

        // when
        int existsResult = memberSignupMapper.checkEmailExists(TEST_EMAIL);
        int notExistsResult = memberSignupMapper.checkEmailExists("nonexistent@test.com");

        // then
        System.out.println("이메일 [" + TEST_EMAIL + "] 존재 여부: " + existsResult);
        System.out.println("이메일 [nonexistent@test.com] 존재 여부: " + notExistsResult);

        assertThat(existsResult).isEqualTo(1);
        assertThat(notExistsResult).isEqualTo(0);
        System.out.println("결과: 성공");
    }

    @Test
    @DisplayName("회원 정보 수정 테스트")
    void updateMemberTest() {
        System.out.println("========== 회원 정보 수정 테스트 ==========");

        // given
        memberSignupMapper.insertMember(testMember);
        System.out.println("테스트 데이터 INSERT 완료");

        MemberVO updateMember = MemberVO.builder()
                .memberId(TEST_MEMBER_ID)
                .memberName("수정된이름")
                .email("updated@test.com")
                .phone("010-9999-8888")
                .build();

        System.out.println("수정할 정보:");
        System.out.println("  - 이름: 테스트유저 -> 수정된이름");
        System.out.println("  - 이메일: " + TEST_EMAIL + " -> updated@test.com");
        System.out.println("  - 전화번호: 010-1234-5678 -> 010-9999-8888");

        // when
        int result = memberSignupMapper.updateMember(updateMember);
        MemberVO foundMember = memberSignupMapper.selectMemberById(TEST_MEMBER_ID);

        // then
        System.out.println("UPDATE 결과: " + result + "건");
        System.out.println("수정 후 조회 결과:");
        System.out.println("  - 이름: " + foundMember.getMemberName());
        System.out.println("  - 이메일: " + foundMember.getEmail());
        System.out.println("  - 전화번호: " + foundMember.getPhone());

        assertThat(result).isEqualTo(1);
        assertThat(foundMember.getMemberName()).isEqualTo("수정된이름");
        assertThat(foundMember.getEmail()).isEqualTo("updated@test.com");
        assertThat(foundMember.getPhone()).isEqualTo("010-9999-8888");
        System.out.println("결과: 성공");
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    void updatePasswordTest() {
        System.out.println("========== 비밀번호 변경 테스트 ==========");

        // given
        memberSignupMapper.insertMember(testMember);
        System.out.println("테스트 데이터 INSERT 완료");

        MemberVO passwordUpdate = MemberVO.builder()
                .memberId(TEST_MEMBER_ID)
                .password("newEncodedPassword456")
                .build();

        System.out.println("비밀번호 변경: encodedPassword123 -> newEncodedPassword456");

        // when
        int result = memberSignupMapper.updatePassword(passwordUpdate);
        MemberVO foundMember = memberSignupMapper.selectMemberById(TEST_MEMBER_ID);

        // then
        System.out.println("UPDATE 결과: " + result + "건");
        System.out.println("변경된 비밀번호: " + foundMember.getPassword());

        assertThat(result).isEqualTo(1);
        assertThat(foundMember.getPassword()).isEqualTo("newEncodedPassword456");
        System.out.println("결과: 성공");
    }

    @Test
    @DisplayName("마지막 로그인 시간 업데이트 테스트")
    void updateLastLoginTest() {
        System.out.println("========== 마지막 로그인 시간 업데이트 테스트 ==========");

        // given
        memberSignupMapper.insertMember(testMember);
        System.out.println("테스트 데이터 INSERT 완료");

        // when
        int result = memberSignupMapper.updateLastLogin(TEST_MEMBER_ID);
        MemberVO foundMember = memberSignupMapper.selectMemberById(TEST_MEMBER_ID);

        // then
        System.out.println("UPDATE 결과: " + result + "건");
        System.out.println("마지막 로그인 시간: " + foundMember.getLastLoginAt());

        assertThat(result).isEqualTo(1);
        assertThat(foundMember.getLastLoginAt()).isNotNull();
        System.out.println("결과: 성공");
    }

    @Test
    @DisplayName("회원 상태 변경 테스트")
    void updateStatusTest() {
        System.out.println("========== 회원 상태 변경 테스트 ==========");

        // given
        memberSignupMapper.insertMember(testMember);
        System.out.println("테스트 데이터 INSERT 완료");

        System.out.println("상태 변경: ACTIVE -> SUSPENDED");

        // when
        int result = memberSignupMapper.updateStatus(TEST_MEMBER_ID, "SUSPENDED");
        MemberVO foundMember = memberSignupMapper.selectMemberById(TEST_MEMBER_ID);

        // then
        System.out.println("UPDATE 결과: " + result + "건");
        System.out.println("변경된 상태: " + foundMember.getStatus());

        assertThat(result).isEqualTo(1);
        assertThat(foundMember.getStatus()).isEqualTo("SUSPENDED");
        System.out.println("결과: 성공");
    }

    @Test
    @DisplayName("회원 삭제 테스트")
    void deleteMemberTest() {
        System.out.println("========== 회원 삭제 테스트 ==========");

        // given
        memberSignupMapper.insertMember(testMember);
        System.out.println("테스트 데이터 INSERT 완료");

        // when
        System.out.println("회원 [" + TEST_MEMBER_ID + "] 삭제 실행");
        int result = memberSignupMapper.deleteMember(TEST_MEMBER_ID);
        MemberVO foundMember = memberSignupMapper.selectMemberById(TEST_MEMBER_ID);

        // then
        System.out.println("DELETE 결과: " + result + "건");
        System.out.println("삭제 후 조회 결과: " + foundMember);

        assertThat(result).isEqualTo(1);
        assertThat(foundMember).isNull();
        System.out.println("결과: 성공");
    }

    @Test
    @DisplayName("존재하지 않는 회원 조회시 null 반환 테스트")
    void selectNonExistentMemberTest() {
        System.out.println("========== 존재하지 않는 회원 조회 테스트 ==========");

        // when
        MemberVO foundMember = memberSignupMapper.selectMemberById("nonexistent");

        // then
        System.out.println("ID [nonexistent] 조회 결과: " + foundMember);

        assertThat(foundMember).isNull();
        System.out.println("결과: 성공 (null 반환 확인)");
    }
}
