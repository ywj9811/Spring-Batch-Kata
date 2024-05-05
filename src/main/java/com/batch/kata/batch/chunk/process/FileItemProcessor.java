package com.batch.kata.batch.chunk.process;

import com.batch.kata.batch.domain.Product;
import com.batch.kata.batch.domain.ProductDto;
import org.springframework.batch.item.ItemProcessor;

public class FileItemProcessor implements ItemProcessor<ProductDto, Product> {
    @Override
    public Product process(ProductDto item) throws Exception {
        return new Product(item.id(), item.name(), item.price(), item.type());
    }
}
