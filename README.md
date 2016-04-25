### Weather Alarm Clock

### Building project
There are some dependencies that need to be added and configured before this project can be built:
 - Install and enable Android5.1.1 SDK (api level 22) by selecting **tools -> Android -> SDK Manager** from the Android Studio overhead menu bar.  This API is required for the  [volley](http://developer.android.com/training/volley/index.html) module
 - Clone volley to some location on your computer with `git clone https://android.googlesource.com/platform/frameworks/volley`
 - In android studio `file -> new -> import module`. Use the file selector to navigate to the volley folder that was just cloned and select the top-level `volley` folder to import. **Your project might try to build automatically at this point.  It will fail, this is to be expected**.
 - In Android Studio open `Gradle Scripts -> build.gradle (Module: volley)`  find the line `apply from: 'bintray.gradle'` (should be at the end of the file) and comment it out or remove it.
 - Rebuild your project and it should work.  Note: It's possible (depending on your JDK version/setup) that there are additional SDKs that need to be setup.  If there are error messages being thrown at this point, it is highly likely that you're missing an SDK, and should be able to click the error links to install the proper SDKs.

### Team:
Brandon Barrett - ditofry, Amir Kashipazha - amirkashi, Dilara Madinger - cudilara, Derek Riemer - derekriemer

### Title:
Weather Alarm Clock

### Description:
An Android app that serves as a customized alarm clock. Depending on the current weather, this alarm clock will ring earlier to give the user time to plan for a slower commute or clean the driveway.

### Vision statement:
The Weather Alarm Clock will allow people to be on time, regardless of the weather.

### Motivation:
We want to develop an app that we would use ourselves. Since many of us commute to school, we would like to have extra time to handle inclement weather, i.e. snow shoveling driveways. This app will also help us to prepare for the day.

### Risks:
Most of our team members did not develop an app, therefore this project presents us with the challenge of learning as we go. Also a few of team members do not have an Android platform to test the app, which will slow the process.

### Mitigation strategy:
There are five team members in our group and this allows for working on separate parts of the project at the same time. We are all avid learners and are excited about creating an app. Our motivation will help with learning new platforms. Currently all of us are actively pursuing acquisition of required hardware.

### Requirements:
ID	Requirements	Time (hour)	Type of Requirements	User Story
01	alarm set/disable	7	user	As a user, I want to set and disable the alarm so that I can control when I wake up.
02	alarm tone	7	user	As a user, I want to set different alarm tones so that I can hear pleasant sounds.
03	find location	2	fucntional	As a developer, I need access to location data so that I can retrieve weather data for the user.
04	track weather	4	fucntional	As a developer, I need to track weather data so that I can wake up the user at appropriate time.
05	specify beahavior dependent upon the weather	7	user	As a user, I would like to specify alarm clock behavior, so that I can be awakened appropriately according to weather conditions.
06	weekend mode	6	user	As a user I would like my alarm clock disabled during the weekend, so that I can sleep in.

### Methodology:
Agile.

### Project Tracking Software:
Trello https://trello.com/b/McxWBv2w/project-ideas and GitHub https://github.com/derekriemer/colorado_alarm

### Project Plan:
alarm set/disable

alarm tone

find location

track weather

specify behavior dependent upon the weather

weekend mode
