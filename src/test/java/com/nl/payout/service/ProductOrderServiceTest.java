package com.nl.payout.service;

import com.nl.payout.business.BusinessHelper;
import com.nl.payout.dto.repository.ProductRepository;
import com.nl.payout.mockdata.MockDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProductOrderServiceTest {

    @Mock
    ProductRepository productRepository;

    BusinessHelper businessLayer = new BusinessHelper();
    ProductOrderService productOrderService;


    @BeforeEach
    public void setup() {
        productOrderService = new ProductOrderService(productRepository, businessLayer);
    }

    @Test
    void testGetProductDetails() {
        when(productRepository.findAll()).thenReturn(MockDataProvider.getProductDetails());
        var orderDetails = productOrderService.getProductDetails();
        assertAll(
                () -> assertNotNull(orderDetails),
                () -> assertEquals("Tomatto", orderDetails.getProducts().stream().findFirst().get().getProductName()),
                () -> assertEquals(1, orderDetails.getProducts().stream().findFirst().get().getProductId())
        );
    }

    @Test
    void testGetEmptyProductDetails() {
        when(productRepository.findAll()).thenReturn(null);
        var orderDetails = productOrderService.getProductDetails();
        assertNotNull(orderDetails);
        assertNull(orderDetails.getProducts());
    }

    @Test
    void testGetOrderOnlyVegetablesDetails() {
        when(productRepository.findAll()).thenReturn(MockDataProvider.getProductVegetablesDetails());
         var orderDetails= productOrderService.getCardDetails(MockDataProvider.getCardRequest(1L,1L,1L,100));
        assertAll(
                () -> assertNotNull(orderDetails),
                () -> assertEquals(0.99, orderDetails.getTotalPrice()),
                () -> assertEquals(1.0, orderDetails.getCartList().stream().findFirst().get().getOriginalPrice())
        );
    }

    @Test
    void testGetOrderOnlyDrinksDetailsWithoutOffer() {
        when(productRepository.findAll()).thenReturn(MockDataProvider.getProductDrinksDetails());
        var orderDetails= productOrderService.getCardDetails(MockDataProvider.getCardRequest(2L,3L,5L,0));
        assertAll(
                () -> assertNotNull(orderDetails),
                () -> assertEquals(5.0, orderDetails.getTotalPrice()),
                () -> assertEquals(1.0, orderDetails.getCartList().stream().findFirst().get().getOriginalPrice())
        );

        var orderDetails_4= productOrderService.getCardDetails(MockDataProvider.getCardRequest(2L,3L,4L,0));
        assertAll(
                () -> assertNotNull(orderDetails_4),
                () -> assertEquals(4.0, orderDetails_4.getTotalPrice()),
                () -> assertEquals(1.0, orderDetails_4.getCartList().stream().findFirst().get().getOriginalPrice())
        );
    }
    @Test
    void testGetOrderOnlyDrinksDetailsWithDiscount() {
        when(productRepository.findAll()).thenReturn(MockDataProvider.getProductDrinksDetails());
        var orderDetails= productOrderService.getCardDetails(MockDataProvider.getCardRequest(2L,3L,8L,0));
        assertAll(
                () -> assertNotNull(orderDetails),
                () -> assertEquals(6.0, orderDetails.getTotalPrice()),
                () -> assertEquals(1.0, orderDetails.getCartList().stream().findFirst().get().getOriginalPrice())
        );

        var orderDetails_6= productOrderService.getCardDetails(MockDataProvider.getCardRequest(2L,3L,6L,0));
        assertAll(
                () -> assertNotNull(orderDetails_6),
                () -> assertEquals(4.0, orderDetails_6.getTotalPrice()),
                () -> assertEquals(1.0, orderDetails_6.getCartList().stream().findFirst().get().getOriginalPrice())
        );
    }


    @Test
    void testGetOrderOnlyBakeryDetailsCurrentDate() {

        when(productRepository.findAll()).thenReturn(MockDataProvider.getProductBakeryDetails(new Date()));
        var orderDetails= productOrderService.getCardDetails(MockDataProvider.getCardRequest(3L,2L,1L,0));
        System.out.println(orderDetails);
        assertAll(
                () -> assertNotNull(orderDetails),
                () -> assertEquals(1.0, orderDetails.getTotalPrice()),
                () -> assertEquals("1", orderDetails.getCartList().stream().findFirst().get().getProductQuantity())
        );
    }

    @Test
    void testGetOrderOnlyBakeryDetailsBuyOneGetOne() {
        Calendar cal  = Calendar.getInstance();
        //subtracting a day
        cal.add(Calendar.DATE, -3);
        cal.getTimeInMillis();
        when(productRepository.findAll()).thenReturn(MockDataProvider.getProductBakeryDetails(new Date(cal.getTimeInMillis())));
        var orderDetails= productOrderService.getCardDetails(MockDataProvider.getCardRequest(3L,2L,1L,0));
        assertAll(
                () -> assertNotNull(orderDetails),
                () -> assertEquals(1.0, orderDetails.getTotalPrice()),
                () -> assertEquals("2", orderDetails.getCartList().stream().findFirst().get().getProductQuantity())
        );
    }

    @Test
    void testGetOrderOnlyBakeryDetailsBuyOneGetTwo() {
        Calendar cal  = Calendar.getInstance();
        cal.add(Calendar.DATE, -6);
        cal.getTimeInMillis();
        when(productRepository.findAll()).thenReturn(MockDataProvider.getProductBakeryDetails(new Date(cal.getTimeInMillis())));
        var orderDetails= productOrderService.getCardDetails(MockDataProvider.getCardRequest(3L,2L,1L,0));
        assertAll(
                () -> assertNotNull(orderDetails),
                () -> assertEquals(1.0, orderDetails.getTotalPrice()),
                () -> assertEquals("3", orderDetails.getCartList().stream().findFirst().get().getProductQuantity())
        );
    }

}
