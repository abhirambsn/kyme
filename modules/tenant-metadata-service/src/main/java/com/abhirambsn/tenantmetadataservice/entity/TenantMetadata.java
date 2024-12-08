package com.abhirambsn.tenantmetadataservice.entity;

import com.github.slugify.Slugify;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class TenantMetadata extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, updatable = false)
    private String name;

    @Column(unique = true, updatable = false)
    private String slug;

    @Column(nullable = false)
    private String logo;

    @Column(nullable = false)
    private String ownerId;

    @Column(nullable = false)
    private String planId;

    @PrePersist
    public void onPrePersist() {
        final Slugify slg = Slugify.builder().build();
        this.slug = slg.slugify(this.name);
    }
}
