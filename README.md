Laurea-projet
=============

Study unit : Information Security Development Project II

This project has been made to demonstrate how to exchange encrypted and anonimous messages between different OS (Windows, Mac OS, Linux, Android).

The project is divided in 3 parts :

- the server
- the desktop client app
- the Android app

## Requirements ##

To compile and execute the project, you need to install different tools

- Node.js : http://nodejs.org/download/
- Eclipse : https://www.eclipse.org/downloads/
- Java developper kit 7 : http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html
- Android SDK : http://developer.android.com/sdk/index.html

## Compile ##

### Desktop app ###

- Import the project in eclipse
- Modify the file `desktop/src/laurea_project/ContactWindow.java` to put the right address of your server
- run

### Android app ###

- Import the project in the Android SDK
- Modify the file `android/Laurea-Project/src/fr/ligol/laurea_project/MainActivity.java` to put the right address of your server
- run

## Execution ##

Before tu use the client apps, you have to run the server. All the encrypted messages will transit on the server.

In a command line :
```sh
  $> node app.js
```
