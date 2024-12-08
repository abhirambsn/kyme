package com.abhirambsn.tenantmetadataservice.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewTenantDto {
    private String name;
    private String ownerId;
    private String planId;
    private String logo;
}
