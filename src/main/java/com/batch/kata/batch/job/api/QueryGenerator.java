package com.batch.kata.batch.job.api;

import com.batch.kata.batch.domain.ProductDto;
import com.batch.kata.batch.rowmapper.ProductRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QueryGenerator {
    public static ProductDto[] getProductDtos(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<ProductDto> query = jdbcTemplate.query("select type from product group by type", new ProductRowMapper());
        return query.toArray(new ProductDto[]{});
    }

    public static Map<String, Object> setParameterValues(String parameter, String value) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(parameter, value);
        return parameters;
    }
}
