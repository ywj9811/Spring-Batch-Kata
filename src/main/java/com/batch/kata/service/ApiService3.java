package com.batch.kata.service;

import com.batch.kata.batch.domain.ApiRequest;
import com.batch.kata.batch.domain.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiService3 extends ApiService{
    @Override
    public ApiResponse apiService(RestTemplate restTemplate, List<ApiRequest> apiRequests) {
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8083/api/product/1", apiRequests, String.class);
        String statusCode = responseEntity.getStatusCode().toString();
        String message = responseEntity.getBody();
        return new ApiResponse(statusCode, message);
    }
}
