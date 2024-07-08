package com.naz.organaiz.controller;

import com.naz.organaiz.dto.OrganisationDto;
import com.naz.organaiz.payload.ApiResponse;
import com.naz.organaiz.payload.OrganisationData;
import com.naz.organaiz.service.OrganisationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(
        name = "Organisations",
        description = "REST APIs for organisation related actions"
)
public class OrganisationController {
    private final OrganisationService organisationService;

    @GetMapping("/organisations")
    public ResponseEntity<ApiResponse<List<OrganisationData>>> getOrganisations(){
        return organisationService.getUserOrganisations();
    }

    @GetMapping("/organisations/{orgId}")
    public ResponseEntity<ApiResponse<OrganisationData>> getOrganisations(@PathVariable String orgId){
        return organisationService.getOrganisation(orgId);
    }

    @PostMapping("/organisations")
    public ResponseEntity<ApiResponse<OrganisationData>> createOrganisations(@RequestBody @Validated OrganisationDto dto){
        return organisationService.createOrganisation(dto);
    }

    @PostMapping("/organisations/{orgId}/users")
    public ResponseEntity<ApiResponse<String>> addUserToOrganisation(@PathVariable String orgId){
        return organisationService.addUserToOrganisation(orgId);
    }
}
