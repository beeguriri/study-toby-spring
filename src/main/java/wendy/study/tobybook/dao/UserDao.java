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
    private JdbcContext jdbcContext;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setJdbcContext(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public void add(User user) throws SQLException {
        this.jdbcContext.executeSql(
                "insert into users(id, name, password) values(?,?,?)",
                user.getId(), user.getName(), user.getPassword());
    }

    public void deleteAll() throws SQLException {
        this.jdbcContext.executeSql("delete from users");
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
