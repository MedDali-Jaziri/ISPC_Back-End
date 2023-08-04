package ispc.hermes.controller;

import ispc.hermes.payload.request.LoginRequest;
import ispc.hermes.payload.request.SignupRequest;
import ispc.hermes.service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return this.userService.authenticateUserService(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return this.userService.registerUserService(signUpRequest);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        return this.userService.logoutUserService();
    }

    @PostMapping("/activationLink")
    public ResponseEntity<?> activationLink(@RequestParam String email){
        return  this.userService.activationLink(email);
    }
}