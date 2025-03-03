[![LinkedIn][linkedin-shield]][linkedin-url]


<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/tserdas/credit-module">
    <img src="src/main/resources/static/logo.png" alt="Logo" width="120" height="80">
  </a>

<h3 align="center">Loan API</h3>

  <p align="center">
    To store and process loan applications
    <br />
    <a href="https://github.com/tserdas/credit-module"><strong>Explore the docs »</strong></a>
    <br />
    <br />
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li><a href="#installation">Installation</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#info">Info</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->

## About The Project

This project focuses on developing a backend Loan API for a banking system, enabling bank employees to manage and
process customer loans. The API provides endpoints for creating, listing, and paying loans, offering a seamless way for
employees to handle customer financials. Key functionalities include:

Create Loan: The API allows employees to create a new loan for a customer by specifying the loan amount, interest rate,
and number of installments. The system ensures that the customer has sufficient credit to take out the loan.
Installments are constrained to specific periods (6, 9, 12, or 24 months), and interest rates are restricted between 0.1
and 0.5. The total loan amount is calculated based on the provided amount and interest rate, and the due dates for
installments are set to the first day of each month.

List Loans: Employees can retrieve a list of loans associated with a given customer, with optional filters such as the
number of installments or whether the loan has been paid off.

List Installments: The API provides an endpoint to list the installments of a specific loan, giving a clear view of the
payment schedule.

Pay Loan: This endpoint enables employees to process installment payments for a loan. The system ensures that
installments are paid in full; partial payments are not allowed. If the payment amount exceeds the installment amount,
the excess is used to pay subsequent installments, with the earliest installments paid first. The API restricts payments
to installments that are due within the current month and the previous two months. A detailed response is returned,
including the number of installments paid, the total amount spent, and whether the loan is fully paid.

This backend service integrates with customer, loan, and installment data to ensure accurate financial tracking, and it
supports efficient loan management for bank employees.

### Built With

* Spring Boot 3.4.3
* Java 17
* H2 Database (in-memory)
* Spring Security
* Junit & Mockito

<!-- INSTALLATION -->

## Installation

***

Follow the steps below for installation and execution

* Clone

```bash
git clone https://github.com/tserdas/credit-module.git
cd credit-module
```

* Install dependencies

```bash
mvn clean install
```

* Run the application

```bash
mvn spring-boot:run
```

* After running, easy to reach the database like;

```bash
URL : http://localhost:8080/h2-console/login.jsp
JDBC URL: jdbc:h2:mem:loan
USER : sa
PASSWORD : password
```

<!-- ROADMAP -->

## Roadmap

***

### API Endpoints

| HTTP Method | Endpoint                    | Description                      | Request Body        | Response Body                            |
|-------------|-----------------------------|----------------------------------|---------------------|------------------------------------------|
| POST        | `/api/loan/create`          | Create a new loan for a customer | `CreateLoanRequest` | `CreateLoanResponse`                     |
| GET         | `/api/loan/{customerId}`    | Get loans by customer ID         | N/A                 | List of `GetLoanResponse` objects        |
| GET         | `/api/installment/{loanId}` | Get installments for a loan      | N/A                 | List of `GetInstallmentResponse` objects |
| POST        | `/api/installment/pay`      | Pay installments for a loan      | `PayLoanRequest`    | `PayLoanResponse`                        |

### API Basic Auth

| username | password   |
|----------|------------|
| admin    | `admin123` |

### Data Models

#### CreateLoanRequest

```json
{
  "customerId": "1",
  "loanAmount": 100,
  "interestRate": 0.38,
  "numberOfInstallments": 6
}
```

#### CreateLoanResponse

```json
{
  "loanId": 1,
  "totalAmount": 138.000,
  "numberOfInstallments": 6
}
```

#### GetLoanResponse

```json
[
  {
    "loanId": 1,
    "customerId": 1,
    "loanAmount": 138.00,
    "numberOfInstallment": 6,
    "createDate": "2025-03-03T23:33:02.040634+03:00",
    "paid": false
  }
]
```

#### GetInstallmentResponse

```json
[
  {
    "id": 1,
    "loanId": 1,
    "amount": 23.00,
    "paidAmount": 0.00,
    "dueDate": "2025-04-01",
    "paymentDate": null,
    "paid": false
  },
  {
    "id": 2,
    "loanId": 1,
    "amount": 23.00,
    "paidAmount": 0.00,
    "dueDate": "2025-05-01",
    "paymentDate": null,
    "paid": false
  },
  {
    "id": 3,
    "loanId": 1,
    "amount": 23.00,
    "paidAmount": 0.00,
    "dueDate": "2025-06-01",
    "paymentDate": null,
    "paid": false
  },
  {
    "id": 4,
    "loanId": 1,
    "amount": 23.00,
    "paidAmount": 0.00,
    "dueDate": "2025-07-01",
    "paymentDate": null,
    "paid": false
  },
  {
    "id": 5,
    "loanId": 1,
    "amount": 23.00,
    "paidAmount": 0.00,
    "dueDate": "2025-08-01",
    "paymentDate": null,
    "paid": false
  },
  {
    "id": 6,
    "loanId": 1,
    "amount": 23.00,
    "paidAmount": 0.00,
    "dueDate": "2025-09-01",
    "paymentDate": null,
    "paid": false
  }
]
```

#### PayLoanRequest

```json
{
  "loanId": 1,
  "amount": 150
}
```

#### PayLoanResponse

```json
{
  "paidInstallments": 3,
  "totalPaid": 64.90600,
  "paidCompletely": false
}
```

## Tables

```
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
```

<!-- INFO -->

## Info

* Project test coverage is %86
* The customer table should be filled manually because limit information is needed. The scenario could be that the
  customer onboarding process and the allocation process already fill this table. So customer 1 is manually saved while
  the project is running.
* Bonus 2 added to project but Bonus 1 not. Current version authorized with an admin user and password.

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555

[linkedin-url]: https://linkedin.com/in/turgayserdas
