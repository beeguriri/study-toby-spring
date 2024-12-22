package wendy.study.tobybook.service;

import org.springframework.stereotype.Service;
import wendy.study.tobybook.domain.User;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);
}
