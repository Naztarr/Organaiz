package com.naz.organaiz.mapper;

import com.naz.organaiz.dto.OrganisationDto;
import com.naz.organaiz.model.Organisation;
import com.naz.organaiz.payload.OrganisationData;

public class OrganisationMapper {

    public static OrganisationData mapToOrganisationData(Organisation org, OrganisationData orgData){
        orgData.setOrgId(org.getOrgId());
        orgData.setName(org.getName());
        orgData.setDescription(org.getDescription());

        return orgData;
    }
    public static Organisation mapToOrganisation(OrganisationDto orgDto, Organisation org){
        org.setName(orgDto.name());
        org.setDescription(orgDto.description());

        return org;
    }
}
