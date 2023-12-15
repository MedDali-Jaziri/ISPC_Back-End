package ispc.hermes.controller;

import ispc.hermes.payload.request.GET.GetAllPoIInEachTripsComingFromHAIRequest;
import ispc.hermes.payload.request.POST.Tourist.LoginRequest;
import ispc.hermes.payload.response.ExpertResponse.GET.AddTopicsResponse;
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
    public ResponseEntity<?> addExpert(@Valid @RequestBody LoginRequest loginRequest){
        return this.expertService.loginExpertService(loginRequest);
    }

    @GetMapping("/addNewTopicFromHAI")
    @PreAuthorize("hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<AddTopicsResponse> addNewTopicFromHAI(HttpServletRequest request){
        return this.expertService.addNewTopicFromHAIService(request);
    }

    @GetMapping("/getListPoIsOfByTheHAI")
    @PreAuthorize("hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<?> getListPoIsOfByTheHAI(HttpServletRequest request){
        return this.expertService.getListPoIsOfByTheHAIService(request);
    }

    @GetMapping("/getListOfTripsUsingHAI")
    @PreAuthorize("hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<?> getListOfTripsUsingHAI(HttpServletRequest request){
        return this.expertService.getListOfTripsUsingHAIService(request);
    }

    @PostMapping("/getAllPoIInEachTripsComingFromHAI")
    @PreAuthorize("hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<?> getAllPoIInEachTripsComingFromHAI(@Valid @RequestBody GetAllPoIInEachTripsComingFromHAIRequest getAllPoIInEachTripsComingFromHAIRequest){
        return this.expertService.getAllPoIInEachTripsComingFromHAIService(getAllPoIInEachTripsComingFromHAIRequest);
    }

}
