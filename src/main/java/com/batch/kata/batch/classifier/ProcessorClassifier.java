package com.batch.kata.batch.classifier;

import com.batch.kata.batch.domain.ApiRequest;
import com.batch.kata.batch.domain.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

import java.util.Map;

@RequiredArgsConstructor
public class ProcessorClassifier<C, T> implements Classifier<C, T> {

    private final Map<String, ItemProcessor<ProductDto, ApiRequest>> processorMap;

    @Override
    public T classify(C classifiable) {
        return (T) processorMap.get(((ProductDto) classifiable).type());
    }
}
