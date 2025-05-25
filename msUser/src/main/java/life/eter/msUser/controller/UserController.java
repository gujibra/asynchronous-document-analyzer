package life.eter.msUser.controller;


import life.eter.msUser.dto.LoginRequest;
import life.eter.msUser.dto.RegisterRequest;
import life.eter.msUser.dto.UserLoginResponse;
import life.eter.msUser.dto.UserResponse;
import life.eter.msUser.model.User;
import life.eter.msUser.service.UserService;
import life.eter.msUser.util.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/ping")
    public ResponseEntity<String> teste(){
        return ResponseEntity.ok("pong");
    }

    @PostMapping("/register")
   public ResponseEntity<UserLoginResponse> register(@RequestBody RegisterRequest registerRequest){
        User user = registerRequest.toModel();
        UserResponse userResponse = userService.registerUser(user);
        if(userResponse == null){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new UserLoginResponse("Esse email já está em uso!"));
        }
        var response = userService.generetateToken(registerRequest.email(), registerRequest.password());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody LoginRequest loginRequest) {

        try {
            System.out.println("Logging in user: " + loginRequest.email() + " with password: " + loginRequest.password());
            var response = userService.generetateToken(loginRequest.email(), loginRequest.password());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            throw new CustomException("Email ou senha inválidos", HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("/sendDoc")
    public ResponseEntity<String> sendDoc(@RequestParam("document") MultipartFile file,
                                          @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        User user = userService.getUserByToken(authHeader);
        return ResponseEntity.ok("Documento enviado com sucesso para o usuário: " + user.getEmail() +
                " com nome: " + user.getName());

    }
}
