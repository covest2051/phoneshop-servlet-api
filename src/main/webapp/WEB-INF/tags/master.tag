<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} | PhoneShop</title>
    <link href='https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap' rel='stylesheet'
          type='text/css'>
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
    document.addEventListener('DOMContentLoaded', function () {
        // Initialize all interactive features
        initSmoothScrolling();
        initLoadingStates();
        initProductCardEffects();
        initSearchEnhancements();
        initParallaxEffects();
        initAnimations();
    });

    function initSmoothScrolling() {
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function (e) {
                e.preventDefault();
                const target = document.querySelector(this.getAttribute('href'));
                if (target) {
                    target.scrollIntoView({
                        behavior: 'smooth',
                        block: 'start'
                    });
                }
            });
        });
    }

    function initLoadingStates() {
        document.querySelectorAll('button[type="submit"]').forEach(button => {
            button.addEventListener('click', function () {
                if (!this.classList.contains('loading')) {
                    const originalText = this.textContent;
                    this.classList.add('loading');
                    this.innerHTML = '<span class="loading"></span> Processing...';
                    this.disabled = true;

                    setTimeout(() => {
                        this.classList.remove('loading');
                        this.innerHTML = originalText;
                        this.disabled = false;
                    }, 2000);
                }
            });
        });
    }

    function initProductCardEffects() {
        document.querySelectorAll('.product-card').forEach((card, index) => {
            // Staggered entrance animation
            card.style.opacity = '0';
            card.style.transform = 'translateY(50px)';

            setTimeout(() => {
                card.style.transition = 'all 0.8s cubic-bezier(0.175, 0.885, 0.32, 1.275)';
                card.style.opacity = '1';
                card.style.transform = 'translateY(0)';
            }, index * 150);

            // Enhanced hover effects
            card.addEventListener('mouseenter', function () {
                this.style.transform = 'translateY(-15px) rotateY(5deg) scale(1.02)';
                this.style.boxShadow = '0 25px 50px rgba(102, 126, 234, 0.4)';

                // Image zoom effect
                const img = this.querySelector('.product-tile');
                if (img) {
                    img.style.transform = 'scale(1.2) rotate(2deg)';
                }
            });

            card.addEventListener('mouseleave', function () {
                this.style.transform = 'translateY(0) rotateY(0) scale(1)';
                this.style.boxShadow = '0 5px 20px rgba(0, 0, 0, 0.1)';

                const img = this.querySelector('.product-tile');
                if (img) {
                    img.style.transform = 'scale(1) rotate(0deg)';
                }
            });
        });
    }

    function initSearchEnhancements() {
        const searchInput = document.querySelector('input[name="query"]');
        if (searchInput) {
            // Live search feedback
            searchInput.addEventListener('input', function () {
                this.style.transform = 'scale(1.02)';
                this.style.boxShadow = '0 8px 25px rgba(102, 126, 234, 0.4)';

                setTimeout(() => {
                    this.style.transform = 'scale(1)';
                    this.style.boxShadow = '0 2px 10px rgba(0, 0, 0, 0.1)';
                }, 300);
            });

            // Search suggestions (mock implementation)
            searchInput.addEventListener('focus', function () {
                createSearchSuggestions(this);
            });
        }
    }

    function createSearchSuggestions(input) {
        const suggestions = ['Samsung', 'Apple', 'iPhone', 'Galaxy', 'Android'];
        let suggestionBox = document.querySelector('.search-suggestions');

        if (!suggestionBox) {
            suggestionBox = document.createElement('div');
            suggestionBox.className = 'search-suggestions';
            suggestionBox.style.cssText = `
            position: absolute;
            top: 100%;
            left: 0;
            right: 0;
            background: white;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
            z-index: 1000;
            margin-top: 0.5rem;
            opacity: 0;
            transform: translateY(-10px);
            transition: all 0.3s ease;
        `;

            input.parentElement.style.position = 'relative';
            input.parentElement.appendChild(suggestionBox);

            suggestions.forEach(suggestion => {
                const item = document.createElement('div');
                item.textContent = suggestion;
                item.style.cssText = `
                padding: 1rem;
                cursor: pointer;
                transition: background 0.3s ease;
                border-radius: 10px;
                margin: 0.5rem;
            `;
                item.addEventListener('mouseenter', () => {
                    item.style.background = '#f8f9fa';
                });
                item.addEventListener('mouseleave', () => {
                    item.style.background = '';
                });
                item.addEventListener('click', () => {
                    input.value = suggestion;
                    suggestionBox.remove();
                });
                suggestionBox.appendChild(item);
            });

            setTimeout(() => {
                suggestionBox.style.opacity = '1';
                suggestionBox.style.transform = 'translateY(0)';
            }, 100);

            document.addEventListener('click', function (e) {
                if (!input.parentElement.contains(e.target)) {
                    suggestionBox.remove();
                }
            });
        }
    }

    function initParallaxEffects() {
        window.addEventListener('scroll', function () {
            const scrolled = window.pageYOffset;
            const header = document.querySelector('header');
            if (header) {
                header.style.transform = `translateY(${scrolled * 0.5}px)`;
            }
        });
    }

    function initAnimations() {
        // Add pulse animation to cart badge when items are added
        const observer = new MutationObserver(function (mutations) {
            mutations.forEach(function (mutation) {
                if (mutation.type === 'childList') {
                    const cartBadge = document.querySelector('.cart-badge');
                    if (cartBadge) {
                        cartBadge.style.animation = 'pulse 0.6s ease-in-out';
                        setTimeout(() => {
                            cartBadge.style.animation = '';
                        }, 600);
                    }
                }
            });
        });

        const cartInfo = document.querySelector('.cart-info');
        if (cartInfo) {
            observer.observe(cartInfo, {childList: true, subtree: true});
        }
    }

    // Enhanced add to cart animation with confetti effect
    function addToCartAnimation(button) {
        // Button animation
        button.style.transform = 'scale(0.9)';
        button.innerHTML = '🎉 Added!';
        button.style.background = 'linear-gradient(135deg, #28a745 0%, #20c997 100%)';
        button.disabled = true;

        // Create confetti effect
        createConfetti(button);

        // Update cart badge with animation
        updateCartBadge();

        setTimeout(() => {
            button.style.transform = 'scale(1)';
            button.innerHTML = '🛒 Add to Cart';
            button.style.background = 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)';
            button.disabled = false;
        }, 2000);
    }

    function createConfetti(element) {
        const rect = element.getBoundingClientRect();
        const confettiCount = 10;

        for (let i = 0; i < confettiCount; i++) {
            const confetti = document.createElement('div');
            confetti.style.cssText = `
            position: fixed;
            width: 8px;
            height: 8px;
            background: red;
            left: ${rect.left + rect.width / 2}px;
            top: ${rect.top + rect.height / 2}px;
            border-radius: 50%;
            pointer-events: none;
            z-index: 10000;
        `;

            document.body.appendChild(confetti);

            const angle = (i / confettiCount) * 360;
            const velocity = 100 + Math.random() * 100;

            confetti.animate([
                {
                    transform: `translate(0, 0) rotate(0deg)`,
                    opacity: 1
                },
                {
                    transform: `translate(${Math.cos(angle * Math.PI / 180) * velocity}px, ${Math.sin(angle * Math.PI / 180) * velocity}px) rotate(720deg)`,
                    opacity: 0
                }
            ], {
                duration: 1000,
                easing: 'cubic-bezier(0.25, 0.46, 0.45, 0.94)'
            }).addEventListener('finish', () => {
                confetti.remove();
            });
        }
    }

    function getRandomColor() {
        const colors = ['#667eea', '#764ba2', '#28a745', '#dc3545', '#ffc107', '#17a2b8'];
        return colors[Math.floor(Math.random() * colors.length)];
    }

    function updateCartBadge() {
        const cartBadge = document.querySelector('.cart-badge');
        if (cartBadge) {
            cartBadge.style.animation = 'cartBounce 0.6s ease-in-out';
            cartBadge.style.background = 'linear-gradient(135deg, #28a745 0%, #20c997 100%)';

            setTimeout(() => {
                cartBadge.style.animation = '';
                cartBadge.style.background = '';
            }, 600);
        }
    }

    // Add CSS animations dynamically
    const style = document.createElement('style');
    style.textContent = `
    @keyframes pulse {
        0% { transform: scale(1); }
        50% { transform: scale(1.2); }
        100% { transform: scale(1); }
    }

    @keyframes cartBounce {
        0%, 20%, 50%, 80%, 100% { transform: scale(1); }
        40% { transform: scale(1.3); }
        60% { transform: scale(1.1); }
    }

    .product-tile {
        transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
    }

    .search-suggestions {
        backdrop-filter: blur(10px);
    }

    @media (max-width: 768px) {
        .product-card:hover {
            transform: translateY(-5px) scale(1.02) !important;
        }
    }
`;
    document.head.appendChild(style);
</script>
</body>
</html>