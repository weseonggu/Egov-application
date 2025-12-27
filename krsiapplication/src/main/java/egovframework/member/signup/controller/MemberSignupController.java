package egovframework.member.signup.controller;

import egovframework.member.signup.dto.MemberSignupRequestDTO;
import egovframework.member.signup.dto.MemberSignupResponseDTO;
import egovframework.member.signup.service.MemberSignupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 회원가입 컨트롤러
 * 전자정부 프레임워크 시큐어코딩 가이드 준수
 * - 입력값 검증
 * - XSS 방지
 * - CSRF 방지 (Spring Security)
 */
@Slf4j
@RestController
@RequestMapping("/api/member/signup")
@RequiredArgsConstructor
public class MemberSignupController {

    private final MemberSignupService memberSignupService;

    /**
     * 회원가입 처리
     * POST /api/member/signup
     */
    @PostMapping
    public ResponseEntity<MemberSignupResponseDTO> signup(
            @Valid @RequestBody MemberSignupRequestDTO requestDTO,
            BindingResult bindingResult) {

        // 1. 입력값 검증 오류 처리
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .findFirst()
                    .map(error -> error.getDefaultMessage())
                    .orElse("입력값이 올바르지 않습니다.");

            log.warn("회원가입 요청 검증 실패: {}", errorMessage);
            return ResponseEntity.badRequest()
                    .body(MemberSignupResponseDTO.fail("VALIDATION_ERROR", errorMessage));
        }

        // 2. 회원가입 처리
        MemberSignupResponseDTO response = memberSignupService.signup(requestDTO);

        // 3. 결과 반환
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 회원 ID 중복 체크
     * GET /api/member/signup/check-id?memberId=xxx
     */
    @GetMapping("/check-id")
    public ResponseEntity<Map<String, Object>> checkMemberId(
            @RequestParam("memberId") String memberId) {

        Map<String, Object> result = new HashMap<>();

        // 입력값 검증
        if (memberId == null || memberId.trim().isEmpty()) {
            result.put("available", false);
            result.put("message", "회원 ID를 입력해주세요.");
            return ResponseEntity.badRequest().body(result);
        }

        // ID 형식 검증 (영문, 숫자만 4~20자)
        if (!memberId.matches("^[a-zA-Z0-9]{4,20}$")) {
            result.put("available", false);
            result.put("message", "회원 ID는 영문과 숫자 4~20자로 입력해주세요.");
            return ResponseEntity.badRequest().body(result);
        }

        boolean available = memberSignupService.checkMemberIdAvailable(memberId);
        result.put("available", available);
        result.put("message", available ? "사용 가능한 ID입니다." : "이미 사용 중인 ID입니다.");

        return ResponseEntity.ok(result);
    }

    /**
     * 이메일 중복 체크
     * GET /api/member/signup/check-email?email=xxx
     */
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmail(
            @RequestParam("email") String email) {

        Map<String, Object> result = new HashMap<>();

        // 입력값 검증
        if (email == null || email.trim().isEmpty()) {
            result.put("available", false);
            result.put("message", "이메일을 입력해주세요.");
            return ResponseEntity.badRequest().body(result);
        }

        // 이메일 형식 검증
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            result.put("available", false);
            result.put("message", "올바른 이메일 형식이 아닙니다.");
            return ResponseEntity.badRequest().body(result);
        }

        boolean available = memberSignupService.checkEmailAvailable(email);
        result.put("available", available);
        result.put("message", available ? "사용 가능한 이메일입니다." : "이미 사용 중인 이메일입니다.");

        return ResponseEntity.ok(result);
    }
}
