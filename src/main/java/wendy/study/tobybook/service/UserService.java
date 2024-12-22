package wendy.study.tobybook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wendy.study.tobybook.constant.Level;
import wendy.study.tobybook.dao.UserDao;
import wendy.study.tobybook.domain.User;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for(User user : users) {
            boolean changed = false;

            if(user.getLevel() == Level.GOLD) continue;

            if(user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
                user.setLevel(Level.SILVER);
                changed = true;
            } else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
                user.setLevel(Level.GOLD);
                changed = true;
            }

            if(changed) userDao.update(user);
        }
    }

    public void addUser(User user) {
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}
