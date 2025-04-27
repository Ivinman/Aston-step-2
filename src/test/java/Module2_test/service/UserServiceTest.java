package Module2_test.service;

import Module2.repository.RepositoryImpl;
import Module2.repository.User;
import Module2.service.UserService;
import Module2.utils.ValidatorUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private RepositoryImpl repository;

    @Mock
    private MockedStatic<ValidatorUtils> validatorUtilsSpy;

    @InjectMocks
    private UserService userService;


    //=== createUser ===

    @Test
    void createUser_success() {
        User valid = new User();
        when(repository.createUser(valid)).thenReturn(42L);

        long id = userService.createUser(valid);

        // Спай должен был вызвать реальный метод валидатора
        validatorUtilsSpy.verify(() -> ValidatorUtils.validate(valid), times(1));
        verify(repository, times(1)).createUser(valid);
        assertEquals(42L, id);
    }

    @Test
    void createUser_validationFails() {
        User invalid = new User();
        // Перехватываем вызов валидатора и бросаем исключение
        validatorUtilsSpy
                .when(() -> ValidatorUtils.validate(invalid))
                .thenThrow(new IllegalArgumentException("Invalid user"));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(invalid)
        );
        assertEquals("Invalid user", ex.getMessage());

        // Репозиторий при провале валидации не вызывается
        verify(repository, never()).createUser(any());
    }

    //=== updateUser ===

    @Test
    void updateUser_success() {
        User user = new User();
        when(repository.updateUser(user)).thenReturn(true);

        boolean result = userService.updateUser(user);

        validatorUtilsSpy.verify(() -> ValidatorUtils.validate(user), times(1));
        verify(repository, times(1)).updateUser(user);
        assertTrue(result);
    }

    @Test
    void updateUser_validationFails() {
        User invalid = new User();
        validatorUtilsSpy
                .when(() -> ValidatorUtils.validate(invalid))
                .thenThrow(new IllegalArgumentException("Bad data"));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUser(invalid)
        );
        assertEquals("Bad data", ex.getMessage());

        verify(repository, never()).updateUser(any());
    }

    @Test
    void updateUser_repositoryReturnsFalse() {
        User user = new User();
        when(repository.updateUser(user)).thenReturn(false);

        boolean result = userService.updateUser(user);

        // В позитивном кейсе валидатор всё так же вызывается
        validatorUtilsSpy.verify(() -> ValidatorUtils.validate(user), times(1));
        verify(repository, times(1)).updateUser(user);
        assertFalse(result);
    }

    //=== deleteUser ===

    @Test
    void deleteUser_success() {
        long id = 1L;
        when(repository.deleteUser(id)).thenReturn(true);

        assertTrue(userService.deleteUser(id));
        verify(repository, times(1)).deleteUser(id);
    }

    @Test
    void deleteUser_notFound() {
        long id = 2L;
        when(repository.deleteUser(id)).thenReturn(false);

        assertFalse(userService.deleteUser(id));
        verify(repository, times(1)).deleteUser(id);
    }

    //=== findAll ===

    @Test
    void findAll_returnsList() {
        List<User> users = Arrays.asList(new User(), new User());
        when(repository.getAllUsers()).thenReturn(users);

        List<User> result = userService.findAll();

        assertSame(users, result);
        assertEquals(2, result.size());
        verify(repository, times(1)).getAllUsers();
    }

    @Test
    void findAll_emptyList() {
        when(repository.getAllUsers()).thenReturn(Collections.emptyList());

        List<User> result = userService.findAll();

        assertTrue(result.isEmpty());
        verify(repository, times(1)).getAllUsers();
    }

    //=== findById ===

    @Test
    void findById_found() {
        long id = 7L;
        User user = new User();
        when(repository.getUserById(id)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(id);

        assertTrue(result.isPresent());
        assertSame(user, result.get());
        verify(repository, times(1)).getUserById(id);
    }

    @Test
    void findById_notFound() {
        long id = 8L;
        when(repository.getUserById(id)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(id);

        assertFalse(result.isPresent());
        verify(repository, times(1)).getUserById(id);
    }

    //=== exit ===

    @Test
    void exit_invokesRepositoryExit() {
        userService.exit();
        verify(repository, times(1)).exit();
    }
}
