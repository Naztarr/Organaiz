package com.naz.organaiz.service;

import com.naz.organaiz.dto.OrganisationDto;
import com.naz.organaiz.exception.OrganaizException;
import com.naz.organaiz.mapper.OrganisationMapper;
import com.naz.organaiz.model.Organisation;
import com.naz.organaiz.model.User;
import com.naz.organaiz.payload.ApiResponse;
import com.naz.organaiz.payload.OrganisationData;
import com.naz.organaiz.repository.OrganisationRepository;
import com.naz.organaiz.repository.UserRepository;
import com.naz.organaiz.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganisationImplementation implements OrganisationService{
    private final UserRepository userRepository;
    private final OrganisationRepository organisationRepository;
    @Override
    public ResponseEntity<ApiResponse<List<OrganisationData>>> getUserOrganisations() {
        User user = userRepository.findByEmail(UserUtil.getLoginUser()).orElseThrow(
                () -> new OrganaizException("User not found"));
        List<OrganisationData> organisationDataList = user.getOrganisations().stream().map(
                org -> OrganisationMapper.mapToOrganisationData(org,
                        new OrganisationData())).collect(Collectors.toList());
        return new ResponseEntity<>(new ApiResponse<>("success",
                "Organisations retrieved successfully", organisationDataList), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse<OrganisationData>> getOrganisation(String orgId) {
        userRepository.findByEmail(UserUtil.getLoginUser()).orElseThrow(
                () -> new OrganaizException("User not found"));
        Organisation org = organisationRepository.findById(orgId).orElseThrow(
                () -> new OrganaizException("Organisation not found"));

        return new ResponseEntity<>(new ApiResponse<>("success",
                "Organisation data retrieved successfully",
                OrganisationMapper.mapToOrganisationData(org, new OrganisationData())), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse<OrganisationData>> createOrganisation(OrganisationDto dto) {
        Optional<User> optionalUser = userRepository.findByEmail(UserUtil.getLoginUser());
        if(optionalUser.isPresent()){
            Organisation organisation = new Organisation();
            organisation.getUsers().add(optionalUser.get());

            optionalUser.get().getOrganisations().add(organisation);
            OrganisationMapper.mapToOrganisation(dto, organisation);
            OrganisationData orgData = OrganisationMapper.mapToOrganisationData(
                    organisationRepository.save(organisation), new OrganisationData());
            return new ResponseEntity<>(new ApiResponse<>("success",
                    "Organisation created successfully", orgData), HttpStatus.CREATED);
        } else{
            return new ResponseEntity<>(new ApiResponse<>("Bad request",
                    "Client error", 400), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<ApiResponse<String>> addUserToOrganisation(String orgId) {
        User user = userRepository.findByEmail(UserUtil.getLoginUser()).orElseThrow(
                () -> new OrganaizException("User not found"));
        Organisation organisation = organisationRepository.findById(orgId).orElseThrow(
                () -> new OrganaizException("Organisation not found"));
        if(user.getOrganisations().contains(organisation)){
            throw new OrganaizException("User already belongs to the organisation");
        } else{
            user.getOrganisations().add(organisation);
            organisation.getUsers().add(user);
            userRepository.save(user);
            organisationRepository.save(organisation);
        }
        return new ResponseEntity<>(new ApiResponse<>("success",
                "User added to organization successfully"), HttpStatus.OK);
    }
}
