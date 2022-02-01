package org.example.shop.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.shop.domain.item.Item;
import org.example.shop.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

  private final ItemRepository itemRepository;

  @Transactional
  public void saveItem(Item item) {
    itemRepository.save(item);
  }

  public Item findOne(Long id) {
    return itemRepository.findOne(id);
  }

  public List<Item> findItems() {
    return itemRepository.findAll();
  }

  public void updateItem(Long itemId, String name, int price, int stockQuantity) {
    Item item = itemRepository.findOne(itemId);
    item.setName(name);
    item.setPrice(price);
    item.setStockQuantity(stockQuantity);
  }
}
