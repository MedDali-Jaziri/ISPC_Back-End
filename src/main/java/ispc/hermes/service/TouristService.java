package ispc.hermes.service;

import ispc.hermes.model.*;
import ispc.hermes.payload.request.GET.GetAllPoIInEachTripsRequest;
import ispc.hermes.payload.request.POST.Tourist.*;
import ispc.hermes.payload.request.PUT.ModifyNameTripRequest;
import ispc.hermes.payload.request.PUT.PutStateOfTripsRequest;
import ispc.hermes.payload.response.ErrorMessage;
import ispc.hermes.payload.response.TouristResponse.GET.GetAllPoIInFavoriteTripsResponse;
import ispc.hermes.payload.response.TouristResponse.GET.GetListFavoritePoIResponse;
import ispc.hermes.payload.response.TouristResponse.GET.GetListOfTripsResponse;
import ispc.hermes.payload.response.TouristResponse.POST.*;
import ispc.hermes.payload.response.TouristResponse.PUT.ModifyNameTripResponse;
import ispc.hermes.payload.response.TouristResponse.PUT.PutStateOfTripsResponse;
import ispc.hermes.payload.response.UserInfoResponse;
import ispc.hermes.repositoriy.*;
import ispc.hermes.security.JWT.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
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
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TouristService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicHAIRepository topicHAIRepository;

    @Autowired
    private PoIRepository poIRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserTopicRepository userTopicRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SetPoIHAIRepository setPoIHAIRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private HAIService haiService;

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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("There is an error on the function getSpecificUserService!!"));
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

            return ResponseEntity.ok(new AddNewPoIResponse(
                    "New Point of Interest is added on "+nameLocationPoI,
                    poIResponse
            ));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("There is an error on the function addNewPoIService!!"));
        }
    }

    public ResponseEntity<?> addNewUserTopicService(UserTopicRequest userTopicRequest, HttpServletRequest request){
        try {
            Set<String> listOfTopics = userTopicRequest.getLabelOfTopics();
            LocalDateTime localDateTime = LocalDateTime.now();
            Date currentDate = Date.from(localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            Optional<PoI> poI = this.poIRepository.findByPosition(userTopicRequest.getPoiId());
            List<UserTopic> userTopicList = new ArrayList<>();

            for (String topic: listOfTopics){
                UserTopic userTopic = new UserTopic();
                Optional<TopicHAI> interest = this.topicHAIRepository.findTopicHAIByLabel(topic);
                userTopic.setUser(user.get());
                userTopic.setTopicHAI(interest.get());
                userTopic.setTopicHAI(interest.get());
                userTopic.setPoI(poI.get());
                userTopic.setDateSelection(currentDate);
                userTopicList.add(this.userTopicRepository.save(userTopic));
            }
            return ResponseEntity.ok().body(new AddNewUserInterestResponse(
                    "The interest: "+userTopicRequest+" is interested by "+ userName,
                    userTopicList
            ));
        }catch (Exception exception){
            System.out.println(exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("There is an error on the function addNewUserInterestService!!"));
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
                    .body(new GetListOfPoIsResponse(
                            "List of Point of interest not published!",
                            tripsResponse
                    ));
        }
        catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("There is an error on the function getListOfPoIsNotPublishedService!!"));
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
                    .body(new GetListOfPoIsResponse(
                            "List of Point of interest published!",
                            tripsResponse
                    ));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("There is an error getListOfPoIsPublishedService!!"));
        }
    }

    // Checking this end-point !!
//    public ResponseEntity<?> getSpecificOfPoIsNotPublishedService(GetSpecificOfPoIsNotPublishedRequest getSpecificOfPoIsNotPublishedRequest, HttpServletRequest request){
//        try {
//            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
//            Optional<User> user = this.userRepository.findByUsername(userName);
//            Optional<UserCategory> categoryInterest = this.userCategoryRepository.findByUserIdAndPoIPosition(user.get().getId(), getSpecificOfPoIsNotPublishedRequest.getPoiId());
//            return ResponseEntity.ok("This is the result of a specific PoIs "+ Map.of("dataOfInterests", categoryInterest));
//        }catch (Exception exception){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error on the function getListOfPoIsPublishedService !!");
//        }
//    }

    public ResponseEntity<?> addAnTripService(AddNewTripRequest addNewTripRequest, HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            Optional<Trip> trip = this.tripRepository.findTripByNameLocationTrip(addNewTripRequest.getNameLocationTrip());
            if (trip.isPresent()){
                return ResponseEntity.ok(new ErrorMessage("You cannot create another trip with the same name: " + trip.get().getNameLocationTrip()));
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
                return ResponseEntity.ok(new AddAnTripResponse(
                        "New trip with name: "+ addNewTripRequest.getNameLocationTrip()+" is inserted with success!!",
                        newTrip
                        ));
            }
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("There is an error on the function addAnTripService !!"));
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
            return ResponseEntity.ok(new AddAnPoIToATripResponse(
                    "We add the point interested: "+poIS.get().getNameLocationPoI()+ " on the trip: "+trip.get().getNameLocationTrip(),
                    trip
            ));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("There is an error on the addAnPoIToATripService addAnTrip !!"));
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
                    .body(new GetListOfTripsResponse(
                            "List of Trips add with "+userName,
                            tripsResponse
                    ));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("There is an error on the function getListOfTrips !!"));
        }
    }

    public ResponseEntity<?> getAllPoIInEachTripsService(GetAllPoIInEachTripsRequest getAllPoIInEachTripsRequest, HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            Optional<Trip> trip = this.tripRepository.findById(getAllPoIInEachTripsRequest.getTripId());
            Set<PoI> poISResponse = trip.get().getPoIS();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new GetAllPoIInEachTripsResponse(
                            "List of Point of Interest on the trip of "+trip.get().getNameLocationTrip(),
                            poISResponse
                    ));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("There is an error on the function getAllPoIInEachTripsService !!"));
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
                    .body(new PutStateOfTripsResponse(
                            "We have modified the status of the trip "+trip.get().getNameLocationTrip()+" to be "+putStateOfTripsRequest.getIsPublishedToHerMeS(),
                            tripResponse
                    ));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("There is an error on the function putStateOfTripsService !!"));
        }
    }

//    public ResponseEntity<?> addPoIToFavoriteTripService(AddPoIToFavoriteTripRequest addPoIToFavoriteTripRequest, HttpServletRequest request){
//        try {
//            String nameLocationTrip = addPoIToFavoriteTripRequest.getNameLocationTrip();
//            Optional<Trip> existingTrip = tripRepository.findTripByNameLocationTrip(nameLocationTrip);
//
//            Trip trip;
//            if (existingTrip.isPresent()) {
//                trip = existingTrip.get();
//            } else {
//                trip = new Trip();
//                trip.setNameLocationTrip(nameLocationTrip);
//                trip.setIsFavoriteTrip(addPoIToFavoriteTripRequest.getIsFavoriteTrip());
//            }
//
//            Optional<PoI> poi = poIRepository.findByPosition(addPoIToFavoriteTripRequest.getPoiId());
//            if (poi.isPresent()) {
//                Set<PoI> poISTrips = trip.getPoIS();
//                if (poISTrips == null) {
//                    poISTrips = new HashSet<>();
//                }
//                poISTrips.add(poi.get());
//                trip.setPoIS(poISTrips);
//                trip = tripRepository.save(trip);
//
//                String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
//                Optional<User> user = userRepository.findByUsername(userName);
//                Set<Trip> userTrips = user.get().getTrips();
//                if (userTrips == null) {
//                    userTrips = new HashSet<>();
//                }
//                userTrips.add(trip);
//                user.get().setTrips(userTrips);
//                userRepository.save(user.get());
//
//                return ResponseEntity.status(HttpStatus.OK).body(
//                        new AddPoIToFavoriteTripResponse(
//                                "New Favorite trip is created with name "+trip.getNameLocationTrip(),
//                                trip
//                        ));
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(new ErrorMessage("POI with ID " + addPoIToFavoriteTripRequest.getPoiId() + " not found."));
//            }
//        } catch (Exception exception) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ErrorMessage("There is an error on the function addPoIToFavoriteTripService!!"));
//        }
//    }

    public ResponseEntity<?> getAllPoIInFavoriteTripsService(HttpServletRequest request) {
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            Set<Trip> trips = user.get().getTrips();
            Set<Trip> tripsResponse = new HashSet<>();
            String nameFavoriteTrip="";
            for (Trip trip: trips) {
                Boolean isFavoriteTrip = trip.getIsFavoriteTrip();
                if (isFavoriteTrip != null && isFavoriteTrip) {
                    nameFavoriteTrip = trip.getNameLocationTrip();
                    tripsResponse.add(trip);
                }
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new GetAllPoIInFavoriteTripsResponse(
                            "List of Point of interest on the favorite trip: "+nameFavoriteTrip,
                            tripsResponse
                    ));
        }catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessage("There is an error on the function getAllPoIInFavoriteTripsService !!"));
        }
    }


    // Checking this end-point !!
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

    // Checking this end-point !!
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
                    .body(new ModifyNameTripResponse(
                            "The modfication of the nameLocationTripUpdated is make it with success !",
                            tripResponse
                    ));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("There is an error on the function modifyNameTripService !!"));
        }
    }


    public ResponseEntity<?> addSetPoIHAIService(AddSetPoIRequest addSetPoIRequest, HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            SetPoIHAI setPoIHAI = new SetPoIHAI();
            setPoIHAI.setUser(user.get());
            setPoIHAI.setDuration(addSetPoIRequest.getDuration());
            setPoIHAI.setUserLocation(addSetPoIRequest.getUserLocation());
            setPoIHAI.setGroupSize(addSetPoIRequest.getGroupSize());
            setPoIHAI.setDVector(addSetPoIRequest.getDisabilityVector());
            setPoIHAI.setMVector(addSetPoIRequest.getMobilityVector());
            SetPoIHAI setPoIHAIResponse = this.setPoIHAIRepository.save(setPoIHAI);

            return ResponseEntity.ok(new AddSetPoIHAIResponse(
                    user.get().getUsername()+ " is setting a new trip : ",
                    setPoIHAIResponse));

        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("There is an error on the function addSetPoIHAI !!"));
        }
    }

    public ResponseEntity<?> setPoIToHAIService(SetPoIToHAIRequest setPoIToHAIRequest,HttpServletRequest request){
        try {
            Set<String> userTopic = setPoIToHAIRequest.getLabelOfTopics();
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);

            Long setPoIHAIId= setPoIToHAIRequest.getSetPoIHAIId();
            Optional<SetPoIHAI> setPoIHAI = this.setPoIHAIRepository.findSetPoIHAIById(setPoIHAIId);

            List<TopicRequest> topicRequests = new ArrayList<>();
            TopicRequest topics;

            for (String label: userTopic){
                Optional<TopicHAI> topicHAIS = this.topicHAIRepository.findTopicHAIByLabel(label);
                topics = new TopicRequest(topicHAIS.get().getIdHAITopic(),topicHAIS.get().getLabel());
                topicRequests.add(topics);
            }

            Long userId = user.get().getId();
            int duration = setPoIHAI.get().getDuration();
            List<Double> userLocation = setPoIHAI.get().getUserLocation();
            int groupSize = setPoIHAI.get().getGroupSize();
            List<Boolean> dVector = setPoIHAI.get().getDVector();
            List<Boolean> mVector = setPoIHAI.get().getMVector();

            SetHAIRequest setHAIRequest = new SetHAIRequest(userId.toString(), duration, userLocation, groupSize, dVector, mVector, topicRequests);
            String apiUrl = "http://80.211.238.135:8080/planner/trip";

            String propTripResult = haiService.fetchTripFromHAIAPI(apiUrl, setHAIRequest);

            JSONObject jsonObject = new JSONObject(propTripResult);
            // Extracting the "hops" array from the JSON object
            JSONArray hopsArray = jsonObject.getJSONArray("hops");
            PoI poI;
            for (int i = 0; i < hopsArray.length(); i++) {
                // Get the i-th element (a hop) from the array
                poI = new PoI();
                JSONObject hopObject = hopsArray.getJSONObject(i);
                String hopId = hopObject.getString("id");
                Double hopRanking = hopObject.getDouble("ranking");
                int hopCounter = hopObject.getInt("counter");
                poI.setRanking(hopRanking);
                poI.setCounter(hopCounter);
                poI.setIsFavoritePoI(false);
                JSONArray descriptionsArray = hopObject.getJSONArray("descriptions");
                for (int j = 0; j < descriptionsArray.length(); j++) {
                    JSONObject descriptionObject = descriptionsArray.getJSONObject(j);
                    String descriptionText = descriptionObject.getString("text");
                    JSONObject entityObject = descriptionObject.getJSONObject("entity");
                    String entityLabel = entityObject.getString("label");

                    poI.setDescriptionPoI(descriptionText);
                    poI.setNameLocationPoI(entityLabel);
                }
                this.poIRepository.save(poI);
            }

            // Cause the ResponseEntity.ok() uses an object mapper (like Jackson) to convert the object to JSON
            // We need to Convert JSONObject to a string
            String jsonStringResult = jsonObject.toString();
            return ResponseEntity.ok(jsonStringResult);

        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("There is an error on the function setPoIToHAI !!"));
        }
    }

    public ResponseEntity<?> addFavoritePoIOfHAIService(AddFavoritePoIRequest addFavoritePoIRequest,HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);

            Optional<PoI> poI = this.poIRepository.findByPosition(addFavoritePoIRequest.getId());

            Set<PoI> favoritePoI= user.get().getPoIS();

            if(favoritePoI==null){favoritePoI = new HashSet<>();}
            favoritePoI.add(poI.get());
            poI.get().setIsFavoritePoI(true);
            user.get().setPoIS(favoritePoI);
//            for (PoI poIItem: poI){
//                poIItem.setIsFavoritePoI(true);
//                this.poIRepository.save(poIItem);
//            }
//
//            user.get().setPoIS(poI);
            User userResponse = this.userRepository.save(user.get());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new AddFavoritePoIOfHAIResponse("The PoI n° "+addFavoritePoIRequest.getId()+" now is one favorite PoI ", userResponse));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("There is an error on the function setPoIToHAI !!"));
        }
    }

    public ResponseEntity<?> getListFavoritePoIService(HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);
            Set<PoI> poIS= user.get().getPoIS();
            List<PoI> poIList = new ArrayList<>();
            for (PoI poIItem: poIS){
                if(poIItem.getIsFavoritePoI()==true){
                    poIList.add(poIItem);
                }
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new GetListFavoritePoIResponse("The Favorite PoI ", poIList));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("There is an error on the function setPoIToHAI !!"));
        }
    }
}