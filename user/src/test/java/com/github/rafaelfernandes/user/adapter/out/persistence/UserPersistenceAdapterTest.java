package com.github.rafaelfernandes.user.adapter.out.persistence;

import com.github.rafaelfernandes.common.enums.UserType;
import com.github.rafaelfernandes.common.utils.PasswordUtils;
import com.github.rafaelfernandes.user.application.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserPersistenceAdapterTest {

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserPersistenceAdapter userPersistenceAdapter;

    private User user;

    private String password;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        password = PasswordUtils.generatePassayPassword();
        user = User.of(
                UUID.randomUUID().toString(),
                UserType.EMPLOYEE.name(),
                "+12 34 56789-0955",
                password
        );

    }

    @Test
    public void testSave_Success() {
        var encryptedPassword = password;
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn(encryptedPassword);

        userPersistenceAdapter.save(user);

        verify(bCryptPasswordEncoder).encode(user.getPassword());
        verify(userRepository).save(any());
        assertEquals(encryptedPassword, user.getPassword());
    }

    @Test
    public void testFindByCellphone_UserFound() {
        when(userRepository.findByCellphone(user.getCellphone())).thenReturn(Optional.of(UserMapper.toEntity(user)));

        Optional<User> result = userPersistenceAdapter.findByCellphone(user.getCellphone());

        assertTrue(result.isPresent());
        assertEquals(user.getCellphone(), result.get().getCellphone());
        verify(userRepository).findByCellphone(user.getCellphone());
    }

    @Test
    public void testFindByCellphone_UserNotFound() {
        when(userRepository.findByCellphone(user.getCellphone())).thenReturn(Optional.empty());

        Optional<User> result = userPersistenceAdapter.findByCellphone(user.getCellphone());

        assertFalse(result.isPresent());
        verify(userRepository).findByCellphone(user.getCellphone());
    }
}