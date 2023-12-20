# Info for subject BI-TJV

As implemented complex request the application checks whether trainer is available or busy when adding trainer to class,
creating class
and updating class.
It also counts if capacities of all the classes in room at given time don't exceed capacity of the room.
Server looks up all the classes with given room in given timeframe and checks whether at any point the sum of capacities
of these classes are more than capacity of the room.

Business operation: Registering new user - client first sends request to check, whether given username isn't used
by another user already.
If it's taken, client notifies the user. If the username is available, client sends another request to create
user.

<img src="entity_diagram.png">
