package ispc.hermes.controller;

import ispc.hermes.payload.request.GET.GetAllPoIInEachTripsUsingAdminAccountRequest;
import ispc.hermes.payload.request.GET.GetSpecificOfPoIsNotPublishedRequest;
import ispc.hermes.payload.request.POST.Tourist.*;
import ispc.hermes.payload.request.PUT.ModifyNameTripRequest;
import ispc.hermes.payload.request.PUT.PutStateOfTripsRequest;
import ispc.hermes.service.TouristService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tourist")
public class TouristController {
    @Autowired
    private TouristService touristService;


    @GetMapping("/getSpecificUser")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<?> getSpecificUser(HttpServletRequest request) {
        return this.touristService.getSpecificUserService(request);
    }
    @PostMapping("/addNewUserInterest")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<?> addNewUserInterest(@Valid @RequestBody UserInterestRequest userInterestRequest, HttpServletRequest request){
        return this.touristService.addNewUserInterestService(userInterestRequest, request);
    }

    @PostMapping("/addNewPoI")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<?> addNewPoI(@Valid @RequestBody AddPoIRequest addPoIRequest , HttpServletRequest request){
        return this.touristService.addNewPoIService(addPoIRequest, request);
    }

    @PostMapping("/addNewCategoryInterest")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<?> addNewCategoryInterest(AddNewCategoryInterstRequest addNewCategoryInterst, HttpServletRequest request){
        return this.touristService.addNewCategoryInterestService(addNewCategoryInterst, request);
    }

    @GetMapping("/getListOfPoIsNotPublished")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<?> getListOfPoIsNotPublished(HttpServletRequest request){
        return this.touristService.getListOfPoIsNotPublishedService(request);
    }

    @GetMapping("/getListOfPoIsPublished")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<?> getListOfPoIsPublished(HttpServletRequest request){
        return this.touristService.getListOfPoIsPublishedService(request);
    }

    @PostMapping("/getSpecificOfPoIsNotPublished")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<?> getSpecificOfPoIsNotPublished(@Valid @RequestBody GetSpecificOfPoIsNotPublishedRequest getSpecificOfPoIsNotPublishedRequest , HttpServletRequest request){
        return this.touristService.getSpecificOfPoIsNotPublishedService(getSpecificOfPoIsNotPublishedRequest, request);
    }

    @PostMapping("/addAnTrip")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<?> addAnTrip(@Valid @RequestBody AddNewTripRequest addNewTripRequest, HttpServletRequest request){
        return this.touristService.addAnTripService(addNewTripRequest, request);
    }

    @PostMapping("/addAnPoIToATrip")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<?> addAnPoIToATrip(@Valid @RequestBody AddAnPoIToTripRequest addAnPoIToTripRequest){
        return this.touristService.addAnPoIToATripService(addAnPoIToTripRequest);
    }

    @GetMapping("/getListOfTrips")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<?> getListOfTrips(HttpServletRequest request){
        return this.touristService.getListOfTripsService(request);
    }

    @PostMapping("/getAllPoIInEachTrips")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<?> getAllPoIInEachTrips(@Valid @RequestBody GetSpecificOfPoIsNotPublishedRequest getSpecificOfPoIsNotPublishedRequest, HttpServletRequest request){
        return this.touristService.getAllPoIInEachTripsService(getSpecificOfPoIsNotPublishedRequest, request);
    }

    @PutMapping("/putStateOfTrips")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<?> putStateOfTrips(@Valid @RequestBody PutStateOfTripsRequest putStateOfTripsRequest, HttpServletRequest request){
        return this.touristService.putStateOfTripsService(putStateOfTripsRequest, request);
    }

    @PostMapping("/addPoIToFavoriteTrip")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<?> addPoIToFavoriteTrip(@Valid @RequestBody AddPoIToFavoriteTripRequest addPoIToFavoriteTripRequest, HttpServletRequest request){
        return this.touristService.addPoIToFavoriteTripService(addPoIToFavoriteTripRequest, request);
    }

    @GetMapping("/getAllPoIInFavoriteTrips")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<?> getAllPoIInFavoriteTrips(HttpServletRequest request){
        return this.touristService.getAllPoIInFavoriteTripsService(request);
    }

    @PostMapping("/addUserSocialMedia")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<?> addUserSocialMedia(@Valid @RequestBody AddUserSocialMediaRequest addUserSocialMediaRequest, HttpServletRequest request){
        return this.touristService.addUserSocialMediaService(addUserSocialMediaRequest, request);
    }

    @PostMapping("/loginUserSocialMedia")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<?> loginUserSocialMedia(@Valid @RequestBody LoginUserSocialMediaRequest loginUserSocialMediaRequest){
        return this.touristService.loginUserSocialMediaService(loginUserSocialMediaRequest);
    }

    @GetMapping("/getListPoIsOfTouringClubAddByTheScript")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<?> getListPoIsOfTouringClubAddByTheScript(HttpServletRequest request){
        return this.touristService.getListPoIsOfTouringClubAddByTheScriptService(request);
    }

    @GetMapping("/getListOfTripsUsingAdminAccount")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<?> getListOfTripsUsingAdminAccount(){
        return this.touristService.getListOfTripsUsingAdminAccountService();
    }

    @PostMapping("/getAllPoIInEachTripsUsingAdminAccount")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<?> getAllPoIInEachTripsUsingAdminAccount(@Valid @RequestBody GetAllPoIInEachTripsUsingAdminAccountRequest getAllPoIInEachTripsUsingAdminAccountRequest){
        return this.touristService.getAllPoIInEachTripsUsingAdminAccountService(getAllPoIInEachTripsUsingAdminAccountRequest);
    }

    @PutMapping("/modifyNameTrip")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<?> modifyNameTrip(@Valid @RequestBody ModifyNameTripRequest modifyNameTripRequest){
        return this.touristService.modifyNameTripService(modifyNameTripRequest);
    }
}
