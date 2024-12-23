package gleb.dresher.AccountShopApi.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "sellers")
@JsonIgnoreProperties({"accounts"})
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "seller_name")
    private String name;
    private String email;

    @OneToMany(mappedBy = "seller")
    @JsonBackReference
    private List<Account> accounts;

    public Seller(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
