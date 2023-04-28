# Setup

```bash
# Start the container
docker-compose up -d

# Create the db mtcg
docker exec <CONTAINER-ID> createdb -U josip tourplanner
```

Create a `config.properties` file in the root directory and add the following variables:

```bash
api_key=YOUR_API_KEY
```
