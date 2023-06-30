package com.cleverpine.viravamanageaccessdb.repository;

import com.cleverpine.viravamanageaccessdb.entity.ViravaPermissionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ViravaPermissionRepository extends CrudRepository<ViravaPermissionEntity, Long> {

    List<ViravaPermissionEntity> findAll();

    boolean existsByName(String name);

    Optional<ViravaPermissionEntity> findByName(String name);
}
