<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.List" scope="request"/>
<tags:master pageTitle="Product Catalog">
    <div class="welcome-message">
        <h1>Welcome to PhoneShop</h1>
        <p>Discover the latest smartphones with unbeatable prices and premium quality</p>
    </div>
    
    <div class="search-container">
        <form style="display: flex; width: 100%; max-width: 500px; gap: 1rem;">
            <input name="query" 
                   value="${param.query}" 
                   placeholder="🔍 Search for your perfect phone..."
                   style="flex: 1;">
            <button type="submit">Search</button>
        </form>
    </div>

    <div class="products-header">
        <h2 style="margin: 0; color: #333; font-weight: 600;">Featured Products</h2>
        <div class="sort-options">
            <span style="color: #666; font-weight: 500;">Sort by:</span>
            <div class="sort-link">
                Description
                <div class="sort-arrows">
                    <c:choose>
                        <c:when test="${not empty param.query}">
                            <a href="?sort=description&order=asc&query=${param.query}" class="sort-arrow">▲</a>
                            <a href="?sort=description&order=desc&query=${param.query}" class="sort-arrow">▼</a>
                        </c:when>
                        <c:otherwise>
                            <a href="?sort=description&order=asc" class="sort-arrow">▲</a>
                            <a href="?sort=description&order=desc" class="sort-arrow">▼</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div class="sort-link">
                Price
                <div class="sort-arrows">
                    <c:choose>
                        <c:when test="${not empty param.query}">
                            <a href="?sort=price&order=asc&query=${param.query}" class="sort-arrow">▲</a>
                            <a href="?sort=price&order=desc&query=${param.query}" class="sort-arrow">▼</a>
                        </c:when>
                        <c:otherwise>
                            <a href="?sort=price&order=asc" class="sort-arrow">▲</a>
                            <a href="?sort=price&order=desc" class="sort-arrow">▼</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <div class="products-container">
        <c:forEach var="product" items="${products}">
            <div class="product-card" data-product-id="${product.id}">
                <div class="product-image">
                    <img src="${product.imageUrl}" alt="${product.description}" class="product-tile">
                    <div class="product-overlay">
                        <button class="quick-view-btn" onclick="showQuickView('${product.id}')">
                            👁️ Quick View
                        </button>
                    </div>
                </div>
                
                <div class="product-details">
                    <a href="/phoneshop/products/${product.id}" class="product-title">
                        ${product.description}
                    </a>
                    
                    <a href="/phoneshop/products/priceHistory/${product.id}" class="product-price">
                        <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
                    </a>
                    
                    <form method="post" action="${pageContext.request.contextPath}/products/${product.id}" class="add-to-cart-form">
                        <div class="quantity-controls">
                            <label for="quantity-${product.id}" style="font-weight: 500; color: #666;">Qty:</label>
                            <input type="number" 
                                   name="quantity" 
                                   id="quantity-${product.id}"
                                   value="1" 
                                   min="1" 
                                   max="99"
                                   class="quantity-input">
                        </div>
                        <button type="submit" 
                                class="add-to-cart-btn" 
                                onclick="addToCartAnimation(this); return true;">
                            🛒 Add to Cart
                        </button>
                    </form>
                </div>
            </div>
        </c:forEach>
    </div>

    <c:if test="${not empty sessionScope.viewHistory}">
        <div class="recently-viewed">
            <h2>Recently Viewed</h2>
            <div class="recently-viewed-grid">
                <c:forEach var="product" items="${sessionScope.viewHistory}">
                    <div class="recently-viewed-item">
                        <div class="product-image">
                            <img src="${product.imageUrl}" alt="${product.description}" class="product-tile"/>
                        </div>
                        <a href="/phoneshop/products/${product.id}" class="product-title">
                            ${product.description}
                        </a>
                        <a href="/phoneshop/products/priceHistory/${product.id}" class="product-price">
                            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </div>
    </c:if>

    <style>
        .product-overlay {
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(102, 126, 234, 0.9);
            display: flex;
            align-items: center;
            justify-content: center;
            opacity: 0;
            transition: opacity 0.3s ease;
            border-radius: 15px;
        }
        
        .product-image:hover .product-overlay {
            opacity: 1;
        }
        
        .quick-view-btn {
            background: white;
            color: #667eea;
            border: none;
            padding: 0.75rem 1.5rem;
            border-radius: 25px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            transform: translateY(10px);
        }
        
        .product-image:hover .quick-view-btn {
            transform: translateY(0);
        }
        
        .quick-view-btn:hover {
            background: #667eea;
            color: white;
        }
    </style>

    <script>
        function showQuickView(productId) {
            // Create modal for quick view
            const modal = document.createElement('div');
            modal.style.cssText = `
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0, 0, 0, 0.8);
                display: flex;
                align-items: center;
                justify-content: center;
                z-index: 10000;
                opacity: 0;
                transition: opacity 0.3s ease;
            `;

            modal.innerHTML = `
                <div style="
                    background: white;
                    padding: 2rem;
                    border-radius: 20px;
                    max-width: 500px;
                    width: 90%;
                    max-height: 80vh;
                    overflow-y: auto;
                    transform: scale(0.8);
                    transition: transform 0.3s ease;
                ">
                    <h3 style="margin-bottom: 1rem; color: #333;">Quick Preview</h3>
                    <p style="color: #666; margin-bottom: 2rem;">Loading product details...</p>
                    <button onclick="this.closest('.quick-view-modal').remove()" style="
                        background: #dc3545;
                        color: white;
                        border: none;
                        padding: 0.75rem 1.5rem;
                        border-radius: 10px;
                        cursor: pointer;
                        width: 100%;
                    ">Close</button>
                </div>
            `;

            modal.className = 'quick-view-modal';
            document.body.appendChild(modal);

            // Animate in
            setTimeout(() => {
                modal.style.opacity = '1';
                modal.querySelector('div').style.transform = 'scale(1)';
            }, 10);

            // Close on background click
            modal.addEventListener('click', (e) => {
                if (e.target === modal) {
                    modal.remove();
                }
            });
        }

        // Enhanced product card interactions
        document.addEventListener('DOMContentLoaded', function() {
            document.querySelectorAll('.product-card').forEach(card => {
                card.addEventListener('mouseenter', function() {
                    this.style.transform = 'translateY(-10px) rotateY(2deg)';
                    this.style.boxShadow = '0 20px 40px rgba(102, 126, 234, 0.3)';
                });

                card.addEventListener('mouseleave', function() {
                    this.style.transform = 'translateY(0) rotateY(0)';
                    this.style.boxShadow = '0 5px 20px rgba(0, 0, 0, 0.1)';
                });
            });
        });
    </script>
</tags:master>