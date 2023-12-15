package ispc.hermes.service;

import ispc.hermes.model.PoI;
import ispc.hermes.model.TopicHAI;
import ispc.hermes.model.Trip;
import ispc.hermes.model.User;
import ispc.hermes.payload.request.GET.GetAllPoIInEachTripsComingFromHAIRequest;
import ispc.hermes.payload.request.POST.Tourist.LoginRequest;
import ispc.hermes.payload.response.AdminResponse.GET.GetListOfTripsComingFromHAIResponse;
import ispc.hermes.payload.response.AdminResponse.GET.GetListPoIsComingFromHAIServiceResponse;
import ispc.hermes.payload.response.AdminResponse.POST.GetAllPoIInEachTripsComingFromHAIResponse;
import ispc.hermes.payload.response.ExpertResponse.GET.AddTopicsResponse;
import ispc.hermes.payload.response.UserInfoResponse;
import ispc.hermes.repositoriy.PoIRepository;
import ispc.hermes.repositoriy.TopicHAIRepository;
import ispc.hermes.repositoriy.TripRepository;
import ispc.hermes.repositoriy.UserRepository;
import ispc.hermes.security.JWT.JwtUtils;
import ispc.hermes.security.services.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExpertService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicHAIRepository topicHAIRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private HAIService haiService;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private PoIRepository poIRepository;


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

    public ResponseEntity<AddTopicsResponse> addNewTopicFromHAIService(HttpServletRequest request){
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            Optional<User> user = this.userRepository.findByUsername(userName);

            String apiUrl = "http://80.211.238.135:8080/knowledge/topics";

            String listTopicResult = haiService.fetchListTopicsFromHAIAPI(apiUrl);
            JSONArray jsonArray = new JSONArray(listTopicResult);

            List<String> duplicateTopics = new ArrayList<>();
            for (int i=0; i< jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String label = jsonObject.getString("label");
                if (this.topicHAIRepository.existsByLabel(label)) {
                    duplicateTopics.add(label);
                } else {
                    TopicHAI topicHAI = new TopicHAI();
                    topicHAI.setIdHAITopic(jsonObject.getString("id"));
                    topicHAI.setLabel(jsonObject.getString("label"));
                    topicHAI.setUser(user.get());
                    this.topicHAIRepository.save(topicHAI);
                }
            }
            if (!duplicateTopics.isEmpty()){
                return ResponseEntity.status(HttpStatus.FOUND).body(new AddTopicsResponse("Topics already exist: ", duplicateTopics));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new AddTopicsResponse("The topic(s) added successfully from HAI!!"));

        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AddTopicsResponse("There is an error on the function addNewCategory!!"));
        }
    }

    public ResponseEntity<?> getListPoIsOfByTheHAIService(HttpServletRequest request){
        try {
            Set<PoI> poIS = this.poIRepository.findAllByIsPersonalPoI(false);
            List<PoI> poIListResponse = List.copyOf(poIS);
            return ResponseEntity.ok(new GetListPoIsComingFromHAIServiceResponse(
                    "All the PoI's coming from the HAI Service !! ",
                    poIListResponse
            ));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GetListPoIsComingFromHAIServiceResponse("There is an error on the function getListPoIsOfByTheHAIService !!"));
        }
    }

    public ResponseEntity<?> getListOfTripsUsingHAIService(HttpServletRequest request){
        try {
            Set<Trip> trips = this.tripRepository.findAllByIsPersonalTrip(false);
            List<Trip> tripListResponse = List.copyOf(trips);

            return ResponseEntity.ok(new GetListOfTripsComingFromHAIResponse(
                    "The list of trips coming from HAI Service !",
                    tripListResponse
            ));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GetListOfTripsComingFromHAIResponse("There is an error on the function getListOfTripsUsingHAIService !!"));
        }
    }

    public ResponseEntity<?> getAllPoIInEachTripsComingFromHAIService(GetAllPoIInEachTripsComingFromHAIRequest getAllPoIInEachTripsComingFromHAIRequest){
        try {
            Optional<Trip> trip = this.tripRepository.findTripByTripId(getAllPoIInEachTripsComingFromHAIRequest.getTripId());
            List<PoI> poIListResponse = List.copyOf(trip.get().getPoIS());
            return ResponseEntity.ok(new GetAllPoIInEachTripsComingFromHAIResponse(
                    "List of Point of Interest related with trip n°"+ getAllPoIInEachTripsComingFromHAIRequest.getTripId(),
                    poIListResponse
            ));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GetAllPoIInEachTripsComingFromHAIResponse("There is an error on the function getAllPoIInEachTripsComingFromHAIService !!"));
        }
    }
}
