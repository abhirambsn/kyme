package com.abhirambsn.tenantmetadataservice.service;

import com.abhirambsn.tenantmetadataservice.dto.CreateNewTenantDto;
import com.abhirambsn.tenantmetadataservice.dto.TenantMetadataResponseDto;
import com.abhirambsn.tenantmetadataservice.dto.UpdateTenantDto;
import com.abhirambsn.tenantmetadataservice.entity.TenantMetadata;
import com.abhirambsn.tenantmetadataservice.mapper.TenantMetadataMapper;
import com.abhirambsn.tenantmetadataservice.repository.TenantMetadataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
public class TenantMetadataService {
    private final TenantMetadataRepository tenantMetadataRepository;
    public TenantMetadataService(
            TenantMetadataRepository tenantMetadataRepository
    ) {
        this.tenantMetadataRepository = tenantMetadataRepository;
    }

    public TenantMetadataResponseDto createTenantMetadata(CreateNewTenantDto createNewTenantDto) {
        try {
            TenantMetadata newTenant = TenantMetadata.builder()
                    .name(createNewTenantDto.getName())
                    .ownerId(createNewTenantDto.getOwnerId())
                    .planId(createNewTenantDto.getPlanId())
                    .logo(createNewTenantDto.getLogo())
                    .build();
            TenantMetadata savedTenant = tenantMetadataRepository.save(newTenant);
            return TenantMetadataMapper.mapToTenantMetadataResponseDto(savedTenant);
        } catch (Exception e) {
            log.error("e: ", e);
            throw new RuntimeException("Error creating tenant metadata");
        }
    }

    public TenantMetadataResponseDto getTenantMetadata(String slug) {
        try {
            TenantMetadata tenantMetadata = tenantMetadataRepository.findBySlug(slug).orElseThrow();
            return TenantMetadataMapper.mapToTenantMetadataResponseDto(tenantMetadata);
        } catch (NoSuchElementException e) {
            log.error("Tenant not found:", slug);
            throw new NoSuchElementException("Tenant not found");
        }
        catch (Exception e) {
            log.error("e: ", e);
            throw new RuntimeException("Error fetching tenant metadata");
        }
    }

    public TenantMetadataResponseDto updateTenant(String slug, UpdateTenantDto dto) {
        try {
            TenantMetadata tenantMetadata = tenantMetadataRepository.findBySlug(slug).orElseThrow();
            if (dto.logo != null) {
                tenantMetadata.setLogo(dto.getLogo());
            }
            if (dto.ownerId != null) {
                tenantMetadata.setOwnerId(dto.getOwnerId());
            }
            if (dto.planId != null) {
                tenantMetadata.setPlanId(dto.getPlanId());
            }
            TenantMetadata updatedTenant = tenantMetadataRepository.save(tenantMetadata);
            return TenantMetadataMapper.mapToTenantMetadataResponseDto(updatedTenant);
        } catch (Exception e) {
            log.error("e:", e);
            throw new RuntimeException("Error updating tenant");
        }
    }
}
