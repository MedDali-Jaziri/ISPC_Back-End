package ispc.hermes.service;

import ispc.hermes.payload.response.TouristResponse.POST.SetHAIRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;

@Service

public class HAIService {

    @Autowired
    private RestTemplate restTemplate;

    public String fetchListTopicsFromHAIAPI(final String apiUrl) {
        // Make a GET request to the external API
        try {
            return restTemplate.getForObject(apiUrl, String.class);
        }catch (RestClientException exception){
            exception.printStackTrace();
            return "Error occurred during API call";
        }
    }

    public String fetchTripFromHAIAPI(final String apiUrl, final SetHAIRequest setHAIRequest) {
        try {
            // Set up headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create an HttpEntity with headers and the request body
            HttpEntity<SetHAIRequest> requestEntity = new HttpEntity<>(setHAIRequest, headers);

            // Make a POST request to the external API using RestTemplate.postForObject method
            // The method returns the response as String.
            return restTemplate.postForObject(apiUrl, requestEntity, String.class);
        }catch (RestClientException exception){
            return "Error occurred during POST request !!";
        }
    }
}
