package gleb.dresher.AccountShopApi.repository;

import gleb.dresher.AccountShopApi.entity.Account;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByPriceGreaterThan(double price);

    @Query(value = "SELECT * FROM accounts WHERE price < :price", nativeQuery = true)
    List<Account> findByPriceLessThan(@Param("price") double price);

    @Query(value = "SELECT * FROM accounts WHERE price BETWEEN :min AND :max",nativeQuery = true)
    List<Account> findByPriceBetween(@Param("min") double min, @Param("max") double max);

    @Query(value = "SELECT * FROM accounts WHERE type LIKE :type", nativeQuery = true)
    List<Account> findByAccountType(@Param("type") String type);

    @Query(value = "SELECT * FROM accounts WHERE id = :id", nativeQuery = true)
    Account customIdSearch(@Param("id") int id);

    @Query(value = "DELETE FROM accounts WHERE seller_id = :id", nativeQuery = true)
    @Modifying
    void deleteAllBySellerId(@Param("id") int id);
}
