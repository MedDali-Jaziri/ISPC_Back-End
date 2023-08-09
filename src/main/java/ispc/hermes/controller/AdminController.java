package ispc.hermes.controller;

import ispc.hermes.payload.request.POST.Admin.ActivateNewCategoryRequest;
import ispc.hermes.payload.request.POST.Admin.ActivateNewInterestsRequest;
import ispc.hermes.payload.request.POST.Admin.LoginAdminRequest;
import ispc.hermes.payload.request.POST.Tourist.SignupRequest;
import ispc.hermes.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/addExpert")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addExpert(@Valid @RequestBody SignupRequest signupRequest){
        return this.adminService.addExpertService(signupRequest);
    }

    @PostMapping("/loginAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> loginAdmin(@Valid @RequestBody LoginAdminRequest loginAdminRequest){
        return this.adminService.loginAdminService(loginAdminRequest);
    }

    @PostMapping("/activateNewCategory")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activateNewCategory(@Valid @RequestBody ActivateNewCategoryRequest activateNewCategoryRequest, HttpServletRequest request){
        return this.adminService.activateNewCategoryService(activateNewCategoryRequest, request);
    }

    @PostMapping("/activateNewInterests")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activateNewInterests(@Valid @RequestBody ActivateNewInterestsRequest activateNewInterestsRequest, HttpServletRequest request){
        return this.adminService.activateNewInterestsService(activateNewInterestsRequest, request);
    }
}
