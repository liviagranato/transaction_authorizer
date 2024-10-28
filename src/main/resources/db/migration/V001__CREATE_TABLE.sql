CREATE TABLE public.account (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    cash_amount DOUBLE PRECISION,
    food_amount DOUBLE PRECISION,
    meal_amount DOUBLE PRECISION,
    merchant VARCHAR(255),
    mcc VARCHAR(4)
);

CREATE TABLE public.transaction (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    account_id UUID NOT NULL,
    amount DOUBLE PRECISION,
    merchant VARCHAR(255),
    mcc VARCHAR(4),
    FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE
);