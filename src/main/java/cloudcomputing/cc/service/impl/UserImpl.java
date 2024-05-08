package cloudcomputing.cc.service.impl;

import cloudcomputing.cc.entity.User;
import cloudcomputing.cc.repository.UserRepository;
import cloudcomputing.cc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserImpl implements UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }
}
