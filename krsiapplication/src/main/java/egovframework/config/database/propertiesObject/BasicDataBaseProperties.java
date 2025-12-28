package egovframework.config.database.propertiesObject;

import egovframework.common.constants.BeanNames;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component(BeanNames.BASIC_DATABASE_PROPERTIES)
@ConfigurationProperties(prefix = "spring.basic.datasource")
public class BasicDataBaseProperties implements DataBaseProperties {

    private String driverClassName;
    private String url;
    private String username;
    private String password;

}
