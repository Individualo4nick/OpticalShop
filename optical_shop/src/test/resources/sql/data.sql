INSERT INTO users (id, login, email, name, surname, address) VALUES
            (1, 'user1', 'user1@example.com', 'John', 'Doe', '123 Main St'),
            (2, 'user2', 'user2@example.com', 'Jane', 'Smith', '456 Elm St'),
            (3, 'user3', 'user3@example.com', 'Michael', 'Johnson', '789 Oak St'),
            (4, 'user4', 'user4@example.com', 'Emily', 'Brown', '321 Pine St'),
            (5, 'user5', 'user5@example.com', 'David', 'Wilson', '654 Maple St');

INSERT INTO products (id, title, description, count, category, price) VALUES
            (1, 'Title1', 'Description1', 10, 'Category1', 100),
            (2, 'Title2', 'Description2', 5, 'Category2', 200),
            (3, 'Title3', 'Description3', 8, 'Category1', 150),
            (4, 'Title4', 'Description4', 3, 'Category3', 300),
            (5, 'Title5', 'Description5', 0, 'Category2', 250);
SELECT SETVAL('products_id_seq', (SELECT MAX(id) FROM products));

INSERT INTO comments (id, product_id, user_id, text) VALUES
            (1, 1, 1, 'Text1'),
            (2, 2, 2, 'Text2'),
            (3, 3, 1, 'Text3'),
            (4, 4, 3, 'Text4'),
            (5, 5, 2, 'Text5');
SELECT SETVAL('comments_seq', (SELECT MAX(id) FROM comments));

INSERT INTO shopping_cart (id, user_id, product_id, count) VALUES
            (1, 1, 1, 1),
            (2, 2, 2, 4),
            (3, 1, 3, 1),
            (4, 3, 4, 8),
            (5, 2, 5, 1);