package wendy.study.tobybook.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Deprecated
public interface StatementStrategy {
    PreparedStatement makePreparedStatement(Connection conn) throws SQLException;
}
