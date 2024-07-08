package com.naz.organaiz.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naz.organaiz.dto.LoginDto;
import com.naz.organaiz.dto.UserDto;
import com.naz.organaiz.model.Organisation;
import com.naz.organaiz.model.User;
import com.naz.organaiz.repository.OrganisationRepository;
import com.naz.organaiz.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class spec {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        // Ensuring a clean state for each test
        userRepository.deleteAll();
        userRepository.deleteAll();
    }
    private User createUser(){
        User user = new User();
        user.setFirstName("Naz");
        user.setLastName("Star");
        user.setEmail("Naz@gmail.com");
        user.setPassword(passwordEncoder.encode("Azanaz1$"));
        user.setPhone("09056754347");
        userRepository.save(user);

        return user;
    }

    @Test
    public void testRegisterUserSuccessfully() throws Exception {
        UserDto dto = new UserDto("Naz", "Star",
                "Naz@gmail.com", "Azanaz1$",
                "Azanaz1$", "09032456534");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Registration successful"))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.userData.firstName").value(dto.firstName()))
                .andExpect(jsonPath("$.data.userData.lastName").value(dto.lastName()))
                .andExpect(jsonPath("$.data.userData.email").value(dto.email()));

        // Verifying that the registered user is in the database
        Optional<User> userOptional = userRepository.findByEmail(dto.email());
        assert (userOptional.isPresent());
        // Verifying that an organisation was created and is in the database
        Optional<Organisation> orgOptional = organisationRepository.findById(userOptional.get().getOrganisations().get(0).getOrgId());
        assert (orgOptional.isPresent());
        // Verifying correct naming for the organisation
        assert (Objects.equals(orgOptional.get().getName(), "Naz's Organisation"));

        User user = userOptional.get();
        assert (passwordEncoder.matches(dto.password(), user.getPassword()));
        assert (dto.firstName().equals(user.getFirstName()));
        assert (dto.lastName().equals(user.getLastName()));
    }

    @Test
    public void testRegisterUserWithMissingFields() throws Exception {
        UserDto dto = new UserDto("Naz", "Star",
                "", "Azanaz1$",
                "Azanaz1$", "09032456534");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.statusCode").value(422));

        Optional<User> userOptional = userRepository.findByEmail(dto.email());
        assert (!userOptional.isPresent());
    }

    @Test
    public void testLoginUserSuccessfully() throws Exception {
        User user = createUser();

        // Login with the same user
        LoginDto loginDto = new LoginDto("Naz@gmail.com", "Azanaz1$");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.userData.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.data.userData.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.data.userData.email").value(user.getEmail()))
                .andExpect(jsonPath("$.data.userData.phone").value(user.getPhone()));
    }

    @Test
    public void testTokenExpiration() throws Exception {

        User user = createUser();

        long expireTime = 90;
        Thread.sleep(expireTime);

        LoginDto loginDto = new LoginDto("Naz@gmail.com", "Azanaz1$");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.userData.userId").value(user.getUserId()))
                .andExpect(jsonPath("$.data.userData.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.data.userData.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.data.userData.email").value(user.getEmail()))
                .andExpect(jsonPath("$.data.userData.phone").value(user.getPhone()));
    }

    @Test
    public void testRegisterUserWithExistingEmail() throws Exception {
        User user = createUser();
        // Registering with an existing email
        UserDto dto = new UserDto("Paul", "Kim", "Naz@gmail.com",
                "Azanaz1$", "Azanaz1$", "09032456534");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value("Bad request"))
                .andExpect(jsonPath("$.message").value("A user already exists with this email"))
                .andExpect(jsonPath("$.statusCode").value(422));
    }
}
