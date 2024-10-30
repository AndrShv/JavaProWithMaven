package com.example.controller;

import com.example.model.Item;
import com.example.service.ItemService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import com.example.model.Item;
import com.example.service.ItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/all")
    public List<Item> getItems() {
        return itemService.getAllItems();
    }

    @PostMapping("/add")
    public Item addItem(@RequestBody Item item) {
        return itemService.addItem(item);
    }

    @PutMapping("/{index}")
    public Item updateItem(@PathVariable int index, @RequestBody Item item) {
        return itemService.updateItem(index, item);
    }

    @DeleteMapping("/{index}")
    public String deleteItem(@PathVariable int index) {
        itemService.deleteItem(index);
        return "Item deleted successfully";
    }
}
