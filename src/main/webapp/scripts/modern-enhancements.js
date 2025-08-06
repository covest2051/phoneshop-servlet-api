// Modern JavaScript enhancements for PhoneShop

document.addEventListener('DOMContentLoaded', function() {
    console.log('🚀 PhoneShop Modern UI загружен!');
    
    // Smooth animations and transitions
    initSmoothAnimations();
    
    // Enhanced form interactions
    initFormEnhancements();
    
    // Cart animations
    initCartAnimations();
    
    // Product card interactions
    initProductCardAnimations();
    
    // Search enhancements
    initSearchEnhancements();
    
    // Responsive improvements
    initResponsiveEnhancements();
});

function initSmoothAnimations() {
    // Add smooth scrolling
    document.documentElement.style.scrollBehavior = 'smooth';
    
    // Add page entrance animation
    document.body.style.opacity = '0';
    document.body.style.transform = 'translateY(20px)';
    
    setTimeout(() => {
        document.body.style.transition = 'all 0.8s ease';
        document.body.style.opacity = '1';
        document.body.style.transform = 'translateY(0)';
    }, 100);
    
    // Add stagger animation to product cards
    const productCards = document.querySelectorAll('.product-card');
    productCards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(30px)';
        card.style.transition = 'all 0.6s ease';
        
        setTimeout(() => {
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, 100 + index * 100);
    });
}

function initFormEnhancements() {
    // Enhanced button states
    const submitButtons = document.querySelectorAll('button[type="submit"]');
    submitButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            const originalText = this.innerHTML;
            this.innerHTML = '⏳ Загрузка...';
            this.disabled = true;
            this.style.transform = 'scale(0.95)';
            
            // Re-enable after a delay (for demo purposes)
            setTimeout(() => {
                this.innerHTML = originalText;
                this.disabled = false;
                this.style.transform = 'scale(1)';
            }, 2000);
        });
        
        // Add hover effects
        button.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-2px) scale(1.02)';
        });
        
        button.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0) scale(1)';
        });
    });
    
    // Number input validation with visual feedback
    const numberInputs = document.querySelectorAll('input[type="number"]');
    numberInputs.forEach(input => {
        input.addEventListener('input', function() {
            const value = parseInt(this.value);
            const min = parseInt(this.min) || 1;
            const max = parseInt(this.max) || 99;
            
            if (value < min || value > max || isNaN(value)) {
                this.style.borderColor = '#ff6b6b';
                this.style.boxShadow = '0 0 0 4px rgba(255, 107, 107, 0.1)';
            } else {
                this.style.borderColor = '#10ac84';
                this.style.boxShadow = '0 0 0 4px rgba(16, 172, 132, 0.1)';
            }
        });
        
        input.addEventListener('change', function() {
            if (this.value < 1) this.value = 1;
            if (this.value > 99) this.value = 99;
        });
    });
}

function initCartAnimations() {
    const cartBadge = document.querySelector('.cart-badge');
    if (cartBadge) {
        // Animate cart updates
        const observer = new MutationObserver(function(mutations) {
            mutations.forEach(function(mutation) {
                if (mutation.type === 'childList' || mutation.type === 'characterData') {
                    // Cart update animation
                    cartBadge.style.transform = 'scale(1.3) rotate(10deg)';
                    cartBadge.style.background = 'linear-gradient(135deg, #1dd1a1, #10ac84)';
                    
                    setTimeout(() => {
                        cartBadge.style.transform = 'scale(1) rotate(0deg)';
                        cartBadge.style.background = 'linear-gradient(135deg, #ff6b6b, #feca57)';
                    }, 300);
                }
            });
        });
        observer.observe(cartBadge, { childList: true, subtree: true, characterData: true });
    }
}

function initProductCardAnimations() {
    const productCards = document.querySelectorAll('.product-card');
    
    productCards.forEach(card => {
        // Enhanced hover effects
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-12px) scale(1.02)';
            this.style.boxShadow = '0 25px 50px rgba(0, 0, 0, 0.2)';
            
            const image = this.querySelector('.product-image');
            if (image) {
                image.style.transform = 'scale(1.1) rotate(2deg)';
            }
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0) scale(1)';
            this.style.boxShadow = '0 8px 32px rgba(0, 0, 0, 0.08)';
            
            const image = this.querySelector('.product-image');
            if (image) {
                image.style.transform = 'scale(1) rotate(0deg)';
            }
        });
        
        // Click ripple effect
        card.addEventListener('click', function(e) {
            const ripple = document.createElement('div');
            ripple.style.position = 'absolute';
            ripple.style.borderRadius = '50%';
            ripple.style.background = 'rgba(102, 126, 234, 0.3)';
            ripple.style.transform = 'scale(0)';
            ripple.style.animation = 'ripple 0.6s linear';
            ripple.style.left = (e.clientX - card.offsetLeft - 10) + 'px';
            ripple.style.top = (e.clientY - card.offsetTop - 10) + 'px';
            ripple.style.width = '20px';
            ripple.style.height = '20px';
            
            card.appendChild(ripple);
            
            setTimeout(() => {
                ripple.remove();
            }, 600);
        });
    });
}

function initSearchEnhancements() {
    const searchInput = document.querySelector('input[name="query"]');
    const searchButton = document.querySelector('form:first-of-type button');
    
    if (searchInput && searchButton) {
        // Enhanced focus effects
        searchInput.addEventListener('focus', function() {
            this.style.transform = 'scale(1.02)';
            this.style.boxShadow = '0 8px 24px rgba(102, 126, 234, 0.2)';
            this.parentElement.style.transform = 'translateY(-2px)';
        });
        
        searchInput.addEventListener('blur', function() {
            this.style.transform = 'scale(1)';
            this.style.boxShadow = '0 0 0 4px rgba(102, 126, 234, 0.1)';
            this.parentElement.style.transform = 'translateY(0)';
        });
        
        // Live search suggestions (demo)
        let searchTimeout;
        searchInput.addEventListener('input', function() {
            clearTimeout(searchTimeout);
            const query = this.value.trim();
            
            if (query.length > 2) {
                searchTimeout = setTimeout(() => {
                    // This would typically make an AJAX call for suggestions
                    console.log('Поиск: ' + query);
                }, 300);
            }
        });
        
        // Enter key handling
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                searchButton.click();
            }
        });
    }
}

function initResponsiveEnhancements() {
    // Add touch gestures for mobile
    let startX, startY;
    
    document.addEventListener('touchstart', function(e) {
        startX = e.touches[0].clientX;
        startY = e.touches[0].clientY;
    });
    
    document.addEventListener('touchend', function(e) {
        if (!startX || !startY) return;
        
        const endX = e.changedTouches[0].clientX;
        const endY = e.changedTouches[0].clientY;
        const diffX = startX - endX;
        const diffY = startY - endY;
        
        // Simple swipe detection
        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (Math.abs(diffX) > 50) {
                if (diffX > 0) {
                    console.log('Свайп влево');
                } else {
                    console.log('Свайп вправо');
                }
            }
        }
        
        startX = null;
        startY = null;
    });
    
    // Dynamic viewport adjustments
    function adjustForMobile() {
        if (window.innerWidth <= 768) {
            document.body.classList.add('mobile-view');
            
            // Adjust spacing for mobile
            const main = document.querySelector('main');
            if (main) {
                main.style.margin = '1rem';
                main.style.padding = '1rem';
            }
        } else {
            document.body.classList.remove('mobile-view');
        }
    }
    
    window.addEventListener('resize', adjustForMobile);
    adjustForMobile();
}

// Add CSS animations via JavaScript
const style = document.createElement('style');
style.textContent = `
    @keyframes ripple {
        to {
            transform: scale(4);
            opacity: 0;
        }
    }
    
    @keyframes bounce {
        0%, 20%, 60%, 100% {
            transform: translateY(0);
        }
        40% {
            transform: translateY(-10px);
        }
        80% {
            transform: translateY(-5px);
        }
    }
    
    .mobile-view .product-table {
        grid-template-columns: 1fr !important;
        gap: 1rem !important;
    }
    
    .mobile-view .product-card {
        padding: 1rem !important;
    }
    
    .mobile-view header {
        padding: 0.5rem !important;
    }
    
    .mobile-view .logo {
        font-size: 24px !important;
    }
`;

document.head.appendChild(style);

// Add loading indicator
function showLoading() {
    const loader = document.createElement('div');
    loader.id = 'page-loader';
    loader.innerHTML = `
        <div style="position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(255,255,255,0.95); display: flex; justify-content: center; align-items: center; z-index: 9999;">
            <div style="text-align: center;">
                <div style="width: 50px; height: 50px; border: 3px solid #f3f3f3; border-radius: 50%; border-top: 3px solid #667eea; animation: spin 1s linear infinite;"></div>
                <p style="margin-top: 1rem; font-weight: 600; color: #667eea;">Загрузка...</p>
            </div>
        </div>
        <style>
            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
        </style>
    `;
    document.body.appendChild(loader);
    
    setTimeout(() => {
        loader.remove();
    }, 1000);
}

// Initialize loading on page transitions
document.addEventListener('click', function(e) {
    const link = e.target.closest('a[href]');
    if (link && !link.href.includes('#') && !link.href.includes('javascript:')) {
        showLoading();
    }
});

console.log('✨ Все улучшения UI загружены успешно!');