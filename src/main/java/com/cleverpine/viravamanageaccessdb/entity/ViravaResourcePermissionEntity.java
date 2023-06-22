package com.cleverpine.viravamanageaccessdb.entity;

import com.cleverpine.viravamanageaccessdb.converter.ResourcePermissionIdConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "virava_resource_permissions")
public class ViravaResourcePermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ViravaUserEntity user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id")
    private ViravaResourceEntity resource;
    @Column(name = "all_ids")
    private boolean all;
    @Column(name = "ids")
    @Convert(converter = ResourcePermissionIdConverter.class)
    private String[] ids;
}
