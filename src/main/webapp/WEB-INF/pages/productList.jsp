<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.List" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
        Welcome to Expert-Soft training!
    </p>
    <form>
        <input name="query" value="${param.query}">
        <button>Search</button>
    </form>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Description
                <c:choose>
                    <c:when test="${not empty param.query}">
                        <a href="?sort=description&order=asc&query=${param.query}">▲</a>
                        <a href="?sort=description&order=desc&query=${param.query}">▼</a>
                    </c:when>
                    <c:otherwise>
                        <a href="?sort=description&order=asc">▲</a>
                        <a href="?sort=description&order=desc">▼</a>
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="price">
                Price
                <c:choose>
                    <c:when test="${not empty param.query}">
                        <a href="?sort=price&order=asc&query=${param.query}">▲</a>
                        <a href="?sort=price&order=desc&query=${param.query}">▼</a>
                    </c:when>
                    <c:otherwise>
                        <a href="?sort=price&order=asc">▲</a>
                        <a href="?sort=price&order=desc">▼</a>
                    </c:otherwise>
                </c:choose>

            </td>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}">
            <tr>
                <td>
                    <img class="product-tile" src="${product.imageUrl}">
                </td>
                <td>
                    <a href="/phoneshop/products/${product.id}">
                            ${product.description}
                    </a>
                </td>
                <td class="price">
                    <a href="/phoneshop/products/priceHistory/${product.id}">
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </a>
                </td>
            </tr>
        </c:forEach>
    </table>
    <c:if test="${not empty sessionScope.viewHistory}">
        <h2>Recently viewed</h2>
        <table>
            <tr>
                <c:forEach var="product" items="${sessionScope.viewHistory}">
                    <td style="text-align: center; vertical-align: top;">
                        <img class="product-tile" src="${product.imageUrl}"/><br/>
                        <a href="/phoneshop/products/${product.id}">
                                ${product.description}
                        </a><br/>
                        <a href="/phoneshop/products/priceHistory/${product.id}">
                            <fmt:formatNumber value="${product.price}" type="currency"
                                              currencySymbol="${product.currency.symbol}"/>
                        </a>
                    </td>
                </c:forEach>
            </tr>
        </table>
    </c:if>
</tags:master>