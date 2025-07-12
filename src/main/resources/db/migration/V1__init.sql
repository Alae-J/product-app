CREATE TABLE products (
                          id SERIAL PRIMARY KEY,
                          title TEXT NOT NULL,
                          price NUMERIC,
                          variant JSONB
);
