package com.example.mcptest.map_test.service;

import com.example.mcptest.map_test.entity.Item;
import com.example.mcptest.map_test.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item create(Item item) {
        return itemRepository.save(item);
    }

    public List<Item> readAll() {
        return itemRepository.findAll();
    }

    public Optional<Item> readById(Long id) {
        return itemRepository.findById(id);
    }

    public Optional<Item> update(Long id, Item updated) {
        return itemRepository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setDescription(updated.getDescription());
            existing.setPrice(updated.getPrice());
            existing.setQuantity(updated.getQuantity());
            return itemRepository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (!itemRepository.existsById(id)) {
            return false;
        }
        itemRepository.deleteById(id);
        return true;
    }
}
