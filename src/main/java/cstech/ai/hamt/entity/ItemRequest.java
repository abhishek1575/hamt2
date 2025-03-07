package cstech.ai.hamt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "request")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "quantity_requested")
    private int quantityRequested;

    @Enumerated(EnumType.STRING)  // Persist the enum as a string value in the database
    @Column(name = "approval_status")
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING_FOR_APPROVAL; // Default to PENDING_FOR_APPROVAL

    @Column(name = "user_name")
    private String userName;

    @Column(name="userId" , nullable=false)
    private Long userId;

    private String approvedBy;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "remark")
    private String remark;

    @Column(name = "requestDate")
    private LocalDateTime localDateTime;

    @PrePersist
    public void onCreate() {
        this.localDateTime = LocalDateTime.now();
    }

    @Column(name = "is_new_request", nullable = false)
    private boolean isNewRequest = true;


    @Column(name="returnDate")
    private LocalDateTime returnDate;

}

