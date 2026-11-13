package com.example.mcptest.map_test.controller;

import com.example.mcptest.map_test.entity.Item;
import com.example.mcptest.map_test.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemService itemService;

    private Item sampleItem() {
        Item item = new Item("Widget", "A widget", 9.99, 5);
        item.setId(1L);
        return item;
    }

    @Test
    void create_returns201_withQuantity() throws Exception {
        when(itemService.create(any(Item.class))).thenReturn(sampleItem());

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Widget\",\"description\":\"A widget\",\"price\":9.99,\"quantity\":5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void create_returns400_whenQuantityMissing() throws Exception {
        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Widget\",\"description\":\"A widget\",\"price\":9.99}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_returns400_whenQuantityNull() throws Exception {
        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Widget\",\"description\":\"A widget\",\"price\":9.99,\"quantity\":null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_returns200_withQuantity() throws Exception {
        Item updated = new Item("Widget", "A widget", 9.99, 42);
        updated.setId(1L);
        when(itemService.update(anyLong(), any(Item.class))).thenReturn(Optional.of(updated));

        mockMvc.perform(put("/api/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Widget\",\"description\":\"A widget\",\"price\":9.99,\"quantity\":42}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(42));
    }

    @Test
    void update_returns400_whenQuantityNull() throws Exception {
        mockMvc.perform(put("/api/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Widget\",\"description\":\"A widget\",\"price\":9.99,\"quantity\":null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById_includesQuantity() throws Exception {
        when(itemService.readById(1L)).thenReturn(Optional.of(sampleItem()));

        mockMvc.perform(get("/api/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void getAll_includesQuantity() throws Exception {
        when(itemService.readAll()).thenReturn(List.of(sampleItem()));

        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quantity").value(5));
    }
}
