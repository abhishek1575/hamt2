package cstech.ai.hamt.service;

import ch.qos.logback.core.CoreConstants;
import cstech.ai.hamt.dto.ItemDto;
import cstech.ai.hamt.entity.Item;
import cstech.ai.hamt.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemsService {

    @Autowired
    private ItemRepository itemRepository;


    public List<ItemDto> getAllComponent(){
        return itemRepository.getAll().stream().map(this::mapToItemDto)
                .collect(Collectors.toList());
    }
    @Transactional
    public  String modifyItem(ItemDto itemDto){
        Item item = itemRepository.findById(itemDto.getId())
                .orElseThrow(()->new RuntimeException("Item Not Found"));

        item.setName(itemDto.getName());
        item.setCategory(itemDto.getCategory());
        item.setSubCategory(itemDto.getSubCategory());
        item.setValue(itemDto.getValue());
        item.setDescription(itemDto.getDescription());
        item.setManufacturer(itemDto.getManufacturer());
        item.setMpn(itemDto.getMpn());
        item.setLocation(itemDto.getLocation());
        item.setPackage_box(itemDto.getPackage_box());
        item.setStock(itemDto.getStock());
        item.setSap_no(itemDto.getSap_no());

        itemRepository.updateReturnable(itemDto.isReturnable(), itemDto.getId());


        itemRepository.save(item);
        return "modify Successfully";
    }
    public boolean isPresent(Item item){
        Optional<Item> item1 = itemRepository.findBySap_no(item.getSap_no());
        if(item1.isPresent()){
            return true;
        }else return false;
    }


    public boolean delete(Long id) {

        Optional<Item> item = itemRepository.findById(id);

        if (item.isPresent()) {

            itemRepository.setIsDeleted(id);// Delete the item

            return true;
        } else {
            return false;
        }
    }
    private ItemDto mapToItemDto(Item item){
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .value(item.getValue())
                .category(item.getCategory())
                .subCategory(item.getSubCategory())
                .description(item.getDescription())
                .manufacturer(item.getManufacturer())
                .package_box(item.getPackage_box())
                .mpn(item.getMpn())
                .sap_no(item.getSap_no())
                .location(item.getLocation())
                .stock(item.getStock())
                .build();

    }

}

