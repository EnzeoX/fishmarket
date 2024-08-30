## Fishmarket WEB

Simple web application to store and view info about fish for sale.
This application is using Spring Security to provide some resources only for authorized users and users with authorities

Application contains 2 users by default (With ADMIN and USER roles)

####Admin:
username - 2;
password - 2

####Simple user
username - 1;
password - 1

### Main page (available for all)
UI page to view info about fish for sale
```http
  GET /fish
```

### Login page (available for all)
UI page to login
```http
  GET /user/login
```

### Register page (available for all)
UI page to login
```http
  GET /user/register
```

### Admin page (if user has authorities)
UI page to get info about user accounts
```http
  GET /admin
```

### Create fish page (if user has authorities)
UI page for adding info about new fish for sale
```http
  GET /fish/create
```