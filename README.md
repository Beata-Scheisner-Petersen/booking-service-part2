# Booking System

A microservice-based hotel booking application built with Spring Boot.

The system consists of three services:

* **Booking Service** – main service and gateway for the application
* **Customer Service** – manages customers and customer-related functionality
* **Review Service** – manages reviews and ratings


## Startup
- 1. The service(s) is started using either Docker of Kubernetes from one of the commands below.
- 2. After the service(s) have started the frontend can be reached in a webrowser using ```http://localhost:8082```
- Obs. Kubernetes needs booking-service to be port-forwarded using port 8082 for the frontend to be reachable in the browser.
<br></br>
- Kubernetes: ```kubectl apply -f k8s/```
- Docker: ```docker compose up -d --build```
## Shutdown
- To close/delete your started Kubernetes/Docker container/pods use the commands below
<br></br>
- Kubernetes ```kubectl delete -f k8s/```
- Docker ```docker compose down```

## Architecture

```text
                    Client / Frontend
                           |
                           v
                    Booking Service
                       :8082
                      /     \
                     /       \
                    v         v
          Customer Service   Review Service
               :8081             :8083
                 |                 |
                 v                 v
               MySQL             MySQL

                    |
                    v
             Booking Database
                  MySQL
```

## Booking Service

Booking Service is the main entry point to the application.

It handles booking-related functionality and communicates with the other microservices when customer or review information is needed.

Main functionality:

* Search for available rooms
* Create reservations
* Update reservations
* Cancel reservations
* Retrieve reservations
* Communicate with Customer Service
* Communicate with Review Service
* Serve the frontend
* Act as the gateway between the frontend and the microservices

Example communication:

```text
Frontend
   |
   v
Booking Service
   |
   +------> Customer Service
   |
   +------> Review Service
   |
   +------> Booking Database
```

## Customer Service

Customer Service is responsible for customer-related functionality.

Main functionality:

* Customer registration
* Customer information
* Customer validation
* Authentication
* Customer database operations

Booking Service communicates with Customer Service through REST API calls.

Example:

```text
Booking Service
      |
      | Check if customer exists
      v
Customer Service
```

## Review Service

Review Service is responsible for reviews and ratings.

Main functionality:

* Create reviews
* Retrieve reviews
* Store ratings
* Connect reviews to rooms or bookings

Booking Service communicates with Review Service through REST API calls.

Example:

```text
Booking Service
      |
      | Get reviews / ratings
      v
Review Service
```

## Technologies

* Java
* Spring Boot
* Spring Data JPA
* Spring Security
* REST API
* JWT
* MySQL
* Maven
* Docker
* Kubernetes
* HTML
* CSS
* JavaScript

## Kubernetes

```text
booking-service
customer-service
review-service
```

Example:

```bash
kubectl apply -f booking-service.yaml
kubectl apply -f customer-service.yaml
kubectl apply -f review-service.yaml
```

Check pods:

```bash
kubectl get pods
```

Check services:

```bash
kubectl get services
```

Check logs:

```bash
kubectl logs <pod-name>
```

## Ports

| Service          | Port |
| ---------------- | ---: |
| Customer Service | 8081 |
| Booking Service  | 8082 |
| Review Service   | 8083 |

## Docker

Each microservice has its own Docker image.

Example for Booking Service:

```bash
docker build -t booking-service:1 .
```

The services can then be deployed as separate containers or Kubernetes pods.

## Environment Variables

Sensitive configuration is provided through environment variables or Kubernetes Secrets.

Example:

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
JWT_SECRET
```

Passwords and secrets should not be committed to Git.

## Service Communication

The services communicate using REST APIs.

```text
                 Booking Service
                 /             \
                /               \
               v                 v
      Customer Service       Review Service
```

Booking Service acts as the central service that connects the frontend with the other microservices.
