package com.cleverpine.viravamanageaccessdb.service.impl.db;

import com.cleverpine.viravabackendcommon.dto.Permission;
import com.cleverpine.viravamanageaccessdb.entity.ViravaPermissionEntity;
import com.cleverpine.viravamanageaccessdb.mapper.ViravaPermissionMapper;
import com.cleverpine.viravamanageaccessdb.repository.ViravaPermissionRepository;
import com.cleverpine.viravamanageacesscore.service.contract.permission.AMPermissionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.cleverpine.viravamanageaccessdb.util.ViravaConstants.*;

@Service
@RequiredArgsConstructor
public class ViravaPermissionService implements AMPermissionService {

    private final ViravaPermissionRepository viravaPermissionRepository;
    private final ViravaPermissionMapper viravaPermissionMapper;

    @Override
    public List<Permission> getAll() {
        return viravaPermissionMapper.viravaPermissionEntityListToPermissionList(viravaPermissionRepository.findAll());
    }

    @Override
    public Permission create(Permission permission) {
        validatePermission(permission);
        var viravaPermissionEntity = viravaPermissionMapper.permissionToViravaPermissionEntity(permission);

        var savedPermissionEntity = viravaPermissionRepository.save(viravaPermissionEntity);

        return viravaPermissionMapper.viravaPermissionEntityToPermission(savedPermissionEntity);
    }

    @Override
    public Permission get(long id) {
        var permissionOptional = viravaPermissionRepository.findById(id);
        if (permissionOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format(PERMISSION_NOT_FOUND_ERROR, id));
        }
        return viravaPermissionMapper.viravaPermissionEntityToPermission(permissionOptional.get());
    }

    @Override
    public boolean checkIfExist(long id) {
        return viravaPermissionRepository.existsById(id);
    }

    private void validatePermission(Permission permission) {
        if (permission == null) {
            throw new IllegalArgumentException(PERMISSION_IS_NULL_ERROR);
        }
        if (permission.getName() == null || permission.getName().isBlank()) {
            throw new IllegalArgumentException(PERMISSION_NAME_CANNOT_BE_NULL_ERROR);
        }
        if (checkIfExistsByName(permission.getName())) {
            throw new IllegalArgumentException(String.format(PERMISSION_NAME_ALREADY_EXISTS_ERROR, permission.getName()));
        }
    }

    private boolean checkIfExistsByName(String name) {
        return viravaPermissionRepository.existsByName(name);
    }
}
