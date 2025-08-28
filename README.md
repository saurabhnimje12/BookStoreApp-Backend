📚 BookstoreApp Backend

A Spring Boot backend for an online bookstore application.
This service provides user authentication (JWT), book management, shopping cart, and order placement functionalities.

🚀 Features

🔐 Authentication & Authorization

User sign-up & login

JWT-based authentication

Role-based access (USER, ADMIN)

📖 Book Management

Get all books

Search books

Admin can add/update/delete books

🛒 Cart Management

Add/remove/update items in cart

View user cart

Real-time quantity updates

📦 Order Management

Place orders with shipping details

View user orders

Admin can manage all orders

🗄 Database

MySQL / H2 (configurable)

🛠 Tech Stack

Java 17+

Spring Boot 3+

Spring Security (JWT)

Spring Data JPA (Hibernate)

MySQL / H2

Maven


Clone the repository
git clone https://github.com/your-username/bookstoreapp-backend.git
cd bookstoreapp-backend


Backend runs at:
👉 http://localhost:8080

🔑 API Endpoints
👤 Authentication

POST /user/signup → Register new user

POST /user/signin → Login & get JWT token

📚 Books

GET /bookApi/ → Get all books

POST /bookApi/addBook → Add book (Admin)

PUT /bookApi/updateBook/{id} → Update book (Admin)

DELETE /bookApi/{id} → Delete book (Admin)

🛒 Cart

GET /cartApi/getCartByUser → Get cart by logged-in user

POST /cartApi/addToCart/{bookId} → Add book to cart

PATCH /cartApi/updateCartAdd/{cartId} → Increase quantity

PATCH /cartApi/updateCartRmv/{cartId} → Decrease quantity

DELETE /cartApi/removeFromCart/{cartId} → Remove from cart

📦 Orders

POST /orderApi/orderPlace → Place an order

GET /orderApi/getOrdersByUser → Get user’s orders

GET /orderApi/getAllOrders → Get all orders (Admin)
