package egovframework.config.database.propertiesObject;

import egovframework.common.constants.BeanNames;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * 보조 데이터베이스 설정 클래스
 * application.properties의 spring.secondary.datasource 설정을 바인딩
 */
@Getter
@Setter
@Component(BeanNames.SECONDARY_DATABASE_PROPERTIES)
@ConfigurationProperties(prefix = "spring.secondary.datasource")
public class SecondaryDataBaseProperties implements DataBaseProperties {

    private String driverClassName;
    private String url;
    private String username;
    private String password;

}
