<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart">
    <p>
        Cart: ${cart}
    </p>
    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty errors}">
        <div class="error">
            <p>
                An error occurred while updating the cart
            </p>
        </div>
    </c:if>
    <form method="post" action="${pageContext.servletContext.contextPath}/cart">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>
                    Description
                </td>
                <td class="price">
                    Price
                </td>
                <td>
                    Quantity
                </td>
            </tr>
            </thead>
            <c:forEach var="item" items="${cart.items}" varStatus="status">
                <tr>
                    <td>
                        <img class="product-tile" src="${item.product.imageUrl}">
                    </td>
                    <td>
                        <a href="/phoneshop/products/${item.product.id}">
                                ${item.product.description}
                        </a>
                    </td>
                    <td class="price">
                        <a href="/phoneshop/products/priceHistory/${item.product.id}">
                            <fmt:formatNumber value="${item.product.price}" type="currency"
                                              currencySymbol="${item.product.currency.symbol}"/>
                        </a>
                    </td>
                    <td class="quantity">
                        <fmt:formatNumber value="${item.quantity}" var="quantity"/>
                        <c:set var="error" value="${errors[item.product.id]}"/>
                        <input name="quantity"
                               value="${not empty error ? paramValues['quantity'][status.index] : item.quantity}"
                               class="quantity">
                        <c:if test="${not empty error}">
                            <div class="error">
                                    ${error}
                            </div>
                        </c:if>
                        <input type="hidden" value="${item.product.id}" name="productId">
                    </td>
                    <td>
                        <button type="submit"
                                formaction="${pageContext.servletContext.contextPath}/cart?productId=${item.product.id}"
                                formmethod="post" name="method" value="DELETE">✕</button>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td></td>
                <td></td>
                <td>
                    Total cost: <fmt:formatNumber value="${cart.totalCost}" type="currency"
                                                 currencySymbol="$"/>
                </td>
                <td>Total quantity: ${cart.totalQuantity}</td>
            </tr>
        </table>
        <p>
            <button>Update</button>
        </p>
    </form>
    <form method="get" action="${pageContext.request.contextPath}/checkout">
        <button type="submit">Checkout</button>
    </form>
    <a href="/phoneshop/products">Back to products</a>
</tags:master>