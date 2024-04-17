package xyz.ibudai.database.es.repository.Impl;

import org.springframework.stereotype.Service;
import xyz.ibudai.database.es.entity.User;
import xyz.ibudai.database.es.repository.UserRepository;

@Service("userService")
public class UserServiceImpl extends AbstractRepository<User> implements UserRepository {

}
