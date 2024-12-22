package wendy.study.tobybook.domain;

import lombok.Data;
import wendy.study.tobybook.constant.Level;

@Data
public class User {
    private String id;
    private String name;
    private String password;
    private Level level;
    private int login;
    private int recommend;
}
