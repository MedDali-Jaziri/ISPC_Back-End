package ispc.hermes.controller;

import ispc.hermes.payload.request.GET.GetAllPoIInEachTripsRequest;
import ispc.hermes.payload.request.GET.GetAllPoIInEachTripsUsingAdminAccountRequest;
import ispc.hermes.payload.request.GET.GetSpecificOfPoIsNotPublishedRequest;
import ispc.hermes.payload.request.POST.Tourist.*;
import ispc.hermes.payload.request.PUT.ModifyNameTripRequest;
import ispc.hermes.payload.request.PUT.PutStateOfTripsRequest;
import ispc.hermes.service.TouristService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tourist")
public class TouristController {
    @Autowired
    private TouristService touristService;

    @GetMapping("/getSpecificUser")
    @PreAuthorize("hasRole('TOURIST') or hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<?> getSpecificUser(HttpServletRequest request) {
        return this.touristService.getSpecificUserService(request);
    }

    @PostMapping("/addNewPoI")
    @PreAuthorize("hasRole('TOURIST') or hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<?> addNewPoI(@Valid @RequestParam("descriptionPoI") String descriptionPoI,
                                       @Valid @RequestParam("imagePoI") MultipartFile imagePoI,
                                       @Valid @RequestParam("longitudeLocationPoI") Double longitudeLocationPoI,
                                       @Valid @RequestParam("latitudeLocationPoI") Double latitudeLocationPoI,
                                       @Valid @RequestParam("nameLocationPoI") String nameLocationPoI,
                                       @Valid @RequestParam("hashtagsPoI") String hashtagsPoI,
                                       @Valid @RequestParam("audioPoI") MultipartFile audioPoI,
                                       @Valid @RequestParam("isPublishedToFB") boolean isPublishedToFB,
                                       @Valid @RequestParam("isPublishedToInsta") boolean isPublishedToInsta
                            ) {
        return this.touristService.addNewPoIService(descriptionPoI, imagePoI, longitudeLocationPoI, latitudeLocationPoI,
                nameLocationPoI, hashtagsPoI, audioPoI, isPublishedToFB, isPublishedToInsta);
    }

    @GetMapping("/getImage")
    public ResponseEntity<Resource> getImage(@RequestParam String imageName) throws IOException {
        return this.touristService.getImageService(imageName);
    }

    @GetMapping("/getAudio")
    public ResponseEntity<Resource> getAudio(@RequestParam String audioName) throws IOException {
        return this.touristService.getAudio(audioName);
    }

    @PostMapping("/addNewUserInterest")
    @PreAuthorize("hasRole('TOURIST') or hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<?> addNewUserInterest(@Valid @RequestBody UserInterestRequest userInterestRequest, HttpServletRequest request){
        return this.touristService.addNewUserInterestService(userInterestRequest, request);
    }

    @PostMapping("/addNewCategoryInterest")
    @PreAuthorize("hasRole('TOURIST') or hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<?> addNewCategoryInterest(@Valid @RequestBody AddNewCategoryInterstRequest addNewCategoryInterst, HttpServletRequest request){
        return this.touristService.addNewCategoryInterestService(addNewCategoryInterst, request);
    }

    @GetMapping("/getListOfPoIsNotPublished")
    @PreAuthorize("hasRole('TOURIST') or hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<?> getListOfPoIsNotPublished(HttpServletRequest request){
        return this.touristService.getListOfPoIsNotPublishedService(request);
    }

    @GetMapping("/getListOfPoIsPublished")
    @PreAuthorize("hasRole('TOURIST') or hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<?> getListOfPoIsPublished(HttpServletRequest request){
        return this.touristService.getListOfPoIsPublishedService(request);
    }

    @PostMapping("/getSpecificOfPoIsNotPublished")
    @PreAuthorize("hasRole('TOURIST') or hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<?> getSpecificOfPoIsNotPublished(@Valid @RequestBody GetSpecificOfPoIsNotPublishedRequest getSpecificOfPoIsNotPublishedRequest , HttpServletRequest request){
        return this.touristService.getSpecificOfPoIsNotPublishedService(getSpecificOfPoIsNotPublishedRequest, request);
    }

    @PostMapping("/addAnTrip")
    @PreAuthorize("hasRole('TOURIST') or hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<?> addAnTrip(@Valid @RequestBody AddNewTripRequest addNewTripRequest, HttpServletRequest request){
        return this.touristService.addAnTripService(addNewTripRequest, request);
    }

    @PostMapping("/addAnPoIToATrip")
    @PreAuthorize("hasRole('TOURIST') or hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<?> addAnPoIToATrip(@Valid @RequestBody AddAnPoIToTripRequest addAnPoIToTripRequest){
        return this.touristService.addAnPoIToATripService(addAnPoIToTripRequest);
    }

    @GetMapping("/getListOfTrips")
    @PreAuthorize("hasRole('TOURIST') or hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<?> getListOfTrips(HttpServletRequest request){
        return this.touristService.getListOfTripsService(request);
    }

    @PostMapping("/getAllPoIInEachTrips")
    @PreAuthorize("hasRole('TOURIST') or hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<?> getAllPoIInEachTrips(@Valid @RequestBody GetAllPoIInEachTripsRequest getAllPoIInEachTripsRequest, HttpServletRequest request){
        return this.touristService.getAllPoIInEachTripsService(getAllPoIInEachTripsRequest, request);
    }

    @PutMapping("/putStateOfTrips")
    @PreAuthorize("hasRole('TOURIST') or hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<?> putStateOfTrips(@Valid @RequestBody PutStateOfTripsRequest putStateOfTripsRequest, HttpServletRequest request){
        return this.touristService.putStateOfTripsService(putStateOfTripsRequest, request);
    }

    @PostMapping("/addPoIToFavoriteTrip")
    @PreAuthorize("hasRole('TOURIST') or hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<?> addPoIToFavoriteTrip(@Valid @RequestBody AddPoIToFavoriteTripRequest addPoIToFavoriteTripRequest, HttpServletRequest request){
        return this.touristService.addPoIToFavoriteTripService(addPoIToFavoriteTripRequest, request);
    }

    @GetMapping("/getAllPoIInFavoriteTrips")
    @PreAuthorize("hasRole('TOURIST') or hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<?> getAllPoIInFavoriteTrips(HttpServletRequest request){
        return this.touristService.getAllPoIInFavoriteTripsService(request);
    }

    @PostMapping("/addUserSocialMedia")
    @PreAuthorize("hasRole('TOURIST') or hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<?> addUserSocialMedia(@Valid @RequestBody AddUserSocialMediaRequest addUserSocialMediaRequest, HttpServletRequest request){
        return this.touristService.addUserSocialMediaService(addUserSocialMediaRequest, request);
    }

    @PostMapping("/loginUserSocialMedia")
    @PreAuthorize("hasRole('TOURIST') or hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<?> loginUserSocialMedia(@Valid @RequestBody LoginUserSocialMediaRequest loginUserSocialMediaRequest){
        return this.touristService.loginUserSocialMediaService(loginUserSocialMediaRequest);
    }



    @PutMapping("/modifyNameTrip")
    @PreAuthorize("hasRole('TOURIST') or hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<?> modifyNameTrip(@Valid @RequestBody ModifyNameTripRequest modifyNameTripRequest){
        return this.touristService.modifyNameTripService(modifyNameTripRequest);
    }
}
