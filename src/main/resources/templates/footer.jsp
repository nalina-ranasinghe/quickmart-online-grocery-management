<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <base href="${pageContext.request.contextPath}/">
    <meta charset="UTF-8">
    <title>QuickMart Groceries</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/header&footer.css">
</head>
<body>
<!-- Footer with Contact Us and About sections -->
<footer>
    <div class="footer-container">
        <div id="contact-us" class="footer-section">
            <h3>Contact Us</h3>
            <p>Email: support@quickmart.com</p>
            <p>Phone: (+94) 77 028 4928</p>
            <p>Address: 529 Market Street, Kengalla</p>
        </div>

        <div id="about-us" class="footer-section">
            <h3>About QuickMart</h3>
            <p>QuickMart is your neighborhood grocery store, committed to providing fresh, high-quality products at affordable prices.</p>
        </div>

        <div id="follow-us" class="footer-section">
            <h3>Follow Us</h3>
            <div class="social-icons">
                <a href="#" class="social-icon"><img src="<%= path %>/images/whatsapp.png" alt="WhatsApp"></a>
                <a href="#" class="social-icon"><img src="<%= path %>/images/facebook_b&w.png" alt="Facebook"></a>
                <a href="#" class="social-icon"><img src="<%= path %>/images/messenger.png" alt="Messenger"></a>
                <a href="#" class="social-icon"><img src="<%= path %>/images/instagram.png" alt="Instagram"></a>
                <a href="#" class="social-icon"><img src="<%= path %>/images/linkedin.png" alt="LinkedIn"></a>
            </div>
        </div>

    </div>
    <div class="footer-divider"></div>
    <div class="copyright">
        Copyright © 2025 QuickMart Corporation | All Rights Reserved |
        <a href="#">Terms and Conditions</a> | <a href="#">Privacy Policy</a>
    </div>
</footer>
</body>
</html>