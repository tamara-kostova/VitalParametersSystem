import psycopg2

# Database connection parameters
conn = psycopg2.connect(
    dbname='postgres',
    user='postgres',
    password='metallica',
    host='localhost',
    port='5432'
)
conn.autocommit = True

# Open a cursor to perform database operations
with conn.cursor() as cursor:
    # Read SQL file
    with open('create_tables.sql', 'r') as file:
        sql = file.read()

    # Execute SQL commands
    cursor.execute(sql)

# Close the connection
conn.close()
