INSERT INTO public.account (id, cash_amount, food_amount, meal_amount, merchant, mcc) VALUES
    ('adcde641-a62e-4c26-aa96-bcad76dfa1c9', 100.00, 10.00, 5.00, 'Merchant A', '5411'),
    ('7dc7d93a-b2ca-432a-b484-60c995c18ce7', 200.00, 20.00, 10.00, 'Merchant B', '5412'),
    ('6ab92054-87a6-4add-8a7a-08bbaf1002fb', 100.00, 10.00, 5.00, 'Merchant C', '5811'),
    ('510aac4e-bbda-45c5-99b6-ff38aef05150', 200.00, 20.00, 10.00, 'Merchant D', '5812'),
    ('47930465-921f-4d1a-b625-d3b434485aa9', 150.00, 15.00, 7.50, 'Merchant E', '1234');

INSERT INTO public.transaction (account_id, amount, merchant, mcc) VALUES
    ('adcde641-a62e-4c26-aa96-bcad76dfa1c9', 1000.00, 'Merchant A', '5411'),
    ('7dc7d93a-b2ca-432a-b484-60c995c18ce7', 1.00, 'Merchant B', '5412'),
    ('6ab92054-87a6-4add-8a7a-08bbaf1002fb', 123.00, 'Merchant C', '5811'),
    ('510aac4e-bbda-45c5-99b6-ff38aef05150', 111.00, 'Merchant D', '0000'),
    ('47930465-921f-4d1a-b625-d3b434485aa9', 22.00, 'Merchant E', '0000');