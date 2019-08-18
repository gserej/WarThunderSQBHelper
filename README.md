# War Thunder SQB Helper

An Android application created to help tracking numbers of remaining ally and enemy units in War Thunder Squadron Battles.
It parses JSON data streamed by the game and displays four numbers representing amount of remaining planes and ground units of each side.
Some pieces of code are based on Tim Buchalka's Android Developer Course.

<img src="screenshot.png" height="480">

## Prerequisites:
1. Connected to a local WiFi network with your phone/tablet.
2. Check [the local IP address of the PC](https://kb.wisc.edu/27309) you are running War Thunder on.
3. In the application settings menu type in your squadron tag and that IP address.

- If the numbers don't update at all, check if you can access 8111 port of this IP with a phone/tablet web browser.
 For example, if your IP address is 192.168.0.1, type in 192.168.0.1:8111.
 If the game is running and you don't see the gray page with game info, application won't work unless you configure your router/PC/local network.

- At the moment the application works only when the War Thunder in-game language is set to English or Polish.
If you want to have another language added, check the ParseJSON.java file for RegExp patterns and create a pull request.
- Press the "Reset" button every time you start a battle to reset numbers.

## Legality
WT support didn't answer me at all whether using this application breaches WT EULA but IMO there is no way they can know you are using it.
There are also other applications like War Thunder's Betty that also uses JSON data and I'm yet to read that someone got banned because of it.

## Changelog
- v0.2 - added Polish language support, fixed typos
- v0.1 - first version

## Download
[Here](https://github.com/gserej/WarThunderSQBHelper/raw/master/app/release/WT_SQB_Helper_v0.2.apk)
