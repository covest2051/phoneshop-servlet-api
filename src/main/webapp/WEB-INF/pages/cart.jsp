<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Shopping Cart">
    <div class="cart-header">
        <h1>🛒 Your Shopping Cart</h1>
        <p>Review your items and proceed to checkout</p>
    </div>

    <c:if test="${not empty param.message}">
        <div class="success">
            ✅ ${param.message}
        </div>
    </c:if>
    
    <c:if test="${not empty errors}">
        <div class="error">
            ⚠️ An error occurred while updating the cart
        </div>
    </c:if>

    <c:choose>
        <c:when test="${empty cart.items}">
            <div class="empty-cart">
                <div class="empty-cart-icon">🛒</div>
                <h2>Your cart is empty</h2>
                <p>Start shopping to add items to your cart</p>
                <a href="${pageContext.servletContext.contextPath}/products" class="continue-shopping-btn">
                    Continue Shopping
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <form method="post" action="${pageContext.servletContext.contextPath}/cart" class="cart-form">
                <div class="cart-items">
                    <c:forEach var="item" items="${cart.items}" varStatus="status">
                        <div class="cart-item" data-product-id="${item.product.id}">
                            <div class="item-image">
                                <img src="${item.product.imageUrl}" alt="${item.product.description}" class="product-image">
                            </div>
                            
                            <div class="item-details">
                                <h3 class="item-name">
                                    <a href="/phoneshop/products/${item.product.id}">
                                        ${item.product.description}
                                    </a>
                                </h3>
                                <div class="item-price">
                                    <a href="/phoneshop/products/priceHistory/${item.product.id}">
                                        <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
                                    </a>
                                </div>
                            </div>
                            
                            <div class="item-quantity">
                                <label for="quantity-${status.index}">Quantity:</label>
                                <div class="quantity-controls">
                                    <button type="button" class="quantity-btn" onclick="decreaseQuantity(${status.index})">-</button>
                                    <c:set var="error" value="${errors[item.product.id]}"/>
                                    <input name="quantity"
                                           id="quantity-${status.index}"
                                           value="${not empty error ? paramValues['quantity'][status.index] : item.quantity}"
                                           class="quantity-input ${not empty error ? 'error' : ''}"
                                           min="1"
                                           max="99"
                                           type="number">
                                    <button type="button" class="quantity-btn" onclick="increaseQuantity(${status.index})">+</button>
                                </div>
                                <c:if test="${not empty error}">
                                    <div class="error-message">
                                        ${error}
                                    </div>
                                </c:if>
                                <input type="hidden" value="${item.product.id}" name="productId">
                            </div>
                            
                            <div class="item-subtotal">
                                <fmt:formatNumber value="${item.product.price * item.quantity}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
                            </div>
                            
                            <div class="item-remove">
                                <button type="submit"
                                        formaction="${pageContext.servletContext.contextPath}/cart?productId=${item.product.id}"
                                        formmethod="post" 
                                        name="method" 
                                        value="DELETE"
                                        class="remove-btn"
                                        onclick="return confirmRemove('${item.product.description}')">
                                    🗑️
                                </button>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <div class="cart-summary">
                    <div class="summary-content">
                        <div class="summary-row">
                            <span>Total Items:</span>
                            <span class="total-quantity">${cart.totalQuantity}</span>
                        </div>
                        <div class="summary-row total">
                            <span>Total Cost:</span>
                            <span class="total-cost">
                                <fmt:formatNumber value="${cart.totalCost}" type="currency" currencySymbol="$"/>
                            </span>
                        </div>
                        <div class="cart-actions">
                            <button type="submit" class="update-cart-btn">🔄 Update Cart</button>
                            <a href="${pageContext.servletContext.contextPath}/products" class="continue-shopping-link">
                                ← Continue Shopping
                            </a>
                        </div>
                    </div>
                </div>
            </form>

            <div class="checkout-section">
                <form method="get" action="${pageContext.request.contextPath}/checkout">
                    <button type="submit" class="checkout-btn">
                        🚀 Proceed to Checkout
                    </button>
                </form>
            </div>
        </c:otherwise>
    </c:choose>

    <style>
        .cart-header {
            text-align: center;
            margin-bottom: 2rem;
            padding: 2rem;
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            border-radius: 15px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }

        .cart-header h1 {
            font-size: 2.5rem;
            font-weight: 700;
            color: #333;
            margin-bottom: 0.5rem;
        }

        .cart-header p {
            color: #666;
            font-size: 1.1rem;
        }

        .empty-cart {
            text-align: center;
            padding: 4rem 2rem;
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
            border-radius: 20px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
        }

        .empty-cart-icon {
            font-size: 4rem;
            margin-bottom: 1rem;
            opacity: 0.5;
        }

        .empty-cart h2 {
            font-size: 2rem;
            color: #666;
            margin-bottom: 1rem;
        }

        .empty-cart p {
            color: #999;
            margin-bottom: 2rem;
        }

        .continue-shopping-btn {
            display: inline-block;
            padding: 1rem 2rem;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            text-decoration: none;
            border-radius: 50px;
            font-weight: 600;
            transition: all 0.3s ease;
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.3);
        }

        .continue-shopping-btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 25px rgba(102, 126, 234, 0.5);
        }

        .cart-items {
            margin-bottom: 2rem;
        }

        .cart-item {
            display: grid;
            grid-template-columns: 120px 1fr 200px 120px 60px;
            gap: 1.5rem;
            align-items: center;
            padding: 2rem;
            background: white;
            border-radius: 15px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            margin-bottom: 1.5rem;
            border: 2px solid transparent;
            transition: all 0.3s ease;
        }

        .cart-item:hover {
            border-color: #667eea;
            transform: translateY(-2px);
        }

        .item-image img {
            width: 100px;
            height: 100px;
            object-fit: cover;
            border-radius: 10px;
        }

        .item-details h3 {
            margin: 0 0 0.5rem 0;
            font-size: 1.2rem;
            font-weight: 600;
        }

        .item-details a {
            text-decoration: none;
            color: #333;
            transition: color 0.3s ease;
        }

        .item-details a:hover {
            color: #667eea;
        }

        .item-price {
            font-size: 1.1rem;
            font-weight: 600;
            color: #667eea;
        }

        .item-price a {
            text-decoration: none;
            color: inherit;
        }

        .item-quantity label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: 500;
            color: #666;
        }

        .quantity-controls {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            margin-bottom: 0.5rem;
        }

        .quantity-btn {
            width: 32px;
            height: 32px;
            border: 2px solid #667eea;
            background: white;
            color: #667eea;
            border-radius: 50%;
            cursor: pointer;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .quantity-btn:hover {
            background: #667eea;
            color: white;
            transform: scale(1.1);
        }

        .quantity-input {
            width: 60px;
            padding: 0.5rem;
            border: 2px solid #e1e5e9;
            border-radius: 8px;
            text-align: center;
            font-size: 1rem;
        }

        .quantity-input.error {
            border-color: #dc3545;
        }

        .quantity-input:focus {
            outline: none;
            border-color: #667eea;
        }

        .error-message {
            color: #dc3545;
            font-size: 0.9rem;
        }

        .item-subtotal {
            font-size: 1.3rem;
            font-weight: 700;
            color: #333;
            text-align: center;
        }

        .remove-btn {
            width: 40px;
            height: 40px;
            border: none;
            background: #dc3545;
            color: white;
            border-radius: 50%;
            cursor: pointer;
            font-size: 1.2rem;
            transition: all 0.3s ease;
        }

        .remove-btn:hover {
            background: #c82333;
            transform: scale(1.1);
        }

        .cart-summary {
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
            border-radius: 15px;
            padding: 2rem;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            margin-bottom: 2rem;
        }

        .summary-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 1rem 0;
            border-bottom: 1px solid #dee2e6;
        }

        .summary-row.total {
            border-bottom: none;
            font-size: 1.3rem;
            font-weight: 700;
            color: #333;
        }

        .cart-actions {
            display: flex;
            gap: 1rem;
            align-items: center;
            justify-content: space-between;
            margin-top: 1rem;
        }

        .update-cart-btn {
            padding: 1rem 2rem;
            background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
            color: white;
            border: none;
            border-radius: 50px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 5px 15px rgba(40, 167, 69, 0.3);
        }

        .update-cart-btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 25px rgba(40, 167, 69, 0.5);
        }

        .continue-shopping-link {
            color: #667eea;
            text-decoration: none;
            font-weight: 500;
            transition: all 0.3s ease;
        }

        .continue-shopping-link:hover {
            color: #764ba2;
            text-decoration: underline;
        }

        .checkout-section {
            text-align: center;
            padding: 2rem;
        }

        .checkout-btn {
            padding: 1.5rem 3rem;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 50px;
            font-size: 1.2rem;
            font-weight: 700;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
        }

        .checkout-btn:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 40px rgba(102, 126, 234, 0.6);
        }

        @media (max-width: 768px) {
            .cart-item {
                grid-template-columns: 1fr;
                text-align: center;
                gap: 1rem;
            }
            
            .cart-actions {
                flex-direction: column;
                gap: 1rem;
            }
        }
    </style>

    <script>
        function increaseQuantity(index) {
            const input = document.getElementById('quantity-' + index);
            const currentValue = parseInt(input.value) || 1;
            if (currentValue < 99) {
                input.value = currentValue + 1;
            }
        }

        function decreaseQuantity(index) {
            const input = document.getElementById('quantity-' + index);
            const currentValue = parseInt(input.value) || 1;
            if (currentValue > 1) {
                input.value = currentValue - 1;
            }
        }

        function confirmRemove(productName) {
            return confirm('Are you sure you want to remove "' + productName + '" from your cart?');
        }

        // Add animation to cart items
        document.addEventListener('DOMContentLoaded', function() {
            document.querySelectorAll('.cart-item').forEach((item, index) => {
                item.style.opacity = '0';
                item.style.transform = 'translateY(30px)';
                
                setTimeout(() => {
                    item.style.transition = 'all 0.6s ease';
                    item.style.opacity = '1';
                    item.style.transform = 'translateY(0)';
                }, index * 100);
            });
        });
    </script>
</tags:master>