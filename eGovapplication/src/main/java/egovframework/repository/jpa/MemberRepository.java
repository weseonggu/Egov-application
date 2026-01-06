package egovframework.repository.jpa;

import egovframework.repository.jpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 회원 JPA Repository
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    /**
     * 이메일로 회원 조회
     */
    Optional<Member> findByEmail(String email);

    /**
     * 회원명으로 회원 조회
     */
    List<Member> findByMemberName(String memberName);

    /**
     * 상태로 회원 목록 조회
     */
    List<Member> findByStatus(String status);

    /**
     * 권한으로 회원 목록 조회
     */
    List<Member> findByRole(String role);

    /**
     * 이메일 존재 여부 확인
     */
    boolean existsByEmail(String email);

    /**
     * 회원 ID 존재 여부 확인
     */
    boolean existsByMemberId(String memberId);
}
