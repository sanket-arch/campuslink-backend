package com.api.campuslink.controllers;

import com.api.campuslink.helpers.Result;
import com.api.campuslink.models.dto.QueryDTO;
import com.api.campuslink.services.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/query")
public class QueryController {

    @Autowired
    private QueryService queryService;

    @PostMapping("/add")
    public ResponseEntity<?> addQuery(@RequestBody QueryDTO queryDTO) {
        Result<String> result = queryService.addQuery(queryDTO);
        return new ResponseEntity<>(result.getData(), result.getHttpStatus());
    }
     @GetMapping("/all")
    public ResponseEntity<?> getAllQueries() {
         Result<?> result = queryService.getAllQueries();
         if(!result.isSuccess()) {
             return new ResponseEntity<>(result.getError(), result.getHttpStatus());
         }
         return new ResponseEntity<>(result.getData(), result.getHttpStatus());
     }
}
