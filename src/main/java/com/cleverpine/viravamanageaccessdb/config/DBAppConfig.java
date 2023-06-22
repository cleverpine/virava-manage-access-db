package com.cleverpine.viravamanageaccessdb.config;

import com.cleverpine.viravamanageaccessdb.mapper.*;
import com.cleverpine.viravamanageaccessdb.repository.*;
import com.cleverpine.viravamanageaccessdb.service.impl.db.ViravaResourceService;
import com.cleverpine.viravamanageaccessdb.service.impl.db.ViravaPermissionService;
import com.cleverpine.viravamanageaccessdb.service.impl.db.ViravaUserPermissionService;
import com.cleverpine.viravamanageaccessdb.service.impl.db.ViravaUserService;
import com.cleverpine.viravamanageacesscore.config.AMAppConfig;
import com.cleverpine.viravamanageacesscore.service.contract.resource.AMResourceService;
import com.cleverpine.viravamanageacesscore.service.contract.permission.AMPermissionService;
import com.cleverpine.viravamanageacesscore.service.contract.user.AMUserService;
import com.cleverpine.viravamanageacesscore.service.contract.userpermission.AMUserPermissionService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DBAppConfig extends AMAppConfig {

    private final ViravaUserRepository viravaUserRepository;
    private final ViravaResourceRepository viravaResourceRepository;
    private final ViravaPermissionRepository viravaPermissionRepository;
    private final ViravaResourcePermissionRepository viravaResourcePermissionRepository;
    private final ViravaUserPermissionRepository viravaUserPermissionRepository;
    private final EntityManager viravaEntityManager;

    @Bean
    public ViravaUserMapper viravaUserMapper() {
        return new ViravaUserMapperImpl();
    }

    @Bean
    public ViravaResourceMapper viravaResourceMapper() {
        return new ViravaResourceMapperImpl();
    }

    @Bean
    public ViravaPermissionMapper viravaPermissionMapper() {
        return new ViravaPermissionMapperImpl();
    }

    @Bean
    public ViravaResourcePermissionMapper viravaResourcePermissionMapper() {
        return new ViravaResourcePermissionMapperImpl();
    }

    @Bean
    @Override
    public AMResourceService amResourceService() {
        return new ViravaResourceService(viravaResourceRepository, viravaResourceMapper());
    }

    @Bean
    @Override
    public AMPermissionService amPermissionService() {
        return new ViravaPermissionService(viravaPermissionRepository, viravaPermissionMapper());
    }

    @Bean
    @Override
    public AMUserService amUserService() {
        return new ViravaUserService(viravaUserRepository, viravaUserMapper(), amUserPermissionService(),
                viravaEntityManager);
    }

    @Override
    public AMUserPermissionService amUserPermissionService() {
        return new ViravaUserPermissionService(viravaResourcePermissionRepository, viravaUserPermissionRepository,
                amPermissionService(), amResourceService());
    }
}
