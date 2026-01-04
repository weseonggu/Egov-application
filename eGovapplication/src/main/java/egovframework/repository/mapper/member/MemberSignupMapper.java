package egovframework.repository.mapper.member;

import egovframework.member.signup.dao.MemberVO;
import org.apache.ibatis.annotations.Param;
import org.egovframe.rte.psl.dataaccess.mapper.EgovMapper;

/**
 * 회원가입 Mapper 인터페이스
 * MyBatis와 연동되는 Repository 역할
 *
 * @EgovMapper 설명: egovframework.rte.psl.dataaccess.mapper.Mapper 사용
 * - 전자정부 프레임워크 표준 어노테이션
 * - MapperScan의 annotationClass로 스캔시 설정 가능
 */
@EgovMapper("memberSignupMapper")
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
     * @param email 이메일 주소
     * @return 회원 정보
     */
    MemberVO selectMemberByEmail(@Param("email") String email);

    /**
     * 회원 ID 중복 체크
     * @param memberId 회원 ID
     * @return 존재여부 (1: 존재, 0: 미존재)
     */
    int checkMemberIdExists(@Param("memberId") String memberId);

    /**
     * 이메일 중복 체크
     * @param email 이메일 주소
     * @return 존재여부 (1: 존재, 0: 미존재)
     */
    int checkEmailExists(@Param("email") String email);

    /**
     * 회원 정보 수정
     * @param memberVO 회원 정보
     * @return 수정된 행 수
     */
    int updateMember(MemberVO memberVO);

    /**
     * 비밀번호 변경
     * @param memberVO 회원 정보 (memberId, password)
     * @return 수정된 행 수
     */
    int updatePassword(MemberVO memberVO);

    /**
     * 마지막 로그인 일시 업데이트
     * @param memberId 회원 ID
     * @return 수정된 행 수
     */
    int updateLastLogin(@Param("memberId") String memberId);

    /**
     * 회원 상태 변경
     * @param memberId 회원 ID
     * @param status 상태
     * @return 수정된 행 수
     */
    int updateStatus(@Param("memberId") String memberId, @Param("status") String status);

    /**
     * 회원 삭제
     * @param memberId 회원 ID
     * @return 삭제된 행 수
     */
    int deleteMember(@Param("memberId") String memberId);
}
