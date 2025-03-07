package gleb.dresher.AccountShopApi.controller;

import gleb.dresher.AccountShopApi.dto.SellerDTO;
import gleb.dresher.AccountShopApi.dto.SellerResponse;
import gleb.dresher.AccountShopApi.entity.Account;
import gleb.dresher.AccountShopApi.entity.Seller;
import gleb.dresher.AccountShopApi.repository.AccountRepository;
import gleb.dresher.AccountShopApi.repository.SellerRepository;
import gleb.dresher.AccountShopApi.service.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerController {
    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Operation(summary = "Get seller",
            description = "Get seller and list of seller accounts from database.")
    @GetMapping("/{id}")
    public ResponseEntity<SellerResponse> getSeller(@PathVariable int id){
        return sellerService.getSeller(id);
    }

    @Operation(summary = "Add seller",
            description = "Add new seller to database, using SellerDto")
    @PostMapping
    public ResponseEntity<Seller> addSeller(@RequestBody SellerDTO sellerDTO){
        return sellerService.addSeller(sellerDTO);
    }

    @Operation(summary = "Delete seller",
    description = "Delete seller and all account that connected to seller " +
            "from db")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteSeller(@PathVariable int id){
        return sellerService.deleteSeller(id);
    }

    @Operation(summary = "Update seller",
            description = "Update seller and return updated seller")
    @PutMapping("/{id}")
    public ResponseEntity<Seller> updateSeller(@PathVariable int id,
                                               @RequestBody SellerDTO sellerDTO){
        return sellerService.updateSeller(id, sellerDTO);
    }
}
