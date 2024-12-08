package com.abhirambsn.tenantmetadataservice.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTenantDto {
    public String logo;
    public String planId;
    public String ownerId;
}
