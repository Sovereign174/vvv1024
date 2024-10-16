Assumptions Made:

*****
Code is written in the form of a Spring Boot Rest Controller/Service
Core code is in src/../service/RentalService.java
JUnit Test Cases are in src/test/.../RentalControllerTest.java
*****

Clerks may give customers a discount that is applied to the total daily charges to reduce the final
 charge. (this could mean the discount is applied every day)
vs
Discount amount - calculated from discount % and pre-discount charge. Resulting amount
 rounded half up to cents.

Went with second calculation type/style, though they are probably identical.
 This would normally be clarified.

Assumed that a maximum checkout time exists and set it to 90 days.

Assumed that a rental period will not span multiple years,
 so holiday determination is done only on the year of checkout,
 but is easy to augment with given code and return a list of dates.

Assumed there are no special rules for when a check-in can be made, 
 so it is possible that a tool may need to be returned on a holiday.

Assumed Daily rental charge should display the pre-discount $ amount.
 
Solution includes the usage of a variety of constructs/coding styles as deemed appropriate.

Solution does not include code prettifying/auto-formatting/import organization plugin runs.

Test 1

Request has failed validation due to request discount percent: 101 being out of range: 0 - 100

Test 2

Tool Code: LADW

Tool Type: Ladder

Tool Brand: Werner

Rental Days: 3

Check out date: 07/02/20

Due date: 07/05/20

Daily rental charge: $1.99

Charge days: 2

Pre-discount charge: $3.98

Discount percent: 10%

Discount amount: $0.40

Final Charge: $3.58


Test 3

Tool Code: CHNS

Tool Type: Chainsaw

Tool Brand: Stihl

Rental Days: 5

Check out date: 07/02/15

Due date: 07/07/15

Daily rental charge: $1.49

Charge days: 3

Pre-discount charge: $4.47

Discount percent: 25%

Discount amount: $1.12

Final Charge: $3.35


Test 4

Tool Code: JAKD

Tool Type: Jackhammer

Tool Brand: DeWalt

Rental Days: 6

Check out date: 09/03/15

Due date: 09/09/15

Daily rental charge: $2.99

Charge days: 3

Pre-discount charge: $8.97

Discount percent: 0%

Discount amount: $0.00

Final Charge: $8.97


Test 5

Tool Code: JAKR

Tool Type: Jackhammer

Tool Brand: Ridgid

Rental Days: 9

Check out date: 07/02/15

Due date: 07/11/15

Daily rental charge: $2.99

Charge days: 5

Pre-discount charge: $14.95

Discount percent: 0%

Discount amount: $0.00

Final Charge: $14.95


Test 6

Tool Code: JAKR

Tool Type: Jackhammer

Tool Brand: Ridgid

Rental Days: 4

Check out date: 07/02/20

Due date: 07/06/20

Daily rental charge: $2.99

Charge days: 1

Pre-discount charge: $2.99

Discount percent: 50%

Discount amount: $1.50

Final Charge: $1.49
