package egovframework.common.constants;

/**
 * Spring Bean ?대? ?? ?대??
 * 鍮 ?대?? ??肄?⑺吏 ?怨 ??濡 愿由ы???ㅽ 諛⑹? 諛 由ы⑺留 ?⑹?
 *
 * ?ъ???:
 * - @Component(BeanNames.MEMBER_SERVICE)
 * - @Resource(name = BeanNames.MEMBER_SERVICE)
 * - @Qualifier(BeanNames.MEMBER_SERVICE)
 */
public final class BeanNames {

    private BeanNames() {
        // ?몄ㅽ댁ㅽ 諛⑹?
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
