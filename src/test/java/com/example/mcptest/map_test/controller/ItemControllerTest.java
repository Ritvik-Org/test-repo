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
import static org.mockito.ArgumentMatchers.eq;
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

    private Item itemWithId(Long id, String name, String description, Double price, Integer quantity) {
        Item item = new Item(name, description, price, quantity);
        item.setId(id);
        return item;
    }

    @Test
    void createReturns400WhenQuantityMissing() throws Exception {
        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Widget\",\"description\":\"A widget\",\"price\":9.99}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createReturns400WhenQuantityNull() throws Exception {
        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Widget\",\"description\":\"A widget\",\"price\":9.99,\"quantity\":null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createReturns201WhenValid() throws Exception {
        when(itemService.create(any(Item.class)))
                .thenAnswer(invocation -> {
                    Item input = invocation.getArgument(0);
                    input.setId(1L);
                    return input;
                });

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Widget\",\"description\":\"A widget\",\"price\":9.99,\"quantity\":7}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.quantity").value(7));
    }

    @Test
    void updateReturns400WhenQuantityNull() throws Exception {
        mockMvc.perform(put("/api/items/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Widget\",\"description\":\"A widget\",\"price\":9.99,\"quantity\":null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateReturns200AndPersistsQuantity() throws Exception {
        when(itemService.update(eq(1L), any(Item.class)))
                .thenAnswer(invocation -> {
                    Item input = invocation.getArgument(1);
                    input.setId(1L);
                    return Optional.of(input);
                });

        mockMvc.perform(put("/api/items/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Widget\",\"description\":\"A widget\",\"price\":9.99,\"quantity\":12}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(12));
    }

    @Test
    void readByIdReturnsQuantity() throws Exception {
        when(itemService.readById(1L))
                .thenReturn(Optional.of(itemWithId(1L, "Widget", "A widget", 9.99, 3)));

        mockMvc.perform(get("/api/items/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(3));
    }

    @Test
    void readAllReturnsQuantity() throws Exception {
        when(itemService.readAll())
                .thenReturn(List.of(itemWithId(1L, "Widget", "A widget", 9.99, 3)));

        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quantity").value(3));
    }
}
