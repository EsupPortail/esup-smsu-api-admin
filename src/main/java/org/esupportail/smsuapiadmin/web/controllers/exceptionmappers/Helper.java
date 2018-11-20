package org.esupportail.smsuapiadmin.web.controllers.exceptionmappers;

import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class Helper {
	
    public static ResponseEntity<?> jsonErrorResponse(HttpStatus status, String error) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("error", error);
        return new ResponseEntity<ObjectNode>(node, headers("Content-Type", "application/json;charset=UTF-8"), status);
    }
    
    private static MultiValueMap<String, String> headers(String k, String v){
    	MultiValueMap<String, String> r = new LinkedMultiValueMap<>();
    	r.add(k, v);
    	return r;
    }
}
