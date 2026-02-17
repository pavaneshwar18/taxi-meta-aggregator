// ============================================================
// auth.js  — Combined Login + Registration logic
// All API calls are IDENTICAL to the original login.js + registration.js
// ============================================================

const API_BASE_URL = 'http://localhost:8080/api/auth';
let userPhoneNumber = '';  // Stored across registration steps
let showLoginPw = false;
let showSignupPw = false;

// ============ TAB SWITCHING ============
function switchTab(tab) {
    // Reset messages
    document.querySelectorAll('.message').forEach(m => { m.className = 'message'; });

    // Update tab highlight
    document.getElementById('tab-login').classList.toggle('active', tab === 'login');
    document.getElementById('tab-signup').classList.toggle('active', tab === 'signup');

    // Hide all form sections
    hideAllSections();

    if (tab === 'login') {
        showSection('login-section');
    } else {
        showSection('signup-phone-section');
    }
}

// ============ LOGIN ============
document.getElementById('login-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const phone = document.getElementById('login-phone').value;
    const password = document.getElementById('login-password').value;

    setLoading('login-btn-text', 'login-spinner', 'Logging in...');

    try {
        const response = await fetch(`${API_BASE_URL}/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ phoneNumber: phone, password: password })
        });

        const data = await response.json();

        if (data.success) {
            showMessage('login-message', 'Login successful!', 'success');
            setTimeout(() => { displayProfile(data); }, 1000);
        } else {
            showMessage('login-message', data.message, 'error');
            resetLoading('login-btn-text', 'login-spinner', 'Login');
        }
    } catch (error) {
        showMessage('login-message', 'Error: ' + error.message, 'error');
        resetLoading('login-btn-text', 'login-spinner', 'Login');
    }
});

// ============ REGISTRATION STEP 1: Phone ============
document.getElementById('phone-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const phone = document.getElementById('phone').value;
    userPhoneNumber = phone;

    setLoading('phone-btn-text', 'phone-spinner', 'Sending OTP...');

    try {
        const response = await fetch(`${API_BASE_URL}/register/phone`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ phoneNumber: phone })
        });

        const data = await response.json();

        if (data.success) {
            showMessage('phone-message', 'OTP sent successfully! Check your phone.', 'success');
            setTimeout(() => {
                hideAllSections();
                document.getElementById('otp-phone-display').textContent = phone;
                showSection('otp-section');
            }, 2000);
        } else {
            showMessage('phone-message', data.message, 'error');
        }
        resetLoading('phone-btn-text', 'phone-spinner', 'Send OTP');
    } catch (error) {
        showMessage('phone-message', 'Error: ' + error.message, 'error');
        resetLoading('phone-btn-text', 'phone-spinner', 'Send OTP');
    }
});

// ============ REGISTRATION STEP 2: OTP ============
document.getElementById('otp-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const otp = document.getElementById('otp').value;

    setLoading('otp-btn-text', 'otp-spinner', 'Verifying...');

    try {
        const response = await fetch(`${API_BASE_URL}/register/verify-otp`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ phoneNumber: userPhoneNumber, otp: otp })
        });

        const data = await response.json();

        if (data.success) {
            showMessage('otp-message', 'Phone verified successfully!', 'success');
            setTimeout(() => {
                hideAllSections();
                showSection('password-section');
            }, 1500);
        } else {
            showMessage('otp-message', data.message, 'error');
        }
        resetLoading('otp-btn-text', 'otp-spinner', 'Verify OTP');
    } catch (error) {
        showMessage('otp-message', 'Error: ' + error.message, 'error');
        resetLoading('otp-btn-text', 'otp-spinner', 'Verify OTP');
    }
});

// ============ REGISTRATION STEP 3: Password ============
document.getElementById('password-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm-password').value;

    if (password !== confirmPassword) {
        showMessage('password-message', 'Passwords do not match!', 'error');
        return;
    }
    if (password.length < 8) {
        showMessage('password-message', 'Password must be at least 8 characters long!', 'error');
        return;
    }

    setLoading('password-btn-text', 'password-spinner', 'Setting password...');

    try {
        const response = await fetch(`${API_BASE_URL}/register/set-password`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ phoneNumber: userPhoneNumber, password: password, confirmPassword: confirmPassword })
        });

        const data = await response.json();

        if (data.success) {
            showMessage('password-message', 'Password set! Now complete your profile.', 'success');
            setTimeout(() => {
                hideAllSections();
                showSection('profile-form-section');
            }, 1500);
        } else {
            showMessage('password-message', data.message, 'error');
        }
        resetLoading('password-btn-text', 'password-spinner', 'Set Password');
    } catch (error) {
        showMessage('password-message', 'Error: ' + error.message, 'error');
        resetLoading('password-btn-text', 'password-spinner', 'Set Password');
    }
});

// ============ REGISTRATION STEP 4: Profile ============
document.getElementById('profile-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const firstName = document.getElementById('first-name').value;
    const lastName = document.getElementById('last-name').value;
    const email = document.getElementById('email').value;
    const profilePicture = document.getElementById('profile-picture').files[0];

    const formData = new FormData();
    const profileJson = JSON.stringify({
        phoneNumber: userPhoneNumber,
        firstName: firstName,
        lastName: lastName,
        email: email || null
    });
    formData.append('profile', new Blob([profileJson], { type: 'application/json' }));

    if (profilePicture) {
        formData.append('profilePicture', profilePicture);
    }

    setLoading('profile-btn-text', 'profile-spinner', 'Completing...');

    try {
        const response = await fetch(`${API_BASE_URL}/register/profile`, {
            method: 'POST',
            body: formData
        });

        const data = await response.json();

        if (data.success) {
            showMessage('profile-message', 'Registration complete! 🎉 Redirecting to Login...', 'success');
            setTimeout(() => {
                switchTab('login');
            }, 2000);
        } else {
            showMessage('profile-message', data.message, 'error');
        }
        resetLoading('profile-btn-text', 'profile-spinner', 'Complete Registration');
    } catch (error) {
        showMessage('profile-message', 'Error: ' + error.message, 'error');
        resetLoading('profile-btn-text', 'profile-spinner', 'Complete Registration');
    }
});

// ============ PROFILE PICTURE PREVIEW ============
document.getElementById('profile-picture').addEventListener('change', (e) => {
    const file = e.target.files[0];
    const label = document.getElementById('file-label-text');
    if (file) {
        label.childNodes[0].textContent = file.name;
    } else {
        label.childNodes[0].textContent = 'Profile Picture (optional)';
    }
});

// ============ DISPLAY PROFILE (after login) ============
function displayProfile(data) {
    const fullName = [data.firstName, data.lastName].filter(Boolean).join(' ') || 'User';
    document.getElementById('profile-name').textContent = fullName;
    document.getElementById('profile-phone').textContent = data.phone;

    if (data.email) {
        document.getElementById('profile-email').textContent = data.email;
        document.getElementById('email-row').style.display = 'flex';
    }

    if (data.profilePictureUrl) {
        const avatar = document.getElementById('profile-avatar');
        avatar.src = data.profilePictureUrl;
        avatar.classList.add('visible');
        document.getElementById('avatar-placeholder').style.display = 'none';
    }

    // Hide auth card, show profile card
    document.getElementById('auth-container').style.display = 'none';
    const profileSection = document.getElementById('profile-display-section');
    profileSection.style.display = 'flex';
    profileSection.classList.add('active');
}

// ============ LOGOUT ============
function logout() {
    document.getElementById('login-form').reset();
    resetLoading('login-btn-text', 'login-spinner', 'Login');

    document.getElementById('profile-avatar').classList.remove('visible');
    document.getElementById('profile-avatar').src = '';
    document.getElementById('avatar-placeholder').style.display = 'flex';
    document.getElementById('email-row').style.display = 'none';

    document.querySelectorAll('.message').forEach(m => { m.className = 'message'; });

    document.getElementById('profile-display-section').style.display = 'none';
    document.getElementById('auth-container').style.display = 'block';

    switchTab('login');
}

// ============ TOGGLE PASSWORD VISIBILITY ============
function toggleLoginPw() {
    showLoginPw = !showLoginPw;
    document.getElementById('login-password').type = showLoginPw ? 'text' : 'password';
    document.getElementById('login-eye-icon').textContent = showLoginPw ? 'visibility_off' : 'visibility';
}

function toggleSignupPw() {
    showSignupPw = !showSignupPw;
    document.getElementById('password').type = showSignupPw ? 'text' : 'password';
    document.getElementById('confirm-password').type = showSignupPw ? 'text' : 'password';
    document.getElementById('signup-eye-icon').textContent = showSignupPw ? 'visibility_off' : 'visibility';
}

// ============ BACK BUTTON ============
function goBackTo(sectionId) {
    hideAllSections();
    showSection(sectionId);
}

// ============ UTILITIES ============
function hideAllSections() {
    document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
}

function showSection(sectionId) {
    document.getElementById(sectionId).classList.add('active');
}

function showMessage(messageId, text, type) {
    const el = document.getElementById(messageId);
    el.textContent = text;
    el.className = `message show ${type}`;
}

function setLoading(textId, spinnerId, text) {
    document.getElementById(textId).textContent = text;
    document.getElementById(spinnerId).style.display = 'inline-block';
}

function resetLoading(textId, spinnerId, text) {
    document.getElementById(textId).textContent = text;
    document.getElementById(spinnerId).style.display = 'none';
}
