<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
        Cart: ${cart}
    </p>
    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <p>
            ${product.description}
    </p>
    <form method="post" action="${pageContext.request.contextPath}/products/${product.id}">
        <table>
            <tr>
                <td>Image</td>
                <td>
                    <img src="${product.imageUrl}">
                </td>
            </tr>
            <tr>
                <td>Code</td>
                <td>
                        ${product.code}
                </td>
            </tr>
            <tr>
                <td>Price</td>
                <td>
                    <a href="/phoneshop/products/priceHistory/${product.id}">
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </a>
                </td>
            </tr>
            <tr>
                <td>Stock</td>
                <td>
                        ${product.stock}
                </td>
            </tr>
            <tr>
                <td>Quantity</td>
                <td class="quantity">
                    <input type="number" name="quantity" value=${not empty param.quantity ? param.quantity : 1}>
                    <c:if test="${not empty param.error}">
                        <p class="error">${param.error}</p>
                    </c:if>
                </td>
            </tr>
        </table>
        <p>
            <button>Add to cart</button>
            <c:if test="${not empty param.success}">
        <p class="success">${param.success}</p>
        </c:if>
        </p>
    </form>
</tags:master>