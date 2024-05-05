package com.batch.kata.batch.chunk.writer;

import com.batch.kata.batch.domain.ApiRequest;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class ApiItemWriter3 implements ItemWriter<ApiRequest> {
    @Override
    public void write(Chunk<? extends ApiRequest> chunk) throws Exception {
        List<? extends ApiRequest> items = chunk.getItems();
    }
}
