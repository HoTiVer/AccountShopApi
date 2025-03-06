package gleb.dresher.AccountShopApi.entity;

import com.fasterxml.jackson.annotation.*;
import gleb.dresher.AccountShopApi.enums.AccountType;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Entity
@Component
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AccountType accountType;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    @JsonManagedReference
    private Seller seller;

    public Account() {
    }

    public Account(String name, double price, AccountType accountType) {
        this.name = name;
        this.price = price;
        this.accountType = accountType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
}
