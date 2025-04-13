<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Checkout page">
    <p>
        Order: ${order}
    </p>
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
            <c:forEach var="item" items="${order.items}" varStatus="status">
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
                        <fmt:formatNumber value="${item.product.price}" type="currency"
                                          currencySymbol="${item.product.currency.symbol}"/>
                    </td>
                    <td class="quantity">
                        <fmt:formatNumber value="${item.quantity}"/>
                        <input type="hidden" value="${item.product.id}" name="productId">
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td></td>
                <td></td>
                <td>
                    Subtotal: <fmt:formatNumber value="${order.subtotal}" type="currency"
                                                currencySymbol="$"/>
                </td>
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td>
                    Delivery cost: <fmt:formatNumber value="${order.deliveryCost}" type="currency"
                                                     currencySymbol="$"/>
                </td>
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td>
                    Total cost: <fmt:formatNumber value="${order.totalCost}" type="currency"
                                                  currencySymbol="$"/>
                </td>
                <td>Total quantity: ${order.totalQuantity}</td>
            </tr>
        </table>
    </form>
    <form method="post" action="${pageContext.request.contextPath}/checkout">
        <h2>Your details</h2>
        <table>
            <tags:orderFormRow name="firstName" label="First name" order="${order}" errors="${errors}"/>
            <tags:orderFormRow name="lastName" label="Last name" order="${order}" errors="${errors}"/>
            <tags:orderFormRow name="phone" label="Phone" order="${order}" errors="${errors}"/>
            <tags:orderFormRow name="deliveryDate" label="Delivery date" order="${order}" errors="${errors}"/>
            <tags:orderFormRow name="deliveryAddress" label="Delivery address" order="${order}" errors="${errors}"/>
            <tr>
                <td>Payment method<span style="color:red">*</span></td>
                <td>
                    <select name="paymentMethod">
                        <option></option>
                        <c:forEach var="paymentMethod" items="${paymentMethods}">
                            <option value="${paymentMethod}"
                                    <c:if test="${paymentMethod == param['paymentMethod'] || paymentMethod == order['paymentMethod']}">selected</c:if>>
                                    ${paymentMethod}
                            </option>
                        </c:forEach>
                    </select>
                    <c:set var="error" value="${errors['paymentMethod']}" />
                    <c:if test="${not empty error}">
                        <div class="error">
                                ${error}
                        </div>
                    </c:if>
                </td>
            </tr>
        </table>
        <button type="submit">Place order</button>
    </form>
    <a href="/phoneshop/products">Back to products</a>
</tags:master>