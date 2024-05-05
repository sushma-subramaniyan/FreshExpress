package com.nl.payout.mockdata;

import com.google.gson.JsonObject;
import com.nl.payout.dto.Category;
import com.nl.payout.dto.Discount;
import com.nl.payout.dto.Product;
import com.nl.payout.model.request.CartProduct;
import com.nl.payout.model.request.CartRequest;
import com.nl.payout.model.response.Cart;
import com.nl.payout.model.response.CartResponse;
import com.nl.payout.model.response.ProductDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class MockDataProvider {


    public static ProductDetails getOrderDetails() {
        var prduct = Product.builder()
                .productId(1)
                .productDescription("Tomatto")
                .productName("Tomatto")
                .productPrice(1.1)
                .productQuantity(2L).build();
        return ProductDetails.builder().products(Collections.singletonList(prduct)).build();

    }

    public static List<Product> getProductDetails() {
        return Collections.singletonList(Product.builder()
                .productId(1)
                .productDescription("Tomatto")
                .productName("Tomatto")
                .productPrice(1.1)
                .productQuantity(2L).build());


    }

    public static List<Product> getProductVegetablesDetails() {
        Category category = Category.builder().categoryId(1).build();
        Discount discount =Discount.builder().discountID(1).discountValue(1).discountType("Ea").minQuantity(0).maxQuantity(100).build();
        return Collections.singletonList(Product.
                builder()
                .productId(1)
                .productPrice(1.0)
                .productQuantity(100L)
                .productName("Tomotos")
                .discount(Collections.singletonList(discount))
                .category(category)
                .build());
    }

    public static List<Product> getProductDrinksDetails() {
        Category category = Category.builder().categoryId(3).build();
        Discount discount =Discount.builder().discountID(2).discountValue(4).discountType("PerQuantity").maxQuantity(6).build();
        return Collections.singletonList(Product.
                builder()
                .productId(2)
                .productPrice(1.0)
                .productQuantity(1L)
                .productName("Hertog Beer")
                .discount(Collections.singletonList(discount))
                .category(category)
                .build());
    }

    public static List<Product> getProductBakeryDetails(Date date) {
        Category category = Category.builder().categoryId(2).build();
        Discount discount =Discount.builder().discountID(3).discountValue(1).discountType("PerQuantity").minQuantity(0).maxQuantity(1).build();
        Discount discount1 =Discount.builder().discountID(4).discountValue(2).discountType("PerQuantity").minQuantity(1).maxQuantity(3).build();
        Discount discount2 =Discount.builder().discountID(4).discountValue(3).discountType("PerQuantity").minQuantity(3).maxQuantity(6).build();
        List<Discount> discounts = new ArrayList<>();
        discounts.add(discount);
        discounts.add(discount1);
        discounts.add(discount2);
        return Collections.singletonList(Product.
                builder()
                .productId(3)
                .productPrice(1.0)
                .productQuantity(1L)
                .productName("Hertog Beer")
                .discount(discounts)
                        .updateDate(date)
                .category(category)
                .build());
    }

    public static CartRequest getCardRequest(long productId, long categoryId, long productQuantity, long productWeight ) {
        CartProduct cartProduct = CartProduct.builder().productId(productId).categoryId(categoryId).productQuantity(productQuantity).productPrice(1.0).productWeight(productWeight).build();
        return CartRequest.builder().productList(Collections.singletonList(cartProduct)).build();

    }


    public static CartResponse getCardDetails() {
        var prduct = Cart.builder()
                .productName("Tomatto")
                .originalPrice(1.1)
                .productQuantity("2L").build();
        return CartResponse.builder().build().builder().cartList(Collections.singletonList(prduct)).build();

    }
    public static JsonObject getIngredientsUpdateRequest() {
        return MockJsonBuilder.aRequest()
                .withProperty("ingredientName", "chicken")
                .withProperty("imageURL", "chicken.png")
                .build();
    }


}