package com.nl.payout.controller;

import com.nl.payout.model.request.CartRequest;
import com.nl.payout.model.response.CartResponse;
import com.nl.payout.model.response.ProductDetails;
import com.nl.payout.service.ProductOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductOrderController {



    private final ProductOrderService productOrderService;

    /**
     *
     * @return list of product details
     *
     */
    @GetMapping("/api/frontend-service/product/details")

    public ResponseEntity<ProductDetails> getProductDetails() {
        ProductDetails productDetails = productOrderService.getProductDetails();
        log.debug("List of product details", productDetails.getProducts().size());
        return ResponseEntity.ok(productDetails);
    }
    /**
     *
     * @param cartRequest from User
     * @return product details with discount
     */
    @PostMapping("/api/frontend-service/order/checkout")
    public ResponseEntity<CartResponse> placeOrder(@Validated @RequestBody final CartRequest cartRequest) {
        if(!CollectionUtils.isEmpty(cartRequest.getProductList())) {
            CartResponse cartResponse = productOrderService.getCardDetails(cartRequest);
            return ResponseEntity.ok(cartResponse);
        }else{
            log.error("product details should not empty");
            return (ResponseEntity<CartResponse>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }

    }
}
