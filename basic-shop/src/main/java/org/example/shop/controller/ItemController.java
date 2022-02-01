package org.example.shop.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.shop.domain.item.Book;
import org.example.shop.domain.item.Item;
import org.example.shop.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;

  @GetMapping("/items/new")
  public String createForm(Model model) {

    model.addAttribute("form", new BookForm());
    return "items/createItemForm";
  }

  /**
   * 실무에서는 변경을 최소화하기 위해 setter 를 다 날림 static method or 생성자 메서드를 통해서 생성하는 것이 옳은 방법
   */
  @PostMapping("/items/new")
  public String create(@ModelAttribute("form") BookForm form) {
    Book book = new Book();
    book.setName(form.getName());
    book.setStockQuantity(form.getStockQuantity());
    book.setPrice(form.getPrice());
    book.setAuthor(form.getAuthor());
    book.setIsbn(form.getIsbn());

    itemService.saveItem(book);
    return "redirect:/";
  }

  @GetMapping("/items")
  public String list(Model model) {
    List<Item> items = itemService.findItems();
    model.addAttribute("items", items);
    return "items/itemList";
  }

  @GetMapping("/items/{itemId}/edit")
  public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {

    Book item = (Book) itemService.findOne(itemId);

    BookForm bookForm = new BookForm();
    bookForm.setId(item.getId());
    bookForm.setAuthor(item.getAuthor());
    bookForm.setIsbn(item.getIsbn());
    bookForm.setName(item.getName());
    bookForm.setPrice(item.getPrice());
    bookForm.setStockQuantity(item.getStockQuantity());

    model.addAttribute("form", bookForm);
    return "items/updateItemForm";
  }

  @PostMapping("/items/{itemId}/edit")
  public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) {

    itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
    return "redirect:/items";
  }
}
