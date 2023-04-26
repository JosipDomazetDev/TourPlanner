# Setup

```bash
# Start the container
docker-compose up -d

# Create the db mtcg
docker exec <CONTAINER-ID> createdb -U josip tourplanner

# Run the init.sql script to create the tables (or restore the dump)
docker exec -i <CONTAINER-ID> psql -U josip -d tourplanner < init.sql

# To create a dump
docker exec <CONTAINER-ID> pg_dump -U josip tourplanner > dump.sql
```
