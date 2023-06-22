package com.cleverpine.viravamanageaccessdb.mapper;

import com.cleverpine.viravabackendcommon.dto.Permission;
import com.cleverpine.viravamanageaccessdb.entity.ViravaPermissionEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ViravaPermissionMapper {

    Permission viravaPermissionEntityToPermission(ViravaPermissionEntity viravaPermissionEntity);

    List<Permission> viravaPermissionEntityListToPermissionList(List<ViravaPermissionEntity> viravaPermissionEntityList);

    ViravaPermissionEntity permissionToViravaPermissionEntity(Permission permission);
}
