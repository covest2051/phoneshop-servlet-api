<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Price History">
    <h1>Price History for ${product.description}</h1>
    <table>
        <thead>
        <tr>
            <td>Date</td>
            <td>Price</td>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="entry" items="${product.priceHistory}">
            <tr>
                <td><fmt:formatDate value="${entry.startDate}" pattern="dd-MM-yyyy"/></td>
                <td><fmt:formatNumber value="${entry.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <a href="/phoneshop/products">Back to products</a>
</tags:master>