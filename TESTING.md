## Who
Brandon Barrett, Amir Kashipasha, Dilara Madinger, Derek Riemer.

## Title
Weather Alarm Clock

## Vision
The Weather Alarm Clock will allow people to be on time, regardless of the weather.

## Automated Tests
We have Unit and Functional tests for our API written with
[Minitest](https://github.com/seattlerb/minitest) and [Rails Fixtures](http://guides.rubyonrails.org/testing.html#the-low-down-on-fixtures).

An example of some functional controller tests:  

!['rails sample test code'](/screenshots/testCodeEx.png?raw=true "rails sample test code")

Our tests are organized into a directory structure that mirrors our application
directory structure, so our tests are siloed into small, maintainable chunks:

!['rails test directory structure'](/screenshots/testFileTree.png?raw=true "rails test directory structure")

We are also using a rails database configuration that allows us to run our tests
against a separate Test database, which allows us to use and abuse inserts and other
potentialy destructive operations without having to risk harming data that we may
care about in either our development or production databases.

Tests can be run individually by file via command line with
```ruby
$: bundle exec rake test path/to/file.name
```
The entire test suite can be run with
```ruby
$: bundle exec rake test
```

Our test suite runs in roughly 2.5 seconds with around 2100 assertions:  
!['rails sample test output'](/screenshots/testOutput.png?raw=true "rails sample test output")

The reason for the high number of assertions is that some of our test run with a random number
generation over a large range of values, but since those tests are unit tests
it doesn't add much overhead. In fact, a small handful of tests that involve rest requests
take longer than the thousands of unit tests.

# User Acceptance Tests
These tests will take place on our Android devices and are broken down into
three main user stories that each constitute a single Acceptance test:

### Use Case 1: App Home Screen

#### Description
All of the App's UI as well as its `MainActivity` class will run on our app's home screen.

#### Pre-conditions
 - have app installed

#### Post-conditions
 - Alarm has been set

#### Frequency of Use
 - Always used

#### Flow of Events
  - **Actor Action:** Opens app.  
  **System Response:**  Displays the accurate time of day, date, and current alarm.
  - **Actor Action:** Taps "set alarm" button.  
  **System Response:** Functioning time-set widget pops up.
  - **Actor Action:** Taps "save" button.  
  **System Response:** Alarm is set and is reflected on home screen of app.
  - **Actor Action:** After setting the alarm, the user exits the app, closes the app, and then re-opens the app.  
  **System Response:** the alarm that was most recently set will have been properly remembered.

### Use Case 2: Users receive alarms
#### Description
After setting an alarm, that alarm will go off on time and get the user's attention.
#### Pre-conditions
 - Alarm has been set for very near future
 - Weather conditions have been set to "good" e.g. "Sunny, 70˚ F"

#### Post-conditions
 - Alarm has been successfully de-activated

#### Frequency of Use
 - Daily use

#### Flow of Events
  - **Actor Action:** Alarm has reaches the alarm time  
  **System Response:** Alarm will go off, and it will play an alarm noise.
  - **Actor Action:** User taps "Stop" button on alarm.  
  **System Response:** Alarm noise stops and image behind alarm depicts accurate weather (in this test will be "good weather").
  - **Actor Action:** User will wait for alarm to play again.  
  **System Response:** alarm will not play again because we aren't building snooze functionality.

### Use Case 3: Users have their alarm altered by weather
#### Description
After setting an alarm, that alarm will go off 15 minutes ahead of time to give the user more time to prep for adverse weather.

#### Pre-conditions
 - Alarm has been set for very near future
 - Weather conditions have been set to "bad" e.g. "Snowy, 0˚ F"

#### Post-conditions
 - Alarm has been successfully de-activated

#### Frequency of Use
 - Daily use

#### Flow of Events
  - **Actor Action:** Alarm has reaches 15 minutes before the user-chosen alarm time  
  **System Response:** Alarm will go off, and it will play an alarm noise.
  - **Actor Action:** User taps "Stop" button on alarm.  
  **System Response:** Alarm noise stops and image behind alarm depicts accurate weather (in this test will be "bad weather").

We are using UI test with [Espresso](http://developer.android.com/training/testing/ui-testing/espresso-testing.html) in the [JUnit4](http://junit.org/junit4/) style.
To run the test via Gradle: Use the connectedCheck task
in Gradle to run the test directly via Gradle.
