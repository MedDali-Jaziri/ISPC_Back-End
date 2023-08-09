package ispc.hermes.service;

import ispc.hermes.model.*;
import ispc.hermes.payload.request.POST.Admin.ActivateNewCategoryRequest;
import ispc.hermes.payload.request.POST.Admin.ActivateNewInterestsRequest;
import ispc.hermes.payload.request.POST.Admin.LoginAdminRequest;
import ispc.hermes.payload.request.POST.Tourist.SignupRequest;
import ispc.hermes.payload.response.MessageResponse;
import ispc.hermes.payload.response.UserInfoResponse;
import ispc.hermes.repositoriy.CategoryRepository;
import ispc.hermes.repositoriy.InterestRepository;
import ispc.hermes.repositoriy.RoleRepository;
import ispc.hermes.repositoriy.UserRepository;
import ispc.hermes.security.JWT.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    public ResponseEntity<?> addExpertService(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_TOURIST)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "expert":
                        Role modRole = roleRepository.findByName(ERole.ROLE_EXPERT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_TOURIST)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        user.setIsActive(true);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    public ResponseEntity<?> loginAdminService(LoginAdminRequest loginAdminRequest){
        try {
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtUtils.generateTokenFromUsername(loginAdminRequest.getUsername()))
                    .body(new UserInfoResponse(0L,
                            loginAdminRequest.getUsername(),
                            loginAdminRequest.getEmail()
                    ));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function loginUserSocialMediaService !!");
        }
    }

    public ResponseEntity<?> activateNewCategoryService(ActivateNewCategoryRequest activateNewCategoryRequest, HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<Category> category = this.categoryRepository.findByNameCategory(activateNewCategoryRequest.getNameCategory());
            category.get().setActivationCategory(true);
            this.categoryRepository.save(category.get());
            return ResponseEntity.status(HttpStatus.OK)
                    .body("The "+activateNewCategoryRequest.getNameCategory()+" is activated with success !!");
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function activateNewCategoryService !!");
        }
    }

    public ResponseEntity<?> activateNewInterestsService(ActivateNewInterestsRequest activateNewInterestsRequest, HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<Interest> interest = this.interestRepository.findByNameInterst(activateNewInterestsRequest.getNameInterest());
            interest.get().setActivationInterst(true);
            this.interestRepository.save(interest.get());
            return ResponseEntity.status(HttpStatus.OK)
                    .body("The "+activateNewInterestsRequest.getNameInterest()+" is activated with success !!");
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function activateNewCategoryService !!");
        }
    }
}
