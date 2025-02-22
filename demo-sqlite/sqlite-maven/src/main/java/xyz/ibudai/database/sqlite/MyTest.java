package xyz.ibudai.database.sqlite;

import org.junit.Test;
import xyz.ibudai.database.sqlite.dao.Impl.UserDaoImpl;
import xyz.ibudai.database.sqlite.dao.UserDao;
import xyz.ibudai.database.sqlite.enums.Database;
import xyz.ibudai.database.sqlite.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class MyTest {

    @Test
    public void demo1() {
        try (UserDao dao = new UserDaoImpl()) {
            dao.createIfTableInvalid(Database.TEST_DB);

            User user = new User();
            user.setUsername("Alex");
            user.setCreateTime(LocalDateTime.now());

            int ok = dao.save(user);
            System.out.println("save : " + ok);

            List<User> users = dao.listAll();
            System.out.println("listAll : " + users);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
