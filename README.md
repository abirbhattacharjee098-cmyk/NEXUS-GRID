# Nexus Grid - Distributed Computing Engine

## Prerequisites
- Java 17
- Maven
- PostgreSQL running on localhost:5432 (Database `nexus_grid`, Username `postgres`, Password `password`)

## Running the Application
1. **Start Master Node:**
   ```bash
   cd master-node
   mvn spring-boot:run
   ```
2. **Start Worker Node(s):**
   Open a new terminal and run:
   ```bash
   cd worker-node
   mvn spring-boot:run
   ```
   *You can repeat step 2 in multiple terminals to spin up multiple workers. They will bind to random ports.*

## Testing End-to-End
Submit a job using `curl` or Postman:
```bash
curl -X POST http://localhost:8080/api/jobs/submitJob \
-H "Content-Type: application/json" \
-d '{"jobType": "DATA_PROCESSING", "payload": "Process User Data"}'
```

Watch the master node console split the job into tasks, and the worker node consoles pull, execute, and return the result back to master! Also, try killing a worker node to see the master node detect it as DEAD and reassign its tasks to other available workers.
