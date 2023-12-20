# Scheduling application for fitness center

This application enables scheduling of group classes led by trainers in a fitness center. It keeps track of class
capacity and when the class starts and ends. Before adding new class, the app checks if all the trainers are available
at given time
and if the room capacity is not exceeded at any point, because of other classes in the same room at the same time.

It stores first name, last name, email and phone number of all users as well as their username and password.
Classes are also categorized by sport.

## How to start up the app

### Requirements

Tools: gradle, npm

This app is divided into frontend and backend, so you need to clone two repositories:

1. https://gitlab.fit.cvut.cz/svoboi11/tjv-fitness-center.git
2. https://gitlab.fit.cvut.cz/svoboi11/tjv-fitness-center-fe.git

### Backend start-up

After cloning repository, you simply run "./gradlew bootRun".
Your system might say, It's only 80 % done, but it is working.

If you prefer, starting the backend from Idea or other editor is okay too.

Now swagger is available on: http://localhost:8080/swagger-ui/index.html#/

### Frontend start-up

After cloning repository, run "npm install" and then "npm run dev".
Then use the link npm gives you.
