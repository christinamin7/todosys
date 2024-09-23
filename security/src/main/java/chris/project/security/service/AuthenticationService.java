package chris.project.security.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import chris.project.security.config.JWTService;
import chris.project.security.constant.TokenType;
import chris.project.security.entity.Token;
import chris.project.security.entity.User;
import chris.project.security.exception.UserNotFoundException;
import chris.project.security.exception.ValidationException;
import chris.project.security.repository.TokenRepository;
import chris.project.security.repository.UserRepository;
import chris.project.security.request.AuthenticationRequest;
import chris.project.security.request.CodeResendRequest;
import chris.project.security.request.CodeVerificationRequest;
import chris.project.security.request.RegisterRequest;
import chris.project.security.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TokenRepository tokenRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        Map<String, String> errors = new HashMap<String, String>();
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            errors.put("password", "password does not match");
            throw new ValidationException(errors);
        }
        if (userRepository.findByEmail(request.getEmail()).isEmpty()) {
            var savedUser = userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            saveUserToken(savedUser, jwtToken);
            sendEmail(request.getEmail(), "login");
            return AuthenticationResponse.builder().token(jwtToken).build();
        } else {
            errors.put("email", "User already exist!");
            throw new ValidationException(errors);
        }

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        sendEmail(request.getEmail(), "login");
        return AuthenticationResponse.builder().token(jwtToken).build();

    }
// send email to user

    public void sendEmail(String toEmail, String reason) {
        String otpCode = generateOtp();
        User user = userRepository.findByEmail(toEmail).orElseThrow();
        String userName = user.getFirstName() + user.getLastName();
        String emailBody = String.format(
                "Dear " + userName + " ,\n\n"
                + "To complete your " + reason + ", please use the one-time verification code below:\n\n"
                + "Your Verification Code:" + otpCode + "\n\n"
                + "Thank you for using our service!\n\n"
                + "Best regards,\n"
                + "Fellowship Dev Team");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("christinamin.ucsp@mail.com");
        message.setTo(toEmail);
        message.setText(emailBody);
        message.setSubject("Your One-Time Verification Code");
        mailSender.send(message);
        saveCode(toEmail, otpCode);

    }

    public void sendSuccessEmail(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        User user = userRepository.findByEmail(toEmail).orElseThrow();
        String userName = user.getFirstName() + user.getLastName();
        message.setFrom("christinamin.ucsp@mail.com");
        message.setTo(toEmail);
        message.setText("Hi " + userName + ",\n\n"
                + "We're happy to inform you that your password has been successfully reset. "
                + "You can now log in to your account using your new password.\n\n"
                + "If you did not request this change or believe an unauthorized person has accessed your account, "
                + "please contact our support team immediately.\n\n"
                + "Thank you for using our product!\n\n"
                + "Best regards,\n"
                + "Fellowship Dev Team");
        message.setSubject("Your Password Has Been Successfully Reset");
        mailSender.send(message);

    }
    // Generate a random 6-digit OTP sendSuccessEmail

    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    //save code for user
    public void saveCode(String email, String otpCode) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();  // Fetch the existing user
            user.setCode(otpCode);           // Update only the code field
            userRepository.save(user);           // Save the updated user entity

        }
    }

    public String codeVerify(CodeVerificationRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        String message = "";

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (request.getOtpCode().equals(user.getCode())) {
                user.setEmail_verified_at(new Date());
                user.setCode(null);
                userRepository.save(user);
                message = "Email verified successfully!";
            }
        } else {
            message = "User with email " + request.getEmail() + " not found";
        }
        return message;
    }

    public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public String codeResend(CodeResendRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        String message = "";

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            sendEmail(user.getEmail(), "login");
            message = "Code sent  successfully!";

        } else {
            message = "User with email " + request.getEmail() + " not found";
        }
        return message;
    }

}
