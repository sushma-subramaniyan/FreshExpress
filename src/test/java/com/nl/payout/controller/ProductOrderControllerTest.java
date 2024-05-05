package com.nl.payout.controller;

import com.nl.payout.mockdata.MockDataProvider;
import com.nl.payout.model.request.CartRequest;
import com.nl.payout.service.ProductOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductOrderControllerTest {
    @Mock
    private ProductOrderService productOrderService;

    private MockMvc mockMvc;


    @BeforeEach
    public void setup() {
        ProductOrderController controller = new ProductOrderController(productOrderService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();

    }

    private static final String GET_API_URL = "/api/frontend-service/product/details";
    private static final String PUT_API_URL = "/api/frontend-service/order/checkout";


    private static final String request ="{\"productList\": [{ \"productId\": 1,\"productName\": \"Tomato\",\"productPrice\": 1.0,\"productQuantity\": 1, \"productWeight\":501,\"categoryId\" :1 }]}";

    @Test
    void testGetProductDetails() throws Exception {
        when(productOrderService.getProductDetails()).thenReturn(MockDataProvider.getOrderDetails());
       var response= mockMvc
                .perform(MockMvcRequestBuilders.get(GET_API_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertNotNull(response);
        verify(productOrderService, times(1)).getProductDetails();
    }

    @Test
    void testGetCardDetails() throws Exception {
        when(productOrderService.getCardDetails(any(CartRequest.class))).thenReturn(MockDataProvider.getCardDetails());
        var response= mockMvc
                .perform(MockMvcRequestBuilders.post(PUT_API_URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertNotNull(response);
        verify(productOrderService, times(1)).getCardDetails(any(CartRequest.class));
    }


    @Test
    void testGetProductDetailsInvalidUrl() throws Exception {
        var response= mockMvc
                .perform(MockMvcRequestBuilders.get(GET_API_URL+"InvalidUrl")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        verify(productOrderService, times(0)).getProductDetails();
    }

    @Test
    void testGetCardDetailsWithInvalidRequest() throws Exception {
        var response= mockMvc
                .perform(MockMvcRequestBuilders.post(PUT_API_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        verify(productOrderService, times(0)).getCardDetails(any(CartRequest.class));
    }


}
