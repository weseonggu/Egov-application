package egovframework.application.member.signup.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 회원가입 응답 DTO
 */
@Getter
@Builder
public class MemberSignupResponseDTO {

    /**
     * 성공 여부
     */
    private boolean success;

    /**
     * 응답 메시지
     */
    private String message;

    /**
     * 등록된 회원 ID
     */
    private String memberId;

    /**
     * 에러 코드 (실패 시)
     */
    private String errorCode;

    /**
     * 성공 응답 생성
     */
    public static MemberSignupResponseDTO success(String memberId) {
        return MemberSignupResponseDTO.builder()
                .success(true)
                .message("회원가입이 완료되었습니다.")
                .memberId(memberId)
                .build();
    }

    /**
     * 실패 응답 생성
     */
    public static MemberSignupResponseDTO fail(String errorCode, String message) {
        return MemberSignupResponseDTO.builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .build();
    }
}
