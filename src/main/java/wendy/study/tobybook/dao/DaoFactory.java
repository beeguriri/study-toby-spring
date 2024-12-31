package wendy.study.tobybook.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import wendy.study.tobybook.service.UserService;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {

    // db와 연결을 하는 책임을 가진 bean
    // DB 연결 기술
    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/springbook?characterEncoding=UTF-8");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");
        return dataSource;
    }

    //userdao가 어떤 db와 연결을 할 지 결정을 하는 bean
    // 데이터 엑세스 기술
    @Bean
    public UserDao userDao() {
        UserDao userDao = new UserDao();
        userDao.setDataSource(dataSource());
        return userDao;
    }

    //트랜잭션 매니저 분리
    // 트랜잭션 기술
    @Bean
    public PlatformTransactionManager transactionManager() {
        // 다른 DB 기술을 사용할 때는 여기에 맞는 구현체를 선택해주면 된다.
        // PlatformTransactionManager transactionManager = new JtaTransactionManager();
        return new DataSourceTransactionManager(dataSource());
    }

}
