package com.cleverpine.viravamanageaccessdb.service.impl.db;

import com.cleverpine.viravabackendcommon.dto.Permission;
import com.cleverpine.viravabackendcommon.dto.ResourcePermissions;
import com.cleverpine.viravabackendcommon.dto.User;
import com.cleverpine.viravamanageaccessdb.entity.ViravaUserEntity;
import com.cleverpine.viravamanageaccessdb.mapper.ViravaUserMapper;
import com.cleverpine.viravamanageaccessdb.repository.ViravaUserRepository;
import com.cleverpine.viravamanageacesscore.service.contract.user.AMUserService;
import com.cleverpine.viravamanageacesscore.service.contract.userpermission.AMUserPermissionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.cleverpine.viravamanageaccessdb.util.ViravaConstants.*;

@Service
@RequiredArgsConstructor
public class ViravaUserService implements AMUserService {

    private final ViravaUserRepository viravaUserRepository;
    private final ViravaUserMapper viravaUserMapper;
    private final AMUserPermissionService amUserPermissionService;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public User getById(long id) {
        validateUserId(id);
        return viravaUserRepository.findById(id)
                .map(viravaUserMapper::viravaUserEntityToUser)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_ERROR, id)));
    }

    @Override
    public User getByUsername(String username) {
        validateUsername(username);
        return viravaUserRepository.findByUsername(username)
                .map(viravaUserMapper::viravaUserEntityToUser)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_USERNAME_ERROR, username)));
    }

    @Override
    public List<User> getAll() {
        return viravaUserMapper.viravaUserEntityListToUserList(viravaUserRepository.findAll());
    }

    @Override
    @Transactional
    public User create(User user) {
        validateUser(user);

        validateUsernameDoesNotExist(user);

        var viravaUserEntity = viravaUserMapper.userToViravaUserEntity(user);
        var savedUserEntity = viravaUserRepository.save(viravaUserEntity);

        amUserPermissionService.assignPermissions(savedUserEntity.getId(), getPermissionIds(user));
        amUserPermissionService.assignResourcePermissions(savedUserEntity.getId(), user.getResourcePermissions());

        flushAndClearEntities();

        return viravaUserMapper.viravaUserEntityToUser(findById(savedUserEntity.getId()));
    }

    @Override
    @Transactional
    public User update(long id, User user) {
        validateUserId(id);
        validateUser(user);

        var viravaUserEntity = findById(id);

        viravaUserMapper.userToViravaUserEntity(user, viravaUserEntity);
        var savedUserEntity = viravaUserRepository.save(viravaUserEntity);
        clearPermissions(user.getId());

        amUserPermissionService.assignPermissions(id, getPermissionIds(user));
        amUserPermissionService.assignResourcePermissions(id, user.getResourcePermissions());

        flushAndClearEntities();

        return viravaUserMapper.viravaUserEntityToUser(findById(savedUserEntity.getId()));
    }

    @Override
    @Transactional
    public void delete(long id) {
        validateUserId(id);

        var userEntity = findById(id);
        clearPermissions(id);
        clearData(userEntity);

        viravaUserRepository.deleteById(id);
    }

    private void flushAndClearEntities() {
        entityManager.flush();
        entityManager.clear();
    }

    private long[] getPermissionIds(User user) {
        return user.getPermissions().stream().mapToLong(Permission::getId).toArray();
    }

    private void clearPermissions(long userId) {
        amUserPermissionService.deletePermissionsByUserId(userId);
        amUserPermissionService.deleteResourcePermissionsByUserId(userId);
    }

    private void clearData(ViravaUserEntity viravaUserEntity) {
        viravaUserEntity.setData(null);
    }

    private void validateUserId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException(ID_CANNOT_BE_NEGATIVE_ERROR);
        }
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException(USER_CANNOT_BE_NULL);
        }
        validateResourcePermissions(user.getResourcePermissions());
        validateUsername(user.getUsername());
    }

    private void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException(USERNAME_CANNOT_BE_NULL_ERROR);
        }
    }

    private void validateResourcePermissions(ResourcePermissions resourcePermissions) {
        var unableToParseStrings = new HashMap<String, String>();
        resourcePermissions.getResourcePermissionMap()
                .forEach((key, value) -> {
                    var invalidIds = value.getIds()
                            .stream()
                            .filter(id -> !isConvertibleToLong(id))
                            .collect(Collectors.joining(", "));

                    if (invalidIds.isBlank()) {
                        return;
                    }

                    unableToParseStrings.put(key, invalidIds);
                });

        if (unableToParseStrings.isEmpty()) {
            return;
        }

        var errorMessage = unableToParseStrings
                .entrySet()
                .stream()
                .map(entry -> String.format(RESOURCE_PERMISSION_ERROR_MESSAGE, entry.getKey(),
                        entry.getValue())).collect(Collectors.joining(", "));

        throw new IllegalArgumentException(String.format(UNABLE_TO_PARSE_RESOURCE_PERMISSIONS_ERROR, errorMessage));
    }

    public boolean isConvertibleToLong(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private ViravaUserEntity findById(long id) {
        return viravaUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_ERROR, id)));
    }

    private boolean usernameExists(String username) {
        return viravaUserRepository.findByUsername(username).isPresent();
    }

    private void validateUsernameDoesNotExist(User user) {
        if (usernameExists(user.getUsername())) {
            throw new IllegalArgumentException(String.format(USER_ALREADY_EXISTS_ERROR, user.getUsername()));
        }
    }
}
