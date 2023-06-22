package com.cleverpine.viravamanageaccessdb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "virava_user_permissions")
public class ViravaUserPermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private ViravaUserEntity user;
    @ManyToOne
    @JoinColumn(name = "permission_id")
    private ViravaPermissionEntity permission;
}
