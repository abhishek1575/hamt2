package cstech.ai.hamt.repository;

import cstech.ai.hamt.dto.ItemDto;
import cstech.ai.hamt.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = "Select * from item where is_deleted = false", nativeQuery = true)
    List<Item> getAll();

    @Query(value = "DELETE FROM Request r WHERE r.item.id = :itemId", nativeQuery = true)
    void deleteByItemId(@Param("itemId") Long itemId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE item SET is_deleted = true WHERE item_id = :itemId", nativeQuery = true)
    void setIsDeleted(@Param("itemId") Long itemId);


    @Query(value="SELECT * FROM item WHERE category='asset' ",nativeQuery=true)
    List<ItemDto> findAllAsset();

    @Query(value= "SELECT * FROM item WHERE category='component' ",nativeQuery=true)
    List<ItemDto> findAllComponent();

    @Query(value="SELECT * FROM item WHERE sap_no = :sap_no", nativeQuery=true)
    Optional<Item> findBySap_no(Long sap_no);

    @Modifying
    @Query("UPDATE Item i SET i.isReturnable = :isReturnable WHERE i.id = :id")
    void updateReturnable(@Param("isReturnable") boolean isReturnable, @Param("id") Long id);

    @Query(value="SELECT * FROM item WHERE is_deleted = true", nativeQuery=true)
    List<Item> getAllDeletedItem();

    @Transactional
    @Modifying
    @Query(value="UPDATE item SET is_deleted = false WHERE item_id = :itemId", nativeQuery=true)
    void undoItem(@Param("itemId")Long itemId);

}
