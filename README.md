# Social Network APIs
- Social Network APIs used as backend service for a Social Network Application in real life.
- It serves kinds of socket services below.
  - Messaging.
  - Post Uploading.
  - Comment And Reply.
  - Group Organization.
  - Emotion.
- And full RESTful services.
## Technology Stack
- **Architecture**: Layer Architecture, Restful APIs, STOMP.
- **Framework**: Spring boot, Spring Security, Spring Data JPA.
- **Database**: MySQL.
- **Caching technology**: Redis.
- **Containerization & Deployment**: Docker.
- **Other Technologies**:Java Mail, Cloundinary APIs, Spring OAuth2-client dependency.
## Getting Started
### Dependencies
- Java 21+
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- Redis
- Docker (optional), you can run this project by your local maven
### Environment Setup
When all your dependencies are ready,turn to enviroment setup step.
Set up all your properties as my one and create a `.env` file follow by below properties.

- oauth2.client.google.client-id=your client id
- oauth2.client.google.client-secret=your client secret

***Register oauth2 Google credentials at https://console.cloud.google.com/auth/clients***
- oauth2.client.facebook.client-id=your facebook id
- oauth2.client.facebook.client-secret=your facebook client secret

***Register oauth2 Meta credentials at https://developers.facebook.com/apps***
- databaseUrl=jdbc:mysql://localhost:3306/social_network_project
- databaseUsername=your username
- databasePassword=your secret
- jwt.access.key=your key
- jwt.refresh.key=your key
- jwt.reset.key=your key
- cloudinary.name=your cloud name
- cloudinary.api_key=your api key
- cloudinary.api_secret=your secret
- redisHost=redis host
- redisPassword=redis password
- redisPort=redis port
- mail.username=your username 
- mail.password=mail application password
***Generate application password that used for JavaMail service at https://myaccount.google.com/apppasswords***


### Running the service
You can run this service by several ways
- Run by maven command (mvn spring-boot:run)
- Run in your IDE (vscode, IntellIJ,..)
- Run in docker container


## Project Structure
```text
back-end-app/
├── src/
│ └── main/
│ ├── java/
│ │ └── com/
│ │ └── hien/
│ │ └── back_end_app/
│ │ ├── config/                     # Project configurations
│ │ │ ├── security/                 # Security-related configuration
│ │ │ │ ├── handlers/               # Handlers for authentication & authorization
│ │ │ │ ├── oauth2/                 # OAuth2 configuration for social login
│ │ │ │ │ └── models/               # OAuth2 models used in security
│ │ │ │ └── securityModels/         # Security Models for Spring Security Context
│ │ ├── controllers/                # Controller Layer (API endpoints)
│ │ ├── dto/                        # Data Transfer Objects
│ │ ├── entities/                   # Entity classes (database models)
│ │ ├── exceptions/                 # Exception handling classes
│ │ ├── mappers/                    # Mapper utilities (Entity <-> DTO)
│ │ ├── repositories/               # Data Access Layer (JPA Repositories)
│ │ ├── services/                   # Service Layer (Business logic)
│ │ ├── utils/                      # Utility classes
│ │ └── BackEndAppApplication       # Main Spring Boot Application file
│ └── resources/
│ └── application.properties        # Application environment properties
```




