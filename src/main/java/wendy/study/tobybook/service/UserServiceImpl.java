package wendy.study.tobybook.service;

import lombok.RequiredArgsConstructor;
import wendy.study.tobybook.constant.Level;
import wendy.study.tobybook.dao.UserDao;
import wendy.study.tobybook.domain.User;

import java.util.List;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserLevelUpgradePolicy {

    private final UserDao userDao;
    public static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_COUNT_FOR_GOLD = 30;

    @Override
    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for(User user : users)
            if(canUpgradeLevel(user)) upgradeLevel(user);
    }

    @Override
    public void addUser(User user) {
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }

    @Override
    public boolean canUpgradeLevel(User user) {
        Level current = user.getLevel();
        return switch (current) {
            case BASIC -> user.getLogin() >= MIN_LOGIN_COUNT_FOR_SILVER;
            case SILVER -> user.getRecommend() >= MIN_RECOMMEND_COUNT_FOR_GOLD;
            case GOLD -> false;
            default -> throw new IllegalArgumentException("unknown level : " + current);
        };
    }

    @Override
    public void upgradeLevel(User user) {
        user.setLevel(user.getLevel().getNext());
        userDao.update(user);
    }
}
