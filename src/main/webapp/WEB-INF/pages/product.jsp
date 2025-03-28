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
    <h2>Rating</h2>
    <form method="post" action="${pageContext.request.contextPath}/products/reviews/${product.id}">
        <textarea id="userReview" name="userReview" rows="5" cols="40" required></textarea><br>
        <p>Your rate:</p>
        <select id="rating" name="rating" required>
            <option value="5">5 - Отлично</option>
            <option value="4">4 - Хорошо</option>
            <option value="3">3 - Нормально</option>
            <option value="2">2 - Плохо</option>
            <option value="1">1 - Ужасно</option>
        </select><br><br>

        <button type="submit">Send feedback</button>
    </form>

    <h2>Other users rate</h2>
    <c:choose>
        <c:when test="${not empty product.feedbackList}">
            <table>
                <thead>
                <tr>
                    <td>
                        Rating
                        <a href="?sort=rating&order=asc">▲</a>
                        <a href="?sort=rating&order=desc">▼</a>
                    </td>
                    <td>
                        Text
                    </td>
                </tr>
                </thead>
                <c:forEach var="feedback" items="${feedbackList}">
                    <tr>
                        <td class="review ${feedback.rating <= 2 ? 'bad' : 'good'}">
                            <p>${feedback.rating}</p>
                        </td>
                        <td>
                            <p>${feedback.text}</p>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:when>
        <c:otherwise>
            <p>No other users rates yet</p>
        </c:otherwise>
    </c:choose>
</tags:master>