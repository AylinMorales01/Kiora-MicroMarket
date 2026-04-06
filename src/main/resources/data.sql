INSERT INTO categories (name, description) VALUES ('Drinks', 'Beverages');
INSERT INTO categories (name, description) VALUES ('Snacks', 'Quick snacks');

INSERT INTO products (name, description, barcode, price, stock, active, category_id)
VALUES ('Coke', 'Soft drink', '123456', 1.5, 50, true, 1);
