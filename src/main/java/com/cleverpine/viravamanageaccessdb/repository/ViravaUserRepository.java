package com.cleverpine.viravamanageaccessdb.repository;

import com.cleverpine.viravamanageaccessdb.entity.ViravaUserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ViravaUserRepository extends CrudRepository<ViravaUserEntity, Long> {

    List<ViravaUserEntity> findAll();

    Optional<ViravaUserEntity> findByUsername(String username);
}
