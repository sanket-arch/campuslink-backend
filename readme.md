# Campus Link

Campus Link is a Spring Boot application that connects students across various courses and campuses. It allows students to post and view reviews on hostels, professors, and mess facilities, as well as to post and view queries. Additionally, it offers a marketplace for buying and selling old belongings and keeps students updated on campus activities.

### [Mindmap](https://www.mindmeister.com/app/map/3460790403?source=template)

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)
- [Contact](#contact)

## Features

- **User Authentication**: Secure login with username and password.
- **JWT Authentication**: Protects all endpoints using JWT tokens.
- **CRUD Operations**: Create, Read, Update, and Delete functionalities for various entities.
- **Reviews**: Post and view reviews of hostels and professors.
- **Queries**: Post and view queries related to campus life.
- **Marketplace**: Buy and sell old belongings.
- **Campus Activities**: Stay updated with activities happening in your campus.

## Technologies Used

- **Backend**: Spring Boot, Java
- **Security**: Spring Security with JWT
- **Database**: MySQL or PostgreSQL
- **Build Tool**: Maven
- **API Documentation**: Not Available

## Installation

### Prerequisites

- **Java**: JDK 17 or higher
- **Maven**: 3.6.0 or higher
- **Database**: Installed and running (e.g., MySQL, PostgreSQL)
- **Git**: For cloning the repository

# Getting Started with Campus Link

To get your Campus Link application up and running, follow these steps:


```bash
1. git clone https://github.com/sanket-arch/campuslink-backend.git
2. cd campuslink
3. Refer to the .env-sample file included in the project for the required environment variables. Create a .env file in the root directory of your project and set the variables accordingly.
4. Start spring boot application
```
## Usage

### Accessing the Application

Once the application is running, you can access it via `http://localhost:8080`.

### Authentication

Campus Link employs JWT (JSON Web Token) authentication to secure its endpoints. Here’s how it works:

#### User Login

Users authenticate by sending their credentials (username and password) to the `/api/auth/login` endpoint. Upon successful authentication, a JWT token is issued.

#### Accessing Protected Endpoints

The JWT token must be included in the `Authorization` header as a Bearer token for accessing any protected endpoint.

**Example:**

```http
Authorization: Bearer your_jwt_token
```
### Token Validation

The server validates the token's authenticity and expiry before granting access to the requested resources.

### Example Workflow

***Login Request:***
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "student1",
  "password": "password123"
}
```
***Login Response:***
```http
{
    "Validity": "30 min",
    "Token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhaml0NjkiLCJpY.S-tgXU0Ci6QE"
}
```
***Accessing a Protected Endpoint:***
```http
GET /api/{resource}
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6...
```
## API Endpoints

***Base URL:*** `https://localhost:8080/api/`

### Authorization Header:

All requests must include an Authorization header with a Bearer token:
```http
Authorization: Bearer your_jwt_token
```
### Endpoints
1. ***Add a Resource***
   **`Endpoint:`** POST /api/{entityname}/add

**`Description:`** Add a new resource.

**`Request Body:`** Example for a student entity
```json
{
  "firstName": "Ajit",
  "lastName": "Kumar",
  "userName": "ajit69",
  "profilePicture": "",
  "phoneNumber": 8704587535,
  "email": "ajit.kumar@vitstudent@ac.in",
  "regNo":"22MCA0144",
  "passingYear":"2024",
  "course":{
        "id":"1"
  },
  "campus":{
    "id":"1"
  },
  "role": {
    "id": "1"
  },
  "password":"sanket4@"
}
```
**`Response Body:`** Example for student entity
```http
201 Created: Student saved successfully with registration number {regNo}.
400 Bad Request: Invalid data.
500 Internal Server Error.
```
2. ***Update a Resource***
  
**`Endpoint:`** PUT /api/{entityname}/update

**`Description:`**  Update an existing resource..

**`Request Body:`** Example for a student entity
```json
{
  "userId":"4",
  "firstName": "Sanket",
  "lastName": "Kumar",
  "userName": "Sanket45",
  "profilePicture": "",
  "phoneNumber": 8709687325,
  "email": "megha.shree@vitstudent@ac.in",
  "regNo":"22MCA086",
  "passingYear":"2024",
  "course":{
    "id":"1"
  },
  "campus":{
    "id":"1"
  },
  "role": {
    "id": "1"
  }
}
```
**`Response Body:`** Example for student entity
```http
200 OK: Student updated successfully with registration number {regNo}.
400 Bad Request: Invalid data.
500 Internal Server Error.
```
3. ***Delete a Resource***
   
**`Endpoint:`** DELETE /api/{entityname}/delete?id=1

**`Description:`** Delete an existing resource.

**`Request Param:`** Example for a student entity
    `id = 1`

**`Response Body:`** Example for student entity
```http
200 OK: Student removed successfully with registration number {regNo}.
400 Bad Request: Invalid data.
500 Internal Server Error.
```
4. ***Get a Resource***
   
**`Endpoint:`** GET /api/{entityname}/

**`Description:`** Add a new resource.

**`Request Parameters:(optional)`** Example for a student entity

    id: The ID of the resource to fetch.
**`Response Statuses`**

```
200 Created: The resource(s) were successfully retrieved.
401 Unauthorized.
500 Internal Server Error.
```
**`Response Body:`** Example for student entity

```json
{
    "userId": 1,
    "firstName": "Sanket",
    "lastName": "Kumar",
    "userName": "sanket45",
    "password": "sanket4@",
    "phoneNumber": 8709687535,
    "email": "sanket.kumar@vitstudent@ac.in",
    "profilePicture": "",
    "role": {
        "id": 1,
        "name": null,
        "roleCode": null,
        "description": null
    },
    "campus": {
        "id": 1,
        "campusName": null,
        "campusDescription": null,
        "campusAddress": null,
        "link": null
    },
    "regNo": "22MCA0143",
    "passingYear": 2024,
    "course": {
        "id": 1,
        "courseName": null,
        "courseCode": null,
        "description": null
    }
}
```
## Contributing

We welcome contributions to this project! If you have an idea for a new feature, a bug fix, or any improvement, please follow the steps below:

### 1. Fork the Repository
- Click the "Fork" button at the top right of this repository's page.
- This will create a copy of the repository under your own GitHub account.

### 2. Clone Your Forked Repository
- Clone the forked repository to your local machine using the command:
  ```bash
  git clone https://github.com/sanket-arch/campuslink-backend.git
    ```
### 3. Create a New Branch
- Create a new branch for your feature or bug fix
   ```bash
    git checkout -b feature-or-bugfix-name
    ```
### 4. Make Your Changes
- Implement your feature, bug fix, or other improvements.
- Ensure that your code follows the project's coding standards.
- Write or update tests if necessary.

### 5. Commit Your Changes
- Add and commit your changes with a meaningful commit message:
    ```bash
    git add .
    git commit -m "Brief description of your changes"
     ```
### 6. Push Your Changes
- Push your branch to your forked repository:
    ```bash
        git push origin feature-or-bugfix-name
    ```
### 7. Create a Pull Request
- Go to the original repository on GitHub.
- Click on the "Pull Requests" tab.
- Click the "New Pull Request" button.
- Select your branch from the "Compare" dropdown and the main branch of the original repository from the "Base" dropdown.
- Add a title and description for your pull request, then click "Create Pull Request."

### 8. Participate in the Review Process
- Your pull request will be reviewed by the maintainers.
- You may be asked to make changes; please address any feedback promptly.
- Once approved, your pull request will be merged into the main branch.

### 9. Celebrate 🎉
- Your contribution is now part of the project! Thank you for your help.

## Contact

If you have any questions, suggestions, or would like to connect, feel free to reach out via the following platforms:

- **WhatsApp:** [Click here to chat](https://wa.me/8709687535)
- **GitHub:** [Your GitHub Profile](https://github.com/sanket-arch)
- **LinkedIn:** [Your LinkedIn Profile](https://www.linkedin.com/in/sanket-kumar-525652210/)

We look forward to hearing from you!
