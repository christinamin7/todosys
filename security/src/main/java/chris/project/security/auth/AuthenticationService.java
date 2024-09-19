package chris.project.security.auth;

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
import chris.project.security.token.Token;
import chris.project.security.token.TokenRepository;
import chris.project.security.token.TokenType;
import chris.project.security.user.User;
import chris.project.security.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TokenRepository tokenRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                //  .role(Role.USER)
                .build();
        if (repository.findByEmail(request.getEmail()).isEmpty()) {
            var savedUser = repository.save(user);
            var jwtToken = jwtService.generateToken(user);
            saveUserToken(savedUser, jwtToken);
            return AuthenticationResponse.builder().token(jwtToken).build();

        } else {
            return null;
        }

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        sendEmail(request.getEmail());

        return AuthenticationResponse.builder().token(jwtToken).build();

    }
// send email to user

    public void sendEmail(String toEmail) {
        String otpCode = generateOtp();
        String emailBody = String.format(
                "Dear customer ,\n\n"
                + "To complete your login, please use the one-time verification code below:\n\n"
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
    // Generate a random 6-digit OTP

    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    //save code for user
    public void saveCode(String email, String otpCode) {
        Optional<User> optionalUser = repository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();  // Fetch the existing user
            user.setCode(otpCode);           // Update only the code field
            repository.save(user);           // Save the updated user entity

        }
    }

    public String codeVerify(CodeVerificationRequest request) {
        Optional<User> optionalUser = repository.findByEmail(request.getEmail());
        String message = "";

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (request.getOtpCode().equals(user.getCode())) {
                message = "Valid code";
            }
        } else {
            message = "User with email " + request.getEmail() + " not found";
        }
        return message;
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
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
}
