package com.github.rafaelfernandes.login.adapter.in.web;

import com.github.rafaelfernandes.user.common.annotations.WebAdapter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController
@RequestMapping("/api/login")
@AllArgsConstructor
@Tag(name = "Login", description = "Login Endpoint")
public class LoginController {
}
