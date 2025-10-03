package nesalmanov.ru.auth_service.service.impl;


import nesalmanov.ru.auth_service.model.entity.User;
import nesalmanov.ru.auth_service.model.impl.MyUserDetails;
import nesalmanov.ru.auth_service.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);

        if (user != null) {
            return new MyUserDetails(user);
        }

        throw new UsernameNotFoundException("User is not found");
    }
}
