package maxcompany.realcloudapp.service;

import maxcompany.realcloudapp.domain.User;
import maxcompany.realcloudapp.exceptions.UsernameAlreadyExistsException;
import maxcompany.realcloudapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser){

        try {
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            newUser.setUsername(newUser.getUsername());
            newUser.setConfirmPassword("");

            return userRepository.save(newUser);
        }catch (Exception ex){
            throw new UsernameAlreadyExistsException("User with username: "+newUser.getUsername().toUpperCase()+" already exists");
        }
    }
}
