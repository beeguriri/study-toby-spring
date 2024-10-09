package wendy.study.tobybook.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import wendy.study.tobybook.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/*
스프링 API에 의존하지않고 테스트 만들기
단, 여러 오브젝트와 복잡한 의존관계를 갖고 있는 오브젝트를 테스트 할 경우에는
스프링 컨텍스트를 사용 - DI 방식의 테스트를 사용하면 편리하다. (@Autowired)
 */

class UserDaoTest {

    UserDao userDao;

    @BeforeEach
    void setup () throws SQLException {

        //테스트를 위한 db 설정
        userDao = new UserDao();
        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost/testdb?characterEncoding=UTF-8",
                "root", "1234",
                true);
        userDao.setDataSource(dataSource);

        userDao.deleteAll();
        assertEquals(userDao.getCount(), 0);
    }

    @Test
    void add() throws SQLException {
        User user = new User();

        user.setId("id1");
        user.setName("test1");
        user.setPassword("1234");
        userDao.add(user);

        assertEquals(user, userDao.get("id1"));
    }

    @Test
    void add2() throws SQLException {
        User user = new User();

        user.setId("id1");
        user.setName("test1");
        user.setPassword("1234");
        userDao.add(user);

        assertThrows(SQLException.class, () -> {
            userDao.get("id2");
        });
    }

}