package com.example.rqchallenge.service.client;

import com.example.rqchallenge.common.enumer.ErrorInfo;
import com.example.rqchallenge.common.exception.RemoteApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

public abstract class Client {

    private final RestTemplate restTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(Client.class);

    private static final String LOG_ERROR = "[{}] PATH: {} - ERROR: {}";

    public Client(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public abstract String getServiceName();

    public <T> Optional<T> executeGetRequest(String url, String serviceName, Class<T> responseType) {
        try {
            ResponseEntity<T> response = restTemplate.getForEntity(url, responseType);

            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            LOG.error(LOG_ERROR, serviceName, url, e.getMessage());
            throw new RemoteApiException(ErrorInfo.REMOTE_API_HAS_RETURNED_AN_UNKNOWN_ERROR, e.getMessage());
        }
    }

    public <T> Optional<T> executeDeleteRequest(String url, String serviceName, Class<T> responseType) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity.EMPTY, responseType);

            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            LOG.error(LOG_ERROR, serviceName, url, e.getMessage());
            throw new RemoteApiException(ErrorInfo.REMOTE_API_HAS_RETURNED_AN_UNKNOWN_ERROR, e.getMessage());
        }
    }

    public <T, R> Optional<T> executePostRequest(String url, String serviceName, R requestBody, Class<T> responseType) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<R> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);

            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            LOG.error(LOG_ERROR, serviceName, url, e.getMessage());
            throw new RemoteApiException(ErrorInfo.REMOTE_API_HAS_RETURNED_AN_UNKNOWN_ERROR, e.getMessage());
        }
    }
}
