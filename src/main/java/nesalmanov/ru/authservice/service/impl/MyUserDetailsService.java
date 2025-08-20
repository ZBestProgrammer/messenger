package nesalmanov.ru.authservice.service.impl;

import nesalmanov.ru.authservice.model.entity.User;
import nesalmanov.ru.authservice.model.impl.MyUserDetails;
import nesalmanov.ru.authservice.repository.UserRepository;
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
