package egovframework.config;

import egovframework.common.constants.BeanNames;
import org.junit.jupiter.api.*;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DB 연결 테스트
 *
 * <p>DataSource가 실제 데이터베이스에 정상적으로 연결되는지 확인합니다.</p>
 *
 * <h2>테스트 항목</h2>
 * <ul>
 *   <li>Basic DataSource JDBC 연결</li>
 *   <li>Basic SqlSessionTemplate 쿼리 실행</li>
 * </ul>
 *
 * @see BeanNames
 */
@SpringBootTest
@DisplayName("DB 연결 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DatabaseConnectionTest {

    @Autowired
    @Qualifier(BeanNames.BASIC_DATASOURCE)
    private DataSource basicDataSource;

    @Autowired
    @Qualifier(BeanNames.Basic_Sql_Session_Template)
    private SqlSessionTemplate basicSqlSessionTemplate;

    @Test
    @Order(1)
    @DisplayName("Basic DataSource DB 연결 테스트")
    void basicDataSourceConnectionTest() throws Exception {
        System.out.println("========== Basic DataSource DB 연결 테스트 ==========");

        try (Connection conn = basicDataSource.getConnection()) {
            assertThat(conn).isNotNull();
            assertThat(conn.isClosed()).isFalse();

            System.out.println("Connection: " + conn);
            System.out.println("Catalog: " + conn.getCatalog());
            System.out.println("Schema: " + conn.getSchema());
            System.out.println("AutoCommit: " + conn.getAutoCommit());

            // Oracle 연결 테스트 쿼리
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT 1 FROM DUAL")) {
                assertThat(rs.next()).isTrue();
                int result = rs.getInt(1);
                assertThat(result).isEqualTo(1);
                System.out.println("SELECT 1 FROM DUAL 결과: " + result);
            }

            System.out.println("결과: 성공");
        }
    }

    @Test
    @Order(2)
    @DisplayName("Basic SqlSessionTemplate으로 쿼리 실행 테스트")
    void basicSqlSessionTemplateQueryTest() {
        System.out.println("========== Basic SqlSessionTemplate 쿼리 테스트 ==========");

        Integer result = basicSqlSessionTemplate.selectOne(
                "egovframework.mapper.member.MemberSignupMapper.testConnection"
        );

        System.out.println("쿼리 결과: " + result);
        assertThat(result).isEqualTo(1);
        System.out.println("결과: 성공");
    }
}
