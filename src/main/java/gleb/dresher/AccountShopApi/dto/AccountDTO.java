package gleb.dresher.AccountShopApi.dto;

import gleb.dresher.AccountShopApi.enums.AccountType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {
    private String name;
    private double price;
    private AccountType accountType;

    private int sellerId;

    public AccountDTO(String name, double price, AccountType accountType, int sellerId) {
        this.name = name;
        this.price = price;
        this.accountType = accountType;
        this.sellerId = sellerId;
    }
}
