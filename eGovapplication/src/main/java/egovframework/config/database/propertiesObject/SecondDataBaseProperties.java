package egovframework.config.database.propertiesObject;

import egovframework.common.constants.BeanNames;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * Second 데이터베이스 설정 클래스
 * application.properties의 spring.second.datasource 설정을 바인딩
 */
@Getter
@Setter
@Component(BeanNames.SECOND_DATABASE_PROPERTIES)
@ConfigurationProperties(prefix = "spring.second.datasource")
public class SecondDataBaseProperties implements DataBaseProperties {

    private String driverClassName;
    private String url;
    private String username;
    private String password;

}
