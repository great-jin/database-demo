package xyz.ibudai.database.sqlite.dao.Impl;

import xyz.ibudai.database.sqlite.annotation.Repository;
import xyz.ibudai.database.sqlite.dao.AbstractDao;
import xyz.ibudai.database.sqlite.dao.UserDao;
import xyz.ibudai.database.sqlite.enums.Database;
import xyz.ibudai.database.sqlite.model.User;

@Repository(Database.TEST_DB)
public class UserDaoImpl extends AbstractDao<User, Long> implements UserDao {

}
