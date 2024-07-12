package ch.clip.security6.simple.taskmanager.security;


import com.example.multiuserapplication.domain.TasksUser;
import com.example.multiuserapplication.domain.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TasksUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public TasksUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       final TasksUser tasksUser = userRepository.findOneByUsername(username).get();
       if (tasksUser == null) {
           throw new UsernameNotFoundException(username);
       }
       UserDetails user = User
               .withUsername(tasksUser.getUsername())
               .password(tasksUser.getPassword())
               .roles(tasksUser.getRole())
//               .authorities(cinemaUser.getAuthorities())
               .build();

        return user;
    }
}
