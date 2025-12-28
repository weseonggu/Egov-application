package egovframework.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 테스트용 컨트롤러
 *
 * <p>애플리케이션 동작 확인을 위한 간단한 엔드포인트 제공</p>
 *
 * <h2>엔드포인트</h2>
 * <ul>
 *   <li>GET /test - Hello World 응답</li>
 * </ul>
 */
@RestController
public class TestController {

    /**
     * 테스트 엔드포인트
     *
     * @return "Hello World" 문자열
     */
    @GetMapping("/test")
    public String hello() {
        return "Hello World";
    }
}
