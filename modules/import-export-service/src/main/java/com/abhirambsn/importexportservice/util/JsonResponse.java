package com.abhirambsn.importexportservice.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class JsonResponse {
    public static <T> ResponseEntity<Object> generateResponse(
            String message,
            HttpStatus statusCode,
            T responseObj
    ) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", message);
        map.put("status", statusCode.value());
        map.put("data", responseObj);
        map.put("success", statusCode.is2xxSuccessful());

        return new ResponseEntity<>(map, statusCode);
    }
}
