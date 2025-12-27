package egovframework.member.signup.repository;

import egovframework.member.signup.dto.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 회원가입 Mapper 인터페이스
 * MyBatis와 연동되는 Repository 역할
 */
@Mapper
public interface MemberSignupMapper {

    /**
     * 회원 등록
     * @param memberVO 회원 정보
     * @return 등록된 행 수
     */
    int insertMember(MemberVO memberVO);

    /**
     * 회원 ID로 조회
     * @param memberId 회원 ID
     * @return 회원 정보
     */
    MemberVO selectMemberById(@Param("memberId") String memberId);

    /**
     * 이메일로 조회
     * @param email 이메일
     * @return 회원 정보
     */
    MemberVO selectMemberByEmail(@Param("email") String email);

    /**
     * 회원 ID 중복 체크
     * @param memberId 회원 ID
     * @return 존재 여부 (1: 존재, 0: 미존재)
     */
    int checkMemberIdExists(@Param("memberId") String memberId);

    /**
     * 이메일 중복 체크
     * @param email 이메일
     * @return 존재 여부 (1: 존재, 0: 미존재)
     */
    int checkEmailExists(@Param("email") String email);
}
