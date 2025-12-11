Project Description: ATM Banking System (Java)

The ATM Banking System is a console-based Java application that simulates basic ATM operations. It allows users to perform essential banking tasks such as checking balance, depositing money, withdrawing money, and viewing a mini-statement. This project demonstrates the use of Object-Oriented Programming (OOP) concepts, ArrayList, and menu-driven programming.

📘 Project Overview

The project contains a BankAccount class that represents a user’s bank account. Each account includes:

Account Number

Holder Name

Current Balance

Last 5 Transactions (stored using ArrayList<String>)

The system displays a menu and allows the user to choose operations similar to a real ATM.

🛠️ Functionalities Implemented
1. Check Balance

Displays the current available balance in the user’s account.
Also stores "Checked balance" in the transaction history.

2. Deposit Money

Allows the user to deposit a valid amount.
Amount is added to the existing balance and the transaction is recorded.

Validation included:

Amount must be greater than zero.

3. Withdraw Money

Allows the user to withdraw money if the balance is sufficient.

Validation included:

Withdrawal amount must be greater than zero.

Sufficient balance must be available.

Successful withdrawals are recorded in the transaction list.

4. View Mini-Statement

Displays the last 5 transactions using an ArrayList<String>.
If the list exceeds 5 items, the oldest transaction is automatically removed.

5. Exit

Ends the ATM session gracefully.
