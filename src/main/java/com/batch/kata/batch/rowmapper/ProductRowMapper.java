package com.batch.kata.batch.rowmapper;

import com.batch.kata.batch.domain.ProductDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<ProductDto> {
    @Override
    public ProductDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ProductDto.builder()
                .type(rs.getString("type"))
                .build();
    }
}
