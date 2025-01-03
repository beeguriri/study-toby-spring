package wendy.study.tobybook.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import wendy.study.tobybook.constant.Level;
import wendy.study.tobybook.dao.UserDao;
import wendy.study.tobybook.domain.User;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static wendy.study.tobybook.service.UserLevelUpgradePolicyImpl.MIN_LOGIN_COUNT_FOR_SILVER;
import static wendy.study.tobybook.service.UserLevelUpgradePolicyImpl.MIN_RECOMMEND_COUNT_FOR_GOLD;

class UserServiceTest {

    UserDao userDao;
    UserLevelUpgradePolicy userLevelUpgradePolicy;
    UserService userService;
    PlatformTransactionManager platformTransactionManager;

    @BeforeEach
    public void setUp() {

        //테스트를 위한 DI
        userDao = new UserDao();
        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost/testdb?characterEncoding=UTF-8",
                "root", "1234",
                true);
        userDao.setDataSource(dataSource);
        userLevelUpgradePolicy = new UserLevelUpgradePolicyImpl(userDao);

        //트랜잭션매니저 설정
        platformTransactionManager = new DataSourceTransactionManager(dataSource);

        userService = new UserServiceImpl(userDao, userLevelUpgradePolicy, platformTransactionManager);

        userDao.deleteAll();
        assertEquals(userDao.getCount(), 0);

        //테스트를 위한 데이터 추가
        List<User> users = Arrays.asList(
                new User("aaa", "fff", "pw1234", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER-1, 0),
                new User("bbb", "ggg", "pw1234", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER, 0),
                new User("ccc", "hhh", "pw1234", Level.SILVER, 60, MIN_RECOMMEND_COUNT_FOR_GOLD-1),
                new User("ddd", "iii", "pw1234", Level.SILVER, 60, MIN_RECOMMEND_COUNT_FOR_GOLD),
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