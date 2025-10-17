package com.example.demo.auth;

import com.example.demo.auth.request.AuthenticationRequest;
import com.example.demo.auth.request.RefreshRequest;
import com.example.demo.auth.request.RegistrationRequest;
import com.example.demo.auth.response.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse login(AuthenticationRequest request);

    void register(RegistrationRequest request);

    AuthenticationResponse refreshToken(RefreshRequest request);

}
