# ⚡ NEXUS GRID — Distributed Computing Engine

A fault-tolerant, scalable distributed computing system built in Java that simulates real-world cloud infrastructure using a master-worker architecture.

---

## 🚀 Overview

NEXUS GRID is designed to execute large-scale computational tasks by distributing workloads across multiple worker nodes.

It ensures:

* Parallel task execution
* Fault tolerance (auto-recovery on node failure)
* Intelligent scheduling
* Scalable architecture

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

Breaks large jobs into smaller tasks and processes them across multiple worker nodes.

### 🔹 Intelligent Scheduler

Assigns tasks based on worker availability and system load.

### 🔹 Fault Tolerance

Automatically detects worker failure and reassigns tasks.

### 🔹 Heartbeat Monitoring

Continuously tracks worker health using periodic signals.

### 🔹 Scalable Design

Supports horizontal scaling with additional worker nodes.

---

## 🛠️ Tech Stack

* Java (Core + Advanced)
* Spring Boot
* REST APIs
* PostgreSQL
* Doc
