package com.cleverpine.viravamanageaccessdb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "virava_users")
public class ViravaUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username")
    private String username;
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "data_key")
    @Column(name = "data_value")
    @CollectionTable(name = "virava_users_data", joinColumns = @JoinColumn(name = "user_id"))
    private Map<String, String> data = new HashMap<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "virava_user_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<ViravaPermissionEntity> permissions;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<ViravaResourcePermissionEntity> resourcePermissions;


}
