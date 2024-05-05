package com.nl.payout.service;

import com.nl.payout.business.BusinessHelper;
import com.nl.payout.dto.Product;
import com.nl.payout.dto.repository.ProductRepository;
import com.nl.payout.model.request.CartRequest;
import com.nl.payout.model.response.CartResponse;
import com.nl.payout.model.response.ProductDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductOrderService {

    private final ProductRepository productRepository;
    private final BusinessHelper businessLayer;


    /**
     * @return list of product details
     */
    public ProductDetails getProductDetails() {
       List<Product> productList=  productRepository.findAll();
       log.debug("List of product" , productList);
       return ProductDetails.builder().products(productList).build();
    }



    /**
     * @param cartRequest as request
     * @return product with discount details
     */

    public CartResponse getCardDetails(CartRequest cartRequest) {
        List<Product> productList = productRepository.findAll();
        return businessLayer.buildProductDiscountDetails(cartRequest, productList);
    }



}
