package xyz.ibudai.repository.Impl;

import org.springframework.stereotype.Service;
import xyz.ibudai.model.User;
import xyz.ibudai.repository.UserRepository;

@Service("userService")
public class UserServiceImpl extends AbstractRepository<User> implements UserRepository {

}
