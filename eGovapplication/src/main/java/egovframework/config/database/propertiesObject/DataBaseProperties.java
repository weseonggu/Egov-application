package egovframework.config.database.propertiesObject;

/**
 * 데이터베이스 설정 인터페이스
 * 다양한 데이터베이스 설정을 위한 공통 인터페이스를 구현
 */
public interface DataBaseProperties {

    String getDriverClassName();

    String getUrl();

    String getUsername();

    String getPassword();

}
