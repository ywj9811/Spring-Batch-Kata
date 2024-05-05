package com.batch.kata.batch.domain;

import lombok.Builder;

@Builder
public record ProductDto(Long id, String name, int price, String type) {
}
