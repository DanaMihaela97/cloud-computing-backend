# Overview

This project is a career platform that allows candidates to search for jobs filtered by cities, complete a form, and upload their CVs. The platform consists of a frontend application built with Angular and a backend application built with Spring Boot. The frontend is hosted on AWS EC2, while CVs are stored in AWS S3, and notifications are sent via AWS SNS. The backend uses AWS RDS (MySQL) for database management.

# Career Opportunities - Backend

The backend of the career platform is built using Spring Boot. It handles the business logic for storing CVs in AWS S3, sending notifications via AWS SNS, and managing data in an AWS RDS MySQL database.

# Features

API for candidate form submission.
CV storage in AWS S3.
Email notifications using AWS SNS.
Database management with AWS RDS (MySQL).

# Setup 

Update application.properties with your AWS credentials, S3 bucket details, SNS topic ARN, and RDS (MySQL) database credentials.
````spring.datasource.url=jdbc:mysql://your-rds-endpoint:3306/your-database````
````spring.datasource.username=````
````spring.datasource.password=````
