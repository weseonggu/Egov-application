package egovframework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * eGov Application 메인 클래스
 *
 * <p>Spring Boot 기반 전자정부 프레임워크 애플리케이션의 진입점입니다.</p>
 *
 * <h2>주요 설정</h2>
 * <ul>
 *   <li>Component Scan: egovframework 패키지 하위 전체</li>
 *   <li>Auto Configuration: Spring Boot 자동 설정 적용</li>
 * </ul>
 *
 * <h2>실행 방법</h2>
 * <pre>
 * # Maven
 * mvn spring-boot:run
 *
 * # JAR 실행
 * java -jar eGovapplication-1.0.0.jar
 * </pre>
 *
 * @see <a href="https://www.egovframe.go.kr">전자정부 프레임워크 포털</a>
 */
@SpringBootApplication
public class EgovBootApplication {

	/**
	 * 애플리케이션 메인 메서드
	 *
	 * @param args 명령행 인자
	 */
	public static void main(String[] args) {
		SpringApplication.run(EgovBootApplication.class, args);
	}

}
