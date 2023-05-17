package com.cashew.budgetservice.Integration;

import com.cashew.budgetservice.DAO.Entities.Product;
import com.cashew.budgetservice.DAO.Entities.Receipt;
import com.cashew.budgetservice.DAO.Repos.ReceiptRepository;
import com.cashew.budgetservice.DAO.Repos.UserCheckRepository;
import com.cashew.budgetservice.DAO.Repos.UserRepository;
import com.cashew.budgetservice.DTO.ProductShareDTO;
import com.cashew.budgetservice.DTO.UserExpensesDTO;
import com.cashew.budgetservice.exceptions.FetchDataException;
import com.cashew.budgetservice.services.ExpensesService;
import com.cashew.budgetservice.services.FetchReceiptService;
import com.cashew.budgetservice.services.UsersService;
import org.hamcrest.Matchers;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    String username = "TestUser1";
    String friendUsername = "TestUser2";

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
        usersService.createUser(username, "example@g.com");
        usersService.createUser(friendUsername, "example@y.com");
    }

    @Test
    @Order(1)
    public void testGetExpensesWhenNoExpensesYet() throws Exception {
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/expenses/day")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("expenses").isEmpty());
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/expenses/week")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("expenses").isEmpty());
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/expenses/year")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("expenses").isEmpty());
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/expenses/period")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", username)
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
        when(fetchReceiptServiceMOCK.fetchReceipt(username.toLowerCase(Locale.ROOT),"bebra"))
                .thenThrow(new FetchDataException("Sorry. Failed to fetch receipt from receipt service."));
        this.mockMvc
                .perform(post("http://localhost:8080/api/v1/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "owner": "%s",
                                    "token": "bebra",
                                    "shares": [
                                        {
                                            "username": "%s",
                                            "expenses": [
                                                {
                                                    "name": "Фигурка Funko POP! Animation Avatar Spirit Aang (GW) (Exc) 55052",
                                                    "price": 2200.0
                                                },
                                                {
                                                    "name": "Фигурка Funko POP! Animation Avatar The Last Airbender Appa (540) 36468",
                                                    "price": 2000.0
                                                }
                                            ]
                                        }
                                    ]
                                }""".formatted(username, username)))
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("description").value("Sorry. Failed to fetch receipt from receipt service."));
    }
    @Test
    @Order(3)
    public void testAddReceiptSuccessfully() throws Exception {
        when(fetchReceiptServiceMOCK.fetchReceipt(
                username.toLowerCase(Locale.ROOT),
                "t=20230120T2027&s=4200.00&fn=9961440300674259&i=3790&fp=2608575326&n=1"))
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
                                    "owner": "%s",
                                    "token": "t=20230120T2027&s=4200.00&fn=9961440300674259&i=3790&fp=2608575326&n=1",
                                    "shares": [
                                        {
                                            "username": "%s",
                                            "expenses": [
                                                {
                                                    "name": "Фигурка Funko POP! Animation Avatar Spirit Aang (GW) (Exc) 55052",
                                                    "price": 2200.0
                                                },
                                                {
                                                    "name": "Фигурка Funko POP! Animation Avatar The Last Airbender Appa (540) 36468",
                                                    "price": 1000.0
                                                }
                                            ]
                                        },
                                        {
                                            "username": "%s",
                                            "expenses": [
                                            {
                                                "name": "Фигурка Funko POP! Animation Avatar The Last Airbender Appa (540) 36468",
                                                "price": 1000.0
                                            }
                                            ]
                                        }
                                    ]
                                }""".formatted(username, username, friendUsername)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value("true"));
    }

    @Test
    @Order(4)
    public void testAddCheckAndDisableIt() throws Exception {
        when(fetchReceiptServiceMOCK.fetchReceipt(username.toLowerCase(Locale.ROOT),"t=20230120T2027&s=4200.00&fn=9961440300674259&i=3790&fp=2608575326&n=1"))
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
                                    "owner": "%s",
                                    "token": "t=20230120T2027&s=4200.00&fn=9961440300674259&i=3790&fp=2608575326&n=1",
                                    "shares": [
                                        {
                                            "username": "%s",
                                            "expenses": [
                                                {
                                                    "name": "Фигурка Funko POP! Animation Avatar Spirit Aang (GW) (Exc) 55052",
                                                    "price": 2200.0
                                                },
                                                {
                                                    "name": "Фигурка Funko POP! Animation Avatar The Last Airbender Appa (540) 36468",
                                                    "price": 2000.0
                                                }
                                            ]
                                        }
                                    ]
                                }""".formatted(username, username)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value("true"));
        String receipts = this.mockMvc
                .perform(get("http://localhost:8080/api/v1/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", username))
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("\n=\n=\n=\n=\n=\n=\n=\n="+receipts+"\n=\n=\n=\n=\n=\n=\n=\n=");
        Pattern p = Pattern.compile("\"id\":(\\d*)");
        Matcher m = p.matcher(receipts);
        long id = 0;
        if(m.find()) {
            id = Long.parseLong(m.group(1));
        }

        this.mockMvc
                .perform(delete("http://localhost:8080/api/v1/expenses")
                        .param("username", username)
                        .param("receiptId", Long.toString(id)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("success").value("true"));
    }

    @Test
    @Order(5)
    public void testGetExpensesWhenExpenseExist() throws Exception {
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("expenses").exists())
                .andExpect(jsonPath("expenses", Matchers.hasSize(1)));
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/expenses/day")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("expenses").exists())
                .andExpect(jsonPath("expenses", Matchers.hasSize(0)));
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/expenses/week")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("expenses").exists())
                .andExpect(jsonPath("expenses", Matchers.hasSize(0)));
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/expenses/month")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("expenses").exists())
                .andExpect(jsonPath("expenses", Matchers.hasSize(0)));
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/expenses/year")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("expenses").exists())
                .andExpect(jsonPath("expenses", Matchers.hasSize(1)));
        this.mockMvc
                .perform(get("http://localhost:8080/api/v1/expenses/period")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", username)
                        .param("from", "2022-09-01")
                        .param("to", "2023-03-01"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("expenses").exists())
                .andExpect(jsonPath("expenses", Matchers.hasSize(1)));
    }


}
