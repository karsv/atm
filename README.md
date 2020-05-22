# Back-end part of ATM (REST)
Front-end part (https://github.com/karsv/atmView)
Stack: springboot, spring jpa, java 11, h2, spring security, Mockito, JUnit, log4j2, Lombok;

Here implemented basic logic of ATM: 
- refill ATM (http://localhost:8081/atm/push-cash-to-atm); _(ADMIN)_
- transfer money from one account to another account (http://localhost:8081/atm/transfer-money); _(USER)_
- put money on the account (http://localhost:8081/atm/deposit-money); _(USER)_
- check status of all ATMs, where shows available money on each ATM (http://localhost:8081/atm/); _(USER)_
- withdraw money (http://localhost:8081/atm/withdraw-money); _(USER)_
- check account (http://localhost:8081/account/account-status); _(USER)_

In this project realised Basic authentication with two roles (ADMIN, USER). 
Login (http://localhost:8081/login/) and registration (http://localhost:8081/registration/) are present in this project also.
New user registrates with _USER_ role by default.

When this project starts it initialised with next parameters:
1. ADMIN
login: admin
password: 123
2. USER
login: user
password: 123
with two accounts:
 - id=1 (amount of money = 100000.11) 
 - id=2 (amount of money = 0)
 
3.Two ATMs
 - id=1
   NOTE 100 = 10
   NOTE 200 = 10
   NOTE 500 = 10
 - id=2
   NOTE 100 = 123
   NOTE 200 = 123
   NOTE 500 = 123
