package egovframework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Spring Boot 애플리케이션 컨텍스트 로드 테스트
 *
 * <p>애플리케이션이 정상적으로 시작되고 Spring 컨텍스트가 올바르게 로드되는지 확인합니다.</p>
 *
 * <h2>상세 테스트 파일 안내</h2>
 * <p>테스트가 카테고리별로 분리되어 있습니다:</p>
 * <ul>
 *   <li>{@code egovframework.config.BeanCreationTest} - Bean 생성 테스트</li>
 *   <li>{@code egovframework.config.DatabaseConnectionTest} - DB 연결 테스트</li>
 *   <li>{@code egovframework.config.MultiDataSourceIsolationTest} - 다중 DataSource 격리 테스트</li>
 *   <li>{@code egovframework.mapper.member.MemberSignupMapperTest} - 회원 Mapper 테스트</li>
 * </ul>
 *
 * @see egovframework.config.BeanCreationTest
 * @see egovframework.config.DatabaseConnectionTest
 * @see egovframework.config.MultiDataSourceIsolationTest
 * @see egovframework.mapper.member.MemberSignupMapperTest
 */
@SpringBootTest
@DisplayName("Spring Boot 애플리케이션 컨텍스트 로드 테스트")
class EgovBootApplicationTests {

    @Test
    @DisplayName("애플리케이션 컨텍스트 로드 확인")
    void contextLoads() {
        System.out.println("========== Spring Boot 애플리케이션 컨텍스트 로드 테스트 ==========");
        System.out.println("Spring Boot 애플리케이션 컨텍스트가 정상적으로 로드되었습니다.");
        System.out.println("결과: 성공");
    }
}
