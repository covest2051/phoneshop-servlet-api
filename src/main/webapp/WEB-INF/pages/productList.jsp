<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.List" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
        🛒 Добро пожаловать в современный PhoneShop!
    </p>
    <form>
        <input name="query" value="${param.query}" placeholder="🔍 Поиск товаров...">
        <button>Найти</button>
    </form>
    
    <!-- Sorting Controls -->
    <div style="text-align: center; margin: 2rem 0;">
        <div style="display: inline-flex; gap: 2rem; padding: 1rem; background: rgba(255,255,255,0.9); border-radius: 50px; box-shadow: 0 4px 16px rgba(0,0,0,0.1);">
            <div style="display: flex; align-items: center; gap: 0.5rem;">
                <span style="font-weight: 600;">Описание:</span>
                <c:choose>
                    <c:when test="${not empty param.query}">
                        <a href="?sort=description&order=asc&query=${param.query}" style="text-decoration: none; padding: 4px 8px; border-radius: 4px; background: #667eea; color: white;">↑</a>
                        <a href="?sort=description&order=desc&query=${param.query}" style="text-decoration: none; padding: 4px 8px; border-radius: 4px; background: #667eea; color: white;">↓</a>
                    </c:when>
                    <c:otherwise>
                        <a href="?sort=description&order=asc" style="text-decoration: none; padding: 4px 8px; border-radius: 4px; background: #667eea; color: white;">↑</a>
                        <a href="?sort=description&order=desc" style="text-decoration: none; padding: 4px 8px; border-radius: 4px; background: #667eea; color: white;">↓</a>
                    </c:otherwise>
                </c:choose>
            </div>
            <div style="display: flex; align-items: center; gap: 0.5rem;">
                <span style="font-weight: 600;">Цена:</span>
                <c:choose>
                    <c:when test="${not empty param.query}">
                        <a href="?sort=price&order=asc&query=${param.query}" style="text-decoration: none; padding: 4px 8px; border-radius: 4px; background: #667eea; color: white;">↑</a>
                        <a href="?sort=price&order=desc&query=${param.query}" style="text-decoration: none; padding: 4px 8px; border-radius: 4px; background: #667eea; color: white;">↓</a>
                    </c:when>
                    <c:otherwise>
                        <a href="?sort=price&order=asc" style="text-decoration: none; padding: 4px 8px; border-radius: 4px; background: #667eea; color: white;">↑</a>
                        <a href="?sort=price&order=desc" style="text-decoration: none; padding: 4px 8px; border-radius: 4px; background: #667eea; color: white;">↓</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Modern Product Grid -->
    <div class="product-table">
        <c:forEach var="product" items="${products}">
            <div class="product-card">
                <img class="product-image" src="${product.imageUrl}" alt="${product.description}">
                <div class="product-title">
                    <a href="/phoneshop/products/${product.id}">
                        ${product.description}
                    </a>
                </div>
                <div class="product-price">
                    <a href="/phoneshop/products/priceHistory/${product.id}">
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </a>
                </div>
                <form method="post" action="${pageContext.request.contextPath}/products/${product.id}" class="product-actions">
                    <input type="number" name="quantity" value="1" min="1" max="99" class="quantity-input">
                    <button type="submit" class="add-to-cart-btn">🛒 В корзину</button>
                </form>
            </div>
        </c:forEach>
    </div>
    <c:if test="${not empty sessionScope.viewHistory}">
        <h2>🕒 Недавно просмотренные товары</h2>
        <table>
            <tr>
                <c:forEach var="product" items="${sessionScope.viewHistory}">
                    <td>
                        <img class="product-tile" src="${product.imageUrl}"/>
                        <div style="margin: 0.5rem 0; font-weight: 600;">
                            <a href="/phoneshop/products/${product.id}" style="text-decoration: none; color: #667eea;">
                                ${product.description}
                            </a>
                        </div>
                        <div style="font-weight: 700; color: #ff6b6b;">
                            <a href="/phoneshop/products/priceHistory/${product.id}" style="text-decoration: none; color: inherit;">
                                <fmt:formatNumber value="${product.price}" type="currency"
                                                  currencySymbol="${product.currency.symbol}"/>
                            </a>
                        </div>
                    </td>
                </c:forEach>
            </tr>
        </table>
    </c:if>
</tags:master>