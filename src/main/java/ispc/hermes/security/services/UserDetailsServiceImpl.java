package ispc.hermes.security.services;

import ispc.hermes.model.User;
import ispc.hermes.repositoriy.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: "+username));
        return UserDetailsImpl.build(user);
    }

//    @Transactional
//    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
//        User user = userRepository.findByUserEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: "+email));
//        return UserDetailsImpl.build(user);
//    }
}
