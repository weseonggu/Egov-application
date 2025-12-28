package egovframework.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 설정
 * 전자정부 프레임워크 보안 가이드 준수
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 비밀번호 암호화 (BCrypt)
     * - 단방향 암호화
     * - Salt 자동 적용
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Security Filter Chain 설정
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 설정 (API 서버용으로 비활성화, 필요시 활성화)
            .csrf(csrf -> csrf.disable())

            // 요청 권한 설정 (임시: 모든 요청 허용)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )

            // 폼 로그인 비활성화 (REST API 서버)
            .formLogin(form -> form.disable())

            // HTTP Basic 인증 비활성화
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
