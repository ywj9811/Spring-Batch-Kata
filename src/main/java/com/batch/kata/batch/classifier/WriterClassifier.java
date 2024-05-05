package com.batch.kata.batch.classifier;

import com.batch.kata.batch.domain.ApiRequest;
import com.batch.kata.batch.domain.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

import java.util.Map;

@RequiredArgsConstructor
public class WriterClassifier<C, T> implements Classifier<C, T> {

    private final Map<String, ItemWriter<ApiRequest>> writerMap;

    @Override
    public T classify(C classifiable) {
        return (T) writerMap.get(((ApiRequest)classifiable).productDto().type());
    }
}
