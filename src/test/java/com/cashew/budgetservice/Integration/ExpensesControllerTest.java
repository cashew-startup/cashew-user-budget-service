package com.cashew.budgetservice.Integration;

import com.cashew.budgetservice.DAO.Entities.Product;
import com.cashew.budgetservice.DAO.Entities.Receipt;
import com.cashew.budgetservice.DAO.Repos.ReceiptRepository;
import com.cashew.budgetservice.DAO.Repos.UserCheckRepository;
import com.cashew.budgetservice.DAO.Repos.UserRepository;
import com.cashew.budgetservice.exceptions.FetchDataException;
import com.cashew.budgetservice.services.ExpensesService;
import com.cashew.budgetservice.services.FetchReceiptService;
import com.cashew.budgetservice.services.UsersService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExpensesControllerTest {
    private final UsersService usersService;
    private MockMvc mockMvc;

    @TestConfiguration
    static class MyTestConfiguration {

        @Bean
        public FetchReceiptService fetchReceiptServiceMOCK() {
            return Mockito.mock(FetchReceiptService.class);
        }

        @Bean
        public ExpensesService expensesService(UserCheckRepository userCheckRepository,
                               ReceiptRepository receiptRepository,
                               UserRepository userRepository,
                               FetchReceiptService fetchReceiptServiceMOCK){
            return new ExpensesService(userCheckRepository, receiptRepository, userRepository, fetchReceiptServiceMOCK);
        }

    }
    @Autowired
    private FetchReceiptService fetchReceiptServiceMOCK;



    @Autowired
    public ExpensesControllerTest(UsersService usersService, MockMvc mockMvc) {
        this.usersService = usersService;
        this.mockMvc = mockMvc;
    }

    @BeforeAll
    public void setUp() {
        usersService.createUser("TestUser1", "example@g.com");
    }

    @AfterAll
    void closeUp(){
        Long id = usersService.getUserByUsername("TestUser1").getBody().getId();
        usersService.deleteUserById(id);
    }

    @Test
    @Order(1)
    public void testGetExpensesWhenNoExpensesYet() throws Exception {
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/expenses/day")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("expenses").isEmpty());
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/expenses/week")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("expenses").isEmpty());
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/expenses/year")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("expenses").isEmpty());
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/expenses/period")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser1")
                        .param("from", "2022-09-01")
                        .param("to", "2023-03-01"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("expenses").exists())
                .andExpect(jsonPath("expenses").isEmpty());
    }

    @Test
    @Order(2)
    public void testAddReceiptWithWrongToken() throws Exception {
        when(fetchReceiptServiceMOCK.fetchReceipt("testuser1","bebra"))
                .thenThrow(new FetchDataException("Failed to fetch receipt from receipt service"));
        this.mockMvc
                .perform(post("http://localhost:8080/api/v1/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "TestUser1",
                                    "token": "bebra"
                                }"""))
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("description").value("Failed to fetch receipt from receipt service"));
    }
    @Test
    @Order(3)
    public void testAddReceiptSuccessfully() throws Exception {
        when(fetchReceiptServiceMOCK.fetchReceipt("testuser1","t=20230120T2027&s=4200.00&fn=9961440300674259&i=3790&fp=2608575326&n=1"))
                .thenReturn(new Receipt()
                                .setCompany("ОБЩЕСТВО С ОГРАНИЧЕННОЙ ОТВЕТСТВЕННОСТЬЮ \"ДОМ КОМИКСОВ \"МАЯК\"")
                                .setAddress("190121, г.Санкт-Петербург, наб.Адмиралтейского канала, д.2, литер Т")
                                .setINN("7804588265")
                                .setDate(ZonedDateTime.of(
                                        2023,
                                        1,
                                        20,
                                        20,
                                        27,
                                        0,
                                        0,
                                        ZonedDateTime.now().getZone())
                                )
                                .setReceiptNumber("Чек № 55")
                                .setShift("Смена № 35")
                                .setCashier("Кассир Потапов Глеб")
                                .setTotal(BigDecimal.valueOf(4200.0))
                                .setCash(BigDecimal.valueOf(0.0))
                                .setCard(BigDecimal.valueOf(4200.0))
                                .setVAT20(BigDecimal.valueOf(4200.0))
                                .setVAT10(BigDecimal.valueOf(0.0))
                                .setTaxation("ВИД НАЛОГООБЛОЖЕНИЯ: УСН доход - расход")
                                .setProducts(Arrays.asList(
                                        new Product()
                                                .setName("Фигурка Funko POP! Animation Avatar Spirit Aang (GW) (Exc) 55052")
                                                .setDescription("")
                                                .setCurrency("RUB")
                                                .setPricePerUnit(BigDecimal.valueOf(2200.0))
                                                .setQuantity(1.0)
                                                .setTotalPrice(BigDecimal.valueOf(2200.0)),
                                        new Product()
                                                .setName("Фигурка Funko POP! Animation Avatar The Last Airbender Appa (540) 36468")
                                                .setDescription("")
                                                .setCurrency("RUB")
                                                .setPricePerUnit(BigDecimal.valueOf(2000.0))
                                                .setQuantity(1.0)
                                                .setTotalPrice(BigDecimal.valueOf(2000.0))
                                        )
                                )
                );
        this.mockMvc
                .perform(post("http://localhost:8080/api/v1/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "TestUser1",
                                    "token": "t=20230120T2027&s=4200.00&fn=9961440300674259&i=3790&fp=2608575326&n=1"
                                }"""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value("true"));
    }

    @Test
    @Order(4)
    public void testGetExpensesWhenExpensesExist() throws Exception {
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/expenses/day")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("expenses").exists())
                .andExpect(jsonPath("expenses").isEmpty());
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/expenses/week")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("expenses").exists())
                .andExpect(jsonPath("expenses").isEmpty());
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/expenses/month")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("expenses").exists())
                .andExpect(jsonPath("expenses").isEmpty());
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/expenses/year")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("expenses").exists())
                .andExpect(jsonPath("expenses").isNotEmpty());
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/expenses/period")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser1")
                        .param("from", "2022-09-01")
                        .param("to", "2023-03-01"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("expenses").exists())
                .andExpect(jsonPath("expenses").isNotEmpty());
    }

}
