Feature: Other Expense

@OtherExpense @regression
Scenario Outline: Other Expenses - Positive Scenario

    Given User creates request data "<name>" , "<amount>", "<expenseDateTime>","<expenseNameID>" for OtherExpense API	
    And User submits POST request to "OtherExpense_URL" api
		And User validates if status code is 200
		Then User validate if value of name in response is "<responseName>"
		And User delete the expense with request to "Delete_URL"

    Examples:
     | name        | expenseNameID | expenseDateTime     | amount | responseName |
     | API				 | 1             | 12/31/2022 00:00:00 | 99.00  | Electricity  |
     | API	       | 2             | 12/31/2022 00:00:00 | 99.00  | Rent         |
     | API	       | 3             | 12/31/2022 00:00:00 | 99.00  | Gas          |
     | API	       | 4             | 12/31/2022 00:00:00 | 99.00  | API       	 |