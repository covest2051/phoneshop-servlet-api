<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Детали продукта">
    <div style="text-align: center; margin-bottom: 2rem;">
        <div class="cart-badge" style="display: inline-block;">
            🛒 Корзина: ${cart}
        </div>
    </div>
    <c:if test="${not empty param.message}">
        <div class="success">
            ✅ ${param.message}
        </div>
    </c:if>
    
    <!-- Product Details Card -->
    <div style="max-width: 800px; margin: 0 auto; background: rgba(255,255,255,0.95); border-radius: 20px; padding: 2rem; box-shadow: 0 16px 64px rgba(0,0,0,0.1); backdrop-filter: blur(20px);">
        <h1 style="text-align: center; font-size: 32px; font-weight: 700; margin-bottom: 2rem; background: linear-gradient(135deg, #667eea, #764ba2); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;">
            📱 ${product.description}
        </h1>
        
        <form method="post" action="${pageContext.request.contextPath}/products/${product.id}" style="display: grid; grid-template-columns: 1fr 1fr; gap: 3rem; align-items: center;">
            
            <!-- Left side - Product Image -->
            <div style="text-align: center;">
                <img src="${product.imageUrl}" style="width: 300px; height: 300px; object-fit: contain; border-radius: 16px; background: #f8f9fa; padding: 2rem; box-shadow: 0 8px 32px rgba(0,0,0,0.1);">
            </div>
            
            <!-- Right side - Product Info -->
            <div style="display: flex; flex-direction: column; gap: 1.5rem;">
                
                <div style="display: grid; grid-template-columns: 1fr 2fr; gap: 1rem; align-items: center;">
                    <strong>Код товара:</strong>
                    <span style="font-family: monospace; background: #f8f9fa; padding: 0.5rem 1rem; border-radius: 8px;">${product.code}</span>
                </div>
                
                <div style="display: grid; grid-template-columns: 1fr 2fr; gap: 1rem; align-items: center;">
                    <strong>Цена:</strong>
                    <div style="font-size: 28px; font-weight: 700; background: linear-gradient(135deg, #ff6b6b, #feca57); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;">
                        <a href="/phoneshop/products/priceHistory/${product.id}" style="text-decoration: none; color: inherit;">
                            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
                        </a>
                    </div>
                </div>
                
                <div style="display: grid; grid-template-columns: 1fr 2fr; gap: 1rem; align-items: center;">
                    <strong>В наличии:</strong>
                    <span style="background: linear-gradient(135deg, #1dd1a1, #10ac84); color: white; padding: 0.5rem 1rem; border-radius: 50px; font-weight: 600; display: inline-block; width: fit-content;">
                        ${product.stock} шт.
                    </span>
                </div>
                
                <div style="display: grid; grid-template-columns: 1fr 2fr; gap: 1rem; align-items: center;">
                    <strong>Количество:</strong>
                    <div>
                        <input type="number" name="quantity" value="${not empty param.quantity ? param.quantity : 1}" min="1" max="${product.stock}" 
                               style="width: 100px; padding: 12px 16px; border: 2px solid #e1e8ed; border-radius: 8px; text-align: center; font-size: 16px; transition: all 0.3s ease;">
                        <c:if test="${not empty param.error}">
                            <p class="error" style="margin-top: 0.5rem;">❌ ${param.error}</p>
                        </c:if>
                    </div>
                </div>
                
                <div style="margin-top: 2rem;">
                    <button type="submit" class="add-to-cart-btn" style="width: 100%; padding: 16px 32px; font-size: 18px;">
                        🛒 Добавить в корзину
                    </button>
                    <c:if test="${not empty param.success}">
                        <p class="success" style="margin-top: 1rem;">✅ ${param.success}</p>
                    </c:if>
                </div>
                
            </div>
        </form>
        
        <!-- Back button -->
        <div style="text-align: center; margin-top: 3rem;">
            <a href="/phoneshop/products" style="display: inline-block; padding: 12px 32px; background: linear-gradient(135deg, #667eea, #764ba2); color: white; text-decoration: none; border-radius: 50px; font-weight: 600; transition: all 0.3s ease; box-shadow: 0 4px 16px rgba(102, 126, 234, 0.3);">
                ← Вернуться к каталогу
            </a>
        </div>
    </div>
    
    <style>
        @media (max-width: 768px) {
            form {
                grid-template-columns: 1fr !important;
                text-align: center;
            }
            
            div[style*="display: grid"] {
                grid-template-columns: 1fr !important;
                text-align: center;
                gap: 0.5rem !important;
            }
            
            img {
                width: 200px !important;
                height: 200px !important;
            }
        }
    </style>
    
</tags:master>