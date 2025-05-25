package life.eter.msUser.service;

import life.eter.msUser.dto.UserLoginResponse;
import life.eter.msUser.dto.UserResponse;
import life.eter.msUser.model.User;
import life.eter.msUser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager authenticationManager;




    public synchronized UserResponse registerUser(User user) {
        if(userRepository.findByEmail(user.getEmail()) == null) {
            String encodePassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodePassword);

            User savedUser = userRepository.save(user);
            return new UserResponse(savedUser.getId(), savedUser.getEmail(), savedUser.getName(), savedUser.getPassword());
        }
        return null;
        //throw new CustomException("Esse email já esta em uso!", HttpStatus.CONFLICT);
    }


    public UserLoginResponse generetateToken(String email, String password) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(email, password);
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return new UserLoginResponse(token);
    }


    public User getUserByToken(String token) {
        String email = tokenService.validateToken(token.replace("Bearer ", ""));

        UserDetails userDetails = userRepository.findByEmail(email);

        if (userDetails instanceof User) {
            System.out.println(((User) userDetails).getEmail() + ((User) userDetails).getName());
            return (User) userDetails;
        }

        throw new RuntimeException("Tipo de usuário inesperado para o email: " + email);
    }

}
