# ⚡ NEXUS GRID — Distributed Computing Engine

A fault-tolerant, scalable distributed computing system built in Java that simulates real-world cloud infrastructure using a master-worker architecture.

---

## 🚀 Overview

NEXUS GRID is designed to execute large-scale computational tasks by distributing workloads across multiple worker nodes.

It enables:

* ⚡ Parallel execution of tasks
* 🧠 Intelligent scheduling
* 🔁 Automatic failure recovery
* 📈 Horizontal scalability

---

## 🧠 Architecture

```
          CLIENT
             ↓
      MASTER NODE
   (Scheduler + Brain)
      ↓     ↓     ↓
   Worker Worker Worker
```

---

## ⚙️ Core Features

### 🔹 Distributed Task Execution

Breaks large jobs into smaller tasks and executes them across multiple worker nodes in parallel.

### 🔹 Intelligent Scheduler

Assigns tasks dynamically based on worker availability and system load.

### 🔹 Fault Tolerance

Automatically detects worker failure and reassigns tasks without stopping execution.

### 🔹 Heartbeat Monitoring

Continuously monitors worker health using periodic signals.

### 🔹 Scalable Design

Supports adding new worker nodes without modifying the system.

---

## 🚀 Real Use Case: Distributed Web Scraping

The system was used to scrape **10 websites in parallel** to analyze football statistics (Messi vs Ronaldo).

### 📊 Aggregated Results

| Metric                | Value |
| --------------------- | ----- |
| Messi Mentions        | 1311  |
| Ronaldo Mentions      | 1252  |
| Total Goal Mentions   | 878   |
| Total Assist Mentions | 59    |

### 🏆 Verdict

**Messi dominates the conversation**

---

## 📸 Demo Output

> 📌 <img width="323" height="564" alt="image" src="https://github.com/user-attachments/assets/2811682e-8280-46b0-9b36-6b6fb8b28327" />

![Distributed Scraping Result]

---

## 🔄 Execution Flow

1. Client submits job
2. Master splits job into tasks
3. Scheduler assigns tasks to workers
4. Workers execute tasks in parallel
5. Results are aggregated
6. Failed tasks are reassigned automatically

---

## 🛠️ Tech Stack

* Java (Core + Advanced)
* Spring Boot
* REST APIs
* PostgreSQL
* Docker
* Kubernetes

---

## 📂 Project Structure

```
NEXUS-GRID/
├── master-node/       # Central coordinator
├── worker-node/       # Task executors
├── common-lib/        # Shared DTOs & models
├── kubernetes/        # Deployment configs
├── plans/             # System blueprint
├── docs/              # Screenshots & documentation
├── docker-compose.yml
└── README.md
```

---

## 💣 Demo Scenario

* Start multiple worker nodes
* Submit a distributed job
* Terminate one worker manually
* Observe:

  * Task reassignment
  * System continues execution

---

## 🧩 Future Enhancements

* Auto-scaling worker nodes
* AI-based task scheduling
* Real-time monitoring dashboard
* Distributed file storage

---

## 🎯 Why This Project Matters

This project demonstrates real-world system design concepts used in:

* Cloud Computing Platforms
* Distributed Systems
* Big Data Processing Engines

---

## 👤 Author

**Abirbhab Bhattacharjee**
Engineering Student | Distributed Systems Enthusiast

---
