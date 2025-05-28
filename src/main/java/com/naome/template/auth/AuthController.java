package com.naome.template.auth;

import com.naome.template.auth.dtos.*;
import com.naome.template.auth.otp.OtpService;
import com.naome.template.auth.otp.OtpType;
import com.naome.template.email.EmailService;
import com.naome.template.commons.exceptions.BadRequestException;
import com.naome.template.user.UserService;
import com.naome.template.user.dtos.UserResponseDTO;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;


    @PostMapping("/register")
    @RateLimiter(name = "auth-rate-limiter")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody
                                                        RegisterRequestDTO user, UriComponentsBuilder uriBuilder){
        var userResponse = userService.createUser(user);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userResponse.id()).toUri();
        // Use otp service to send otp to a registered user
        var otpToSend = otpService.generateOtp(userResponse.email(), OtpType.VERIFY_ACCOUNT);

        // Send email to the user with the OTP
        emailService.sendAccountVerificationEmail(userResponse.email(), userResponse.firstName(), otpToSend);
        return ResponseEntity.created(uri).body(userResponse);
    }

    @PatchMapping("/verify-account")
    @RateLimiter(name = "auth-rate-limiter")
    ResponseEntity<?> verifyAccount(@Valid @RequestBody VerifyAccountDto verifyAccountRequest){
        if(!otpService.verifyOtp(verifyAccountRequest.email(), verifyAccountRequest.otp(), OtpType.VERIFY_ACCOUNT))
            throw new BadRequestException("Invalid email or OTP");
        userService.activateUserAccount(verifyAccountRequest.email());
        return ResponseEntity.ok("Account Activated successfully");
    }

    @PostMapping("/login")
    @RateLimiter(name = "auth-rate-limiter")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDto, HttpServletResponse response) {
        var loginResult = authService.login(loginRequestDto, response);
        return ResponseEntity.ok(new LoginResponseDTO(loginResult.accessToken()));
    }


    @PostMapping("/initiate-password-reset")
    ResponseEntity<?> initiatePasswordReset(@Valid @RequestBody InitiatePasswordResetDTO initiateRequest){
        var otpToSend = otpService.generateOtp(initiateRequest.email(), OtpType.RESET_PASSWORD);
        var user = userService.findByEmail(initiateRequest.email());
        emailService.sendResetPasswordOtp(user.getEmail(), user.getFirstName(), otpToSend);
        return ResponseEntity.ok("If your email is registered, you will receive an email with instructions to reset your password.");
    }


    @PatchMapping("/reset-password")
    @RateLimiter(name = "auth-rate-limiter")
    ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordRequest){
        if(!otpService.verifyOtp(resetPasswordRequest.email(), resetPasswordRequest.otp(), OtpType.RESET_PASSWORD))
            throw new BadRequestException("Invalid email or OTP");
        userService.changeUserPassword(resetPasswordRequest.email(), resetPasswordRequest.newPassword());
        return ResponseEntity.ok("Password reset went successfully you can login with your new password.");
    }


}
