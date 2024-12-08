package com.abhirambsn.tenantmetadataservice.mapper;

import com.abhirambsn.tenantmetadataservice.dto.TenantMetadataResponseDto;
import com.abhirambsn.tenantmetadataservice.entity.TenantMetadata;

public class TenantMetadataMapper {
    public static TenantMetadataResponseDto mapToTenantMetadataResponseDto(TenantMetadata metadata) {
        return TenantMetadataResponseDto.builder()
                .id(metadata.getId())
                .name(metadata.getName())
                .slug(metadata.getSlug())
                .planId(metadata.getPlanId())
                .ownerId(metadata.getOwnerId())
                .logo(metadata.getLogo())
                .createdAt(metadata.getCreatedAt())
                .updatedAt(metadata.getUpdatedAt())
                .build();
    }
}
