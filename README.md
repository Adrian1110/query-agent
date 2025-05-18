# AI SQL Query Agent 

An AI-powered Spring Boot service that transforms **natural language rules into executable SQL queries** using OpenAI (GPT-4) and dynamic schema introspection.

## Why?

Inspired by Satya Nadella's vision that CRUD apps will be replaced by AI agents, this project explores how AI can:
- Understand relational data structures
- Translate business rules into SQL
- Retry and self-correct based on query validation errors

## Features

- Natural language ➜ SQL query generation
- Spring Boot + Hibernate introspection
- OpenAI GPT-4 integration via Spring AI
- Smart retry mechanism (up to 3 times) with error feedback
- Supports PostgreSQL & H2
- Returns only valid, executable SQL (validated using `EXPLAIN`)

## Example Prompts

```
"Give me all client names"
"Show total hours worked per client"
"Who worked the most hours in April for 'Random client'?"
"Compare revenue between 2023 and 2024 by client and department"
```

## Tech Stack

- Spring Boot 3
- Spring AI (OpenAI GPT-4)
- Hibernate + JPA
- PostgreSQL / H2
- Jackson for JSON generation

## 🛠️ Run Locally

```bash
# Build
mvn clean install

# Run
./mvnw spring-boot:run
```
---

Made with 💡 and curiosity.
