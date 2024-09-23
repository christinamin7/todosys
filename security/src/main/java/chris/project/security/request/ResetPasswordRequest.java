package chris.project.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ResetPasswordRequest {

    @NotBlank(message = "Please provide a valid email")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    String email;
    @NotBlank(message = "OTP Code cannot be blank")
    @Size(min = 6, max = 6, message = "OTP Code must be 6 characters")
    String otpCode;
    @NotBlank(message = "Password is required")
    String newPassword;
    @NotBlank(message = "Confirm assword is required")
    String confirmPasswod;

}
