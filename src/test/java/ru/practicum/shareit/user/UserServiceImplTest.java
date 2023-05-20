package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl service;
    @Mock
    private UserRepository repository;

    @Test
    void addUserTest() {
        User userToSave = User.builder().id(1L).name("name1").email("1@mail.com").build();
        when(repository.save(any(User.class))).thenReturn(userToSave);

        User actual = service.addUser(new User());

        assertThat(actual).usingRecursiveComparison().isEqualTo(userToSave);
        verify(repository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getUserByIdTest() {
        User expectedUser = User.builder().id(1L).name("name1").email("1@mail.com").build();
        when(repository.findById(anyLong())).thenReturn(Optional.of(expectedUser));

        User actual = service.getUserById(999L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedUser);
        verify(repository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getUserByIdNotFoundTest() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> service.getUserById(999L));
        verify(repository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getAllUsersTest() {
        when(repository.findAll()).thenReturn(List.of(new User(), new User()));

        assertThat(service.getAllUsers()).hasSize(2);
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteUserTest() {
        doNothing().when(repository).deleteById(anyLong());
        when(repository.findById(anyLong())).thenReturn(Optional.of(new User()));

        service.deleteUser(999L);
        verify(repository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void updateUserTest() {
        User userToUpdate = User.builder().id(1L).name("name1").email("1@mail.com").build();
        User user = User.builder().id(1L).name("name1update").email("1@mail.com").build();
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(userToUpdate));
        when(repository.save(any(User.class))).thenReturn(user);

        User actual = service.updateUser(user);

        assertThat(actual).usingRecursiveComparison().isEqualTo(user);
        verify(repository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(repository);
    }
}
