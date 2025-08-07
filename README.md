# Java Internet Shop

This project is an online store implemented in Java using JSP, servlets, and related technologies. The application provides the user with the ability to browse the product catalog, filter and search for devices, add them to the cart, place orders and leave reviews. There is also a browsing history, protection against DoS attacks, and automated testing.

## Features

- 📱 Smartphone catalog with filtering and search support
- 🛒 Adding items to the shopping cart and placing an order
- 💬 User reviews on product pages
- 🔄 Browsing history for each user
- 🔐 Basic protection against DoS attacks and secure session management
- 🧪 Full coverage of unit and integration tests
- 📦 The ability to expand and connect external services

## Technology

- **Java**
- **JSP / Servlets (Jakarta EE)**
- **JUnit 4 / Mockito**
- **HTML / CSS (JSTL, custom tags)**
- **Maven** — project build
- **Jetty / Tomcat** — for local launch

## Project launch

1. Clone the repository:
2. Run run.bat from the root folder of the project.
3. Open the URL in the browser: http://localhost:8080/phoneshop/products

To go to the shopping cart, use URL: http://localhost:8080/phoneshop/cart.

Screenshot from the page http://localhost:8080/phoneshop/products
<img width="734" height="936" alt="image" src="https://github.com/user-attachments/assets/9f27e576-d232-4f18-85c4-9e34a0e3646e" />

Screenshot from the page http://localhost:8080/phoneshop/cart при добавлении двух товаров
![image](https://github.com/user-attachments/assets/c41036aa-500f-4cd5-bce4-1341b0aaf7f8)

Screenshot from the page http://localhost:8080/phoneshop/order/overview/d7167fde-4fa4-4bba-bc5e-5efdf2ed7e0f при оформлении заказа
![image](https://github.com/user-attachments/assets/0166d00b-d200-4871-acda-6e298782c55d)

Screenshot from the page http://localhost:8080/phoneshop/products/4 с добавленными отзывами
![image](https://github.com/user-attachments/assets/667dc59b-f6eb-4925-a2a7-dc101073aec8)
