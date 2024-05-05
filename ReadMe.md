# FreshExpress Online Store

## Prerequisites
Before running this project, ensure that you have the following installed:

- Java Development Kit (JDK) 17
- Apache Maven 3
- Git


## Getting Started
To get started with this project, follow these steps:
1) Clone this repository to your local machine:
   - git clone <repository-url>
2) Navigate to the project directory:
   - cd FreshExpress
3) Build the project using Maven:
   - mvn clean install
4) Run the application:
  - mvn spring-boot:run
5) Access the Application following URL
   - http://localhost:8080/
   - OR http://localhost:8080/product

**Libraries used**
1) Spring Boot 3.2.5
2) Spring Boot Test
3) JPA
4) H2 Database
5) Lombok
6) Thymeleaf

**Features**

**About the Service**
This is a Maven-based Spring Boot project created for Online Grocery .
The process involves viewing a list of products, adding them to the cart, and implementing discount details. To store and retrieve data, it utilizes an in-memory database (H2).
Since there is no admin page, I added all products and discounts when the application started.
