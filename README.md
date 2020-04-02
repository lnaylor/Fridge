# Fridge
RESTful API microservice for tracking/updating the fridge contents

To run from IDE:
FridgeApplication.java -> main -> Run as Java Application
Or run
mvn package
java -jar Fridge*.jar

Then go to localhost:8080

Available users:

Admin: (Access to view metrics)
Username: a
Password: a

User:
Username: u
Password: u

As a logged in user, there are two fridges available. In each fridge, users can add, delete, or update items. When updating an item, users may change the amount or name.

The soda constraint was taken to mean that there can be no more than a total of 12 cans of soda between both fridges, though it was not entirely clear if that was the intent of the given requirement. It was also not specified what the behavior should be if the user attempts to add more soda than is allowed. In the current implementation, if the user attempts to add more soda than is allowed, the fridge will add the maximum number of soda cans that it can while meeting the constraint. I.e., if there are 10 cans of soda between the two fridges, and a user attempts to add 5 cans of soda, then only 2 cans of soda will be added.

Some potential future enhancements:<br/>
-Update the UI to look nice<br/>
-Update logging to send performance logs to their own file<br/>
-logic to autocorrect/ask user if an added item that has a “similar” name to an existing item was meant to be the same as the existing item
