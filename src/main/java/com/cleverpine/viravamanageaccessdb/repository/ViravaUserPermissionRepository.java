package com.cleverpine.viravamanageaccessdb.repository;

import com.cleverpine.viravamanageaccessdb.entity.ViravaUserPermissionEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViravaUserPermissionRepository extends CrudRepository<ViravaUserPermissionEntity, Long> {

    @Modifying
    @Query("delete from virava_user_permissions where user.id = :userId")
    void deleteAllByUserId(@Param("userId") long userId);

    List<ViravaUserPermissionEntity> findAllByUserId(long userId);
}