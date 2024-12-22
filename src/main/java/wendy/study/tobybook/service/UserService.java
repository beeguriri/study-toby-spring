package wendy.study.tobybook.service;

import org.springframework.stereotype.Service;
import wendy.study.tobybook.domain.User;

@Service
public interface UserService {
    void upgradeLevels();
    void addUser(User user);
}
