package com.cleverpine.viravamanageaccessdb.service.impl.db;

import com.cleverpine.viravabackendcommon.dto.Resource;
import com.cleverpine.viravabackendcommon.dto.ResourcePermission;
import com.cleverpine.viravabackendcommon.dto.ResourcePermissions;
import com.cleverpine.viravamanageaccessdb.entity.*;
import com.cleverpine.viravamanageaccessdb.exception.ResourcePermissionAlreadyAssignedException;
import com.cleverpine.viravamanageaccessdb.repository.ViravaResourcePermissionRepository;
import com.cleverpine.viravamanageaccessdb.repository.ViravaUserPermissionRepository;
import com.cleverpine.viravamanageaccessdb.util.ViravaConstants;
import com.cleverpine.viravamanageacesscore.service.contract.permission.AMPermissionService;
import com.cleverpine.viravamanageacesscore.service.contract.resource.AMResourceService;
import com.cleverpine.viravamanageacesscore.service.contract.userpermission.AMUserPermissionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cleverpine.viravamanageaccessdb.util.ViravaConstants.RESOURCE_NOT_FOUND_ERROR;
import static com.cleverpine.viravamanageaccessdb.util.ViravaConstants.USER_PERMISSION_RESOURCE_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
public class ViravaUserPermissionService implements AMUserPermissionService {

    private final ViravaResourcePermissionRepository viravaResourcePermissionRepository;
    private final ViravaUserPermissionRepository viravaUserPermissionRepository;
    private final AMPermissionService amPermissionService;
    private final AMResourceService amResourceService;

    @Override
    public void assignPermission(long userId, long permissionId) {
        assignPermissions(userId, new long[] {permissionId});
    }

    @Override
    public void assignResourcePermission(long userId, String resourceName, ResourcePermission resourcePermission) {
        var resourcePermissions = new ResourcePermissions();
        resourcePermissions.addResourcePermission(resourceName, resourcePermission);

        assignResourcePermissions(userId, resourcePermissions);
    }

    @Override
    public void assignPermissions(long userId, long[] permissionIds) {
        checkIfUserHasAssignedPermissions(userId, permissionIds);

        var userPermissionEntityList = Arrays.stream(permissionIds)
                .mapToObj(permissionId -> createViravaUserPermissionEntity(userId, permissionId))
                .collect(Collectors.toList());

        viravaUserPermissionRepository.saveAll(userPermissionEntityList);
    }

    @Override
    public void assignResourcePermissions(long userId, ResourcePermissions resourcePermissions) {
        checkIfUserHasAssignedResourcePermissions(userId, resourcePermissions);

        var resourceMap = createResourceMap();
        var resourcePermissionEntityList = resourcePermissions.getResourcePermissionMap()
                .entrySet()
                .stream()
                .map(entry ->
                        createViravaResourcePermissionEntity(userId, entry.getKey(), entry.getValue(), resourceMap))
                .collect(Collectors.toList());

        viravaResourcePermissionRepository.saveAll(resourcePermissionEntityList);
    }

    @Override
    public void deleteResourcePermissionsByUserId(long userId) {
        viravaResourcePermissionRepository.deleteAllByUserId(userId);
    }

    @Override
    public void deletePermissionsByUserId(long userId) {
        viravaUserPermissionRepository.deleteAllByUserId(userId);
    }

    @Override
    public void deletePermissionByResourceAndId(String resourceName, long id) {
        var resources = createResourceMap();
        var resource = getResourceByName(resourceName, resources);
        var idAsString = String.valueOf(id);
        var resourcePermissions = viravaResourcePermissionRepository.findAllByResourceIdAndId(resource.getId(), idAsString);

        if (resourcePermissions.isEmpty()) {
            return;
        }

        resourcePermissions.forEach(resourcePermissionEntity -> {
            var filteredIds = Arrays.stream(resourcePermissionEntity.getIds())
                    .filter(resourcePermissionId -> !Objects.equals(resourcePermissionId, idAsString))
                    .toArray(String[]::new);

            resourcePermissionEntity.setIds(filteredIds);
        });

        viravaResourcePermissionRepository.saveAll(resourcePermissions);
    }

    private ViravaUserPermissionEntity createViravaUserPermissionEntity(long userId, long permissionId) {
        var userPermissionEntity = new ViravaUserPermissionEntity();

        var userEntity = new ViravaUserEntity();
        userEntity.setId(userId);
        userPermissionEntity.setUser(userEntity);

        checkPermissionExists(permissionId);

        var permissionEntity = new ViravaPermissionEntity();
        permissionEntity.setId(permissionId);
        userPermissionEntity.setPermission(permissionEntity);

        return userPermissionEntity;
    }

    private void checkPermissionExists(long permissionId) {
        if (!amPermissionService.checkIfExist(permissionId)) {
            throw new EntityNotFoundException(String.format(RESOURCE_NOT_FOUND_ERROR, permissionId));
        }
    }

    private ViravaResourcePermissionEntity createViravaResourcePermissionEntity(long userId, String resourceName,
                                                                                ResourcePermission resourcePermission,
                                                                                Map<String, Resource> resourceMap) {
        var resourcePermissionEntity = new ViravaResourcePermissionEntity();

        var userEntity = new ViravaUserEntity();
        userEntity.setId(userId);
        resourcePermissionEntity.setUser(userEntity);

        var resourceEntity = new ViravaResourceEntity();
        resourceEntity.setId(getResourceByName(resourceName, resourceMap).getId());
        resourcePermissionEntity.setResource(resourceEntity);

        resourcePermissionEntity.setAll(resourcePermission.isAll());
        resourcePermissionEntity.setIds(resourcePermission.getIds().toArray(new String[0]));

        return resourcePermissionEntity;
    }

    private Resource getResourceByName(String resourceName, Map<String, Resource> resourceMap) {
        if (!resourceMap.containsKey(resourceName)) {
            throw new EntityNotFoundException(String.format(USER_PERMISSION_RESOURCE_NOT_FOUND_ERROR, resourceName));
        }
        return resourceMap.get(resourceName);
    }

    private void checkIfUserHasAssignedResourcePermissions(long userId, ResourcePermissions resourcePermissions) {
        var userResourcePermissions = viravaResourcePermissionRepository.findAllByUserId(userId);

        var resourcePermissionMap = resourcePermissions.getResourcePermissionMap();

        var alreadyAssignedPermissions = userResourcePermissions
                .stream()
                .filter(x -> resourcePermissionMap.containsKey(x.getResource().getName()))
                .map(x -> x.getResource().getName())
                .collect(Collectors.joining(","));

        if (alreadyAssignedPermissions.isBlank()) {
            return;
        }

        throw new ResourcePermissionAlreadyAssignedException(
                String.format(ViravaConstants.USER_ALREADY_HAS_RESOURCE_PERMISSION_ERROR, alreadyAssignedPermissions));
    }

    private void checkIfUserHasAssignedPermissions(long userId, long[] permissionIds) {
        var userPermissions = viravaUserPermissionRepository.findAllByUserId(userId);
        var permissionIdsList = Arrays.stream(permissionIds).boxed().collect(Collectors.toList());

        var alreadyAssignedPermissions = userPermissions
                .stream()
                .filter(x -> permissionIdsList.contains(x.getPermission().getId()))
                .map(x -> x.getPermission().getId().toString())
                .collect(Collectors.joining(","));

        if (alreadyAssignedPermissions.isBlank()) {
            return;
        }

        throw new ResourcePermissionAlreadyAssignedException(
                String.format(ViravaConstants.USER_ALREADY_HAS_PERMISSION_ERROR, alreadyAssignedPermissions));
    }

    private Map<String, Resource> createResourceMap() {
        return amResourceService.getAll()
                .stream()
                .collect(Collectors.toMap(Resource::getName, Function.identity()));
    }
}
