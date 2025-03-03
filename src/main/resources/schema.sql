create table customer(
    id bigint auto_increment not null,
    name varchar(50) not null,
    surname varchar(50) not null,
    credit_limit decimal(15,2) not null,
    used_credit_limit decimal(15,2) not null
);

create table loan(
    id bigint auto_increment not null,
    customer_id bigint not null,
    loan_amount decimal(15,2) not null,
    number_of_installment integer not null,
    create_date timestamp not null,
    is_paid boolean not null
);

create table loan_installment(
    id bigint auto_increment not null,
    loan_id bigint not null,
    amount decimal(15,2) not null,
    paid_amount decimal(15,2) not null,
    due_date timestamp not null,
    payment_date timestamp,
    is_paid boolean not null
);

insert into customer values (1,'Turgay','Serdaş',10000,2000);