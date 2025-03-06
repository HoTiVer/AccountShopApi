package gleb.dresher.AccountShopApi.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gleb.dresher.AccountShopApi.controller.SellerController;
import gleb.dresher.AccountShopApi.dto.SellerDTO;
import gleb.dresher.AccountShopApi.dto.SellerResponse;
import gleb.dresher.AccountShopApi.entity.Account;
import gleb.dresher.AccountShopApi.entity.Seller;
import gleb.dresher.AccountShopApi.enums.AccountType;
import gleb.dresher.AccountShopApi.service.SellerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = SellerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SellerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SellerService sellerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Seller seller1;

    private Account account1;

    @BeforeEach
    public void setup() {
        seller1 = new Seller("Adolf", "adolf@gmail.com");
        seller1.setId(1);
        account1 = new Account("Dota 2 account TEST", 52.0, AccountType.DOTA);
    }

    @Test
    public void SellerController_GetSeller_ReturnNotNullSeller() throws Exception {
        seller1.setAccounts(List.of(account1));
        account1.setSeller(seller1);

        when(sellerService.getSeller(Mockito.anyInt())).thenReturn(ResponseEntity
                .ok(new SellerResponse("Adolf", List.of(account1))));

        ResultActions response = mockMvc.perform(get("/seller/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.name").value("Adolf"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accounts").isNotEmpty());
    }

    @Test
    public void SellerController_GetSeller_ReturnNullSeller() throws Exception {
        when(sellerService.getSeller(Mockito.anyInt())).thenReturn(ResponseEntity.notFound().build());

        ResultActions response = mockMvc.perform(get("/seller/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void SellerController_AddSeller_ReturnSeller() throws Exception {
        SellerDTO sellerDTO = new SellerDTO("Adolf", "adolf@gmail.com");

        when(sellerService.addSeller(Mockito.any(SellerDTO.class)))
                .thenReturn(ResponseEntity.created(null).body(seller1));

        ResultActions response = mockMvc.perform(post("/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sellerDTO)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.name").value("Adolf"))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.email").value("adolf@gmail.com"));

    }

    @Test
    public void SellerController_DeleteSeller_ReturnVoidResponseEntityNoContent() throws Exception {

        when(sellerService.deleteSeller(Mockito.anyInt()))
                .thenReturn(ResponseEntity.noContent().build());

        ResultActions response = mockMvc.perform(delete("/seller/1"));

        response.andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    public void SellerController_DeleteSeller_ReturnVoidResponseEntityNotFound() throws Exception {
        when(sellerService.deleteSeller(Mockito.anyInt()))
                .thenReturn(ResponseEntity.notFound().build());

        ResultActions response = mockMvc.perform(delete("/seller/1"));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void SellerController_UpdateSeller_ReturnSeller() throws Exception {

        SellerDTO sellerDTO = new SellerDTO("Adolf", "adolf@gmail.com");

        when(sellerService.updateSeller(Mockito.anyInt(), Mockito.any(SellerDTO.class)))
                .thenReturn(ResponseEntity.ok().body(seller1));

        ResultActions response = mockMvc.perform(put("/seller/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sellerDTO)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.name").value("Adolf"))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.email").value("adolf@gmail.com"));
    }
}
