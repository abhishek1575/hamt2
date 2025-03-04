package cstech.ai.hamt.repository;

import cstech.ai.hamt.entity.ApprovalStatus;
import cstech.ai.hamt.entity.ItemRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {
    Optional<ItemRequest> findById(Long id);

    @Query("SELECT ir FROM ItemRequest ir JOIN FETCH ir.item")
    List<ItemRequest> getAllRequest();

    @Modifying
    @Transactional
    @Query(value = "UPDATE request SET approval_status = 'DENIED' WHERE id = :requestId", nativeQuery = true)
    void updateStatus(@Param("requestId") Long requestId);


    @Query(value = "SELECT * FROM request WHERE is_approved = 0",nativeQuery = true)
    List<ItemRequest> getAllNonApproved();

    @Query("SELECT ir FROM ItemRequest ir JOIN FETCH ir.item WHERE ir.approvalStatus = :approvalStatus")
    List<ItemRequest> findByApprovalStatus(@Param("approvalStatus") ApprovalStatus approvalStatus);

    boolean existsByIsNewRequestTrue();

    @Query(value="SELECT * FROM request  WHERE user_id = :userId", nativeQuery=true)
    List<ItemRequest> findByUserId(@Param("userId") Long userId);
}
