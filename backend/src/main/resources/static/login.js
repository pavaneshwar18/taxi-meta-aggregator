const API_BASE_URL = 'http://localhost:8080/api/auth';

// Login Form Handler
document.getElementById('login-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const phone = document.getElementById('phone').value;
    const password = document.getElementById('password').value;

    const btnText = document.getElementById('btn-text');
    const btnSpinner = document.getElementById('btn-spinner');

    // Show loading state
    btnText.textContent = 'Signing in...';
    btnSpinner.style.display = 'inline-block';

    try {
        const response = await fetch(`${API_BASE_URL}/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                phoneNumber: phone,
                password: password
            })
        });

        const data = await response.json();

        if (data.success) {
            showMessage('login-message', 'Login successful!', 'success');

            // Transition to profile view after a short delay
            setTimeout(() => {
                displayProfile(data);
            }, 1000);
        } else {
            showMessage('login-message', data.message, 'error');
            resetButton();
        }
    } catch (error) {
        showMessage('login-message', 'Error: ' + error.message, 'error');
        resetButton();
    }
});

// Display user profile
function displayProfile(data) {
    // Set name
    const fullName = [data.firstName, data.lastName].filter(Boolean).join(' ') || 'User';
    document.getElementById('profile-name').textContent = fullName;

    // Set phone
    document.getElementById('profile-phone').textContent = data.phone;

    // Set email
    if (data.email) {
        document.getElementById('profile-email').textContent = data.email;
        document.getElementById('email-row').style.display = 'flex';
    }

    // Set profile picture
    if (data.profilePictureUrl) {
        const avatar = document.getElementById('profile-avatar');
        avatar.src = data.profilePictureUrl;
        avatar.classList.add('visible');
        document.getElementById('avatar-placeholder').style.display = 'none';
    }

    // Switch sections
    hideSection('login-section');
    showSection('profile-section');

    // Update header
    document.querySelector('h1').textContent = 'Your Profile';
}

// Logout
function logout() {
    // Reset form
    document.getElementById('login-form').reset();
    resetButton();

    // Reset profile
    document.getElementById('profile-avatar').classList.remove('visible');
    document.getElementById('profile-avatar').src = '';
    document.getElementById('avatar-placeholder').style.display = 'flex';
    document.getElementById('email-row').style.display = 'none';

    // Hide message
    const msg = document.getElementById('login-message');
    msg.className = 'message';

    // Switch back to login
    hideSection('profile-section');
    showSection('login-section');
    document.querySelector('h1').textContent = 'Welcome Back';
}

// Reset button state
function resetButton() {
    document.getElementById('btn-text').textContent = 'Login';
    document.getElementById('btn-spinner').style.display = 'none';
}

// Utility functions
function showSection(sectionId) {
    document.getElementById(sectionId).classList.add('active');
}

function hideSection(sectionId) {
    document.getElementById(sectionId).classList.remove('active');
}

function showMessage(messageId, text, type) {
    const messageElement = document.getElementById(messageId);
    messageElement.textContent = text;
    messageElement.className = `message show ${type}`;
}
