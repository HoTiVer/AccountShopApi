package gleb.dresher.AccountShopApi.controller;

import gleb.dresher.AccountShopApi.dto.SellerDTO;
import gleb.dresher.AccountShopApi.entity.Account;
import gleb.dresher.AccountShopApi.entity.Seller;
import gleb.dresher.AccountShopApi.repository.AccountRepository;
import gleb.dresher.AccountShopApi.repository.SellerRepository;
import gleb.dresher.AccountShopApi.service.SellerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {
    private final SellerService sellerService;

    @GetMapping
    public List<Seller> getAllSellers(){
        return sellerService.getAllSellers();
    }

    @GetMapping("/{id}/accounts")
    public List<Account> getSellerAccounts(@PathVariable int id){
        return sellerService.getSellerAccounts(id);
    }

    @PostMapping
    public Seller addSeller(@RequestBody SellerDTO sellerDTO){
        return sellerService.addSeller(sellerDTO);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void deleteSeller(@PathVariable int id){
        sellerService.deleteSeller(id);
    }

    @PutMapping("/{id}")
    public String updateSeller(@PathVariable int id, @RequestBody SellerDTO sellerDTO){
        return sellerService.updateSeller(id, sellerDTO);
    }
}
