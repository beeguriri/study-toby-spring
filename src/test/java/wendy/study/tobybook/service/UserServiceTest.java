package wendy.study.tobybook.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import wendy.study.tobybook.constant.Level;
import wendy.study.tobybook.dao.UserDao;
import wendy.study.tobybook.domain.User;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    UserDao userDao;
    UserService userService;

    @BeforeEach
    public void setUp() {

        //테스트를 위한 DI
        userDao = new UserDao();
        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost/testdb?characterEncoding=UTF-8",
                "root", "1234",
                true);
        userDao.setDataSource(dataSource);
        userService = new UserService(userDao);

        userDao.deleteAll();
        assertEquals(userDao.getCount(), 0);

        //테스트를 위한 데이터 추가
        List<User> users = Arrays.asList(
                new User("aaa", "fff", "pw1234", Level.BASIC, 49, 0),
                new User("bbb", "ggg", "pw1234", Level.BASIC, 50, 0),
                new User("ccc", "hhh", "pw1234", Level.SILVER, 60, 29),
                new User("ddd", "iii", "pw1234", Level.SILVER, 60, 30),
                new User("eee", "jjj", "pw1234", Level.GOLD, 100, 100)
        );

        for(User user : users) userDao.add(user);


    }

    @Test
    @DisplayName("레벨 업그레이드 확인")
    public void upgradeLevels() {

        userService.upgradeLevels();

        List<User> getUsers = userDao.getAll();

        checkLevel(getUsers.get(0), Level.BASIC);
        checkLevel(getUsers.get(1), Level.SILVER);
        checkLevel(getUsers.get(2), Level.SILVER);
        checkLevel(getUsers.get(3), Level.GOLD);
        checkLevel(getUsers.get(4), Level.GOLD);

    }

    private void checkLevel(User user, Level expectedLevel) {
        User updatedUser = userDao.get(user.getId());
        assertEquals(updatedUser.getLevel(), expectedLevel);
    }
}