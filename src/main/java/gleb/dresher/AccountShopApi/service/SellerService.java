package gleb.dresher.AccountShopApi.service;

import gleb.dresher.AccountShopApi.dto.SellerDTO;
import gleb.dresher.AccountShopApi.dto.SellerResponse;
import gleb.dresher.AccountShopApi.entity.Account;
import gleb.dresher.AccountShopApi.entity.Seller;
import gleb.dresher.AccountShopApi.repository.AccountRepository;
import gleb.dresher.AccountShopApi.repository.SellerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SellerService {
    private final SellerRepository sellerRepository;
    private final AccountRepository accountRepository;

    public SellerService(SellerRepository sellerRepository, AccountRepository accountRepository) {
        this.sellerRepository = sellerRepository;
        this.accountRepository = accountRepository;
    }

    public ResponseEntity<SellerResponse> getSeller(int id) {
        Optional<Seller> seller = sellerRepository.findById(id);

        if (seller.isPresent()){
            SellerResponse sellerResponse = new
                    SellerResponse(seller.get().getName(),
                    seller.get().getAccounts());
            return ResponseEntity.ok(sellerResponse);
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<Seller> addSeller(SellerDTO sellerDTO) {
        Seller seller = new Seller(sellerDTO.getName(), sellerDTO.getEmail());
        URI location = URI.create("/seller/");
        return ResponseEntity.created(location).body(sellerRepository.save(seller));
    }

    public ResponseEntity<Void> deleteSeller(int id) {
        if (!sellerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        accountRepository.deleteAllBySellerId(id);
        sellerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Seller> updateSeller(int id, SellerDTO sellerDTO) {
        if (!sellerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Seller seller = sellerRepository.findById(id).get();
        if (sellerDTO.getName() != null) {
            seller.setName(sellerDTO.getName());
        }
        if (sellerDTO.getEmail() != null) {
            seller.setEmail(sellerDTO.getEmail());
        }
        sellerRepository.save(seller);
        return ResponseEntity.ok(seller);
    }
}
