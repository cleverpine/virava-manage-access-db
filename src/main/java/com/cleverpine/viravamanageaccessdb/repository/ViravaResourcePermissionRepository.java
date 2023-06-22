package com.cleverpine.viravamanageaccessdb.repository;

import com.cleverpine.viravamanageaccessdb.entity.ViravaResourcePermissionEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViravaResourcePermissionRepository extends CrudRepository<ViravaResourcePermissionEntity, Long> {

    @Modifying
    @Query("delete from virava_resource_permissions where user.id = :userId")
    void deleteAllByUserId(@Param("userId") long userId);

    List<ViravaResourcePermissionEntity> findAllByUserId(long userId);
}
