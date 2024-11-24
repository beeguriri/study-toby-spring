package wendy.study.tobybook.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcContext {
    private DataSource dataSource;

    // DataSource 타입 빈을 DI 받을수 있게
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy st, String... params) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dataSource.getConnection();
            ps = st.makePreparedStatement(conn); //주입 된 전략에 따라 실행
            for(int i=0; i<params.length; i++) {
                ps.setString(i+1, params[i]);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if(ps!=null) {
                try {
                    ps.close();
                } catch (SQLException e) {}
            }
            if(conn!=null) {
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
        }
    }

    public void executeSql(String query, String... params) throws SQLException {
        jdbcContextWithStatementStrategy(conn -> conn.prepareStatement(query), params);
    }
}
