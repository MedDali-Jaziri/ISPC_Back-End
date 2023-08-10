package ispc.hermes.service;

import ispc.hermes.model.*;
import ispc.hermes.payload.request.POST.Admin.ActivateNewCategoryRequest;
import ispc.hermes.payload.request.POST.Admin.ActivateNewInterestsRequest;
import ispc.hermes.payload.request.POST.Admin.LoginAdminRequest;
import ispc.hermes.payload.request.POST.Tourist.SignupRequest;
import ispc.hermes.payload.response.ErrorMessage;
import ispc.hermes.payload.response.MessageResponse;
import ispc.hermes.payload.response.UserInfoResponse;
import ispc.hermes.repositoriy.CategoryRepository;
import ispc.hermes.repositoriy.InterestRepository;
import ispc.hermes.repositoriy.RoleRepository;
import ispc.hermes.repositoriy.UserRepository;
import ispc.hermes.security.JWT.JwtUtils;
import ispc.hermes.security.services.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseEntity<?> registerAdminService(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername()) && !signUpRequest.getUsername().equals("Alberto bucciero")) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken or you are not the admin!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail()) && !signUpRequest.getEmail().equals("alberto.bucciero@gmail.com")) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use or you are not the admin!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
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

                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setIsActive(true);
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Admin registered successfully!"));
    }

    public ResponseEntity<?> loginAdminService(LoginAdminRequest loginAdminRequest){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginAdminRequest.getUsername(), loginAdminRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles));
    }

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
            Role userRole = roleRepository.findByName(ERole.ROLE_EXPERT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "expert":
                        Role modRole = roleRepository.findByName(ERole.ROLE_EXPERT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_EXPERT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        user.setIsActive(true);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("Expert registered successfully!"));
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
