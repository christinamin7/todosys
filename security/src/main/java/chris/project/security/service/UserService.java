package chris.project.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import chris.project.security.config.JWTService;
import chris.project.security.entity.User;
import chris.project.security.repository.UserRepository;
import chris.project.security.request.CodeResendRequest;
import chris.project.security.request.ResetPasswordRequest;
import chris.project.security.request.UserInfoRequest;
import chris.project.security.request.UserProfileRequest;
import chris.project.security.response.AuthenticationResponse;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private JWTService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileUploadService fileUploadService;

    public String codeResend(CodeResendRequest request) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
            String message = "";

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                authenticationService.sendEmail(user.getEmail(), "reset password");
                message = "Code sent  successfully!";

            } else {
                message = "User with email " + request.getEmail() + " not found";
            }
            return message;
        } catch (Exception e) {
            throw new RuntimeException("Service error: " + e.getMessage());
        }
    }

    public AuthenticationResponse resetPassword(ResetPasswordRequest request) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
            String message = "";
            String jwtToken = "";
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (request.getOtpCode().equals(user.getCode())) {
                    if (request.getNewPassword().equals(request.getConfirmPasswod())) {
                        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                        jwtToken = jwtService.generateToken(user);
                        authenticationService.revokeAllUserTokens(user);
                        authenticationService.saveUserToken(user, jwtToken);
                        authenticationService.sendSuccessEmail(user.getEmail());

                    } else {
                        message = "Password is not match.";
                    }
                    message = jwtToken;
                } else {

                    message = "Invalid code";
                }
            } else {
                message = "User with email " + request.getEmail() + " not found";
            }
            return AuthenticationResponse.builder().token(jwtToken).build();

        } catch (Exception e) {
            throw new RuntimeException("Service error: " + e.getMessage());
        }
    }

    public String updateUserInfo(UserInfoRequest request) {
        try {
            String message = "";
            Optional<User> existUser = userRepository.findByEmail(request.getEmail());
            if (existUser.isPresent()) {
                var updateUser = existUser.get();
                updateUser.setFirstName(request.getFirstName());
                updateUser.setLastName(request.getLastName());
                userRepository.save(updateUser);
                message = "update successful";
            } else {
                message = "user not found with " + request.getEmail();

            }
            return message;
        } catch (Exception e) {
            throw new RuntimeException("Service error: " + e.getMessage());
        }

    }

    public String updateUserProfile(UserProfileRequest request) {
        String message = "";
        Optional<User> existUser = userRepository.findByEmail(request.getEmail());
        if (existUser.isPresent()) {
            var updateUser = existUser.get();
            String filePath = fileUploadService.uploadImage(request.getProfilePath(), "user");
            updateUser.setProfilePath(filePath);
            userRepository.save(updateUser);
        } else {
            message = "user not found with " + request.getEmail();

        }
        return message;

    }

}
