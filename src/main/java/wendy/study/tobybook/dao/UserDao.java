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

    public void add(User user) throws SQLException {

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement("insert into users(id, name, password) values(?,?,?)");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw  e;
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

    public void deleteAll() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement("delete from users"); //변하는 부분
            ps.executeUpdate();
        } catch (SQLException e) {
            throw  e;
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
