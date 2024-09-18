package chris.project.security.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CodeVerificationRequest {

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Please provide a valid email")
    private String email;
    @NotBlank(message = "OTP Code cannot be blank")
    @Size(min = 6, max = 6, message = "OTP Code must be 6 characters")
    private String otpCode;

}
