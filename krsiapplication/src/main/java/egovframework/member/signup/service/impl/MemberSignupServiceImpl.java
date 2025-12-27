package egovframework.member.signup.service.impl;

import egovframework.member.signup.dto.MemberSignupRequestDTO;
import egovframework.member.signup.dto.MemberSignupResponseDTO;
import egovframework.member.signup.dto.MemberVO;
import egovframework.member.signup.repository.MemberSignupMapper;
import egovframework.member.signup.service.MemberSignupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원가입 서비스 구현체
 * 전자정부 프레임워크 시큐어코딩 가이드 준수
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberSignupServiceImpl implements MemberSignupService {

    private final MemberSignupMapper memberSignupMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 처리
     * - 입력값 검증
     * - 중복 체크
     * - 비밀번호 암호화
     * - 회원 등록
     */
    @Override
    @Transactional
    public MemberSignupResponseDTO signup(MemberSignupRequestDTO requestDTO) {
        try {
            // 1. 비밀번호 일치 확인
            if (!requestDTO.isPasswordMatched()) {
                log.warn("회원가입 실패 - 비밀번호 불일치: {}", requestDTO.getMemberId());
                return MemberSignupResponseDTO.fail("PASSWORD_MISMATCH", "비밀번호가 일치하지 않습니다.");
            }

            // 2. 회원 ID 중복 체크
            if (!checkMemberIdAvailable(requestDTO.getMemberId())) {
                log.warn("회원가입 실패 - ID 중복: {}", requestDTO.getMemberId());
                return MemberSignupResponseDTO.fail("DUPLICATE_ID", "이미 사용 중인 회원 ID입니다.");
            }

            // 3. 이메일 중복 체크
            if (!checkEmailAvailable(requestDTO.getEmail())) {
                log.warn("회원가입 실패 - 이메일 중복: {}", requestDTO.getEmail());
                return MemberSignupResponseDTO.fail("DUPLICATE_EMAIL", "이미 사용 중인 이메일입니다.");
            }

            // 4. 비밀번호 암호화 (BCrypt)
            String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());

            // 5. 회원 VO 생성
            MemberVO memberVO = MemberVO.fromSignupRequest(requestDTO, encodedPassword);

            // 6. 회원 등록
            int result = memberSignupMapper.insertMember(memberVO);

            if (result > 0) {
                log.info("회원가입 성공: {}", requestDTO.getMemberId());
                return MemberSignupResponseDTO.success(requestDTO.getMemberId());
            } else {
                log.error("회원가입 실패 - DB 등록 실패: {}", requestDTO.getMemberId());
                return MemberSignupResponseDTO.fail("DB_ERROR", "회원가입 처리 중 오류가 발생했습니다.");
            }

        } catch (Exception e) {
            log.error("회원가입 중 예외 발생: {}", e.getMessage(), e);
            return MemberSignupResponseDTO.fail("SYSTEM_ERROR", "시스템 오류가 발생했습니다.");
        }
    }

    /**
     * 회원 ID 사용 가능 여부 체크
     */
    @Override
    public boolean checkMemberIdAvailable(String memberId) {
        // 입력값 검증 (시큐어코딩)
        if (memberId == null || memberId.trim().isEmpty()) {
            return false;
        }
        return memberSignupMapper.checkMemberIdExists(memberId) == 0;
    }

    /**
     * 이메일 사용 가능 여부 체크
     */
    @Override
    public boolean checkEmailAvailable(String email) {
        // 입력값 검증 (시큐어코딩)
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return memberSignupMapper.checkEmailExists(email) == 0;
    }
}
