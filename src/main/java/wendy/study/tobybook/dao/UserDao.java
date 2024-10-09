package wendy.study.tobybook.dao;

import lombok.Data;
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

        Connection conn = this.dataSource.getConnection();

        String insertQuery = "insert into users(id, name, password) values(?,?,?)";
        PreparedStatement ps = conn.prepareStatement(insertQuery);
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        conn.close();
    }

    public User get(String id) throws SQLException {

        Connection conn = this.dataSource.getConnection();

        String selectQuery = "select * from users where id = ?";

        PreparedStatement ps = conn.prepareStatement(selectQuery);
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        conn.close();

        return user;
    }

    public void deleteAll() throws SQLException {
        Connection conn = dataSource.getConnection();

        String deleteQuery = "delete from users";
        PreparedStatement ps = conn.prepareStatement(deleteQuery);
        ps.executeUpdate();

        ps.close();
        conn.close();
    }

    public int getCount() throws SQLException  {
        Connection conn = dataSource.getConnection();

        String countQuery = "select count(*) from users";
        PreparedStatement ps = conn.prepareStatement(countQuery);

        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        rs.close();
        ps.close();
        conn.close();

        return count;
    }
}
