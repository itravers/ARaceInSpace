Game Object SubSystem
=====================
<p align="center" alt="MonetizationController">
  <img src="http://i.imgur.com/PVybUkx.png">
</p>
The Game Object SubSystem deals with in-game objects only. 
This would exclude menu’s, etc. In-Game objects all descend from 
the same base GameObject. 
Game Objects use a “Component Pattern”  they have swappable input, 
physics, graphics and state components. This allows constructing a 
new GameObject with a mix of used, or new behaviours, views, 
states, and inputs. Every Component constructed that is utilized by 
a GameObject class is descended from a base Component class.


### GameObject
This is the base abstract object class. All implemented game objects 
will be descendants of this class. To be a GameObject an object will 
need input, physics, graphics and state components. 
A game object will need update, create and dispose methods.


#### ThreeDGameObject
 Any objects that will be rendered as 3d models will descend from this
 abstract class. A ThreeDGameObject’s graphics component will be a 
 3DGraphicsComponent, all other components are the same.
 
 
##### Player
This is the actual player avatar in the game world. This extends 
ThreeDGameObject. It has PlayerInputComponent as its input component, 
which allows it to be controlled by the player and to save all inputs 
to replay a game. It uses a PlayerPhysicsComponent.


##### Ghost
This is the avatar of the challenger in the game world. This also 
extends from ThreeDGameObject, but might load a different Model 
than the player. It has a GhostInputComponent which allows it to 
receive input from saved inputs taken from a previous match. 
(See Input SubSystem). The Ghost also uses the PlayerPhysicsComponent
as we want it’s physics to interact with the world in EXACTLY the 
same way the players does.
 
 
#### TwoDGameObject
Any objects that will be rendered as 2d models will descend from this 
abstract class. A TwoDGameObject’s graphics component will be a 
2dGraphicsComponent, all other components are the same.


##### Planet
This is the actual in-game planets that the players will be landing 
on and taking off from. Uses a PlanetPhysicsComponent as the physics 
of a planet will be different from the physics of a Player or a Ghost.


## Components
<p align="center" alt="MonetizationController">
  <img src="http://i.imgur.com/in40itn.png">
</p>

A Component takes care of a job that a GameObject would normally do 
in it’s own logic. Instead of having the GameObject encapsulate the 
data and logic itself we use Components. This gives us the ability 
to easily duplicate, change and swap out the behaviours of GameObjects. 
It decouples the parts of the GameObject so they can be worked on 
independently, without knowing anything about the other components. 
For example in this game a Player and a Planet will be rendered 
differently. A Player will be rendered as a 3d model and a Planet 
will be rendered as a 2d model. To accomplish this we’ve extended 
GraphicsComponent to have a 2D and a 3D variant. Anytime we want a 
2D version we give the new GameObject a 2D GraphicsComponent. 
Obviously from the name we can tell that this uses the 
“Component Pattern”.


### Component
The base abstract class that all the other Components are descended 
from. Every object that extends Component must have an 
update(GameObject) method implemented. The update message will be 
called from the GameObject that owns this Component.


#### StateComponent
Each GameObject works as a state machine. The StateComponent IS the 
state machine part of a GameObject. The StateComponent completely 
encapsulates the data and the transitions associated with every state. 
Other Components may query the GameObject for it’s state. 
The GameObject will return it’s state from the StateComponent.


##### PlayerStateComponent
The PlayerStateComponent will contain all the data and methods needed 
to track the current state of the Player or Ghost. 
In this game the players and the ghosts will share the same set 
of states, data and transitions.


##### PlayerState
This is an enum owned by the PlayerStateComponent. 
It’s used to define all the different possible player states. 


##### PlanetStateComponent
The PlanetStateComponent will encapsulate all the data and methods 
needed to track the state of any planet. 


##### PlanetState
At this time there are not a whole lot of states planned for the 
planets. They will be much simpler than the PlayerState’s


#### PhysicsComponent
A physics component encapsulates all the data and methods needed for 
a GameObject to update its physical properties. This is where 
the player locomotion and forces are calculated. Everything 
excluding collision physics should be located here.


##### PlayerPhysicsComponent
 Encapsulates all the data and methods needed to drive Players 
 and Ghosts. Both these types of GameObjects should physically 
 operate EXACTLY the same. This holds the Box2D body
 
 
##### PlanetPhysicsComponent
Encapsulates all the data and methods needed to make a Planet 
work correctly. In this game the planet doesn’t do much, but it 
does have a location, size and a density which it’s gravity is 
calculated through.


#### InputComponent
A GameObject uses an input Component to process its input. 
The PlayerInputComponent and GhostInputComponent both receive 
input events from the EventDispatcher. In this game Planets do 
not receive any inputs so we don’t define any for the planets. 
I believe we can just use a null field. Both of their input 
components own their own input recorder. Which is defined in the 
Input SubSystem section.


##### GhostInputComponent
When a GhostInputComponent receives an input from the EventDispatcher 
it will process it normally and then it will NOT record it to it’s 
InputRecorder. This is because with a GhostInputComponent the 
Input Recorder will have originated the input event. It will use the 
EventDispatcher to get the event to the GhostInputComponent. 
Note, this is different than how a PlayerInputComponent processes 
Input Events


##### PlayerInputComponent
When a PlayerINputComponent receives an input from the EventDispatcher 
it will process it normally and then it WILL record it to it’s 
InputRecorder. This is because with a PlayerInputComponent the input 
will have originated with the main games InputManager. This means the 
Player's input is coming from the actual player.


#### GraphicsComponent
A GameObject uses a GraphicsComponent to actually output itself to 
the screen. In this game we want to use both 2d and 3d graphics. 
The planets and indicators and backgrounds and clouds will all be 
2d graphics, but the Players and the Ghosts will be 
3D graphics(possibly).


##### 2DGraphicsComponent
Each 2DGraphicsComponent will have a reference to the AssetManagers 
2D animations. Using LibGdx 2DGraphicsComponents Like 
Players and Ghosts should be able to reuse the same animations. 
So we don’t have multiple duplicate animations being created 
simultaneously for different GameObjects.


##### 3DGraphicsComponent
Each 3DGraphicsComponent will have references to a 3D model and 
Environment from the AssetManager. It’ll then create it’s own 
gdx.ModelInstance and gdx.AnimationController using the 
AssetManagers model. This way the original Model will still be 
duplicated, and each 3D object won’t completely load it’s own Model.
