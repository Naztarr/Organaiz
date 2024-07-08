package com.naz.organaiz.service;

import com.naz.organaiz.dto.OrganisationDto;
import com.naz.organaiz.payload.ApiResponse;
import com.naz.organaiz.payload.OrganisationData;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrganisationService {
    ResponseEntity<ApiResponse<List<OrganisationData>>> getUserOrganisations();
    ResponseEntity<ApiResponse<OrganisationData>> getOrganisation(String orgId);
    ResponseEntity<ApiResponse<OrganisationData>> createOrganisation(OrganisationDto dto);
    ResponseEntity<ApiResponse<String>> addUserToOrganisation(String orgId);
}
