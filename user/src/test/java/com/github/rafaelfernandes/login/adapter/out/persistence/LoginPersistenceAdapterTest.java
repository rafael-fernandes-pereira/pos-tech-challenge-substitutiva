package com.github.rafaelfernandes.login.adapter.out.persistence;

import com.github.rafaelfernandes.common.enums.UserType;
import com.github.rafaelfernandes.login.application.domain.model.LoginHash;
import com.github.rafaelfernandes.user.adapter.out.persistence.UserRepository;

import com.github.rafaelfernandes.user.adapter.out.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class LoginPersistenceAdapterTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginPersistenceAdapter loginPersistenceAdapter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByCellphone_UserExists() {
        String cellphone = "+12 34 56789-9675";
        UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setType(UserType.RESIDENT);
        userEntity.setCellphone(cellphone);
        userEntity.setPassword("password123");

        when(userRepository.findByCellphone(cellphone)).thenReturn(Optional.of(userEntity));

        Optional<LoginHash> result = loginPersistenceAdapter.findByCellphone(cellphone);

        assertTrue(result.isPresent());
        assertEquals(userEntity.getId().toString(), result.get().getUserId().toString());
        assertEquals(userEntity.getType().name(), result.get().getUserType().name());
        assertEquals(userEntity.getCellphone(), result.get().getCellphone());
        verify(userRepository).findByCellphone(cellphone);
    }

    @Test
    public void testFindByCellphone_UserDoesNotExist() {
        String cellphone = "+12 34 56789-9675";

        when(userRepository.findByCellphone(cellphone)).thenReturn(Optional.empty());

        Optional<LoginHash> result = loginPersistenceAdapter.findByCellphone(cellphone);

        assertTrue(result.isEmpty());
        verify(userRepository).findByCellphone(cellphone);
    }
}