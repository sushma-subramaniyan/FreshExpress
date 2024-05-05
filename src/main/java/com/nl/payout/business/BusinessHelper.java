package com.nl.payout.business;

import com.nl.payout.constants.CategoryConstant;
import com.nl.payout.dto.Discount;
import com.nl.payout.dto.Product;
import com.nl.payout.model.request.CartProduct;
import com.nl.payout.model.request.CartRequest;
import com.nl.payout.model.response.Cart;
import com.nl.payout.model.response.CartResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BusinessHelper {

    LocalDate today = LocalDate.now();

    /**
     * @param cartRequest as request
     * @param productList list of product
     * @return CartResponse
     */
    public CartResponse buildProductDiscountDetails(final CartRequest cartRequest, final List<Product> productList) {
        CartResponse cartResponse = new CartResponse();
        buildDiscount(productList, cartRequest, cartResponse);
        return cartResponse;
    }

    /**
     * @param productList list of product
     * @param cartRequest as request
     * @param cartResponse response
     */
    private void buildDiscount(final List<Product> productList, final CartRequest cartRequest, CartResponse cartResponse) {
        var carts = new ArrayList<Cart>();
        for (CartProduct cartProduct : cartRequest.getProductList()) {
            Cart cart = new Cart();
            var totalPrice = 0.0;
            var totalQuantity = cartProduct.getProductQuantity();
            long categoryIds = cartProduct.getCategoryId();
            switch (CategoryConstant.getCategoryByValue(categoryIds)) {
                case VEGETABLE -> totalPrice = buildVegetableDiscountDetails(productList, cartProduct, cart, categoryIds);
                case BAKERY -> {
                    totalPrice = (totalQuantity * cartProduct.getProductPrice());
                    totalQuantity = (int) buildBakeryDiscountPrice(productList, cartProduct, categoryIds, cart);
                }
                case BEVERAGE -> totalPrice = buildBeverageDiscountPrice(productList, cartProduct, categoryIds ,cart);
            }
            if(totalQuantity!=0) {
                cart.setProductOfferPrice(totalPrice);
                cart.setProductName(cartProduct.getProductName());
                cart.setProductQuantity(String.valueOf(totalQuantity));
                cart.setOriginalPrice(cartProduct.getProductPrice());
                cart.setCategoryId(categoryIds);
                carts.add(cart);
            }
        }
        cartResponse.setTotalPrice(Math.round(carts.stream().map(Cart::getProductOfferPrice).reduce(0.0, Double::sum) * 100.0) / 100.0);
        cartResponse.setCartList(carts);
    }


    /**
     * @param productList list of product
     * @param cartProduct request from user
     * @param cart build card
     * @param categoryId unique id
     * @return vegetable discount details
     * Build discount details based vegetable discount
     */
    private double buildVegetableDiscountDetails(final List<Product> productList, final CartProduct cartProduct, final Cart cart, final long categoryId) {

        // Calculate total weight
        long totalProductWeight = cartProduct.getProductQuantity() * cartProduct.getProductWeight();

        // filter only vegetable category
        List<Product> vegetablesList = productList.stream().filter(product -> product.getCategory().getCategoryId() == categoryId
                        && product.getProductId() == cartProduct.getProductId())
                .toList();


        if (!vegetablesList.isEmpty()) {

            var firstVegetable = vegetablesList.get(0); //Since only one list
            var discounts = firstVegetable.getDiscount(); //get the discount
            var productPrice = firstVegetable.getProductPrice();
            if (!discounts.isEmpty()) {

                // filter discount based on weight
                var applicableDiscount = discounts.stream()
                        .filter(discount -> totalProductWeight <= discount.getMaxQuantity() && totalProductWeight >= discount.getMinQuantity())
                        .findFirst();

                if (applicableDiscount.isPresent()) {
                    var discountValue = applicableDiscount.get().getDiscountValue();// offer value
                    var productQuantity = firstVegetable.getProductQuantity(); // original Quantity for price
                    // Set Price of productQuantity to display original Quantity on UI
                    cart.setOriginalQuantity(String.valueOf(productQuantity));
                    // ex : 500g(Total weight)/100g (weight for price);
                    var totalPricePerUnit = totalProductWeight / productQuantity; //get weight
                    //Calculate the discount
                    var discountedPrice = calculatePercentage(discountValue, productPrice);
                    return (totalPricePerUnit * productPrice) - discountedPrice;
                }
            }
            // Product does not have discount
            return totalProductWeight * productPrice;

        }
        return 0;

    }

    /**
     * @param discountValue offer value
     * @param productPrice original product price
     * @return discount value
     */
    private static double calculatePercentage(double discountValue, double productPrice) {
        //Ex 5*1/100;
        return (discountValue * productPrice) / 100;
    }

    /**
     * @param productList list of product
     * @param cartProduct request from user
     * @param categoryId unique id
     * @return Beverage discount details
     * Build discount details based Beverage discount
     */
    private double buildBeverageDiscountPrice(final List<Product> productList, final CartProduct cartProduct, final long categoryId, Cart cart) {

        var totalQuantity = cartProduct.getProductQuantity();
        var totalPrice = 0.0;
        var listOfBeers = productList.stream()
                .filter(product -> product.getCategory().getCategoryId() == categoryId
                        && product.getProductId() == cartProduct.getProductId())
                .toList();

        if (!listOfBeers.isEmpty()) {
            var firstBeer = listOfBeers.get(0);
            var discounts = firstBeer.getDiscount();
            var productPrice = firstBeer.getProductPrice();
            var pricePerQuantity = firstBeer.getProductQuantity();
            cart.setOriginalQuantity(String.valueOf(pricePerQuantity));
            if (!discounts.isEmpty()) {
                var discount = discounts.get(0);
                var discountQuantity = discount.getMaxQuantity();
                if (totalQuantity >= discountQuantity) {
                    var discountValue = discount.getDiscountValue();

                    var discountProduct = totalQuantity / discountQuantity;//ex 8/6 =quotient 1 as need to be discount applied
                    var remainingBottles = totalQuantity % discountQuantity;//ex 8%6 = remainder 2  need to be  without discount
                    // 1 * 4 + 2*1
                    return discountProduct * discountValue + remainingBottles * productPrice;
                }
            }
            return totalQuantity * productPrice;
        }
        return totalPrice;

    }

    /**
     * @param productList list of product
     * @param cartProduct request from user
     * @param categoryId unique id
     * @return Bakery discount details
     * Build discount details based Bakery discount
     */
    private double buildBakeryDiscountPrice(final List<Product> productList, final CartProduct cartProduct, final long categoryId, Cart cart) {
        // Get the quantity of the card product
        var totalQuantity = cartProduct.getProductQuantity();
        // Filter the productList to get bread products matching the card product's ID
        var listOfBread = productList.stream()
                .filter(product -> product.getCategory().getCategoryId() == categoryId && product.getProductId() == cartProduct.getProductId())
                .findFirst();
        if(listOfBread.isEmpty()) {
            return 0;
        }
        var pricePerQuantity = listOfBread.get().getProductQuantity();
        cart.setOriginalQuantity(String.valueOf(pricePerQuantity));
        // Extract discounts from bread products
        var discounts = listOfBread.get().getDiscount();
        // Ensure there are discounts available
        if (!discounts.isEmpty()) {
            // Get the update date of the bread product
            var productExpireDate = listOfBread.get().getUpdateDate();
            // Calculate days between update date and today
            var date = LocalDate.ofInstant(productExpireDate.toInstant(), ZoneId.systemDefault());
            long daysBetween = DAYS.between(date, today);
            // Iterate through discounts and apply the applicable one
            for (Discount discount : discounts) {
                if (daysBetween > discount.getMinQuantity() && daysBetween <= discount.getMaxQuantity()) {
                    totalQuantity *= discount.getDiscountValue();
                    break;
                }
            }
        }

        return totalQuantity;
    }


}
