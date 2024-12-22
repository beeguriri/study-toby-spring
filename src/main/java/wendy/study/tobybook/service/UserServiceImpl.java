package wendy.study.tobybook.service;

import lombok.RequiredArgsConstructor;
import wendy.study.tobybook.constant.Level;
import wendy.study.tobybook.dao.UserDao;
import wendy.study.tobybook.domain.User;

import java.util.List;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserLevelUpgradePolicy userLevelUpgradePolicy;

    @Override
    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for(User user : users)
            if(userLevelUpgradePolicy.canUpgradeLevel(user))
                userLevelUpgradePolicy.upgradeLevel(user);
    }

    @Override
    public void addUser(User user) {
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}
