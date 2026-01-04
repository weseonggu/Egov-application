package egovframework.repository.jpa;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 회원 Entity
 * DB 테이블 TB_MEMBER와 매핑
 */
@Entity
@Table(name = "TB_MEMBER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    /**
     * 회원 ID (PK)
     */
    @Id
    @Column(name = "MEMBER_ID", length = 50, nullable = false)
    private String memberId;

    /**
     * 비밀번호 (BCrypt 암호화)
     */
    @Column(name = "PASSWORD", length = 100, nullable = false)
    private String password;

    /**
     * 회원명
     */
    @Column(name = "MEMBER_NAME", length = 50, nullable = false)
    private String memberName;

    /**
     * 이메일
     */
    @Column(name = "EMAIL", length = 100)
    private String email;

    /**
     * 전화번호
     */
    @Column(name = "PHONE", length = 20)
    private String phone;

    /**
     * 상태 (ACTIVE, INACTIVE, SUSPENDED)
     */
    @Column(name = "STATUS", length = 20, nullable = false)
    @Builder.Default
    private String status = "ACTIVE";

    /**
     * 권한 (USER, ADMIN)
     */
    @Column(name = "ROLE", length = 20, nullable = false)
    @Builder.Default
    private String role = "USER";

    /**
     * 생성일시
     */
    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 수정일시
     */
    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    /**
     * 마지막 로그인 일시
     */
    @Column(name = "LAST_LOGIN_AT")
    private LocalDateTime lastLoginAt;
}
