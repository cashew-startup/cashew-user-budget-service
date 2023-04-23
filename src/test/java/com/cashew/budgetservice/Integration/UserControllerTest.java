package com.cashew.budgetservice.Integration;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {
    private MockMvc mockMvc;
    private UsersService usersService;

    @Autowired
    public UserControllerTest(UsersService usersService, MockMvc mockMvc) {
        this.usersService = usersService;
        this.mockMvc = mockMvc;
    }

    @BeforeAll
    public void setUp() {
    }

    @Test
    @Order(1)
    void createUserSuccessfully() throws Exception{
        this.mockMvc
                .perform(post("http://localhost:8080/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "testuser1",
                                    "email": "example@yahoo.com"
                                }
                                """))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("success").value(true));
    }

    @Test
    @Order(2)
    void cannotCreateUserDueToNonUniqueCredentials() throws Exception{
        this.mockMvc
                .perform(post("http://localhost:8080/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "TestUser1",
                                    "email": "example@mail.ru"
                                }
                                """))
                .andExpectAll(
                        status().isConflict(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("description").value("Not unique username"));
        this.mockMvc
                .perform(post("http://localhost:8080/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "RandomUserName",
                                    "email": "example@yahoo.com"
                                }
                                """))
                .andExpectAll(
                        status().isConflict(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("description").value("Not unique email"));
    }

    @Test
    @Order(3)
    void getUserByUsernameThenByEmail() throws Exception {
        /*Get by username*/
        ResultActions response = this.mockMvc
                .perform(get("http://localhost:8080/api/v1/users/byUsername")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser1"));
        response.andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("username").value("testuser1"),
                jsonPath("email").value("example@yahoo.com"));
        /*Get by username with wrong username*/
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/users/byUsername")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "RandomUsername123"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("description").value("No user with username=randomusername123"));
        /*Get by email with proper email*/
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/users/byEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("email", "example@yahoo.com"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("username").value("testuser1"),
                        jsonPath("email").value("example@yahoo.com"));
        /*Get by email with wrong email*/
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/users/byEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("email", "RandomEmail123"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("description").value("No user with such email=randomemail123"));
    }

    @Test
    void updateUser() throws Exception {
        /*Update username*/
        this.mockMvc
                .perform(put("http://localhost:8080/api/v1/users/updateUsername")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "newUsername": "UpdatedUsername",
                                    "email": "example@yahoo.com"
                                }
                                """))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("username").value("updatedusername"),
                        jsonPath("email").value("example@yahoo.com"));
        /*Check username was really updated*/
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/users/byUsername")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "updatedusername"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("username").value("updatedusername"),
                        jsonPath("email").value("example@yahoo.com"));
        /*Update email*/
        this.mockMvc
                .perform(put("http://localhost:8080/api/v1/users/updateEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "updatedusername",
                                    "newEmail": "UpdatedEmail"
                                }
                                """))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("username").value("updatedusername"),
                        jsonPath("email").value("updatedemail"));
        /*Check email was really updated*/
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/users/byEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("email", "updatedemail"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("username").value("updatedusername"),
                        jsonPath("email").value("updatedemail"));
    }

    @Test
    void deleteUserById() throws Exception {
        /*Delete testuser1*/
        this.mockMvc
                .perform(delete("http://localhost:8080/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "%s"
                                }
                                """.formatted("updatedusername")))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("success").value(true));
        /*Check it was really deleted*/
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/users/byUsername")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username","updatedusername"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("description").value("No user with username=updatedusername"));
    }
}