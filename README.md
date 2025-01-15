# Movie-Reservation-System (in development)

Welcome to the **Movie Reservation System**, a microservice-based application designed to manage movie reservations efficiently. This system allows users to browse available movies, book tickets, and manage user roles, all while ensuring a seamless experience.

### **Features**
- **User Authentication and Authorization**:
  - Users log in using JWT-based security.
  - Role-based access control (Admin, User).

- **Movie Management**:
  - Browse available movies.
  - Fetch detailed information about movies using Feign Clients.

- **Ticket Booking**:
  - Book tickets dynamically based on movie schedules, available seats, and pricing.
  - Automatic seat availability management.

- **Notifications**:
  - Users receive personalized email notifications for successful bookings.

- **Microservices Architecture**:
  - Services like MovieService, SeatService, ScheduleService, and NotificationService are independently managed.

---
### **Technologies Used**

- **Java 21**: Modern features for cleaner and more efficient code.
- **Spring Boot**: For building the microservice architecture.
- **Spring Security**: JWT-based security for authentication and authorization.
- **Spring Data JDBC**: Easy database access and management.
- **Feign Client**: For inter-service communication in a declarative style.

#### **Containerization**
- **Docker**: For simplifying deployments by containerizing application services.

#### **Message-Driven Communication**
- **RabbitMQ**: For publishing and consuming notifications/events.

#### **Database**
- **PostgreSQL**: Relational database to store user, movie, ticket, and related data.
#### **Microservices**
- **Eureka**: For service discovery
  ![Eureka](user/assets/images/Eureka.png)
  
- **Zipkin**: For distributed tracing
  ![Zipkin](user/assets/images/Zipkin.png)


### **How to Contribute**
1. Fork the repository.
2. Clone your forked repository:
```bash
git clone https://github.com/your-username/movie-ticket-booking-system.git
```
3. Create a new branch for your feature/bugfix:
   ```bash
   git checkout -b feature-name
   ```
4. Make changes and commit them:
   ```bash
   git add .
   git commit -m "Add a new feature or fix a bug"
   ```
5. Push changes:
   ```bash
   git push origin feature-name
   ```
6. Submit a pull request.

---

### **Known Issues/TODOs**
- [ ] Enhance pagination for movie listings.
- [ ] Add integration tests for end-to-end validation.
- [ ] Implement dynamic loading of cinema details from an external API.

### **Contact**

If you have questions or suggestions, feel free to reach out:

- **Email**: levantosina1992@gmail.com
- **GitHub**: (https://github.com/levantosina)