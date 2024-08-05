CREATE TABLE vacation_claim (
       id UUID PRIMARY KEY,
       created_by VARCHAR(64) not null,
       status VARCHAR(16) not null,
       description TEXT,
       created_at timestamp not null,
       started_at date not null,
       finished_at date not null
);