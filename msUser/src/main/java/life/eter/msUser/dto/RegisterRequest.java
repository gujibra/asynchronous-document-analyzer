package life.eter.msUser.dto;

import life.eter.msUser.model.User;

public record RegisterRequest(String name, String email, String password) {
    public User toModel() {
        return new User(email,name, password);
    }
}
