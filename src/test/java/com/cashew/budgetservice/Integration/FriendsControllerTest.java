package com.cashew.budgetservice.Integration;

import com.cashew.budgetservice.DTO.UsersDTO;
import com.cashew.budgetservice.services.UsersService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FriendsControllerTest {
    private UsersService usersService;
    private MockMvc mockMvc;


    @Autowired
    public FriendsControllerTest(UsersService usersService, MockMvc mockMvc) {
        this.usersService = usersService;
        this.mockMvc = mockMvc;
    }


    @BeforeAll
    public void setUp() {
        usersService.createUser("TestUser1", "example@g.com");
        usersService.createUser("TestUser2", "example@y.com");
        usersService.createUser("TestUser3", "example@t.com");
    }

    @AfterAll
    void closeUp(){
        usersService.deleteUserByUsername("testuser1");
        usersService.deleteUserByUsername("testuser2");
        usersService.deleteUserByUsername("testuser3");
    }

    @Test
    @Order(1)
    public void testGetFriendsWhenNoFriendsYet() throws Exception {
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("friends").isEmpty());
    }

    @Test
    @Order(2)
    public void testGetFriendRequestsWhenNoFriendsYet() throws Exception {
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/friends/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("friendRequests").isEmpty());
    }

    @Test
    @Order(3)
    public void testFriendRequestSent() throws Exception {
        this.mockMvc
                .perform(post("http://localhost:8080/api/v1/friends/requests/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "sender": "TestUser1",
                                    "receiver": "TestUser3"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(true));
        this.mockMvc
                .perform(post("http://localhost:8080/api/v1/friends/requests/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "sender": "TestUser2",
                                    "receiver": "TestUser3"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(true));
    }

    @Test
    @Order(4)
    public void testGetFriendRequestsAfterRequestSent() throws Exception {
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/friends/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("friendRequests").isEmpty());
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/friends/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser3")
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("friendRequests[0]").value("testuser1"),
                        jsonPath("friendRequests[1]").value("testuser2")
                        );
    }

    @Test
    @Order(5)
    public void testAcceptRequest() throws Exception {
        this.mockMvc
                .perform(patch("http://localhost:8080/api/v1/friends/requests/accept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "sender": "TestUser1",
                                    "receiver": "TestUser3"
                                }
                                """))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("success").value(true));
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser1"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("friends[0]").value("testuser3"));
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser3"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("friends[0]").value("testuser1"));
    }

    @Test
    @Order(6)
    public void testDeclineRequest() throws Exception {
        this.mockMvc
                .perform(patch("http://localhost:8080/api/v1/friends/requests/decline")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "sender": "TestUser2",
                                    "receiver": "TestUser3"
                                }
                                """))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("success").value(true));
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/friends/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser3"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("friendRequests").isEmpty());
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser3"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("friends[0]").value("testuser1"));
    }

    @Test
    @Order(7)
    public void testRemoveFromFriends() throws Exception {
        this.mockMvc
                .perform(delete("http://localhost:8080/api/v1/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "deleter": "TestUser3",
                                    "deleted": "TestUser1"
                                }
                                """))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("success").value(true));
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser3"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("friends").isEmpty());
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser2"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("friends").isEmpty());
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser1"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("friends").isEmpty());

    }

}
