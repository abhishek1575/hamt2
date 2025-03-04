package cstech.ai.hamt.dto;

import cstech.ai.hamt.entity.ApprovalStatus;
import cstech.ai.hamt.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ItemRequestDto  {
    private Long id;
    private Item item;
    private int quantityRequest;
    private ApprovalStatus approvalStatus;
    private LocalDateTime localDateTime;
    private  String userName;
    private Long userId;
    private String projectName;
    private String approvedBy;
    private String remark;
    private boolean isNewRequest;
    private LocalDateTime returnDate;

}
