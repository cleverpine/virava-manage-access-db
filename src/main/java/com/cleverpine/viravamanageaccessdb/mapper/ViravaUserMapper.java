package com.cleverpine.viravamanageaccessdb.mapper;

import com.cleverpine.viravabackendcommon.dto.User;
import com.cleverpine.viravamanageaccessdb.entity.ViravaPermissionEntity;
import com.cleverpine.viravamanageaccessdb.entity.ViravaResourcePermissionEntity;
import com.cleverpine.viravamanageaccessdb.entity.ViravaUserEntity;
import org.aspectj.lang.annotation.After;
import org.mapstruct.*;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.cleverpine.viravamanageaccessdb.util.ViravaConstants.ALL;
import static com.cleverpine.viravamanageaccessdb.util.ViravaConstants.DELIMETER;

@Mapper(componentModel = "spring")
public interface ViravaUserMapper extends ViravaResourcePermissionMapper {

    User viravaUserEntityToUser(ViravaUserEntity viravaUserEntity);

    List<User> viravaUserEntityListToUserList(List<ViravaUserEntity> viravaUserEntityList);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "permissions", ignore = true),
            @Mapping(target = "resourcePermissions", ignore = true)
    })
    ViravaUserEntity userToViravaUserEntity(User user);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "username", ignore = true),
            @Mapping(target = "permissions", ignore = true),
            @Mapping(target = "resourcePermissions", ignore = true)
    })
    void userToViravaUserEntity(User user, @MappingTarget ViravaUserEntity viravaUserEntity);
}
