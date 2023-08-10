package ispc.hermes.service;

import ispc.hermes.model.Category;
import ispc.hermes.model.Interest;
import ispc.hermes.model.User;
import ispc.hermes.payload.request.POST.Expert.AddNewCategoryRequest;
import ispc.hermes.payload.request.POST.Expert.AddNewInterestsRequest;
import ispc.hermes.payload.request.POST.Tourist.LoginRequest;
import ispc.hermes.payload.response.UserInfoResponse;
import ispc.hermes.repositoriy.CategoryRepository;
import ispc.hermes.repositoriy.InterestRepository;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpertService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    public ResponseEntity<UserInfoResponse> loginExpertService(LoginRequest loginRequest){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

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

    public ResponseEntity<?> addNewCategoryService(AddNewCategoryRequest addNewCategoryRequest, HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            Optional<Category> category = this.categoryRepository.findByNameCategory(addNewCategoryRequest.getNameCategory());
            if (category.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("You cannot add the same category ");
            }
            else {
                Category newCategory = new Category();
                newCategory.setNameCategory(addNewCategoryRequest.getNameCategory());
                newCategory.setActivationCategory(false);
                newCategory.setUser(user.get());

                this.categoryRepository.save(newCategory);
                return ResponseEntity.status(HttpStatus.OK)
                        .body("The "+addNewCategoryRequest.getNameCategory()+" is added with success !!");
            }
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function addNewCategory!! "+exception);
        }
    }

    public ResponseEntity<?> getListOfCategoriesNotActivateService(){
        try {
            List<Category> categories = this.categoryRepository.findAll();
            List<Category> categoryList = new ArrayList<>();
            for (Category category: categories){
                if(!category.getActivationCategory()){
                    categoryList.add(category);
                }
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(categoryList);
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function getListOfCategoriesNotActivate!!");
        }
    }

    public ResponseEntity<?> getListOfCategoriesActivateService(){
        try {
            List<Category> categories = this.categoryRepository.findAll();
            List<Category> categoryList = new ArrayList<>();
            for (Category category: categories){
                if(category.getActivationCategory()){
                    categoryList.add(category);
                }
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(categoryList);
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function getListOfCategoriesNotActivate!!");
        }
    }

    public ResponseEntity<?> addNewInterestsService(AddNewInterestsRequest addNewInterestsRequest, HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            Optional<Interest> interest = this.interestRepository.findByNameInterst(addNewInterestsRequest.getNameInterest());
            if (interest.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("You cannot add the same interest ");
            }
            else {
                Interest newInterest = new Interest();
                newInterest.setNameInterst(addNewInterestsRequest.getNameInterest());
                newInterest.setActivationInterst(false);
                newInterest.setUser(user.get());
                this.interestRepository.save(newInterest);
                return ResponseEntity.status(HttpStatus.OK)
                        .body("The "+addNewInterestsRequest.getNameInterest()+" is added with success !!");
            }
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function addNewCategory!!"+exception);
        }
    }

    public ResponseEntity<?> getListOfInterestsNotActivateService(){
        try {
            List<Interest> interests = this.interestRepository.findAll();
            List<Interest> interestList = new ArrayList<>();
            for (Interest interest: interests){
                if(!interest.getActivationInterst()){
                    interestList.add(interest);
                }
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(interestList);
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function getListOfCategoriesNotActivate!!");
        }
    }

    public ResponseEntity<?> getListOfInterestsActivateService(){
        try {
            List<Interest> interests = this.interestRepository.findAll();
            List<Interest> interestList = new ArrayList<>();
            for (Interest interest: interests){
                if(interest.getActivationInterst()){
                    interestList.add(interest);
                }
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(interestList);
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function getListOfCategoriesNotActivate!!");
        }
    }
}
