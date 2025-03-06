package gleb.dresher.AccountShopApi.service;

import gleb.dresher.AccountShopApi.dto.SellerDTO;
import gleb.dresher.AccountShopApi.dto.SellerResponse;
import gleb.dresher.AccountShopApi.entity.Account;
import gleb.dresher.AccountShopApi.entity.Seller;
import gleb.dresher.AccountShopApi.enums.AccountType;
import gleb.dresher.AccountShopApi.repository.AccountRepository;
import gleb.dresher.AccountShopApi.repository.SellerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SellerServiceTest {
    @Mock
    private SellerRepository sellerRepository;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    SellerService sellerService;

    Seller seller1;

    @BeforeEach
    public void setup() {
        seller1 = new Seller("Adolf", "adolf@gmail.com");
        seller1.setId(1);
    }

    @Test
    public void SellerService_GetSellerAccounts_ReturnsListOfAccounts() {
        Account account1 = new Account("Dota 2 account TEST", 52.0, AccountType.DOTA);
        seller1.setAccounts(List.of(account1));
        account1.setSeller(seller1);

        when(sellerRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(seller1));

        ResponseEntity<SellerResponse> sellerResponse = sellerService.getSeller(1);

        Assertions.assertThat(sellerResponse.getStatusCode()).isNotNull();
        Assertions.assertThat(sellerResponse.getBody()).isNotNull();
        Assertions.assertThat(sellerResponse.getBody().getAccounts()).isNotNull();
    }

    @Test
    public void SellerService_AddSeller_ReturnsSeller(){

        when(sellerRepository.save(Mockito.any(Seller.class))).thenReturn(seller1);

        ResponseEntity<Seller> savedSeller = sellerService
                .addSeller(new SellerDTO("Adolf", "adolf@gmail.com"));

        Assertions.assertThat(savedSeller.getStatusCode())
                .isEqualTo(ResponseEntity.created(null).build().getStatusCode());

        Assertions.assertThat(savedSeller.getBody())
                .isEqualTo(seller1);

    }

    @Test
    public void SellerService_DeleteSeller(){

        when(sellerRepository.existsById(Mockito.anyInt())).thenReturn(true);

        ResponseEntity<Void> result = sellerService.deleteSeller(1);

        Assertions.assertThat(result.getStatusCode())
                .isEqualTo(ResponseEntity.noContent().build().getStatusCode());

        verify(sellerRepository, times(1)).deleteById(Mockito.anyInt());
    }

    @Test
    public void SellerService_DeleteSeller_NotFound(){
        when(sellerRepository.existsById(Mockito.anyInt())).thenReturn(false);

        ResponseEntity<Void> result = sellerService.deleteSeller(15);

        Assertions.assertThat(result.getStatusCode())
                .isEqualTo(ResponseEntity.notFound().build().getStatusCode());

        verify(sellerRepository, times(0)).deleteById(Mockito.anyInt());
    }
    
    @Test
    public void SellerService_UpdateSeller_UpdateExistingSeller(){

        when(sellerRepository.existsById(Mockito.anyInt())).thenReturn(true);
        when(sellerRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(seller1));
        when(sellerRepository.save(Mockito.any(Seller.class))).thenReturn(seller1);

        ResponseEntity<Seller> savedSeller = sellerService
                .updateSeller(1, new SellerDTO("Adolf", "adolf@gmail.com"));

        Assertions.assertThat(savedSeller.getStatusCode())
                .isEqualTo(ResponseEntity.ok().build().getStatusCode());
        Assertions.assertThat(savedSeller.getBody())
                .isEqualTo(seller1);
    }

    @Test
    public void SellerService_UpdateSeller_UpdateNonExistingSeller(){
        when(sellerRepository.existsById(Mockito.anyInt())).thenReturn(false);

        ResponseEntity<Seller> savedSeller = sellerService
                .updateSeller(1, new SellerDTO("Adolf", "adolf@gmail.com"));

        Assertions.assertThat(savedSeller.getStatusCode())
                .isEqualTo(ResponseEntity.notFound().build().getStatusCode());

    }
}
