package gleb.dresher.AccountShopApi.dto;

import gleb.dresher.AccountShopApi.enums.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AccountDTO",
        description = "Account data transfer object to create new account or edit account")
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }
}
