// Store phone number for multi-step process
let userPhoneNumber = '';

// Base API URL
const API_BASE_URL = 'http://localhost:8080/api/auth/register';

// Step 1: Phone Registration
document.getElementById('phone-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const phone = document.getElementById('phone').value;
    userPhoneNumber = phone;

    try {
        const response = await fetch(`${API_BASE_URL}/phone`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ phoneNumber: phone })
        });

        const data = await response.json();

        if (data.success) {
            showMessage('phone-message', 'OTP sent successfully! Check your phone.', 'success');

            // Show OTP section after 2 seconds
            setTimeout(() => {
                hideSection('phone-section');
                showSection('otp-section');
            }, 2000);
        } else {
            showMessage('phone-message', data.message, 'error');
        }
    } catch (error) {
        showMessage('phone-message', 'Error: ' + error.message, 'error');
    }
});

// Step 2: OTP Verification
document.getElementById('otp-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const otp = document.getElementById('otp').value;

    try {
        const response = await fetch(`${API_BASE_URL}/verify-otp`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                phoneNumber: userPhoneNumber,
                otp: otp
            })
        });

        const data = await response.json();

        if (data.success) {
            showMessage('otp-message', 'Phone verified successfully!', 'success');

            // Show password section after 1.5 seconds
            setTimeout(() => {
                hideSection('otp-section');
                showSection('password-section');
            }, 1500);
        } else {
            showMessage('otp-message', data.message, 'error');
        }
    } catch (error) {
        showMessage('otp-message', 'Error: ' + error.message, 'error');
    }
});

// Step 3: Password Setup
document.getElementById('password-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm-password').value;

    // Client-side validation
    if (password !== confirmPassword) {
        showMessage('password-message', 'Passwords do not match!', 'error');
        return;
    }

    if (password.length < 8) {
        showMessage('password-message', 'Password must be at least 8 characters long!', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/set-password`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                phoneNumber: userPhoneNumber,
                password: password,
                confirmPassword: confirmPassword
            })
        });

        const data = await response.json();

        if (data.success) {
            showMessage('password-message', 'Registration completed!', 'success');

            // Show success section after 1.5 seconds
            setTimeout(() => {
                hideSection('password-section');
                showSection('success-section');
            }, 1500);
        } else {
            showMessage('password-message', data.message, 'error');
        }
    } catch (error) {
        showMessage('password-message', 'Error: ' + error.message, 'error');
    }
});

// Utility Functions
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
