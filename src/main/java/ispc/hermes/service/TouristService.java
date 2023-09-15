package ispc.hermes.service;

import ispc.hermes.model.*;
import ispc.hermes.payload.request.GET.GetAllPoIInEachTripsRequest;
import ispc.hermes.payload.request.GET.GetAllPoIInEachTripsUsingAdminAccountRequest;
import ispc.hermes.payload.request.GET.GetSpecificOfPoIsNotPublishedRequest;
import ispc.hermes.payload.request.POST.Tourist.*;
import ispc.hermes.payload.request.PUT.ModifyNameTripRequest;
import ispc.hermes.payload.request.PUT.PutStateOfTripsRequest;
import ispc.hermes.payload.response.UserInfoResponse;
import ispc.hermes.repositoriy.*;
import ispc.hermes.security.JWT.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class TouristService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PoIRepository poIRepository;

    @Autowired
    private UserCategoryRepository userCategoryRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserInterestRepository userInterestRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseEntity<?> getSpecificUserService(HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            return ResponseEntity.ok().body(new UserInfoResponse(
                    user.get().getId(),
                    user.get().getUsername(),
                    user.get().getEmail()
            ));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function getSpecificUserService!!");
        }
    }


    private String saveFileToStorageMethods(String directoryPath, String fileName, MultipartFile file) throws IOException {
        // Create the directory if it doesn't exist
        Path fullPath = Paths.get(directoryPath, fileName);
        if (!Files.exists(fullPath.getParent())) {
            Files.createDirectories(fullPath.getParent());
        }

        // Save the file to the specified location
        Files.write(fullPath, file.getBytes());

        // Return the file path as a string
        return fullPath.toString();
    }

    public ResponseEntity<Resource> getImageService(String imageName) throws IOException {
        String imagePath =  "src/main/resources/static/Images/" + imageName; // Replace with the actual image directory path

        Path imageFilePath = Paths.get(imagePath);
        Resource resource = new FileSystemResource(imageFilePath);

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG) // Set the appropriate media type for your image
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Resource> getAudio(String audioName) throws IOException {
        String audioPath = "src/main/resources/static/Audios/" + audioName; // Replace with the actual audio directory path

        Path audioFilePath = Paths.get(audioPath);
        Resource resource = new FileSystemResource(audioFilePath);

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM) // Set the appropriate media type for your audio
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> addNewPoIService(String descriptionPoI,
                                              MultipartFile imagePoI,
                                              Double longitudeLocationPoI,
                                              Double latitudeLocationPoI,
                                              String nameLocationPoI,
                                              String hashtagsPoI,
                                              MultipartFile audioPoI,
                                              boolean isPublishedToFB,
                                              boolean isPublishedToInsta
                                              ){
        try {
            String imageFileName = System.currentTimeMillis()+"."+ FilenameUtils.getExtension(imagePoI.getOriginalFilename());
            String audioFileName  = System.currentTimeMillis()+"."+ FilenameUtils.getExtension(audioPoI.getOriginalFilename());

            PoI poI = new PoI();
            poI.setDescriptionPoI(descriptionPoI);
            poI.setNameLocationPoI(nameLocationPoI);
            poI.setImagePoI(imageFileName);
            poI.setLongitudeLocationPoI(longitudeLocationPoI);
            poI.setLatitudeLocationPoI(latitudeLocationPoI);
            poI.setHashtagsPoI(hashtagsPoI);
            poI.setAudioPoI(audioFileName);
            poI.setIsPublishedToFB(isPublishedToFB);
            poI.setIsPublishedToInsta(isPublishedToInsta);
            poI.setIsPersonalPoI(true);
            PoI poIResponse = this.poIRepository.save(poI);
            String imagePath = "C:\\Users\\real7\\OneDrive\\Desktop\\HerMeS\\HerMeS\\src\\main\\resources\\static\\Images";
            String audioPath = "C:\\Users\\real7\\OneDrive\\Desktop\\HerMeS\\HerMeS\\src\\main\\resources\\static\\Audios";

            saveFileToStorageMethods(imagePath, imageFileName, imagePoI);
            saveFileToStorageMethods(audioPath, audioFileName, audioPoI);

            return ResponseEntity.ok(poIResponse);
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function addNewPoIService!!");
        }
    }

    public ResponseEntity<?> addNewUserInterestService(UserInterestRequest userInterestRequest, HttpServletRequest request){
        try {
            // 1. Get the Interest object from DataBase
            Optional<Interest> interest = this.interestRepository.findByNameInterstAndActivationInterst(userInterestRequest.getNameInterest(), true);
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            // 2. Get the user object from Cookies
            Optional<User> user = this.userRepository.findByUsername(userName);
            // 3. Get the PoI object from Database
            Optional<PoI> poI = this.poIRepository.findByPosition(userInterestRequest.getPoiId());
            UserInterest userInterest = new UserInterest();
            userInterest.setUser(user.get());
            userInterest.setInterest(interest.get());
            userInterest.setPoI(poI.get());
            UserInterest userInterestResponse = this.userInterestRepository.save(userInterest);
            return ResponseEntity.ok().body(userInterestResponse);

        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function addNewUserInterestService!!");
        }
    }

    public ResponseEntity<?> addNewCategoryInterestService(AddNewCategoryInterstRequest addPoIToTripRequest, HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            Optional<PoI> poI =  this.poIRepository.findById(addPoIToTripRequest.getPoiId());
            Optional<Category> category = this.categoryRepository.findByNameCategoryAndActivationCategory(addPoIToTripRequest.getNameCategory(), true);
            UserCategory userCategory = new UserCategory();
            userCategory.setUser(user.get());
            userCategory.setCategory(category.get());
            userCategory.setPoI(poI.get());
            UserCategory userCategoryResponse = this.userCategoryRepository.save(userCategory);
            return ResponseEntity.ok(userCategoryResponse);

        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function addNewCategoryInterestService!!");
        }
    }

    public ResponseEntity<?> getListOfPoIsNotPublishedService(HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            Set<Trip> trips = user.get().getTrips();
            Set<Trip> tripsResponse = new HashSet<>();
            for (Trip trip: trips) {
                Boolean isPublishedToHerMeS = trip.getIsPublishedToHerMeS();
                if (isPublishedToHerMeS != null && !isPublishedToHerMeS) {
                    tripsResponse.add(trip);
                }
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(tripsResponse);
        }
        catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function getListOfPoIsNotPublishedService!!");
        }
    }

    public ResponseEntity<?> getListOfPoIsPublishedService(HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            Set<Trip> trips = user.get().getTrips();
            Set<Trip> tripsResponse = new HashSet<>();
            for (Trip trip: trips) {
                Boolean isPublishedToHerMeS = trip.getIsPublishedToHerMeS();
                if (isPublishedToHerMeS != null && isPublishedToHerMeS) {
                    tripsResponse.add(trip);
                }
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(tripsResponse);
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error getListOfPoIsPublishedService!!");
        }
    }

    public ResponseEntity<?> getSpecificOfPoIsNotPublishedService(GetSpecificOfPoIsNotPublishedRequest getSpecificOfPoIsNotPublishedRequest, HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            Optional<UserCategory> categoryInterest = this.userCategoryRepository.findByUserIdAndPoIPosition(user.get().getId(), getSpecificOfPoIsNotPublishedRequest.getPoiId());
            return ResponseEntity.ok("This is the result of a specific PoIs "+ Map.of("dataOfInterests", categoryInterest));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function getListOfPoIsPublishedService !!");
        }
    }

    public ResponseEntity<?> addAnTripService(AddNewTripRequest addNewTripRequest, HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            Optional<Trip> trip = this.tripRepository.findTripByNameLocationTrip(addNewTripRequest.getNameLocationTrip());
            if (trip.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("You cannot create another trip with the same name: " + trip.get().getNameLocationTrip());
            }else {
                Trip newTrip = new Trip();
                newTrip.setDescriptionBrief(addNewTripRequest.getDescriptionBrief());
                newTrip.setNameLocationTrip(addNewTripRequest.getNameLocationTrip());
                newTrip.setNameLocationTripUpdate(addNewTripRequest.getNameLocationTripUpdate());
                newTrip.setIsPublishedToHerMeS(addNewTripRequest.getIsPublishedToHerMeS());
                newTrip.setIsFavoriteTrip(false);
                newTrip.setIsPersonalTrip(false);
                this.tripRepository.save(newTrip);

                Set<Trip> userTrip = user.get().getTrips();
                if(userTrip == null){userTrip = new HashSet<>();}
                userTrip.add(newTrip);
                user.get().setTrips(userTrip);
                this.userRepository.save(user.get());
                return ResponseEntity.ok(newTrip);
            }
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function addAnTripService !!");
        }
    }

    public ResponseEntity<?> addAnPoIToATripService(AddAnPoIToTripRequest addAnPoIToTripRequest){
        try {
            Optional<Trip> trip = this.tripRepository.findTripByNameLocationTrip(addAnPoIToTripRequest.getNameLocationTrip());
            Optional<PoI> poIS = this.poIRepository.findByPosition(addAnPoIToTripRequest.getPoiId());

            Set<PoI> poISTrips = trip.get().getPoIS();
            if(poISTrips==null){poISTrips = new HashSet<>();}
            poISTrips.add(poIS.get());
            trip.get().setPoIS(poISTrips);
            this.tripRepository.save(trip.get());
            return ResponseEntity.ok(trip);
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the addAnPoIToATripService addAnTrip !!");
        }
    }

    public ResponseEntity<?> getListOfTripsService(HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            Set<Trip> trips = user.get().getTrips();
            Set<Trip> tripsResponse = new HashSet<>();
            for (Trip trip: trips) {
                Boolean isFavoriteTrip = trip.getIsFavoriteTrip();
                if (isFavoriteTrip != null && !isFavoriteTrip) {
                    tripsResponse.add(trip);
                }
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(tripsResponse);
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function getListOfTrips !!");
        }
    }

    public ResponseEntity<?> getAllPoIInEachTripsService(GetAllPoIInEachTripsRequest getAllPoIInEachTripsRequest, HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            Optional<Trip> trip = this.tripRepository.findById(getAllPoIInEachTripsRequest.getTripId());
            Set<PoI> poISResponse = trip.get().getPoIS();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(poISResponse);
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function getAllPoIInEachTripsService !!");
        }
    }

    public ResponseEntity<?> putStateOfTripsService(PutStateOfTripsRequest putStateOfTripsRequest, HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            Optional<Trip> trip = this.tripRepository.findTripByNameLocationTripOrId(putStateOfTripsRequest.getNameLocationTrip(), putStateOfTripsRequest.getNameLocationTripId());
            trip.get().setIsPublishedToHerMeS(putStateOfTripsRequest.getIsPublishedToHerMeS());
            Trip tripResponse = this.tripRepository.save(trip.get());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(tripResponse);
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function putStateOfTripsService !!");
        }
    }

    public ResponseEntity<?> addPoIToFavoriteTripService(AddPoIToFavoriteTripRequest addPoIToFavoriteTripRequest, HttpServletRequest request){
        try {
            String nameLocationTrip = addPoIToFavoriteTripRequest.getNameLocationTrip();
            Optional<Trip> existingTrip = tripRepository.findTripByNameLocationTrip(nameLocationTrip);

            Trip trip;
            if (existingTrip.isPresent()) {
                trip = existingTrip.get();
            } else {
                trip = new Trip();
                trip.setNameLocationTrip(nameLocationTrip);
                trip.setIsFavoriteTrip(addPoIToFavoriteTripRequest.getIsFavoriteTrip());
            }

            Optional<PoI> poi = poIRepository.findByPosition(addPoIToFavoriteTripRequest.getPoiId());
            if (poi.isPresent()) {
                Set<PoI> poISTrips = trip.getPoIS();
                if (poISTrips == null) {
                    poISTrips = new HashSet<>();
                }
                poISTrips.add(poi.get());
                trip.setPoIS(poISTrips);
                trip = tripRepository.save(trip);

                String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
                Optional<User> user = userRepository.findByUsername(userName);
                Set<Trip> userTrips = user.get().getTrips();
                if (userTrips == null) {
                    userTrips = new HashSet<>();
                }
                userTrips.add(trip);
                user.get().setTrips(userTrips);
                userRepository.save(user.get());

                return ResponseEntity.status(HttpStatus.OK).body(trip);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("POI with ID " + addPoIToFavoriteTripRequest.getPoiId() + " not found.");
            }
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("There is an error on the function addPoIToFavoriteTripService!!");
        }
    }

    public ResponseEntity<?> getAllPoIInFavoriteTripsService(HttpServletRequest request) {
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            Set<Trip> trips = user.get().getTrips();
            Set<Trip> tripsResponse = new HashSet<>();
            for (Trip trip: trips) {
                Boolean isFavoriteTrip = trip.getIsFavoriteTrip();
                if (isFavoriteTrip != null && isFavoriteTrip) {
                    tripsResponse.add(trip);
                }
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(tripsResponse);
        }catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("There is an error on the function getAllPoIInFavoriteTripsService !!");
        }
    }


    public ResponseEntity<?> addUserSocialMediaService(AddUserSocialMediaRequest addUserSocialMediaRequest,HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            if (user.get().getUsername().equals(addUserSocialMediaRequest.getUserName()) ==true || user.get().getEmail().equals(addUserSocialMediaRequest.getEmail()) == true){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User already exists " + user.get().getUsername());
            }
            else {
                Set<Role> roles = new HashSet<>();
                Role userRole = this.roleRepository.findByName(ERole.ROLE_TOURIST)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
                User newUser = new User();
                newUser.setUsername(addUserSocialMediaRequest.getUserName());
                newUser.setEmail(addUserSocialMediaRequest.getEmail());
                newUser.setIsConnectedWithFBAccount(addUserSocialMediaRequest.getIsConnectedWithFBAccount());
                newUser.setIsConnectedWithInstAccount(addUserSocialMediaRequest.getIsConnectedWithInstAccount());
                newUser.setIsActive(true);
                newUser.setRoles(roles);
                User userResponse = this.userRepository.save(newUser);
                return ResponseEntity.status(HttpStatus.OK)
                        .body("Add the user "+user.get().getUsername()+ " using social media accounts : "+Map.of("data", userResponse));
            }
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function addUserSocialMediaService !!");
        }
    }

    public ResponseEntity<?> loginUserSocialMediaService(LoginUserSocialMediaRequest loginUserSocialMediaRequest){
        try {
            Optional<User> user =this.userRepository.findByEmail(loginUserSocialMediaRequest.getEmail());
            if (!user.get().getIsActive()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User account not activated !!");
            }
            else {
                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtUtils.generateTokenFromUsername(user.get().getUsername()).toString())
                        .body(new UserInfoResponse(user.get().getId(),
                                user.get().getUsername(),
                                user.get().getEmail()
                                ));
            }
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function loginUserSocialMediaService !!");
        }
    }

        public ResponseEntity<?> modifyNameTripService(ModifyNameTripRequest modifyNameTripRequest){
        try {
            Optional<Trip> trip = this.tripRepository.findTripByNameLocationTripAndIsFavoriteTrip(modifyNameTripRequest.getNameLocationTrip(), false);
            trip.get().setNameLocationTripUpdate(modifyNameTripRequest.getNameLocationTripUpdated());
            Trip tripResponse = this.tripRepository.save(trip.get());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(tripResponse);
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function modifyNameTripService !!");
        }
    }
}

