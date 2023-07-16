package it.unisalento.iot.iotdigitaltwin.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class APICalls {
    private final RestTemplate restTemplate;

    public APICalls() {
        this.restTemplate = new RestTemplate();
    }

    public RoleResponse checkRole(String jwtToken) throws ResponseStatusException {
        try {
            String url = "https://x7oeqezkzi.execute-api.us-east-1.amazonaws.com/dev/api/users/login";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            // You can also set other headers if needed

            ResponseEntity<RoleResponse> response = restTemplate.exchange(
                    url, HttpMethod.GET, new HttpEntity<>(headers), RoleResponse.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else if (response.getStatusCode() == HttpStatus.FORBIDDEN || response.getStatusCode() == HttpStatus.UNAUTHORIZED ) {
                // Return 403 response in case of authentication failure
                System.out.println("Autenticazione Fallita.");
                return new RoleResponse(HttpStatus.FORBIDDEN.value(), "Authentication failed");
            }

        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return null;
    }
}
