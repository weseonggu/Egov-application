package egovframework.member.signup.service;

import egovframework.member.signup.dto.MemberSignupRequestDTO;
import egovframework.member.signup.dto.MemberSignupResponseDTO;

/**
 * 회원가입 서비스 인터페이스
 */
public interface MemberSignupService {

    /**
     * 회원가입 처리
     * @param requestDTO 회원가입 요청 정보
     * @return 회원가입 처리 결과
     */
    MemberSignupResponseDTO signup(MemberSignupRequestDTO requestDTO);

    /**
     * 회원 ID 중복 체크
     * @param memberId 회원 ID
     * @return 사용 가능 여부 (true: 사용 가능)
     */
    boolean checkMemberIdAvailable(String memberId);

    /**
     * 이메일 중복 체크
     * @param email 이메일
     * @return 사용 가능 여부 (true: 사용 가능)
     */
    boolean checkEmailAvailable(String email);
}
