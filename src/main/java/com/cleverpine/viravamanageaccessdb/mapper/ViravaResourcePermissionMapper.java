package com.cleverpine.viravamanageaccessdb.mapper;

import com.cleverpine.viravabackendcommon.dto.ResourcePermission;
import com.cleverpine.viravabackendcommon.dto.ResourcePermissions;
import com.cleverpine.viravamanageaccessdb.entity.ViravaResourcePermissionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ViravaResourcePermissionMapper {

    ResourcePermission mapToResourcePermission(ViravaResourcePermissionEntity entity);

    @Mapping(target = "resource.name", source = "resourceName")
    ViravaResourcePermissionEntity mapToEntity(String resourceName, ResourcePermission permission);

    default ResourcePermissions mapToResourcePermissions(List<ViravaResourcePermissionEntity> entities) {
        if (entities == null) {
            return null;
        }

        var resourcePermissionMap = new HashMap<String, ResourcePermission>();

        for (var permission : entities) {
            if (permission == null) {
                continue;
            }

            resourcePermissionMap.put(permission.getResource().getName(), mapToResourcePermission(permission));
        }

        return new ResourcePermissions(resourcePermissionMap);
    }

    default List<ViravaResourcePermissionEntity> mapToEntities(ResourcePermissions resourcePermissions) {
        if (resourcePermissions == null) {
            return null;
        }

        var entities = new ArrayList<ViravaResourcePermissionEntity>();

        var resourcePermissionMap = resourcePermissions.getResourcePermissionMap();
        for (var entry : resourcePermissionMap.entrySet()) {
            var resourceId = entry.getKey();
            var permission = entry.getValue();

            var entity = mapToEntity(resourceId, permission);
            entities.add(entity);
        }

        return entities;
    }
}
