<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>

<html>
<head>
    <title>${pageTitle}</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/main.css">
    <script>
        // Simple animations and interactions
        document.addEventListener('DOMContentLoaded', function() {
            // Add smooth scrolling
            document.documentElement.style.scrollBehavior = 'smooth';
            
            // Add loading animation to buttons
            const buttons = document.querySelectorAll('button[type="submit"]');
            buttons.forEach(button => {
                button.addEventListener('click', function() {
                    const originalText = this.innerHTML;
                    this.innerHTML = '⏳ Загрузка...';
                    this.disabled = true;
                    
                    // Re-enable after delay for demo
                    setTimeout(() => {
                        this.innerHTML = originalText;
                        this.disabled = false;
                    }, 2000);
                });
            });
            
            // Add number input validation
            const numberInputs = document.querySelectorAll('input[type="number"]');
            numberInputs.forEach(input => {
                input.addEventListener('change', function() {
                    if(this.value < 1) this.value = 1;
                    if(this.value > 99) this.value = 99;
                });
            });
            
            // Add search input animation
            const searchInput = document.querySelector('input[name="query"]');
            if(searchInput) {
                searchInput.addEventListener('focus', function() {
                    this.style.transform = 'scale(1.02)';
                });
                searchInput.addEventListener('blur', function() {
                    this.style.transform = 'scale(1)';
                });
            }
            
            // Add cart update animation
            const cartBadge = document.querySelector('.cart-badge');
            if(cartBadge) {
                // Animate cart updates
                const observer = new MutationObserver(function(mutations) {
                    mutations.forEach(function(mutation) {
                        if(mutation.type === 'childList' || mutation.type === 'characterData') {
                            cartBadge.style.transform = 'scale(1.2)';
                            setTimeout(() => {
                                cartBadge.style.transform = 'scale(1)';
                            }, 200);
                        }
                    });
                });
                observer.observe(cartBadge, { childList: true, subtree: true, characterData: true });
            }
            
            // Add product card hover effects
            const productCards = document.querySelectorAll('.product-card');
            productCards.forEach(card => {
                card.addEventListener('mouseenter', function() {
                    this.style.transform = 'translateY(-12px) scale(1.02)';
                });
                
                card.addEventListener('mouseleave', function() {
                    this.style.transform = 'translateY(0) scale(1)';
                });
            });
        });
    </script>
    <script src="${pageContext.servletContext.contextPath}/scripts/modern-enhancements.js"></script>
</head>
<body class="product-list">
<header>
    <a href="${pageContext.servletContext.contextPath}">
        <img src="${pageContext.servletContext.contextPath}/images/logo.svg"/>
        <span class="logo">📱 PhoneShop</span>
    </a>
    <div class="cart-badge">
        <jsp:include page="/cart/minicart"/>
    </div>
</header>
<main>
    <jsp:doBody/>
</main>
</body>
</html>