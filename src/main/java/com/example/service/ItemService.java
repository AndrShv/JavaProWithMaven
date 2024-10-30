package com.example.service;

import com.example.model.Item;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {
    private final List<Item> items = new ArrayList<>();

    public List<Item> getAllItems() {
        return new ArrayList<>(items);
    }

    public Item addItem(Item item) {
        items.add(item);
        return item;
    }

    public Item updateItem(int index, Item item) {
        if (index >= 0 && index < items.size()) {
            items.set(index, item);
        }
        return item;
    }

    public void deleteItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        }
    }
}
