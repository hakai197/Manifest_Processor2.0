document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');
    const errorMessage = document.getElementById('errorMessage');

    // Form submission handler
    loginForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const username = usernameInput.value;
        const password = passwordInput.value;
        
        // Simple validation (replace with actual authentication)
        if(authenticateUser(username, password)) {
            // Successful login - redirect to index.html
            window.location.href = '../html/index.html';
        } else {
            // Show error message
            errorMessage.style.display = 'block';
        }
    });
    
    // Hide error message when user starts typing again
    usernameInput.addEventListener('input', hideErrorMessage);
    passwordInput.addEventListener('input', hideErrorMessage);
    
    function hideErrorMessage() {
        errorMessage.style.display = 'none';
    }
    
    // Authentication function (replace with real implementation)
    function authenticateUser(username, password) {
        // Temporary implementation - replace with:
        // 1. API call to backend
        // 2. JWT or session-based auth
        // 3. Proper password hashing
        return username === 'admin' && password === 'password';
    }
});