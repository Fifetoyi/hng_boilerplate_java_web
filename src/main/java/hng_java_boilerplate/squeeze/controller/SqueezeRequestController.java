package hng_java_boilerplate.squeeze.controller;

import hng_java_boilerplate.squeeze.entity.SqueezeRequest;
import hng_java_boilerplate.squeeze.exceptions.DuplicateEmailException;
import hng_java_boilerplate.squeeze.service.SqueezeRequestService;
import hng_java_boilerplate.squeeze.util.ResponseMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/squeeze")
@Validated
public class SqueezeRequestController {

    @Autowired
    private SqueezeRequestService service;

    @PostMapping
    public ResponseEntity<?> handleSqueezeRequest(@Valid @RequestBody SqueezeRequest request) {
        try {
            service.saveSqueezeRequest(request);
            return ResponseEntity.ok().body(new ResponseMessage("Your request has been received. You will get a template shortly.", HttpStatus.OK.value()));
        } catch (DuplicateEmailException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage(e.getMessage(), HttpStatus.CONFLICT.value()));
        }
    }

}