package com.github.rafaelfernandes.login.application.domain.service;

import com.github.rafaelfernandes.common.annotations.UseCase;
import com.github.rafaelfernandes.common.enums.UserType;
import com.github.rafaelfernandes.common.exceptions.UnauthorizedException;
import com.github.rafaelfernandes.login.application.domain.model.Login;
import com.github.rafaelfernandes.login.application.port.in.AuthenticateUseCase;
import com.github.rafaelfernandes.login.application.port.in.TokenUseCase;
import com.github.rafaelfernandes.login.application.port.out.LoginPort;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.UUID;

@UseCase
@AllArgsConstructor
public class AuthenticateService implements AuthenticateUseCase {

    private final AuthenticationManager authenticationManager;
    private final TokenUseCase tokenUseCase;

    private final LoginPort loginPort;

    @Override
    public String authenticate(Login login) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            login.getCellphone(),
                            login.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException();
        }

        var loginHash = loginPort.findByCellphone(login.getCellphone())
                .orElseThrow(UnauthorizedException::new);

        return tokenUseCase.generateToken(
                loginHash.getCellphone(),
                loginHash.getUserType(),
                loginHash.getUserId()
        );


    }

    @Override
    public void isInvalid(String token) {
        Boolean isInvalid =  tokenUseCase.isInvalid(token);

        if (isInvalid) {
            throw new UnauthorizedException();
        }
    }
}
