package com.api.campuslink.services;

import com.api.campuslink.dao.QueryRepository;
import com.api.campuslink.exceptions.InternalProcessingException;
import com.api.campuslink.helpers.Result;
import com.api.campuslink.models.dto.QueryDTO;
import com.api.campuslink.models.entities.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class QueryService {

    @Autowired
    private QueryRepository queryRepository;

    public Result<String> addQuery(QueryDTO queryDto) {
        log.info("Got request for adding query: {}", queryDto);

        Query query = buildQuery(queryDto);
        try {
            Query createdQuery = queryRepository.save(query);
            log.info("Query added successfully with ID: {}", createdQuery.getQueryId());
            return Result.success("Query added successfully with ID: " + createdQuery.getQueryId());
        } catch (InternalProcessingException exception) {
            log.error("Internal processing error while adding query: {}", exception.getMessage());
            return Result.error("Internal processing error", exception.getResponseStatus());
        } catch (Exception e) {
            log.error("Error while adding query: {}", e.getMessage());
            return Result.error("Failed to add query", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Result<?> getAllQueries() {
        log.info("Fetching all queries");
        try {
            Iterable<Query> queries = queryRepository.findAll();

            if (!queries.iterator().hasNext()) {
                log.info("No queries found");
                return Result.error("No queries found",HttpStatus.NOT_FOUND);
            }
            return Result.success(queries);
        } catch (Exception e) {
            log.error("Error while fetching all queries: {}", e.getMessage());
            return Result.error("Failed to fetch queries", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public Query buildQuery(QueryDTO queryDto) {
        Query query = new Query();
        query.setQueryId(queryDto.getQueryId());
        query.setQueryType(queryDto.getQueryType());
        query.setQueryTitle(queryDto.getQueryTitle());
        query.setQueryDescription(queryDto.getQueryDescription());
        query.setQueryStatus(queryDto.getQueryStatus());
        query.setQueryPriority(queryDto.getQueryPriority());
        query.setPostedBy(queryDto.getPostedBy());
        query.setPostedOn(LocalDate.now());
        query.setAnswers(null);
        return query;
    }
}
