package ispc.hermes.controller;

import ispc.hermes.payload.request.POST.Expert.AddNewCategoryRequest;
import ispc.hermes.payload.request.POST.Expert.AddNewInterestsRequest;
import ispc.hermes.payload.request.POST.Tourist.LoginRequest;
import ispc.hermes.payload.request.POST.Tourist.SignupRequest;
import ispc.hermes.service.ExpertService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/expert")
public class ExpertController {
    @Autowired
    private ExpertService expertService;

    @PostMapping("/loginExpert")
    @PreAuthorize("hasRole('EXPERT')")
    public ResponseEntity<?> addExpert(@Valid @RequestBody LoginRequest loginRequest){
        return this.expertService.loginExpertService(loginRequest);
    }

    @PostMapping("/addNewCategory")
    @PreAuthorize("hasRole('EXPERT')")
    public ResponseEntity<?> addNewCategory(@Valid @RequestBody AddNewCategoryRequest addNewCategoryRequest){
        return this.expertService.addNewCategoryService(addNewCategoryRequest);
    }

    @GetMapping("/getListOfCategoriesNotActivate")
    @PreAuthorize("hasRole('EXPERT')")
    public ResponseEntity<?> getListOfCategoriesNotActivate(){
        return this.expertService.getListOfCategoriesNotActivateService();
    }

    @GetMapping("/getListOfCategoriesActivate")
    @PreAuthorize("hasRole('EXPERT')")
    public ResponseEntity<?> getListOfCategoriesActivate(){
        return this.expertService.getListOfCategoriesActivateService();
    }

    @PostMapping("/addNewInterests")
    @PreAuthorize("hasRole('EXPERT')")
    public ResponseEntity<?> addNewInterests(@Valid @RequestBody AddNewInterestsRequest addNewInterestsRequest){
        return this.expertService.addNewInterestsService(addNewInterestsRequest);
    }

    @GetMapping("/getListOfInterestsNotActivate")
    @PreAuthorize("hasRole('EXPERT')")
    public ResponseEntity<?> getListOfInterestsNotActivate(){
        return this.expertService.getListOfInterestsNotActivateService();
    }

    @GetMapping("/getListOfInterestsActivate")
    @PreAuthorize("hasRole('EXPERT')")
    public ResponseEntity<?> getListOfInterestsActivate(){
        return this.expertService.getListOfInterestsActivateService();
    }

}
