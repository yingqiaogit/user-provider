# User Provider

## Java/Spring boot backend
It is a backend that supports [UI dashboard](https://github.com/yingqiaogit/user-dashboard)

### API
- `http://localhost:8080/api/v1/users?role=<role>` with role as one of `admin`,  `user`, `viewer`, `all`
- GET method
- returns an event stream of user data filtered by role

### Run
- `mvn install`
- `mvn spring-boot:run`

### Users
![GET Users](readme/users.png)



