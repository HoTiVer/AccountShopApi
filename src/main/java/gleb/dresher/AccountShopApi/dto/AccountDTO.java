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
}
