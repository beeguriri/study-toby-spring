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

    public void jdbcContextWithStatementStrategy(StatementStrategy st) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dataSource.getConnection();
            ps = st.makePreparedStatement(conn); //주입 된 전략에 따라 실행
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
}
