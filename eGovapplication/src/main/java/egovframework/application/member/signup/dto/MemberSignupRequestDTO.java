package egovframework.application.member.signup.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원가입 요청 DTO
 * 전자정부 프레임워크 시큐어코딩 가이드 준수
 */
@Getter
@Setter
@NoArgsConstructor
public class MemberSignupRequestDTO {

    /**
     * 회원 ID
     * - 영문, 숫자만 허용 (4~20자)
     * - SQL Injection, XSS 방지
     */
    @NotBlank(message = "회원 ID는 필수 입력값입니다.")
    @Size(min = 4, max = 20, message = "회원 ID는 4~20자 사이로 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "회원 ID는 영문과 숫자만 사용 가능합니다.")
    private String memberId;

    /**
     * 비밀번호
     * - 최소 8자 이상
     * - 영문, 숫자, 특수문자 조합
     */
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자 사이로 입력해주세요.")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]+$",
        message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다."
    )
    private String password;

    /**
     * 비밀번호 확인
     */
    @NotBlank(message = "비밀번호 확인은 필수 입력값입니다.")
    private String passwordConfirm;

    /**
     * 회원명
     * - 한글, 영문만 허용 (2~50자)
     */
    @NotBlank(message = "회원명은 필수 입력값입니다.")
    @Size(min = 2, max = 50, message = "회원명은 2~50자 사이로 입력해주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z]+$", message = "회원명은 한글 또는 영문만 사용 가능합니다.")
    private String memberName;

    /**
     * 이메일
     */
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Size(max = 100, message = "이메일은 100자 이내로 입력해주세요.")
    private String email;

    /**
     * 전화번호
     * - 숫자와 하이픈만 허용
     */
    @Pattern(regexp = "^$|^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다. (예: 010-1234-5678)")
    private String phone;

    /**
     * 비밀번호 일치 여부 확인
     */
    public boolean isPasswordMatched() {
        return password != null && password.equals(passwordConfirm);
    }
}
