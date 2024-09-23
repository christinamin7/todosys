package chris.project.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chris.project.security.request.CodeResendRequest;
import chris.project.security.request.ResetPasswordRequest;
import chris.project.security.request.UserInfoRequest;
import chris.project.security.request.UserProfileRequest;
import chris.project.security.response.AuthenticationResponse;
import chris.project.security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @GetMapping("/forgetPassword")
    public ResponseEntity<String> forgetPassword(@Valid @RequestBody CodeResendRequest request) {
        try {
            return ResponseEntity.ok(userService.codeResend(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(("An error occoured " + e.getMessage()));

        }
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<AuthenticationResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            return ResponseEntity.ok(userService.resetPassword(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthenticationResponse("An error occoured " + e.getMessage()));

        }

    }

    @PutMapping("/updateUserInfo")
    public ResponseEntity<String> updateUserInfo(@Valid @RequestBody UserInfoRequest request) {
        try {
            return ResponseEntity.ok(userService.updateUserInfo(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(("An error occoured " + e.getMessage()));

        }

    }

    @PutMapping("/updateUserProfile")
    public ResponseEntity<String> updateUserProfile(@Valid @RequestBody UserProfileRequest request) {

        try {
            return ResponseEntity.ok(userService.updateUserProfile(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(("An error occoured " + e.getMessage()));

        }

    }
}
