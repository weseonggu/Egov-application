package egovframework.repository.mybatis.dao;

import egovframework.application.member.signup.dto.MemberSignupRequestDTO;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 회원 VO (Value Object)
 * DB 테이블 TB_MEMBER와 매핑
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberVO {

    /**
     * 회원 ID (PK)
     */
    private String memberId;

    /**
     * 비밀번호 (BCrypt 암호화)
     */
    private String password;

    /**
     * 회원명
     */
    private String memberName;

    /**
     * 이메일
     */
    private String email;

    /**
     * 전화번호
     */
    private String phone;

    /**
     * 상태 (ACTIVE, INACTIVE, SUSPENDED)
     */
    private String status;

    /**
     * 권한 (USER, ADMIN)
     */
    private String role;

    /**
     * 생성일시
     */
    private LocalDateTime createdAt;

    /**
     * 수정일시
     */
    private LocalDateTime updatedAt;

    /**
     * 마지막 로그인 일시
     */
    private LocalDateTime lastLoginAt;

    /**
     * 회원가입 요청 DTO로부터 VO 생성
     */
    public static MemberVO fromSignupRequest(MemberSignupRequestDTO request, String encodedPassword) {
        return MemberVO.builder()
                .memberId(request.getMemberId())
                .password(encodedPassword)
                .memberName(request.getMemberName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .status("ACTIVE")
                .role("USER")
                .build();
    }
}
