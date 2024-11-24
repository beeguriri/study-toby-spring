package wendy.study.tobybook.dao;

import lombok.NoArgsConstructor;
import wendy.study.tobybook.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
public class UserDao {

    private DataSource dataSource;

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

    public void add(User user) throws SQLException {
        StatementStrategy statementStrategy = new AddStatement(user); //전략 클래스의 오브젝트 생성
        jdbcContextWithStatementStrategy(statementStrategy); //컨텍스트 호출, 전략오브젝트 전달
    }

    public void deleteAll() throws SQLException {
        StatementStrategy statementStrategy = new DeleteAllStatement(); //전략 클래스의 오브젝트 생성
        jdbcContextWithStatementStrategy(statementStrategy); //컨텍스트 호출, 전략오브젝트 전달
    }

    public User get(String id) throws SQLException {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = new User();

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement("select * from users where id = ?"); //변하는 부분
            ps.setString(1, id);
            rs = ps.executeQuery();
            rs.next();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));

        } catch (SQLException e) {
            throw e;
        } finally {
            if(rs!=null) {
                try {
                    rs.close();
                } catch (SQLException e) {}
            }
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
        return user;
    }

    public int getCount() throws SQLException  {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement("select count(*) from users"); //변하는 부분
            rs = ps.executeQuery();
            rs.next();
            count = rs.getInt(1);
        } catch (SQLException e) {
            throw  e;
        } finally {
            if(rs!=null) {
                try {
                    rs.close();
                } catch (SQLException e) {}
            }
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

        return count;
    }
}
