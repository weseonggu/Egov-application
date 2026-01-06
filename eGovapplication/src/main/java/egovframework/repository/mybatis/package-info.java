/**
 * MyBatis 데이터 액세스 계층 패키지
 *
 * <h2>개요</h2>
 * <p>MyBatis를 사용한 데이터베이스 접근을 담당하는 패키지입니다.
 * 주로 <b>복잡한 조회 쿼리와 동적 SQL</b>을 처리합니다.</p>
 *
 * <h2>패키지 구조</h2>
 * <pre>
 * egovframework.repository.mybatis/
 * +-- dao/                              <- VO/DAO 클래스
 * |   +-- MemberVO.java                 <- DB 테이블 매핑 Value Object
 * +-- mapper/                           <- Mapper 인터페이스
 *     +-- MemberSignupMapper.java       <- @EgovMapper 어노테이션 사용
 * </pre>
 *
 * <h2>DAO vs Mapper 역할</h2>
 * <table border="1">
 *   <tr><th>구분</th><th>역할</th><th>예시</th></tr>
 *   <tr>
 *     <td><b>dao/</b></td>
 *     <td>Value Object (VO) - DB 테이블과 1:1 매핑되는 데이터 전송 객체</td>
 *     <td>MemberVO.java</td>
 *   </tr>
 *   <tr>
 *     <td><b>mapper/</b></td>
 *     <td>Mapper 인터페이스 - SQL과 메서드를 연결</td>
 *     <td>MemberSignupMapper.java</td>
 *   </tr>
 * </table>
 *
 * <h2>Mapper 인터페이스 작성 규칙</h2>
 * <ul>
 *   <li>{@code @EgovMapper} 어노테이션 사용 (전자정부 프레임워크 표준)</li>
 *   <li>XML 파일 위치: {@code resources/egovframework/sqlmap/mappers/}</li>
 *   <li>namespace는 Mapper 인터페이스의 FQCN(Fully Qualified Class Name) 사용</li>
 * </ul>
 *
 * <h3>사용 예시</h3>
 * <pre>{@code
 * @EgovMapper("memberSignupMapper")
 * public interface MemberSignupMapper {
 *
 *     MemberVO selectMemberById(@Param("memberId") String memberId);
 *
 *     int insertMember(MemberVO memberVO);
 * }
 * }</pre>
 *
 * <h2>XML 매핑 파일 예시</h2>
 * <pre>{@code
 * <?xml version="1.0" encoding="UTF-8"?>
 * <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
 *     "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
 * <mapper namespace="egovframework.repository.mybatis.mapper.MemberSignupMapper">
 *
 *     <select id="selectMemberById" resultType="MemberVO">
 *         SELECT * FROM TB_MEMBER WHERE MEMBER_ID = #{memberId}
 *     </select>
 *
 * </mapper>
 * }</pre>
 *
 * <h2>설정 클래스</h2>
 * <p>이 패키지의 Mapper는 {@link egovframework.config.mybatis.BasicDBMybatisMapperConfig}에서
 * {@code @MapperScan}을 통해 자동으로 스캔됩니다.</p>
 *
 * <h3>스캔 설정</h3>
 * <pre>{@code
 * @MapperScan(
 *     basePackages = "egovframework.repository.mybatis.mapper",
 *     sqlSessionFactoryRef = BeanNames.Basic_Sql_Session
 * )
 * }</pre>
 *
 * @see egovframework.config.mybatis.BasicDBMybatisMapperConfig MyBatis 설정 클래스
 * @see egovframework.repository.mybatis.mapper.MemberSignupMapper Mapper 인터페이스 예시
 * @see egovframework.repository.mybatis.dao.MemberVO VO 클래스 예시
 */
package egovframework.repository.mybatis;
