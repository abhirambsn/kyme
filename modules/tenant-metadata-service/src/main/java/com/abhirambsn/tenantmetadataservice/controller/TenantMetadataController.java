package com.abhirambsn.tenantmetadataservice.controller;

import com.abhirambsn.tenantmetadataservice.dto.CreateNewTenantDto;
import com.abhirambsn.tenantmetadataservice.dto.TenantMetadataResponseDto;
import com.abhirambsn.tenantmetadataservice.dto.UpdateTenantDto;
import com.abhirambsn.tenantmetadataservice.service.TenantMetadataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Slf4j
@RestController
public class TenantMetadataController {
    private final TenantMetadataService metadataService;

    public TenantMetadataController(TenantMetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @GetMapping("/{slug}")
    public ResponseEntity<TenantMetadataResponseDto> getTenantMetadata(@PathVariable(name = "slug") String slug) {
        try {
            TenantMetadataResponseDto tenantMetadata = metadataService.getTenantMetadata(slug);
            return ResponseEntity.ok(tenantMetadata);
        } catch (NoSuchElementException e) {
            log.error("Tenant with slug already exists: ", slug);
            return ResponseEntity.notFound().build();
        }  catch (Exception e) {
            log.error("e: ", e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<TenantMetadataResponseDto> createTenant(@RequestBody CreateNewTenantDto dto) {
        try {
            TenantMetadataResponseDto tenantMetadata = metadataService.createTenantMetadata(dto);
            return ResponseEntity.ok(tenantMetadata);
        } catch (Exception e) {
            log.error("e: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{slug}")
    public ResponseEntity<TenantMetadataResponseDto> updateTenant(@PathVariable(name = "slug") String slug, @RequestBody UpdateTenantDto dto)  {
        try {
            TenantMetadataResponseDto updatedTenant = metadataService.updateTenant(slug, dto);
            return ResponseEntity.ok(updatedTenant);
        } catch (Exception e) {
            log.error("e: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
