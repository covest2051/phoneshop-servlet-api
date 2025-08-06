<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>

<div class="minicart-container">
    <a href="${pageContext.servletContext.contextPath}/cart" class="minicart-link">
        <span class="cart-icon">🛒</span>
        <span class="cart-text">Cart</span>
        <span class="cart-badge">${cart.totalQuantity}</span>
        <c:if test="${cart.totalQuantity > 0}">
            <span class="item-text">items</span>
        </c:if>
        <c:if test="${cart.totalQuantity == 0}">
            <span class="item-text">empty</span>
        </c:if>
    </a>
</div>

<style>
.minicart-container {
    display: flex;
    align-items: center;
}

.minicart-link {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    text-decoration: none;
    color: inherit;
    transition: all 0.3s ease;
}

.cart-icon {
    font-size: 1.2rem;
    animation: ${cart.totalQuantity > 0 ? 'bounce 0.6s ease-in-out' : 'none'};
}

.cart-badge {
    background: rgba(255, 255, 255, 0.9);
    color: #667eea;
    padding: 0.25rem 0.5rem;
    border-radius: 15px;
    font-size: 0.9rem;
    font-weight: 600;
    min-width: 24px;
    text-align: center;
}

@keyframes bounce {
    0%, 20%, 50%, 80%, 100% { transform: translateY(0); }
    40% { transform: translateY(-5px); }
    60% { transform: translateY(-3px); }
}
</style>