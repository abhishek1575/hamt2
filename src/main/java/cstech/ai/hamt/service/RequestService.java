package cstech.ai.hamt.service;

import cstech.ai.hamt.dto.ItemRequestDto;
import cstech.ai.hamt.entity.ApprovalStatus;
import cstech.ai.hamt.entity.Item;
import cstech.ai.hamt.entity.ItemRequest;
import cstech.ai.hamt.repository.ItemRepository;
import cstech.ai.hamt.repository.RequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ItemRepository itemRepository;

    public String  requestItem(ItemRequest itemRequest){
        Optional<Item> optionalItem = itemRepository.findById(itemRequest.getId());
        if(optionalItem.isEmpty()){
            return "Item Not Found";
        }
        Item item = optionalItem.get();
        if(item.getStock()< itemRequest.getQuantityRequested()){
            return "Requested Quantity is exceeds Available Quantity";
        }

        ItemRequest itemRequest1 = mapToItemRequest(itemRequest,item  );
//            ItemRequest itemRequest1 = new ItemRequest();
//            itemRequest1.setItem(item);
//            itemRequest1.setQuantityRequested(itemRequest.getQuantityRequested());
//            itemRequest1.setRemark(itemRequest.getRemark());
//            itemRequest1.setProjectName(itemRequest.getProjectName());
//            itemRequest1.setUserName(itemRequest.getUserName());
//            itemRequest1.setApprovalStatus(ApprovalStatus.PENDING_FOR_APPROVAL);
//            itemRequest1.setNewRequest(true);

        requestRepository.save(itemRequest1);
        return "Request sent to the admin";
    }

    public List<ItemRequestDto> getAllNonApprovedRequest() {
        List<ItemRequest> itemRequests=requestRepository.findByApprovalStatus(ApprovalStatus.PENDING_FOR_APPROVAL);
        // Map ItemRequest objects to ItemRequestDto objects
        return itemRequests.stream().map(this::mapToItemRequestDto).collect(Collectors.toList());
    }

    @Transactional
    public String approveRequest(Long requestId){
        Optional<ItemRequest> itemRequest = requestRepository.findById(requestId);
        if(itemRequest.isEmpty()){
            return "Request Not Found";
        }

        ItemRequest request = itemRequest.get();
        if(request.getApprovalStatus().toString().equals(ApprovalStatus.APPROVED.toString())){
            return "Request is Already Approved";
        }

        Item item = request.getItem();
        item.setStock(item.getStock()- request.getQuantityRequested());
        request.setApprovalStatus(ApprovalStatus.APPROVED);
        itemRepository.save(item);
        requestRepository.save(request);

        return "Request Approved and Stock Updated";
    }

//    @Transactional
//    public List<ItemRequestDto> getRequestByUserId(Long userId){
//        List<ItemRequest> itemRequests = requestRepository.findByUserId(userId);
//        if (itemRequests.isEmpty()){
//            throw new ResourceNotFoundException("No Request found for this User");
//        }
//        return itemRequests.stream().map(this::mapToItemRequestDto)
//                .collect(Collectors.toList());
//    }

    @Transactional
    public List<ItemRequestDto> getRequestByUserId(Long userId) {
        List<ItemRequest> itemRequests = requestRepository.findByUserId(userId);
        return itemRequests.stream().map(this::mapToItemRequestDto)
                .collect(Collectors.toList());
    }

    private ItemRequestDto mapToItemRequestDto (ItemRequest itemRequest){
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .item(itemRequest.getItem())
                .quantityRequest(itemRequest.getQuantityRequested())
                .localDateTime(itemRequest.getLocalDateTime())
                .approvalStatus(itemRequest.getApprovalStatus())
                .userName(itemRequest.getUserName())
                .userId(itemRequest.getUserId())
                .projectName(itemRequest.getProjectName())
                .remark(itemRequest.getRemark())
                .build();
    }

    private ItemRequest mapToItemRequest(ItemRequest itemRequest, Item item ){
        return ItemRequest.builder()
                .item(item)
                .quantityRequested(itemRequest.getQuantityRequested())
                .projectName(itemRequest.getProjectName())
                .remark(itemRequest.getRemark())
                .approvalStatus(itemRequest.getApprovalStatus())
                .userName(itemRequest.getUserName())
                .userId(itemRequest.getUserId())
                .build();
    }
}
