package com.cleverpine.viravamanageaccessdb.service.impl.db;

import com.cleverpine.viravabackendcommon.dto.Resource;
import com.cleverpine.viravamanageaccessdb.entity.ViravaResourceEntity;
import com.cleverpine.viravamanageaccessdb.mapper.ViravaResourceMapper;
import com.cleverpine.viravamanageaccessdb.repository.ViravaResourceRepository;
import com.cleverpine.viravamanageacesscore.service.contract.resource.AMResourceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.cleverpine.viravamanageaccessdb.util.ViravaConstants.*;

@Service
@RequiredArgsConstructor
public class ViravaResourceService implements AMResourceService {

    private final ViravaResourceRepository viravaResourceRepository;
    private final ViravaResourceMapper viravaResourceMapper;


    @Override
    public List<Resource> getAll() {
        var result = viravaResourceRepository.findAll();

        return viravaResourceMapper.viravaResourceEntityListToResourceList(result);
    }

    @Override
    public Resource create(Resource resource) {
        validateResource(resource);
        var viravaResourceEntity = viravaResourceMapper.resourceToViravaResourceEntity(resource);

        var savedViravaResourceEntity = viravaResourceRepository.save(viravaResourceEntity);

        return viravaResourceMapper.viravaResourceEntityToResource(savedViravaResourceEntity);
    }

    @Override
    public void deleteById(long id) {
        findById(id);
        viravaResourceRepository.deleteById(id);
    }

    private ViravaResourceEntity findById(long id) {
        return viravaResourceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(RESOURCE_NOT_FOUND_ERROR, id)));
    }

    private void validateResource(Resource resource) {
        if (resource == null) {
            throw new IllegalArgumentException(RESOURCE_CANNOT_BE_NULL);
        }
        if (resource.getName() == null || resource.getName().isBlank()) {
            throw new IllegalArgumentException(RESOURCE_NAME_CANNOT_BE_NULL);
        }
        if (checkIfExistsByName(resource.getName())) {
            throw new IllegalArgumentException(String.format(RESOURCE_NAME_ALREADY_EXISTS_ERROR, resource.getName()));
        }
    }

    private boolean checkIfExistsByName(String name) {
        return viravaResourceRepository.existsByName(name);
    }
}
