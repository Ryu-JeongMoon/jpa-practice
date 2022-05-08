package org.example.shop.service;

import java.util.List;

import org.example.shop.domain.item.Item;
import org.example.shop.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

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

/*
실무에서는 Service 계층에서 setter 남발하면 변경 추적이 어려워진다
의미 있는 이름의 메서드를 만들어 변경하도록 하고 단발성 변경을 줄이거나 없애야 한다
 */