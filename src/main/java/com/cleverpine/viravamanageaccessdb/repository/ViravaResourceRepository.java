package com.cleverpine.viravamanageaccessdb.repository;

import com.cleverpine.viravamanageaccessdb.entity.ViravaResourceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViravaResourceRepository extends CrudRepository<ViravaResourceEntity, Long> {

    List<ViravaResourceEntity> findAll();

    boolean existsByName(String name);
}
