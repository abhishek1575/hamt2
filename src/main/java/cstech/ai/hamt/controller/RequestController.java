package cstech.ai.hamt.controller;

import cstech.ai.hamt.dto.ItemRequestDto;
import cstech.ai.hamt.entity.ItemRequest;
import cstech.ai.hamt.repository.RequestRepository;
import cstech.ai.hamt.service.EmailService;
import cstech.ai.hamt.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/request")
public class RequestController {


    @Autowired
    private RequestService requestService;

    @Autowired
    private RequestRepository requestRepository;


    // to send request from available button
    @PostMapping("/request")
    @PreAuthorize("hasAuthority('SUPER_ADMIN') or hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<?> requestItem(@RequestBody ItemRequest itemRequest){
        System.out.println("ItemRequest Body from controller "+ itemRequest);
        String response = requestService.requestItem(itemRequest);
        return ResponseEntity.ok(response);
    }

    //approve request from request in admin dashboard
    @PostMapping("/approved")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> approvedRequest(@RequestParam Long requestId, @RequestParam String adminName){
        String response = requestService.approveRequest(requestId, adminName);
        return ResponseEntity.ok(response);
    }

    //Get all history of request in History table in admin dashboard
    @GetMapping("/getAllRequest")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllHistory() {
        try {
            List<ItemRequest> itemRequests = requestRepository.getAllRequest();
            List<ItemRequestDto> itemDtos = itemRequests.stream().map(itemRequest -> {
                ItemRequestDto itemRequestDto = new ItemRequestDto();
                itemRequestDto.setItem(itemRequest.getItem());
                itemRequestDto.setQuantityRequest(itemRequest.getQuantityRequested());
                itemRequestDto.setId(itemRequest.getId());
                itemRequestDto.setLocalDateTime(itemRequest.getLocalDateTime());
                itemRequestDto.setApprovalStatus(itemRequest.getApprovalStatus());
                itemRequestDto.setUserName(itemRequest.getUserName());
                itemRequestDto.setProjectName(itemRequest.getProjectName());
                itemRequestDto.setRemark(itemRequest.getRemark());
                itemRequestDto.setApprovedBy(itemRequest.getApprovedBy());
                itemRequestDto.setUserId(itemRequest.getUserId());
                itemRequestDto.setReturnDate(itemRequest.getReturnDate());
                return itemRequestDto;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(itemDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    // get all non-approved request in request table in admin dashboard
    @GetMapping("/getAllNonApprovedRequests")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllNonApprovedRequests() {
        try {
            List<ItemRequestDto> itemDtos = requestService.getAllNonApprovedRequest();
            if(itemDtos.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Request found");
            }
            return ResponseEntity.ok(itemDtos);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    // deny request from request table in admin dashboard
    @DeleteMapping("/deleteRequest")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteRequest(@RequestParam Long requestId, @RequestParam String adminName){
        Optional<ItemRequest> requestInfo = requestRepository.findById(requestId);

        if (requestInfo.isPresent()) {
            try {
                ItemRequest request = requestInfo.get();
                request.setApprovedBy(adminName);
                requestRepository.updateStatus(requestId);
                return ResponseEntity.ok("request deny successfully");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("request not found");
        }
    }

    //Getting History By User ID
    @GetMapping("/getRequestByUserId")
    public ResponseEntity<?> getRequestByUserId(@RequestParam Long userId) {
        List<ItemRequestDto> dtoList = requestService.getRequestByUserId(userId);
        if (dtoList.isEmpty()) {
            // Returning a JSON object instead of a plain string
            return ResponseEntity.ok(Collections.singletonMap("message", "No History Found"));
        }
        return ResponseEntity.ok(dtoList);
    }



}

