package com.cleverpine.viravamanageaccessdb.mapper;

import com.cleverpine.viravabackendcommon.dto.Resource;
import com.cleverpine.viravamanageaccessdb.entity.ViravaResourceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ViravaResourceMapper {
    @Mapping(target = "id", ignore = true)
    ViravaResourceEntity resourceToViravaResourceEntity(Resource resource);

    Resource viravaResourceEntityToResource(ViravaResourceEntity viravaResourceEntity);

    List<Resource> viravaResourceEntityListToResourceList(List<ViravaResourceEntity> viravaResourceEntityList);
}
