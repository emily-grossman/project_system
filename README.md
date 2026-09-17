   # Запустить пустой postgres
   docker run -d --name project-system-db -p 5433:5432 -e POSTGRES_PASSWORD=1234 postgres:16-alpine
   
   # Накатить дамп
   docker cp db/db_dump.sql project-system-db:/tmp/db_dump.sql
   docker exec project-system-db psql -U postgres -d postgres -f /tmp/db_dump.sql
