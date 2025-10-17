package com.example.demo.auth.request;

import com.example.demo.validation.NonDisposableEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequest {

    @NotBlank(message = "VALIDATION.REGISTRATION.FIRSTNAME.NOT_BLANK")
    @Size(min = 2, max = 255, message = "VALIDATION.REGISTRATION.FIRSTNAME.SIZE")
    @Pattern(regexp = "^[\\p{L} '-]+$", message = "VALIDATION.REGISTRATION.FIRSTNAME.PATTERN")
    @Schema(example = "Azamat")
    private String firstName;

    @NotBlank(message = "VALIDATION.AUTHENTICATION.LASTNAME.NOT_BLANK")
    @Size(min = 2, max = 255, message = "VALIDATION.REGISTRATION.LASTNAME.SIZE")
    @Pattern(regexp = "^[\\p{L} '-]+$", message = "VALIDATION.REGISTRATION.LASTNAME.PATTERN")
    @Schema(example = "Khabibullaev")
    private String lastName;

    @Schema(example = "1995-07-15")
    private LocalDate dateOfBirth;

    @NotBlank(message = "VALIDATION.AUTHENTICATION.USERNAME.NOT_BLANK")
    @Size(min = 2, max = 255, message = "VALIDATION.REGISTRATION.USERNAME.SIZE")
    @Schema(example = "username")
    private String username;

    @NotBlank(message = "VALIDATION.REGISTRATION.EMAIL.NOT_BLANK")
    @Email(message = "VALIDATION.REGISTRATION.EMAIL.FORMAT")
    @Schema(example = "example@gmail.com")
    @NonDisposableEmail(message = "VALIDATION.REGISTRATION.EMAIL.DISPOSABLE")
    private String email;

    @NotBlank(message = "VALIDATION.REGISTRATION.PHONE.NOT_BLANK")
    @Pattern(regexp = "^\\+?[0-9]{10,13}$", message = "VALIDATION.REGISTRATION.PHONE.FORMAT")
    @Schema(example = "+998909179930")
    private String phoneNumber;

    @NotBlank(message = "VALIDATION.REGISTRATION.PASSWORD.NOT_BLANK")
    @Size(min = 8, max = 72, message = "VALIDATION.REGISTRATION.PASSWORD.SIZE")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*\\W).*$", message = "VALIDATION.REGISTRATION.PASSWORD.WEAK")
    @Schema(example = "<PASSWORD>")
    private String password;

    @NotBlank(message = "VALIDATION.REGISTRATION.CONFIRM_PASSWORD.NOT_BLANK")
    @Size(min = 8, max = 72, message = "VALIDATION.REGISTRATION.CONFIRM_PASSWORD.SIZE")
    @Schema(example = "<CONFIRM_PASSWORD>")
    private String confirmPassword;

}
