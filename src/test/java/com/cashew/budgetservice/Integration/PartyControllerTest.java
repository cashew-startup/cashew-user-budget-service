package com.cashew.budgetservice.Integration;

import com.cashew.budgetservice.services.PartiesService;
import com.cashew.budgetservice.services.UsersService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PartyControllerTest {
    private PartiesService partiesService;
    private UsersService usersService;
    private MockMvc mockMvc;

    private Long testPartyId;
    private String testPartyName = "TestParty";
    private String ownerUsername = "owneruser";
    private String guest1Username = "guestuser1";
    private String guest2Username = "guestuser2";

    @Autowired
    public PartyControllerTest(PartiesService partiesService, UsersService usersService, MockMvc mockMvc) {
        this.partiesService = partiesService;
        this.usersService = usersService;
        this.mockMvc = mockMvc;
    }

    @BeforeAll
    public void setUp() {
        Assertions.assertTrue(usersService.createUser(ownerUsername, "example@g.com").getBody().isSuccess());
        Assertions.assertTrue(usersService.createUser(guest1Username, "example@y.com").getBody().isSuccess());
        Assertions.assertTrue(usersService.createUser(guest2Username, "example@t.com").getBody().isSuccess());
    }

    @AfterAll
    void closeUp() {
        usersService.deleteUserByUsername(ownerUsername);
        usersService.deleteUserByUsername(guest1Username);
        usersService.deleteUserByUsername(guest2Username);
    }

    @Test
    @Order(1)
    public void testCannotCreatePartyDueToInvalidInput() throws Exception {
        this.mockMvc
                .perform(post("http://localhost:8080/api/v1/parties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "%s",
                                    "ownerUsername": "0"
                                }""".formatted(testPartyName)))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("code").value(404),
                        jsonPath("description").value("No user with username=0"));
    }

    @Test
    @Order(2)
    public void testCreatePartySuccessfully() throws Exception {
        /*Create party and remember its id*/
        ResultActions response = this.mockMvc
                .perform(post("http://localhost:8080/api/v1/parties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "%s",
                                    "ownerUsername": "%s"
                                }""".formatted(testPartyName, ownerUsername)));
        response.andExpectAll(
                status().isCreated(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("partyId").exists());
        testPartyId = Long.valueOf(JsonPath.parse(response.andReturn().getResponse().getContentAsString()).read("partyId").toString());
    }

    @Test
    @Order(3)
    public void testCannotAddUserToPartyDueToInvalidInput() throws Exception {
        this.mockMvc
                .perform(patch("http://localhost:8080/api/v1/parties/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "partyId": 0,
                                    "username": ""
                                }"""))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("code").value(404));
    }

    @Test
    @Order(4)
    public void testAddUserToPartySuccessfully() throws Exception {
        this.mockMvc
                .perform(patch("http://localhost:8080/api/v1/parties/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "partyId": %s,
                                    "username": "%s"
                                }""".formatted(testPartyId, guest1Username)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("success").value(true));
        this.mockMvc
                .perform(patch("http://localhost:8080/api/v1/parties/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "partyId": %s,
                                    "username": "%s"
                                }""".formatted(testPartyId, guest2Username)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("success").value(true));
    }

    @Test
    @Order(5)
    public void testCannotRemoveUserFromPartyDueToInvalidInput() throws Exception {
        this.mockMvc
                .perform(delete("http://localhost:8080/api/v1/parties/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "partyId": 0,
                                    "guestId": 0
                                }"""))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("code").value(404));
    }

    @Test
    @Order(6)
    public void testRemoveUserFromPartySuccessfully() throws Exception {
        this.mockMvc
                .perform(delete("http://localhost:8080/api/v1/parties/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "partyId": %s,
                                    "username": "%s"
                                }""".formatted(testPartyId, guest1Username)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("success").value(true));
    }

    @Test
    @Order(7)
    public void testCannotGetFullPartyInfoDueToInvalidInput() throws Exception {
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/parties/0/info"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("code").value(404));
    }

    @Test
    @Order(8)
    public void testGetFullPartyInfoSuccessfully() throws Exception {
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/parties/%s/info".formatted(testPartyId)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("party.id").value(testPartyId),
                        jsonPath("party.['owner username']").value(ownerUsername),
                        jsonPath("party.users").value(hasItems(ownerUsername,guest1Username,guest2Username)));
    }

    @Test
    @Order(9)
    public void testCannotGetPartiesOfUserDueToInvalidInput() throws Exception {
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/parties/of")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", ""))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("code").value(404),
                        jsonPath("description").value("No user with such username"));
    }

    @Test
    @Order(10)
    public void testGetPartiesOfUserSuccessfully() throws Exception {
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/parties/of")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", ownerUsername))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("parties[0].id").value(testPartyId));
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/parties/of")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", guest1Username))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("parties[0].id").value(testPartyId));
    }

    @Test
    @Order(11)
    public void testCannotChangePartyNameDueToInvalidInput() throws Exception {
        this.mockMvc
                .perform(patch("http://localhost:8080/api/v1/parties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "partyId": 0,
                                    "name": "new name"
                                }"""))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("code").value(404));
    }

    @Test
    @Order(12)
    public void testChangePartyNameSuccessfully() throws Exception {
        this.mockMvc
                .perform(patch("http://localhost:8080/api/v1/parties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "partyId": %s,
                                    "name": "new name"
                                }""".formatted(testPartyId)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("success").value(true));
    }


    @Test
    @Order(13)
    public void testCannotDeletePartyDueToInvalidInput() throws Exception {
        this.mockMvc
                .perform(delete("http://localhost:8080/api/v1/parties/0"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("code").value(404));
    }

    @Test
    @Order(14)
    public void testDeletePartySuccessfully() throws Exception {
        this.mockMvc
                .perform(delete("http://localhost:8080/api/v1/parties/%s" .formatted(testPartyId)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("success").value(true));
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/parties/%s" .formatted(testPartyId)))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("code").value(404));
    }
}
