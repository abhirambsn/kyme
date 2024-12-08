package com.abhirambsn.expenseservice.service;

import com.abhirambsn.expenseservice.entity.SearchQuery;
import com.abhirambsn.expenseservice.util.QueryParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SearchService {
    private final MongoTemplate mongoTemplate;

    public SearchService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<?> search(
            String collectionName,
            SearchQuery queryRequest
    ) {
        Query query = new Query();

        if (queryRequest.getFilter() != null) {
            Criteria criteria = QueryParser.parseFilter(queryRequest.getFilter());
            query.addCriteria(criteria);
        }

        if (queryRequest.getOrderBy() != null) {
            String[] sortParts = queryRequest.getOrderBy().split(" ");
            if (sortParts.length == 2) {
                String field = sortParts[0];
                Sort.Direction direction = sortParts[1].equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;
                query.with(Sort.by(direction, field));
            }
        }

        if (queryRequest.getSkip() != null) {
            query.skip(queryRequest.getSkip());
        }
        if (queryRequest.getTop() != null) {
            query.limit(queryRequest.getTop());
        }

        return mongoTemplate.find(query, Object.class, collectionName);
    }

}
