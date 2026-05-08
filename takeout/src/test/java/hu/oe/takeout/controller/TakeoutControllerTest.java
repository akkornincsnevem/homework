package hu.oe.takeout.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.oe.takeout.service.CategoryService;
import hu.oe.takeout.service.TakeoutService;
import hu.oe.takeout.takeout.generated.rest.model.TakeoutResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class TakeoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TakeoutService takeoutService;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    void shouldReturnAllTakeouts() throws Exception {

        // given
        TakeoutResponse pizza = new TakeoutResponse();
        pizza.setId("1");
        pizza.setName("Pizza");
        pizza.setDescription("Cheese pizza");

        TakeoutResponse burger = new TakeoutResponse();
        burger.setId("2");
        burger.setName("Burger");
        burger.setDescription("Beef burger");

        when(takeoutService.getAll())
                .thenReturn(List.of(pizza, burger));

        // when + then
        mockMvc.perform(get("/takeout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Pizza"))
                .andExpect(jsonPath("$[0].description").value("Cheese pizza"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].name").value("Burger"))
                .andExpect(jsonPath("$[1].description").value("Beef burger"));
    }
}