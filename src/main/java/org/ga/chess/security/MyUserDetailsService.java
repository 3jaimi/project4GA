package org.ga.chess.security;



import lombok.Setter;
import org.ga.chess.model.User;
import org.ga.chess.repository.IPlayerRepository;
import org.ga.chess.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service

public class MyUserDetailsService<T> implements UserDetailsService {
    private UserService userService;

    @Setter
    @Autowired
    private IPlayerRepository playerRepository;

    @Autowired
    public MyUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new MyUserDetails((User)userService.getUser(email).getBody(),playerRepository);
    }
}
