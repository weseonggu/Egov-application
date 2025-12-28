package egovframework.common.constants;

/**
 * Spring Bean 이름 상수 클래스
 * Bean 이름을 하드코딩하지 않고 상수로 관리하여 오타 방지 및 리팩토링 용이
 *
 * 사용예시:
 * - @Component(BeanNames.MEMBER_SERVICE)
 * - @Resource(name = BeanNames.MEMBER_SERVICE)
 * - @Qualifier(BeanNames.MEMBER_SERVICE)
 */
public final class BeanNames {

    private BeanNames() {
        // 인스턴스화 방지
    }

    // ============================================
    // Database Properties
    // ============================================
    public static final String BASIC_DATABASE_PROPERTIES = "basicDatabaseProperties";

    // ============================================
    // DataSource
    // ============================================
    public static final String BASIC_DATASOURCE = "basicDataSource";

    public static final String Application_Config_Datasource = "applicationConfigDatasource";
    public static final String Basic_Sql_Session = "basicSqlSession";
    public static final String Basic_Transaction = "basicTransaction";
    public static final String Basic_Sql_Session_Template = "basicSqlSessionTemplate";

    // ============================================
    // Service
    // ============================================
    public static final String MEMBER_SIGNUP_SERVICE = "memberSignupService";

    // ============================================
    // Mapper
    // ============================================
    public static final String MEMBER_SIGNUP_MAPPER = "memberSignupMapper";

}
