package com.batch.kata.batch.chunk.writer;

import com.batch.kata.batch.domain.ApiRequest;
import com.batch.kata.batch.domain.ApiResponse;
import com.batch.kata.service.ApiService1;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@RequiredArgsConstructor
public class ApiItemWriter1 implements ItemWriter<ApiRequest> {
    private final ApiService1 apiService1;

    @Override
    public void write(Chunk<? extends ApiRequest> chunk) throws Exception {
        apiService1.service((List<ApiRequest>) chunk.getItems());
    }
}
