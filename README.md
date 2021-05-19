<h1 align="center">
  <br>
<a href="https://imgur.com/li6jEto"><img src="https://i.imgur.com/li6jEto.png" title="source: imgur.com" width="200" height="200" /></a>
  <br>
 POPPETX
</h1>

___
## What does what?

1. **MainActivity.java**
</br> This currently has just a small chunk that executes `socketWork.java` when onCreate gets called.

2. **socketWork.java**
</br> This contains our TCP Socket Stuff
</br> The IP address and PORT values need to be stored in `NetworkConfig.java`
</br> This also executes some module from `module.java` and returns the output back
</br> One exception to this is the takePhoto module that sends the data back in a new thread refer [here](https://github.com/0xSagaCity/POPPETX/blob/main/AndroidCode/app/src/main/java/com/poppet/poppetx/modules.java#L149)

3. **NetworkConfig.java**
</br> Variables that would be changed by `builder.py` like the ones here need to be in separate files for less hassle.
5. **modules.java**
</br> Here we have almost every command and the stuff it does (RAT has very few modules currently need to add way more)

## TODO
- [x] Merge takeSelfie and takePhoto in one single method
- [x] Clean some code and make `server.py` not just print stuff but write stuff in files.
- [ ] Add a broadcastReciever to start the app every time device boots.
- [ ] Add a Scheduler that would execute socketWork in background and not just when `onCreate` from `MainActivity` is called.
- [ ] Handle Runtime Permission Checks when trying to extract Contacts from the device.
- [ ] Add rootDetection module to make RAT do more stuff if root is available.
- [ ] Start building `builder.py` to become bob the builder.
- [ ] Add even more TODO's as we go through this ones untill this becomes a ball of depression to handle.

>There is a plan to write a complete documentation of what part of RAT does what.
>What real malware has done something like this.
>What part does one need to look for while looking through a reversed malware and stuff.
___
<div>Image used is from <a href="https://smashicons.com/" title="Smashicons">Smashicons </a>
