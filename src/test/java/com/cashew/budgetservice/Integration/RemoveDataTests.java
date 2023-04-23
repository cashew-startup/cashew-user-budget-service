package com.cashew.budgetservice.Integration;

import com.cashew.budgetservice.DAO.Entities.Product;
import com.cashew.budgetservice.DAO.Entities.Receipt;
import com.cashew.budgetservice.DAO.Repos.ReceiptRepository;
import com.cashew.budgetservice.DAO.Repos.UserCheckRepository;
import com.cashew.budgetservice.DAO.Repos.UserRepository;
import com.cashew.budgetservice.services.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RemoveDataTests {
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

    private ExpensesService expensesService;
    private FriendsService friendsService;
    private PartiesService partiesService;
    private UsersService usersService;
    private FetchReceiptService fetchReceiptServiceMOCK;
    private String exampleReceiptToken = "t=20230120T2027&s=4200.00&fn=9961440300674259&i=3790&fp=2608575326&n=1";


    @Autowired
    public RemoveDataTests(ExpensesService expensesService,
                           FriendsService friendsService,
                           PartiesService partiesService,
                           UsersService usersService,
                           FetchReceiptService fetchReceiptServiceMOCK) {
        this.expensesService = expensesService;
        this.friendsService = friendsService;
        this.partiesService = partiesService;
        this.usersService = usersService;
        this.fetchReceiptServiceMOCK = fetchReceiptServiceMOCK;
    }


    @Test
    @Order(1)
    public void testDeleteUserProperly() {
        /*Delete User that :
        * has incoming friend request,
        * has friend,
        * has receipts,
        * is in the party,
        * owns a party*/
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
        String username = "TestUser1";
        String friendUsername = "friendUser";
        String friendRequesterUsername = "friendRequestUser";
        usersService.createUser(username,"e@a.com");
        usersService.createUser(friendUsername,"e@b.com");
        usersService.createUser(friendRequesterUsername,"e@c.com");
        friendsService.sendRequest(friendUsername, username);
        friendsService.acceptRequest(friendUsername, username);
        friendsService.sendRequest(friendRequesterUsername, username);
        expensesService.addReceipt(username, exampleReceiptToken);
        Long partyId1 = partiesService.createParty("Test Party1", username).getBody().getPartyId();
        Long partyId2 = partiesService.createParty("Test Party2", friendUsername).getBody().getPartyId();
        partiesService.addUserToParty(partyId1, friendUsername);
        partiesService.addUserToParty(partyId2, username);
        System.out.println(partiesService.getFullInfoOfParty(partyId1).getBody());
        System.out.println(partiesService.getFullInfoOfParty(partyId2).getBody());
        usersService.deleteUserByUsername(username);
        usersService.deleteUserByUsername(friendUsername);
        usersService.deleteUserByUsername(friendRequesterUsername);
        assertThrows(NoSuchElementException.class, () -> usersService.getUserByUsername(username));
        assertThrows(NoSuchElementException.class, () -> usersService.getUserByUsername(friendUsername));
        assertThrows(NoSuchElementException.class, () -> usersService.getUserByUsername(friendRequesterUsername));
        partiesService.deleteParty(partyId1);
        partiesService.deleteParty(partyId2);
        assertThrows(NoSuchElementException.class, () -> partiesService.getFullInfoOfParty(partyId1));
        assertThrows(NoSuchElementException.class, () -> partiesService.getFullInfoOfParty(partyId2));
    }

}
