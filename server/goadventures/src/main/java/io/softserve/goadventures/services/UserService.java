package io.softserve.goadventures.services;

import io.softserve.goadventures.dto.UserAuthDto;
import io.softserve.goadventures.enums.UserStatus;
import io.softserve.goadventures.errors.UserNotFoundException;
import io.softserve.goadventures.models.User;
import io.softserve.goadventures.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //TODO this method is never used and should be removed. In a case if you need it you can autowire the necessary repository class in a service class.
    public User getUserById(int id){
        return userRepository.findUserById(id);
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        if(userRepository.existsByEmail(email)){
            return userRepository.findByEmail(email);
        } else {
            throw new UserNotFoundException("User not found"); //TODO there is no need in this exception. You can simply return null
        }
    }

    public User confirmUser(String email) {
        User user = userRepository.findByEmail(email);
        user.setStatusId(UserStatus.ACTIVE.getUserStatus());
        userRepository.save(user);

        return user;
    }

    public User addUser(UserAuthDto userAuthDto) {
        User user = new User();
        BeanUtils.copyProperties(userAuthDto, user);

        if (checkingEmail(user.getEmail())) {
            user.setStatusId(UserStatus.PENDING.getUserStatus());
            user.setUsername(user.getEmail().split("@")[0]);
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            userRepository.save(user);
        }
        return user;
    }

    public String singIn(String email, String password) {
        if (checkingEmail(email)) {
            return "User not found";
        } else {
            User user = userRepository.findByEmail(email);

            if (BCrypt.checkpw(password, user.getPassword())) {
                if (user.getStatusId() == UserStatus.PENDING.getUserStatus()) {
                    return "User is not confirm auth!"; //TODO bad idea with such statuses! enums!
                }
                if (user.getStatusId() == UserStatus.BANNED.getUserStatus()) {
                    return "User is banned";
                }
                if (user.getStatusId() == UserStatus.DELETED.getUserStatus()) {
                    return "User is deleted";
                }
                user.setStatusId(UserStatus.ACTIVE.getUserStatus());
                userRepository.save(user);

                return "User log in";
            } else {
                return "User password is wrong";
            }
        }
    }

    public void singOut(String email) {
        User user = userRepository.findByEmail(email);
        user.setStatusId(UserStatus.UNACTIVE.getUserStatus());
        userRepository.save(user);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean checkingEmail(String email){
        return userRepository.findByEmail(email) == null;
    }
}
