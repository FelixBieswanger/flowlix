# flowlix -  a smartwatch application to measure flow in real-time.

<div style="margin: 0 auto; width: 100%;display:flex;">
<img width="350" alt="flowlix_logo" src="https://user-images.githubusercontent.com/40392565/218503760-4daf25ee-4fc9-4d08-af63-68b94e60d5e0.jpg" />
</div>

\
  

This repo contributes to the seminar "Seminar Digital Platforms, Markets & Work" of KIT in the winter semester 2022/23. In this project, a smartwatch app is to be developed, which should enable the effortless recording of the flow state.
 

Flowlix is a smartwatch application developed to measure the user's experience of flow, a state of mind that leads to a higher level of engagement and productivity. The project was implemented using the Android Studio development environment and consists of the following main components:

## Structure of the Project

The project consists of several packages, each serving a specific purpose in the overall functionality of the application. It is build using the Model-View-ViewModel Pattern. Here's a rundown of the major packages and their contents.


* Model Package \
The model package contains the implementation of the flowlix class, which serves as the main model for the application.


  * Data Package \
  This package contains all the data classes required by the application, such as the Activity, FlowUIState, Question, and MyTime classes.

  * schedule Package \
  The schedule package includes the implementation of the Scheduler and Schedule classes, which serve as the components responsible for scheduling the notifications to be sent to the user.


  * resources Package \
  This package includes all the resources used by the application, such as io and the implementation of the QuestionProvider class, which serves as the data provider for the questions used in the experiment. \
  Currently the questions consinst of the Flow Short Scale. To adapt the questions to another study, please use the [QuestionProvider file](app/src/main/java/com/example/flowlix/model/resources/QuestionProvider.kt). The UI will then adapt dynamically.
  
  * notification Package \
  The notification package includes the implementation of the ActionReceiver and SendNotificationWorker classes, which serve as the components responsible for sending notifications to the user.


* viewmodel Package \
This package includes the implementation of the FlowViewModel class, which serves as the primary view model for the application.

* View Packacke \
This package includes the implementation of the various screens that make up the user interface of the application, such as the MainActivity, presentation, and theme classes.









### Purpose of the Application

The purpose of the Flowlix application is to provide a simple and intuitive way to measure a user's flow experience while using a smartwatch. The application sends periodic notifications to the user throughout the day, asking them to complete a quick questionnaire about their current flow state. The results of the questionnaire are then analyzed to gain insights into the user's flow experience.

### Using the Application

To use the Flowlix application, simply load it on to your watch using Android Studio and follow the instructions provided by the application. You will be asked to complete a short questionnaire several times throughout the day, and your responses will be used to determine your flow state at any given moment. The results of the questionnaire can be analyzed to gain insights into your flow experience, helping you to better understand how to improve your level of engagement and productivity.
 
 
 
 
 
 

