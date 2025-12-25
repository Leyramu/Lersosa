docker exec -it lersosa-postgresql-dev /bin/bash
psql -p 5432 -d postgres -U postgres -h 127.0.0.1
psql "host=127.0.0.1 dbname=postgres user=postgres sslmode=verify-full"
