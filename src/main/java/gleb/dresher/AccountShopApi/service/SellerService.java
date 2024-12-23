package gleb.dresher.AccountShopApi.service;

import gleb.dresher.AccountShopApi.dto.SellerDTO;
import gleb.dresher.AccountShopApi.entity.Account;
import gleb.dresher.AccountShopApi.entity.Seller;
import gleb.dresher.AccountShopApi.repository.AccountRepository;
import gleb.dresher.AccountShopApi.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;
    private final AccountRepository accountRepository;

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    public List<Account> getSellerAccounts(int id) {
        return sellerRepository.findById(id).get().getAccounts();
    }

    public Seller addSeller(SellerDTO sellerDTO) {
        Seller seller = new Seller(sellerDTO.getName(), sellerDTO.getEmail());
        return sellerRepository.save(seller);
    }

    public void deleteSeller(int id) {
        accountRepository.deleteAllBySellerId(id);
        sellerRepository.deleteById(id);
    }

    public String updateSeller(int id, SellerDTO sellerDTO) {
        if (!sellerRepository.existsById(id)) {
            return "Seller not found";
        }
        Seller seller = sellerRepository.findById(id).get();
        if (sellerDTO.getName() != null) {
            seller.setName(sellerDTO.getName());
        }
        if (sellerDTO.getEmail() != null) {
            seller.setEmail(sellerDTO.getEmail());
        }
        sellerRepository.save(seller);
        return "Seller with id " + id + " updated successfully";
    }
}
