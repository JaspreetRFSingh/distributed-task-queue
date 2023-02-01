# Distributed Task Queue System

A production-grade distributed task queue system built with Spring Boot, Redis, and RabbitMQ.

## Features

- **Task Prioritization**: Priority-based task execution
- **Retry Policies**: Configurable retry with exponential backoff
- **Dead Letter Queues**: Failed tasks are moved to dead letter queue for inspection
- **Horizontal Scaling**: Multiple workers can process tasks concurrently
- **Monitoring Dashboard**: Real-time metrics and task tracking
- **Scheduled Tasks**: Support for delayed task execution

## Tech Stack

- Java 17
- Spring Boot 2.7.9
- PostgreSQL 15
- Redis 7
- RabbitMQ 3.x
- Maven
- Docker & Docker Compose

## Getting Started

### Prerequisites

- Java 17+
- Docker & Docker Compose
- Maven 3.6+

### Running with Docker Compose

```bash
cd docker
docker-compose up -d
```

### Running Locally

```bash
mvn clean install
mvn spring-boot:run
```

## API Endpoints

### Tasks
- `POST /api/v1/tasks` - Create a new task
- `GET /api/v1/tasks/{taskId}` - Get task details
- `GET /api/v1/tasks` - List tasks by status
- `GET /api/v1/tasks/dead-letter` - List dead letter tasks
- `POST /api/v1/tasks/{taskId}/retry` - Retry a failed task
- `DELETE /api/v1/tasks/{taskId}` - Cancel a task

### Workers
- `POST /api/v1/workers` - Register a worker
- `POST /api/v1/workers/{workerId}/heartbeat` - Send heartbeat
- `GET /api/v1/workers` - List active workers
- `DELETE /api/v1/workers/{workerId}` - Mark worker offline

## Architecture

The system uses:
- RabbitMQ for message distribution
- Redis for caching and scheduled tasks
- PostgreSQL for persistent storage
- Spring Boot for the application framework

## License

MIT
