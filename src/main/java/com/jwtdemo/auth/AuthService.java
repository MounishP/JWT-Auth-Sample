package com.jwtdemo.auth;

import com.jwtdemo.config.JwtService;
import com.jwtdemo.model.User;
import com.jwtdemo.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegisterRequest registerRequest){
        var user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .mobileNo(registerRequest.getMobileNo())
                .alternativeNo(registerRequest.getAlternativeNo())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .collegeName(registerRequest.getCollegeName())
                .collegeLocation(registerRequest.getCollegeLocation())
                .degree(registerRequest.getDegree())
                .stream(registerRequest.getStream())
                .percentage(registerRequest.getPercentage())
                .passingYear(registerRequest.getPassingYear())
                .arrears(registerRequest.getArrears())
                .location(registerRequest.getLocation())
                .experience(registerRequest.getExperience())
                .languages(registerRequest.getLanguages())
                .role(registerRequest.getRole())
                .build();
        userRepo.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().accessToken(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepo.findByEmail(request.getEmail())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().accessToken(jwtToken).build();
    }
}
