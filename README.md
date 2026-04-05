# ⚡ NEXUS GRID — Distributed Computing Engine

## Overview

NEXUS-GRID is a robust distributed computing engine designed to efficiently parallelize workloads, ensure fault tolerance through automatic node failure handling, and deliver real-time analytical results. This system is ideal for tasks requiring high-throughput processing and resilient execution across multiple nodes.

## Key Highlights

*   **Distributed Web Scraping**: Capable of distributing web scraping tasks across multiple worker nodes for enhanced performance and coverage.
*   **Parallel Execution**: Utilizes a master-worker architecture to enable parallel processing of tasks, significantly reducing execution time for large workloads.
*   **Fault Tolerance**: Implements automatic task reassignment mechanisms to ensure continuous operation even in the event of worker node failures.
*   **Real-time Data Aggregation**: Facilitates the aggregation of processed data in real-time, enabling immediate insights and decision-making.

## Real Use Case: Distributed Web Scraping

The system was successfully employed to scrape **10 websites in parallel** for an in-depth analysis of football statistics, specifically comparing mentions of Lionel Messi and Cristiano Ronaldo.

### Results

| Metric                | Value |
| :-------------------- | :---- |
| Messi Mentions        | 1311  |
| Ronaldo Mentions      | 1252  |
| Total Goal Mentions   | 878   |
| Total Assist Mentions | 59    |

### Verdict

**Messi dominates the conversation** in the analyzed data.

## Demo Output

![Distributed Scraping Result](./docs/demo.png)

## Architecture

The NEXUS-GRID architecture follows a classic master-worker pattern:

```
CLIENT → MASTER NODE → WORKER NODES
```

*   **Master Node**: Responsible for task scheduling, coordination, and overall job management.
*   **Worker Nodes**: Execute tasks in parallel as assigned by the Master Node.
*   **Common Lib**: Contains shared communication models and data transfer objects used across the system.

## How It Works

1.  A client submits a job to the Master Node.
2.  The Master Node splits the job into smaller, manageable tasks.
3.  A scheduler assigns these tasks to available worker nodes.
4.  Worker nodes execute their assigned tasks in parallel.
5.  Results from worker nodes are aggregated by the Master Node.
6.  In case of worker failure, failed tasks are automatically reassigned to other available workers, ensuring fault tolerance.

## How to Run

### Prerequisites

*   Java 17
*   Maven
*   PostgreSQL running on `localhost:5432` with:
    *   Database: `nexus_grid`
    *   Username: `postgres`
    *   Password: `password`

### Start Master Node

```bash
cd master-node
mvn spring-boot:run
```

### Start Worker Node(s)

```bash
cd worker-node
mvn spring-boot:run
```

Run multiple instances of the worker node to simulate a distributed environment.

### Submit Job

```bash
curl -X POST http://localhost:8080/api/jobs/submitJob \
-H "Content-Type: application/json" \
-d '{"jobType":"SCRAPE","payload":"messi ronaldo stats"}'
```

## Demo Scenario

To observe the fault-tolerance mechanism:

*   Start multiple worker nodes.
*   Submit a job.
*   Manually terminate one of the worker nodes.
*   Observe that the system continues execution without interruption, reassigning tasks from the failed worker.

## Tech Stack

*   **Languages**: Java (Core + Advanced)
*   **Frameworks**: Spring Boot
*   **Databases**: PostgreSQL
*   **Containerization**: Docker
*   **Orchestration**: Kubernetes

## Project Structure

```
NEXUS-GRID/
├── master-node/         # Master node application
├── worker-node/         # Worker node application
├── common-lib/          # Shared libraries and communication models
├── kubernetes/          # Kubernetes deployment configurations
├── docs/                # Project documentation and assets
└── README.md            # Project overview and instructions
```

## Why This Project Matters

This project serves as an excellent demonstration of real-world system design principles applied in:

*   Cloud Computing
*   Distributed Systems
*   Big Data Processing

## Author

**Abirbhab Bhattacharjee**
Engineering Student | System Builder
