package chris.project.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chris.project.security.request.AuthenticationRequest;
import chris.project.security.request.CodeResendRequest;
import chris.project.security.request.CodeVerificationRequest;
import chris.project.security.request.RegisterRequest;
import chris.project.security.response.AuthenticationResponse;
import chris.project.security.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.authenticate(request));
    }

    @PostMapping("/codeVerify")
    public ResponseEntity<String> codeVerify(@Valid @RequestBody CodeVerificationRequest request) {
        try {
            return ResponseEntity.ok(service.codeVerify(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(("An error occoured " + e.getMessage()));

        }
    }

    @GetMapping("/resendCode")
    public ResponseEntity<String> resendCode(@Valid @RequestBody CodeResendRequest request) {
        try {
            return ResponseEntity.ok(service.codeResend(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(("An error occoured " + e.getMessage()));

        }

    }

}
