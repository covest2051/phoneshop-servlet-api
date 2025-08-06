<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Корзина покупок">
    <div style="text-align: center; margin-bottom: 2rem;">
        <h1 style="font-size: 36px; font-weight: 700; background: linear-gradient(135deg, #667eea, #764ba2); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;">
            🛒 Ваша корзина
        </h1>
        <p style="font-size: 18px; color: #666; margin-top: 0.5rem;">
            Общий объем: ${cart}
        </p>
    </div>
    
    <c:if test="${not empty param.message}">
        <div class="success">
            ✅ ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty errors}">
        <div class="error">
            ❌ Произошла ошибка при обновлении корзины
        </div>
    </c:if>
    
    <c:choose>
        <c:when test="${empty cart.items}">
            <!-- Empty Cart -->
            <div style="text-align: center; padding: 4rem; background: rgba(255,255,255,0.95); border-radius: 20px; box-shadow: 0 8px 32px rgba(0,0,0,0.08);">
                <div style="font-size: 64px; margin-bottom: 1rem;">🛒</div>
                <h2 style="font-size: 24px; font-weight: 600; margin-bottom: 1rem; color: #666;">
                    Ваша корзина пуста
                </h2>
                <p style="color: #888; margin-bottom: 2rem;">
                    Добавьте товары, чтобы начать покупки
                </p>
                <a href="/phoneshop/products" class="add-to-cart-btn" style="display: inline-block; text-decoration: none; padding: 16px 32px; font-size: 18px;">
                    🛍️ Перейти к покупкам
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <!-- Cart Items -->
            <form method="post" action="${pageContext.servletContext.contextPath}/cart">
                <div class="product-table" style="margin-bottom: 3rem;">
                    <c:forEach var="item" items="${cart.items}" varStatus="status">
                        <div class="product-card" style="padding: 2rem;">
                            
                            <!-- Product Image -->
                            <div style="text-align: center; margin-bottom: 1.5rem;">
                                <img class="product-image" src="${item.product.imageUrl}" style="height: 150px;">
                            </div>
                            
                            <!-- Product Title -->
                            <div class="product-title" style="margin-bottom: 1rem;">
                                <a href="/phoneshop/products/${item.product.id}">
                                    ${item.product.description}
                                </a>
                            </div>
                            
                            <!-- Product Price -->
                            <div class="product-price" style="margin-bottom: 1.5rem;">
                                <a href="/phoneshop/products/priceHistory/${item.product.id}">
                                    <fmt:formatNumber value="${item.product.price}" type="currency"
                                                      currencySymbol="${item.product.currency.symbol}"/>
                                </a>
                            </div>
                            
                            <!-- Quantity Controls -->
                            <div style="display: flex; gap: 1rem; align-items: center; margin-bottom: 1.5rem;">
                                <label style="font-weight: 600;">Количество:</label>
                                <c:set var="error" value="${errors[item.product.id]}"/>
                                <input name="quantity"
                                       value="${not empty error ? paramValues['quantity'][status.index] : item.quantity}"
                                       class="quantity-input"
                                       min="1" max="99" type="number"
                                       style="flex: 1;">
                                <input type="hidden" value="${item.product.id}" name="productId">
                                <c:if test="${not empty error}">
                                    <div class="error" style="font-size: 14px; margin: 0;">
                                        ${error}
                                    </div>
                                </c:if>
                            </div>
                            
                            <!-- Remove Button -->
                            <button type="submit"
                                    formaction="${pageContext.servletContext.contextPath}/cart?productId=${item.product.id}"
                                    formmethod="post" name="method" value="DELETE"
                                    style="background: linear-gradient(135deg, #ff6b6b, #ff5757); color: white; border: none; padding: 12px 24px; border-radius: 50px; font-weight: 600; cursor: pointer; transition: all 0.3s ease; box-shadow: 0 4px 16px rgba(255, 107, 107, 0.3); width: 100%;">
                                🗑️ Удалить товар
                            </button>
                        </div>
                    </c:forEach>
                </div>
                
                <!-- Cart Summary -->
                <div style="background: rgba(255,255,255,0.95); border-radius: 20px; padding: 2rem; box-shadow: 0 8px 32px rgba(0,0,0,0.08); margin-bottom: 2rem;">
                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 2rem; align-items: center;">
                        <div>
                            <div style="font-size: 24px; font-weight: 700; margin-bottom: 0.5rem; background: linear-gradient(135deg, #ff6b6b, #feca57); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;">
                                Итого: <fmt:formatNumber value="${cart.totalCost}" type="currency" currencySymbol="$"/>
                            </div>
                            <div style="font-size: 16px; color: #666;">
                                Общее количество: <strong>${cart.totalQuantity}</strong> товаров
                            </div>
                        </div>
                        <div style="display: flex; gap: 1rem;">
                            <button type="submit" class="add-to-cart-btn" style="flex: 1;">
                                🔄 Обновить корзину
                            </button>
                        </div>
                    </div>
                </div>
            </form>
            
            <!-- Checkout Section -->
            <div style="display: flex; gap: 2rem; justify-content: center; align-items: center;">
                <form method="get" action="${pageContext.request.contextPath}/checkout">
                    <button type="submit" style="background: linear-gradient(135deg, #10ac84, #1dd1a1); color: white; border: none; padding: 20px 48px; border-radius: 50px; font-size: 20px; font-weight: 700; cursor: pointer; transition: all 0.3s ease; box-shadow: 0 8px 24px rgba(16, 172, 132, 0.4);">
                        💳 Оформить заказ
                    </button>
                </form>
                <a href="/phoneshop/products" style="display: inline-block; padding: 20px 48px; background: linear-gradient(135deg, #667eea, #764ba2); color: white; text-decoration: none; border-radius: 50px; font-size: 18px; font-weight: 600; transition: all 0.3s ease; box-shadow: 0 4px 16px rgba(102, 126, 234, 0.3);">
                    🛍️ Продолжить покупки
                </a>
            </div>
        </c:otherwise>
    </c:choose>
    
    <style>
        @media (max-width: 768px) {
            .product-table {
                grid-template-columns: 1fr !important;
            }
            
            div[style*="display: grid"] {
                grid-template-columns: 1fr !important;
                text-align: center;
            }
            
            div[style*="display: flex"] {
                flex-direction: column !important;
            }
        }
    </style>
    
</tags:master>