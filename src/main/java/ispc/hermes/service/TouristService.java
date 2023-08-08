package ispc.hermes.service;

import ispc.hermes.model.*;
import ispc.hermes.payload.request.GET.GetAllPoIInEachTripsRequest;
import ispc.hermes.payload.request.GET.GetAllPoIInEachTripsUsingAdminAccountRequest;
import ispc.hermes.payload.request.GET.GetSpecificOfPoIsNotPublishedRequest;
import ispc.hermes.payload.request.POST.*;
import ispc.hermes.payload.request.PUT.ModifyNameTripRequest;
import ispc.hermes.payload.request.PUT.PutStateOfTripsRequest;
import ispc.hermes.payload.response.MessageResponse;
import ispc.hermes.payload.response.UserInfoResponse;
import ispc.hermes.repositoriy.*;
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
import org.apache.commons.io.FilenameUtils;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class TouristService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    InterestRepository interestRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PoIRepository poIRepository;

    @Autowired
    CategoryInterestRepository categoryInterestRepository;

    @Autowired
    TripRepository tripRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

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

    public ResponseEntity<?> addNewUserInterestService(UserInterestRequest userInterestRequest, HttpServletRequest request){
        try {
            Optional<Interest> interest = this.interestRepository.findByNameInterstAndActivationInterst(userInterestRequest.getNameRequest(), true);
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            interest.get().setUser(user.get());
            Interest interestResponse = this.interestRepository.save(interest.get());
            return ResponseEntity.ok().body(new MessageResponse("We add the "+userInterestRequest.getNameRequest()+" for the username: "+ user.get().getUsername()+ Map.of("data",interestResponse)));

        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function addNewUserInterestService!!");
        }
    }

    public ResponseEntity<?> addNewPoIService(AddPoIRequest addPoIRequest, HttpServletRequest request){
        try {
            String imageFileName = System.currentTimeMillis()+"."+ FilenameUtils.getExtension(addPoIRequest.getImagePoI().getOriginalFilename());
            String audioFileName  = System.currentTimeMillis()+"."+ FilenameUtils.getExtension(addPoIRequest.getAudioPoI().getOriginalFilename());

            PoI poI = new PoI();
            poI.setNameLocationPoI(addPoIRequest.getNameLocationPoI());
            poI.setImagePoI(imageFileName);
            poI.setLongitudeLocationPoI(addPoIRequest.getLongitudeLocationPoI());
            poI.setLatitudeLocationPoI(addPoIRequest.getLatitudeLocationPoI());
            poI.setNameLocationPoI(addPoIRequest.getNameLocationPoI());
            poI.setHashtagsPoI(addPoIRequest.getHashtagsPoI());
            poI.setAudioPoI(audioFileName);
            poI.setIsPublishedToFB(addPoIRequest.getIsPublishedToFB());
            poI.setIsPublishedToInsta(addPoIRequest.getIsPublishedToInsta());
            poI.setIsPersonalPoI(true);
            PoI poIResponse = this.poIRepository.save(poI);
            return ResponseEntity.ok("New PoI added successfully "+ Map.of("data", poIResponse));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function addNewPoIService!!");
        }
    }

    public ResponseEntity<?> addNewCategoryInterestService(AddNewCategoryInterstRequest addPoIToTripRequest, HttpServletRequest request){
        try {
            Optional<PoI> poI =  this.poIRepository.findById(addPoIToTripRequest.getPoiId());
            Optional<Category> category = this.categoryRepository.findByNameCategoryAndActivationCategory(addPoIToTripRequest.getNameCategory(), true);
            CategoryInterest categoryInterest = new CategoryInterest();
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            categoryInterest.setUser(user.get());
            categoryInterest.setCategory(category.get());
            categoryInterest.setPoI(poI.get());
            CategoryInterest categoryInterestResponse = this.categoryInterestRepository.save(categoryInterest);
            return ResponseEntity.ok("We add a new category interest with success !!"+ Map.of("data", categoryInterestResponse));

        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function addNewCategoryInterestService!!");
        }
    }

    public ResponseEntity<?> getListOfPoIsNotPublishedService(HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            Set<Trip> trips = user.get().getTrips();
            Set<PoI> poIS = new HashSet<>();
            for (Trip trip: trips) {
                if(trip.getIsPublishedToHerMeS()==false){
                    poIS.add((PoI) trip.getPoIS());
                }
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body("All the poi's not published of " + user.get().getUsername()+ Map.of("data", poIS));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function getListOfPoIsNotPublishedService!!");
        }
    }

    public ResponseEntity<?> getListOfPoIsPublishedService(HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            Set<Trip> trips = user.get().getTrips();
            Set<PoI> poIS = new HashSet<>();
            for (Trip trip: trips) {
                if(trip.getIsPublishedToHerMeS()){
                    poIS.add((PoI) trip.getPoIS());
                }
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body("All the poi's not published of " + user.get().getUsername()+ Map.of("data", poIS));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error getListOfPoIsPublishedService!!");
        }
    }

    public ResponseEntity<?> getSpecificOfPoIsNotPublishedService(GetSpecificOfPoIsNotPublishedRequest getSpecificOfPoIsNotPublishedRequest, HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            Optional<CategoryInterest> categoryInterest = this.categoryInterestRepository.findByUserIdAndPoIPosition(user.get().getId(), getSpecificOfPoIsNotPublishedRequest.getPoiId());
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
            if (trip.get() != null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("You cannot create another trip with the same name: " + trip.get().getNameLocationTrip());
            }else {
                Trip newTrip = new Trip();
                newTrip.setDescriptionBrief(addNewTripRequest.getDescriptionBrief());
                newTrip.setNameLocationTrip(addNewTripRequest.getNameLocationTrip());
                newTrip.setNameLocationTripUpdate(addNewTripRequest.getNameLocationTripUpdate());
                newTrip.setIsPublishedToHerMeS(addNewTripRequest.getIsPublishedToHerMeS());
                this.tripRepository.save(newTrip);
                user.get().setTrips((Set<Trip>) newTrip);
                this.userRepository.save(user.get());
                return ResponseEntity.ok("We Add a an PoI to a Trip "+ Map.of("data", newTrip));
            }
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function addAnTripService !!");
        }
    }

    public ResponseEntity<?> addAnPoIToATripService(AddAnPoIToTripRequest addAnPoIToTripRequest){
        try {
            Optional<Trip> trip = this.tripRepository.findTripByNameLocationTrip(addAnPoIToTripRequest.getNameLocationTrip());
            trip.get().setPoIS((Set<PoI>) this.poIRepository.getReferenceById(addAnPoIToTripRequest.getPoiId()));
            this.tripRepository.save(trip.get());
            return ResponseEntity.ok("We Add a an PoI to a Trip "+ Map.of("data", trip));
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
                if(trip.getIsFavoriteTrip()==false){
                    tripsResponse.add(trip);
                }
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body("All the favorite trips of" + user.get().getUsername()+ Map.of("data", tripsResponse));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function getListOfTrips !!");
        }
    }

    public ResponseEntity<?> getAllPoIInEachTripsService(GetSpecificOfPoIsNotPublishedRequest getSpecificOfPoIsNotPublishedRequest, HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            Optional<Trip> trip = this.tripRepository.findById(getSpecificOfPoIsNotPublishedRequest.getPoiId());
            Set<PoI> poISResponse = trip.get().getPoIS();
            return ResponseEntity.status(HttpStatus.OK)
                    .body("All pois on each trip of" + user.get().getUsername()+ Map.of("data", poISResponse));
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
                    .body("The trip id: "+putStateOfTripsRequest.getNameLocationTripId()+" is modified by "+user.get().getUsername()+ Map.of("data", tripResponse));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function putStateOfTripsService !!");
        }
    }

    public ResponseEntity<?> addPoIToFavoriteTripService(AddPoIToFavoriteTripRequest addPoIToFavoriteTripRequest, HttpServletRequest request){
        try {
            Optional<Trip> trip = this.tripRepository.findTripByNameLocationTrip(addPoIToFavoriteTripRequest.getNameLocationTrip());
            if (trip.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("You cannot create another trip with the same name: " + trip.get().getNameLocationTrip());
            }
            else {
                PoI poI = this.poIRepository.findByPosition(addPoIToFavoriteTripRequest.getPoiId());
                Trip newTrip = new Trip();
                newTrip.setNameLocationTrip(addPoIToFavoriteTripRequest.getNameLocationTrip());
                newTrip.setIsFavoriteTrip(addPoIToFavoriteTripRequest.getIsFavoriteTrip());
                newTrip.setPoIS((Set<PoI>) poI);
                Trip tripResponse = this.tripRepository.save(newTrip);
                String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
                Optional<User> user = this.userRepository.findByUsername(userName);
                user.get().setTrips((Set<Trip>) newTrip);
                this.userRepository.save(user.get());
                return ResponseEntity.status(HttpStatus.OK)
                        .body("We add a favorite PoI with id number : "+addPoIToFavoriteTripRequest.getPoiId()+" for "+user.get().getUsername()+ Map.of("data", tripResponse));
            }
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function addPoIToFavoriteTripService !!");
        }
    }

    public ResponseEntity<?> getAllPoIInFavoriteTripsService(HttpServletRequest request){
        try{
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            Set<Trip> trips = user.get().getTrips();
            Set<PoI> poISResponse = new HashSet<>();
            for (Trip trip: trips) {
                poISResponse.add((PoI) trip.getPoIS());
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body("All PoI in favorite trip of the : "+user.get().getUsername()+ Map.of("data", poISResponse));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function getAllPoIInFavoriteTripsService !!");
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
                User newUser = new User();
                newUser.setUsername(addUserSocialMediaRequest.getUserName());
                newUser.setEmail(addUserSocialMediaRequest.getEmail());
                newUser.setIsConnectedWithFBAccount(addUserSocialMediaRequest.getIsConnectedWithFBAccount());
                newUser.setIsConnectedWithInstAccount(addUserSocialMediaRequest.getIsConnectedWithInstAccount());
                newUser.setIsActive(true);
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

    public ResponseEntity<?> getListPoIsOfTouringClubAddByTheScriptService(HttpServletRequest request){
        try {
            Set<PoI> poIS = this.poIRepository.findAllByIsPersonalPoI(false);
            List<PoI> poIListResponse = List.copyOf(poIS);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("All the PoI's of touring club "+Map.of("data", poIListResponse));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function getListPoIsOfTouringClubAddByTheScriptService !!");
        }
    }

    public ResponseEntity<?> getListOfTripsUsingAdminAccountService(){
        try {
            Optional<User> user = this.userRepository.findByEmail("jaziriabdelkader@gmail.com");
            Set<Trip> trips = user.get().getTrips();
            Set<Trip> tripsResponse = new HashSet<>();
            for (Trip trip: trips){
                tripsResponse.add(trip);
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body("All the PoI's of touring club using the Expert account"+Map.of("data", tripsResponse));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function GetListOfTripsUsingAdminAccountService !!");
        }
    }

    public ResponseEntity<?> getAllPoIInEachTripsUsingAdminAccountService(GetAllPoIInEachTripsUsingAdminAccountRequest getAllPoIInEachTripsUsingAdminAccountRequest){
        try {
            Optional<Trip> trip = this.tripRepository.findById(getAllPoIInEachTripsUsingAdminAccountRequest.getTripId());
            List<PoI> poIListResponse = List.copyOf(trip.get().getPoIS());
            return ResponseEntity.status(HttpStatus.OK)
                    .body("All the PoI's In specific trip using the Expert account "+Map.of("data", poIListResponse));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function getAllPoIInEachTripsUsingAdminAccountService !!");
        }
    }

    public ResponseEntity<?> modifyNameTripService(ModifyNameTripRequest modifyNameTripRequest){
        try {
            Optional<Trip> trip = this.tripRepository.findTripByNameLocationTripAndIsFavoriteTrip(modifyNameTripRequest.getNameLocationTrip(), false);
            trip.get().setNameLocationTripUpdate(modifyNameTripRequest.getNameLocationTripUpdated());
            Trip tripResponse = this.tripRepository.save(trip.get());
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Modify the old trip "+ modifyNameTripRequest.getNameLocationTrip()+" to "+modifyNameTripRequest.getNameLocationTripUpdated()+" "+Map.of("data", tripResponse));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function modifyNameTripService !!");
        }
    }
}

