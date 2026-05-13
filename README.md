## Quick start

1) Start the database (from the project root):

```shell
docker-compose up -d
```

2) Run the backend API (from the project root):

```shell
./mvnw spring-boot:run
```

3) Run the frontend (from the frontend folder):

```shell
cd frontend
npm install
npm run dev
```

Database access (optional): localhost:5432 with database/user/password "postgres".