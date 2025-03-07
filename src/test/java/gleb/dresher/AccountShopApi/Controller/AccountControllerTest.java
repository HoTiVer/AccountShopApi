package gleb.dresher.AccountShopApi.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gleb.dresher.AccountShopApi.controller.AccountController;
import gleb.dresher.AccountShopApi.dto.AccountDTO;
import gleb.dresher.AccountShopApi.entity.Account;
import gleb.dresher.AccountShopApi.enums.AccountType;
import gleb.dresher.AccountShopApi.repository.AccountRepository;
import gleb.dresher.AccountShopApi.service.AccountService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;


import java.util.Arrays;
import java.util.List;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private AccountService accountService;

    private Account account1;
    private Account account2;

    @BeforeEach
    public void setup() {
        account1 = new Account("Dota 2 account TEST", 52.0, AccountType.DOTA);
        account2 = new Account("Fortnite account TEST", 92.0, AccountType.FORTNITE);
    }

    @Test
    public void AccountController_getAccounts_GetTwoAccounts() throws Exception {
        when(accountService.getAllAccounts()).thenReturn(Arrays.asList(account1, account2));

        ResultActions response = mockMvc.perform(get("/accounts")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers
                .jsonPath("$.length()").value(2));
    }

    @Test
    public void AccountController_getAccounts_GetZeroAccounts() throws Exception {
        when(accountService.getAllAccounts()).thenReturn(List.of());

        ResultActions response = mockMvc.perform(get("/accounts")
                .contentType(MediaType.APPLICATION_JSON));


        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
    }

    @Test
    public void AccountController_getAccountById() throws Exception {
        int accountId = 1;
        account1.setId(accountId);

        when(accountService.getAccountById(accountId)).thenReturn(ResponseEntity.ok(account1));

        ResultActions response = mockMvc.perform(get("/account/" + accountId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Dota 2 account TEST"));

    }

    @Test
    public void AccountController_getAccountsByDotaType() throws Exception {
        when(accountService.getAccountsByType("dota")).thenReturn(Arrays.asList(account1));

        ResultActions resultActions = mockMvc.perform(get("/accounts/dota")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$[0].name", CoreMatchers.is(account1.getName())));

    }

    @Test
    public void AccountController_getAccountsByFortniteType() throws Exception {
        when(accountService.getAccountsByType("Fortnite")).thenReturn(Arrays.asList(account2));

        ResultActions resultActions = mockMvc.perform(get("/accounts/Fortnite")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$[0].name", CoreMatchers.is(account2.getName())));

    }

    @Test
    public void AccountController_getAccountsByPriceLowerThan100() throws Exception {
        int price = 100;
        when(accountService.getCheapAccounts(price))
                .thenReturn(Arrays.asList(account1, account2));

        ResultActions resultActions = mockMvc
                .perform(get("/accounts/pricedown/" + price)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", CoreMatchers.is(account1.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", CoreMatchers.is(account2.getName())));;
    }

    @Test
    public void AccountController_AddAccount_ReturnAccount() throws Exception {
        AccountDTO accountDTO = new AccountDTO(
                "Dota 2 account TEST", 52.0, AccountType.DOTA, 1);

        given(accountService.addAccount(any(AccountDTO.class)))
                .willAnswer(invocation -> ResponseEntity.created(null).body(account1));

        ResultActions response = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDTO)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.name").value("Dota 2 account TEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(52.0));
    }

    @Test
    public void AccountController_AddAccount_BadRequest() throws Exception {
        AccountDTO accountDTO = new AccountDTO(
                "Dota 2 account TEST", 52.0, AccountType.DOTA, 52);

        when(accountService.addAccount(any(AccountDTO.class)))
                .thenReturn(ResponseEntity.badRequest().build());

        ResultActions resultActions = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDTO)));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void AccountController_UpdateAccount_ReturnAccount() throws Exception {
        AccountDTO accountDTO = new AccountDTO("Dota 2 account TEST", 52.0, AccountType.DOTA, 1);

        when(accountService.updateAccount(eq(1), any(AccountDTO.class)))
                .thenReturn(ResponseEntity.ok(account1)); // Мокаем метод сервиса

        ResultActions response = mockMvc.perform(put("/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDTO)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Dota 2 account TEST"));    }

    @Test
    public void AccountController_DeleteAccount_Success() throws Exception {
        when(accountService.deleteAccount(1)).thenReturn(ResponseEntity.noContent().build());

        ResultActions resultActions = mockMvc.perform(delete("/accounts/1"));
        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void AccountController_DeleteAccount_NotFound() throws Exception {
        when(accountService.deleteAccount(52)).thenReturn(ResponseEntity.notFound().build());

        ResultActions resultActions = mockMvc.perform(delete("/accounts/52"));

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
