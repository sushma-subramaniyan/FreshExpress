$(document).ready(function() {
    var cart = [];
    fetchProductDetails();
    $('.order-detail').hide();
    $('.cart-section').hide();
    $('.checkout-button').hide();


    // Function to handle AJAX request to fetch product details
    function fetchProductDetails() {
        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/api/frontend-service/product/details",
            dataType: 'json',
            cache: false,
            timeout: 600000,
            //complete data
            success: function(data) {
                renderProductDetails(data.products);
            },
            error: function(e) {
                console.error("Error fetching product details:", e);
            }
        });
    }

    // Function to render product details on the page
    function renderProductDetails(products) {
        products.forEach(function(value, index) {
            var html_to_append = $('<div class="col-xl-3 col-lg-4 col-sm-6"><div class="product-wrapper aos-init aos-animate" data-aos="fade-up"><div class="product-img"><img src="js/images/' + value.productImageUrl + '"></div><div class="product-info"><div class="product-description"><a href="#" class="product-details">' + value.productName + '</a><div class="price"><span class="price-cut"> Price : ' + value.currency.symbol + value.productPrice + '</span></div></div></div></div></div>');
            var category = value.category;
            var quantity = 'quantity' + value.productId;
            var weightId = 'weight' + (index + 1);

            if (category.categoryId === 1) {
                var weightSelect = $('<div class="weight-prd"><select name="weight" id=' + weightId + ' style="padding: 10px 20px;border-radius:5px;"><option value="100" selected>100g</option><option value="250">250g</option><option value="500">500g</option></select></div>');
                var quantityInput = $('<div class="quantity-prd"><div class="value-button" id="decrease" onclick="decreaseValue(' + (index + 1) + ')" value="Decrease Value">-</div><input type="number" id="' + quantity + '" value="1"  class="value-textbox" maxlength="2" size="2"/><div class="value-button" onclick="increaseValue(' + (index + 1) + ')" value="Increase Value">+</div><div class="addCard"><button class="add-to-cart" data-product-id="' + value.productId + '" data-product-name="' + value.productName + '" data-product-price="' + value.productPrice + '" data-category-id="' + category.categoryId + '"data-weight-id="' + weightId + '">Add to Cart</button><div></div>');
                html_to_append.find('.product-info').append(weightSelect).append(quantityInput);
                $("#vegi").append(html_to_append);
            } else {
                var quantityInput = $('<div class="quantity-prd"><div class="value-button" id="decrease" onclick="decreaseValue(' + (index + 1) + ')" value="Decrease Value">-</div><input type="number" id="' + quantity + '" value="1"  class="value-textbox" maxlength="2" size="2"/><div class="value-button" id="increase" onclick="increaseValue(' + (index + 1) + ')" value="Increase Value">+</div><div  class="addCard"><button class="add-to-cart" data-product-id="' + value.productId + '" data-product-name="' + value.productName + '" data-product-price="' + value.productPrice + '" data-category-id="' + category.categoryId + '">Add to Cart</button></div></div>');
                html_to_append.find('.product-info').append(quantityInput);
                category.categoryId === 2 ? $("#drink").append(html_to_append) : $("#bread").append(html_to_append);
            }
        });
    }




    // Event listener for "Add to Cart" buttons
    $(document).on('click', '.add-to-cart', function(event) {
        $('.cart-section').show();
        $('.checkout-button').show();

        event.preventDefault();
        var productId = $(this).data('product-id');
        var productName = $(this).data('product-name');
        var productPrice = $(this).data('product-price');
        var categoryId = $(this).data('category-id');
        var weightId = $(this).data('weight-id');

        var productQuantity = parseInt($('#quantity' + productId).val());

        var productWeight = 0;

        // Check if the vegetables is in category ID 1 and get the selected weight from the dropdown
        if (categoryId === 1) {
            productWeight = parseInt($('#' + weightId).val());
        }

        var existingItemIndex = cart.findIndex(item => item.productId === productId && item.productWeight === productWeight);



        if (existingItemIndex !== -1) {
            // If the product is already in the cart, update its quantity
            cart[existingItemIndex].productQuantity += productQuantity;
        } else {
            // If the product is not in the cart, add it as a new item
            var cartItem = {
                productId: productId,
                productName: productName,
                productPrice: productPrice,
                productQuantity: productQuantity,
                productWeight: productWeight,
                categoryId: categoryId
            };
            cart.push(cartItem);
        }

        // Update the UI to reflect the added item
        updateCartUI();
    });



    // Function to update the UI with cart items
    function updateCartUI() {
        var cardCount = parseInt($('#cart-count').text(), 10) || 0;
        $("#cart-count").html(++cardCount);
        $('#cart-items').empty();
        cart.forEach(function(item) {
            var itemHtml = '<li>' + item.productName + ' - £' + item.productPrice.toFixed(2) + ' x ' + item.productQuantity;
            if (item.productWeight !== 0) {
                itemHtml += '  Quantity ' + item.productWeight + 'g';
            }
            itemHtml += '</li>';
            $('#cart-items').append(itemHtml);
        });
    }

    $('#checkout-btn').on('click', function(event) {

        if (cart.length === 0) {
            alert("Please Add the item on Cart");
            return;
        }
        event.preventDefault();
       $('#cart-count').empty();

        $('.cart-section').hide();
        $('.fresh-section').hide();
        $('#checkout-btn').hide();
        $('.order-detail').show();


        var jsonData = {
            productList: cart
        };
        // Prepare cart data as JSON
        var cartData = JSON.stringify(jsonData);

        // Send cart data to the API using AJAX POST request
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/api/frontend-service/order/checkout",
            data: cartData,
            success: function(response) {
                // Handle successful response from the API
                console.log("Checkout successful:", response);
                // Optionally, you can reset the cart after successful checkout
                cart = [];
                var i = 1 * 1;
                response.cartList.forEach(function(item) {
                    var itemHtml = '<li>' + i++ + '. &nbsp;&nbsp;&nbsp;&nbsp; ' + item.productQuantity + ' x ' + item.productName + ' &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -------------------- £' + item.productOfferPrice.toFixed(2) + '</li>';
                    $('#cart-items-discount').append(itemHtml);
                    var productHtml = item.productName + '&nbsp;:£ ' + item.originalPrice + '&nbsp;per&nbsp;';


                    if (item.categoryId === 1) {
                        productHtml += item.originalQuantity + 'gram , ';
                    } else if (item.categoryId === 2) {
                        productHtml += item.originalQuantity + 'pack ,';
                    } else if (item.categoryId === 3) {
                        productHtml += item.originalQuantity + 'bottle ,';
                    }

                    $('#product-list').append(productHtml)

                });

                // Display the total price
                $('#total-price').text('Total Price: £' + response.totalPrice.toFixed(2));
            },
            error: function(xhr, status, error) {
                // Handle error response from the API
                console.error("Checkout failed:", error);
            }
        });
    });


$('.go-back').click(function() {
    location.reload();
});

});


// Function to increase product quantity
function increaseValue(id) {
    var value = parseInt($('#quantity' + id).val(), 10) || 0;
    $('#quantity' + id).val(++value);
}

// Function to decrease product quantity
function decreaseValue(id) {
    var value = parseInt($('#quantity' + id).val(), 10) || 0;
    if (value > 0) {
        $('#quantity' + id).val(--value);
    }
}