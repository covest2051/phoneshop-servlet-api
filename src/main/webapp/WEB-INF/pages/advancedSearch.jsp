<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Advanced Search">
    <form method="get" action="${pageContext.request.contextPath}/advancedSearch">
        <table>
            <tr>
                <td>Description:</td>
                <td><input name="query" value="${param.query}" /></td>
            </tr>
            <tr>
                <td>Match:</td>
                <td>
                    <select name="match">
                        <option value="all" ${param.match == 'all' ? 'selected' : ''}>all words</option>
                        <option value="any" ${param.match == 'any' ? 'selected' : ''}>any word</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Min price:</td>
                <td>
                    <input name="minPrice" value="${param.minPrice}" />
                    <c:if test="${not empty errors.minPrice}">
                        <span style="color: red;">${errors.minPrice}</span>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td>Max price:</td>
                <td>
                    <input name="maxPrice" value="${param.maxPrice}" />
                    <c:if test="${not empty errors.maxPrice}">
                        <span style="color: red;">${errors.maxPrice}</span>
                    </c:if>
                </td>
            </tr>
        </table>
        <p><button type="submit">Search</button></p>
    </form>

    <c:if test="${not empty param || not empty param.query}">
        <c:if test="${not empty products}">
            <table>
                <thead>
                <tr>
                    <th>Image</th>
                    <th>Description</th>
                    <th>Price</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="product" items="${products}">
                    <tr>
                        <td>
                            <img class="product-tile" src="${product.imageUrl}" />
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/products/${product.id}">
                                    ${product.description}
                            </a>
                        </td>
                        <td>
                            <fmt:formatNumber value="${product.price}" type="currency"
                                              currencySymbol="${product.currency.symbol}" />
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
        <c:if test="${not empty param && empty products && empty errors}">
            <p>No products found for your query.</p>
        </c:if>
    </c:if>
</tags:master>