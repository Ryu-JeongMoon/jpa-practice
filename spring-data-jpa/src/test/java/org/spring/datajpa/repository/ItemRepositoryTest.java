package org.spring.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.spring.datajpa.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;

class ItemRepositoryTest {

  @Autowired
  ItemRepository itemRepository;

  @Test
  void itemTest() {
    Item item = new Item();
    itemRepository.save(item);
  }
}