package life.eter.msUser.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/ping")
    public ResponseEntity<String> teste(){
        return ResponseEntity.ok("pong");
    }
}
