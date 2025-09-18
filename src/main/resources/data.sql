-- Products
INSERT INTO product (name, price, stock, category, is_active, created_at)
VALUES ('Laptop', 1200.00, 10, 'Electronics', true, CURRENT_TIMESTAMP);

INSERT INTO product (name, price, stock, category, is_active, created_at)
VALUES ('Phone', 800.00, 20, 'Electronics', true, CURRENT_TIMESTAMP);

INSERT INTO product (name, price, stock, category, is_active, created_at)
VALUES ('Headphones', 150.00, 50, 'Accessories', true, CURRENT_TIMESTAMP);

INSERT INTO product (name, price, stock, category, is_active, created_at)
VALUES ('TV', 2000.00, 5, 'Electronics', true, CURRENT_TIMESTAMP);

INSERT INTO product (name, price, stock, category, is_active, created_at)
VALUES ('Keyboard', 70.00, 30, 'Accessories', true, CURRENT_TIMESTAMP);

-- Orders
INSERT INTO orders (customer_name, customer_email, order_date, status, total_amount)
VALUES ('John Doe', 'john@example.com', CURRENT_TIMESTAMP, 'PENDING', 2000.00);

INSERT INTO orders (customer_name, customer_email, order_date, status, total_amount)
VALUES ('Alice Smith', 'alice@example.com', CURRENT_TIMESTAMP, 'COMPLETED', 1270.00);

-- Order Items for Order 1
INSERT INTO order_item (order_id, product_id, quantity, unit_price, total_price)
VALUES (1, 1, 1, 1200.00, 1200.00);

INSERT INTO order_item (order_id, product_id, quantity, unit_price, total_price)
VALUES (1, 2, 1, 800.00, 800.00);

-- Order Items for Order 2
INSERT INTO order_item (order_id, product_id, quantity, unit_price, total_price)
VALUES (2, 3, 2, 150.00, 300.00);

INSERT INTO order_item (order_id, product_id, quantity, unit_price, total_price)
VALUES (2, 5, 1, 70.00, 70.00);

INSERT INTO order_item (order_id, product_id, quantity, unit_price, total_price)
VALUES (2, 2, 1, 800.00, 800.00);
