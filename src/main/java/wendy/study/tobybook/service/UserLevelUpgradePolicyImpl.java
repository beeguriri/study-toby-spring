package wendy.study.tobybook.service;

import lombok.RequiredArgsConstructor;
import wendy.study.tobybook.constant.Level;
import wendy.study.tobybook.dao.UserDao;
import wendy.study.tobybook.domain.User;

@RequiredArgsConstructor
public class UserLevelUpgradePolicyImpl implements UserLevelUpgradePolicy{

    public static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_COUNT_FOR_GOLD = 30;
    private final UserDao userDao;

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
