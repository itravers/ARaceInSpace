Input SubSystem
===============
The Input SubSystem deals with getting input from the player. 
It also deals with recording input for later playback. In this 
case we want to be able to replay an entire performance from a Player. 
The input SubSystem does not deal with how an actual object consumes 
its input. The InputComponents in the GameObject SubSystem deal with 
this.


<p align="center" alt="Monetization SubSystem">
  <img src="http://i.imgur.com/yGhNYWg.png">
</p>


### InputManager
The InputManager is responsible for receiving any input from the 
actual player. It accomplishes this by using LibGdX’s InputProcessor 
interface and it’s GestureListener interface. This allows it to 
listen for keyboard keys and mouse clicks on a computer, or touches 
flings and zooms on a touch screen. The InputManager does NOT decide 
what to do with the inputs it gets. Instead it creates an GameInput, 
then requests an Event of type.INPUT from the EventDispatcher. It adds 
the created GameInput to the event then asks the EventDispatcher to 
dispatch it. A GameObjects InputComponent receives this and consumes it.


### InputRecorder
The InputRecorder serves two different purposes depending on what 
type of InputComponent owns it. If a PlayerInputComponent owns it, 
then whenever the PlayerInputComponent consumes an InputEvent it 
passes that Event to it’s InputRecorder to be saved.
If a GhostInputComponent owns the InputRecorder then it no longer 
functions as a recorder. Instead the InputRecorder originates the 
Ghost’s events. The Input asks the EventDispatcher for an InputEvent,
it then loads the data of that event with a specific GameInput. 
It then sets the ID of the InputEvent to be the same ID of the Ghost. 
When the Ghost receives an Event from the EventDispatcher it will 
check to see if the ID matches. If it does it will consume the event, 
if it doesn’t it will just discard it. This allows an InputRecorder 
to control a Ghost.


#### Action
An Action is just a GameInput associated with a specific game time. 
In theory by recording every action a Player takes we should be able 
to completely duplicate his play behaviour in a future game. 
This is why the InputRecorder keeps an array of Actions.


##### KeyAction
In reality completely duplicating a play behaviour exactly is 
actually quite hard. The physics simulation needs to be completely 
deterministic, no matter what is going on, what time it is, what 
machine you are playing on. Everything needs to be repeated EXACTLY. 
This doesn’t work. So instead of relying on complete determinism, 
we are going to try to keep things close, but if they get out of whack, 
we will be able to reset them using a KeyAction. 
A KeyAction will only be generated once every X amount of Actions.


##### GameInput
A GameInput is just an enum we use to define what input has been 
pressed. While we are not doing it with this game, we may want to 
remap input controls in a future game. Using this enum will make it 
easier to change the input map in the future.
