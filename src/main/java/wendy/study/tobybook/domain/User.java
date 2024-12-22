package wendy.study.tobybook.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wendy.study.tobybook.constant.Level;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String name;
    private String password;
    private Level level;
    private int login;
    private int recommend;
}
