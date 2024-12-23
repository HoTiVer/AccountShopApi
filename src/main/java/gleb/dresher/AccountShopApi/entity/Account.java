package gleb.dresher.AccountShopApi.entity;

import com.fasterxml.jackson.annotation.*;
import gleb.dresher.AccountShopApi.enums.AccountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Entity
@Component
@NoArgsConstructor
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

//  @ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne
    @JoinColumn(name = "seller_id")
    @JsonManagedReference
    private Seller seller;

    public Account(String name, double price, AccountType accountType) {
        this.name = name;
        this.price = price;
        this.accountType = accountType;
    }
}
