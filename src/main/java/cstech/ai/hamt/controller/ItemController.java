package cstech.ai.hamt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cstech.ai.hamt.dto.ItemDto;
import cstech.ai.hamt.entity.Item;
import cstech.ai.hamt.repository.ItemRepository;
import cstech.ai.hamt.repository.RequestRepository;
import cstech.ai.hamt.service.ItemsService;
import cstech.ai.hamt.service.RequestService;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemsService itemsService;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ItemRepository itemRepository;


    // get all item in dashboard
    @GetMapping("/getAll")
    @PreAuthorize("hasAuthority('SUPER_ADMIN') or hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<?> getAllComponent(){
        try {
            List<ItemDto> itemDtos = itemsService.getAllComponent();
            return ResponseEntity.ok(itemDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error " + e.getMessage());
        }
    }


    // add a new items in add items button
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> addItems(@RequestBody Item item){
        try {
            if (itemsService.isPresent(item)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Item Already Exist");
            }else {
                itemRepository.save(item);
                return ResponseEntity.ok("Item Added Successfully");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected Error"+e.getMessage());
        }
    }

    // edit item in edit button
    @PutMapping("/edit")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> modifyUser(@RequestBody ItemDto itemDto){
        try {
            String response = itemsService.modifyItem(itemDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected Body"+e.getMessage());
        }
    }

    // request for notification
    @GetMapping("/hasNewRequests")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Boolean> hasNewRequests() {
        boolean hasNewRequests = requestRepository.existsByIsNewRequestTrue();
        return ResponseEntity.ok(hasNewRequests);
    }

    // delete item in delete button
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteItem(@RequestParam Long ID) {

        try {
            if (itemsService.delete(ID)) {
                return ResponseEntity.ok("Item Deleted Successfully");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Item Not Found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected Error: " + e.getMessage());
        }
    }

    @GetMapping("getAllDeleteItems")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllDeleteItems(){
        try{
            List<ItemDto> itemDtos = itemsService.getAllDeletedItems();
            if(itemDtos.isEmpty()){
                return ResponseEntity.ok("No Deleted Item Found");
            }
            return ResponseEntity.ok(itemDtos);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected Error "+e.getMessage());
        }

    }
    @PostMapping("/undo")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> undoDeletedItem(@RequestParam Long id){
        try{
            if (itemsService.undoDeletedItems(id)){
                return ResponseEntity.ok("Item Saved Successfully");
            }else return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Item Not Found ");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected Error "+e.getMessage());
        }
    }

    @DeleteMapping("/deletePermanent")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteItemPermanent(@RequestParam Long id) {
        try {
            System.out.println("id from controller: "+id);
            if (itemsService.deletePermanent(id)) {
                return ResponseEntity.ok("Item Deleted Permanently");
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item Not Found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected Error"+e.getMessage());
        }
    }
}
