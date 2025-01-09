package de.unibayreuth.se.taskboard.business.impl;

import de.unibayreuth.se.taskboard.business.domain.User;
import de.unibayreuth.se.taskboard.business.exceptions.DuplicateNameException;
import de.unibayreuth.se.taskboard.business.exceptions.MalformedRequestException;
import de.unibayreuth.se.taskboard.business.exceptions.UserNotFoundException;
import de.unibayreuth.se.taskboard.business.ports.UserPersistenceService;
import de.unibayreuth.se.taskboard.business.ports.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserPersistenceService userPersistenceService;

    @Override
    public void clear(){
        userPersistenceService.clear();
    }
    @Override
    @NonNull
    public User create(@NonNull User user) {
        if(user.getId() != null){
            throw new MalformedRequestException("User must not have an ID");
        }
        return upsert(user);
    }

    @Override
    @NonNull
    public List<User> getAll() {
        return userPersistenceService.getAll();
    }

    @Override
    @NonNull
    public  Optional<User> getById(@NonNull UUID id) throws UserNotFoundException {
        return userPersistenceService.getById(id);
    }

    @Override
    public @NonNull User upsert(@NonNull User user) throws UserNotFoundException, DuplicateNameException {
        if (user.getId() != null){
            verifyUserExists(user.getId());
        }
        return userPersistenceService.upsert(user);
    }

    private void verifyUserExists(@NonNull UUID id) throws UserNotFoundException {
        userPersistenceService.getById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " does not exist."));
    }
}
