package wendy.study.tobybook.dao;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import lombok.NoArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.PathVariable;
import wendy.study.tobybook.constant.Level;
import wendy.study.tobybook.domain.User;
import wendy.study.tobybook.exception.DuplicateUserIdException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@NoArgsConstructor
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(User user) {
        try {
            this.jdbcTemplate.update("insert into users(id, name, password, level, login, recommend) values(?,?,?,?,?,?)",
                    user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
        } catch (DuplicateKeyException dke) {
            throw new DuplicateUserIdException(dke);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }

    //queryForObject -> 1row만 반환
    // 쿼리가 결과를 반환하지 않으면 EmptyResultDataAccessException
    // 쿼리가 2개 이상의 row를 반환하면 IncorrectResultSizeDataAccessException
    public User get(String id) {
        String query = "select * from users where id = ?";
        return this.jdbcTemplate.queryForObject(query, userRowMapper(), id);
    }

    //query -> list 반환
    // 결과가 없으면 빈 리스트를 반환
    public List<User> getAll() {
        String query = "select * from users";
        return this.jdbcTemplate.query(query, userRowMapper());
    }

    //공통기능 추출
    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setLevel(Level.valueOf(rs.getInt("level")));
            user.setLogin(rs.getInt("login"));
            user.setRecommend(rs.getInt("recommend"));
            return user;
        };
    }

    public int getCount() {
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }
}
