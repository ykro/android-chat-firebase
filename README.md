# android-chat-firebase

Adaptado a la version 9.0.2 de Firebase.

Instrucciones: https://firebase.google.com/docs/android/setup#add_firebase_to_your_app
Add Firebase to your app

It's time to add Firebase to your app. To do this you'll need a Firebase project and a Firebase configuration file for your app.

1. Create a Firebase project in the Firebase console, if you don't already have one. If you already have an existing Google project associated with your mobile app, click Import Google Project. Otherwise, click Create New Project.
2. Click Add Firebase to your Android app and follow the setup steps. If you're importing an existing Google project, this may happen automatically and you can just download the config file.
3. When prompted, enter your app's package name. It's important to enter the package name your app is using; this can only be set when you add an app to your Firebase project.
4. **At the end, you'll download a google-services.json file. You can download this file again at any time.**
5. **If you haven't done so already, copy this into your project's module folder, typically app/.**

Errores conocidos:
- Crash de la aplicación al cerrar sesión.
