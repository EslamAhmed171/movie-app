# Movie Management System

This project is a Spring Boot-based application that enables admins to manage movies, handle user accounts, and secure the application with JWT-based authentication. It includes a comprehensive feature set for managing movies, user accounts, and file handling.

## Key Features

- **Movie Management**: Admins can add and manage movies with details such as title, release year, and cast.
- **User Account Management**: Users can register, log in, and delete their accounts. It includes user authentication and authorization.
- **JWT Authentication**: The application is secured with JWTs for access and refresh tokens. Refresh tokens are stored in the database for secure session management.
- **File Handling**: The application supports file handling for movie posters, ensuring efficient storage and retrieval of images.
- **Pagination and Sorting**: Movies can be retrieved using pagination and sorting, providing a user-friendly experience for browsing the movie list.
- **Password Reset via OTP**: Added functionality for users to reset their passwords securely using an OTP (One-Time Password) sent via email. This is powered by Spring Email to ensure secure and easy recovery of account credentials.

## Prerequisites

- **Java 11 or higher**
- **Maven** for building the application
- **MySQL** as the database
- **SMTP server** for sending OTP emails (Spring Email configuration required)

## System Architecture
The application follows a typical Spring Boot architecture with the following key components:

- **Spring Data JPA/Hibernate**: Used for database interaction, enabling CRUD operations for movies and users.
- **Spring Security**: Used for securing endpoints, managing authentication, and authorization using JWTs.
- **Spring Email**: Used for sending OTP codes for password reset functionality.
- **MySQL Database**: Stores user information and movie details.
- **Spring Boot**: Core framework for the application.
