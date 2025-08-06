<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} | PhoneShop</title>
    <link href='https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/main.css">
    <link rel="icon" type="image/svg+xml" href="${pageContext.servletContext.contextPath}/images/logo.svg">
</head>
<body class="product-list">
<header>
    <div class="header-container">
        <div class="logo-section">
            <a href="${pageContext.servletContext.contextPath}">
                <img src="${pageContext.servletContext.contextPath}/images/logo.svg" alt="PhoneShop Logo"/>
                <span class="logo-text">PhoneShop</span>
            </a>
        </div>
        <div class="cart-info">
            <jsp:include page="/cart/minicart"/>
        </div>
    </div>
</header>
<main>
    <jsp:doBody/>
</main>

<script>
// Modern JavaScript for enhanced interactions
document.addEventListener('DOMContentLoaded', function() {
    // Add smooth scrolling to all links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            document.querySelector(this.getAttribute('href')).scrollIntoView({
                behavior: 'smooth'
            });
        });
    });
    
    // Add loading state to buttons
    document.querySelectorAll('button[type="submit"]').forEach(button => {
        button.addEventListener('click', function() {
            if (!this.classList.contains('loading')) {
                this.classList.add('loading');
                this.innerHTML = '<span class="loading"></span> ' + this.textContent;
                
                // Remove loading state after 2 seconds (adjust as needed)
                setTimeout(() => {
                    this.classList.remove('loading');
                    this.innerHTML = this.textContent.replace(/^.*?\s/, '');
                }, 2000);
            }
        });
    });
    
    // Add hover effects to product cards
    document.querySelectorAll('.product-card').forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-10px) rotateX(5deg)';
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0) rotateX(0)';
        });
    });
    
    // Enhanced search functionality
    const searchInput = document.querySelector('input[name="query"]');
    if (searchInput) {
        let searchTimeout;
        searchInput.addEventListener('input', function() {
            clearTimeout(searchTimeout);
            searchTimeout = setTimeout(() => {
                // Add visual feedback for search
                this.style.boxShadow = '0 5px 20px rgba(102, 126, 234, 0.3)';
                setTimeout(() => {
                    this.style.boxShadow = '';
                }, 1000);
            }, 300);
        });
    }
});

// Add to cart animation
function addToCartAnimation(button) {
    button.style.transform = 'scale(0.95)';
    button.innerHTML = '✓ Added!';
    button.style.background = 'linear-gradient(135deg, #28a745 0%, #20c997 100%)';
    
    setTimeout(() => {
        button.style.transform = 'scale(1)';
        button.innerHTML = 'Add to cart';
        button.style.background = 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)';
    }, 1500);
}
</script>
</body>
</html>