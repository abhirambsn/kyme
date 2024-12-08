package com.abhirambsn.tenantmetadataservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TenantMetadataResponseDto {
    private String id;
    private String name;
    private String slug;
    private String ownerId;
    private String planId;
    private String logo;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
