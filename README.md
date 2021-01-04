# DevMobileProjet

Project for 2020-2021 Developping Application for Mobile Devices Course at CY Cergy Paris Université

This application is a Music Player that has 3 activities :
- A music List, to choose the music we want
- the Player, that gives control on the current music
- the Option Panel, to control the settings of the App

There is only one notable Intent, the one that comes from the list activity to the Player, that gives to the player 
the position (an integer) of the music chosen by the user. This is useful for the player to find the music, prepare it in the service and then play it.

About the Services, there is one that play the music in background, even if the player activity is close. 
The second service is the one for the sensor, but it's a service given directly by Android.

We have many threads on the player activity:
- One that update the seekbar every seconds according to the played time of the music
- 3 other for the buttons play/pause, next and previous. This allow to change the music and do actions of pause and play music.

The sensor we use is the light sensor, that we use to change the theme of the application automatically when the light goes under a certain value. This can be
stopped in the application settings.


This application has been coded using Nexus 4 VM, API 26, and is usable on every devices for sure that use API 24 at least.
