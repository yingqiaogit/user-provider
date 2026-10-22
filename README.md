# User Provider

## Java/Spring Boot backend
It is a backend that supports the [UI dashboard](https://github.com/yingqiaogit/user-dashboard)

### API
- `http://localhost:8080/api/v1/users?role=<role>` with role as one of `admin`,  `user`, `viewer`, `all`
- GET method
- returns an event stream of user data filtered by role

### Run
- `mvn install`
- `mvn spring-boot:run`

### Pull users
Send a request to pull users through Postman: 

![GET Users](readme/users.png)



