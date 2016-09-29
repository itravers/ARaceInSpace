Monetization SubSystem
======================
The Monetization SubSystem is responsible for monetizing the game via 
in-app banner advertisements, interstitial advertisements, reward video 
advertisements and in app purchases. The Monetization SubSystems has 
been designed to be modular, meaning we are able to switch out the main 
Monetization Controller. This will allow us to change out to a 
different Monetization Controller for google play (which has already 
been implemented), ios, kongregate, amazon and others. This SubSystem 
is broken into several parts. The main part of the Subsystem will be 
in the project’s “core” system. Each individual different libgdx 
launcher will have it’s own part of the Monetization SubSystem.

<p align="center" alt="Monetization SubSystem">
  <img src="http://i.imgur.com/Oj8idJu.png">
</p>

### MonetizationController
<p align="center" alt="MonetizationController">
  <img src="http://i.imgur.com/ztzewo5.png">
</p>
The MonetizationController is an interface that provides a way 
to manipulate the monetization system. It provides methods that, 
when implemented, will allow us to load, show and hide different 
types of ads. It also allow us to implement in-app purchases.

#### DummyController
The DummyController is an object that implements 
MonetizationController, but it doesn’t actually do anything. 
It functions as a space holder in all the launchers that have 
not implemented their own MonetizationController.


#### AndroidMonetizationController
The AndroidMonetizationController is an object that implements 
the MonetizationController interface. It only operates in the 
AndroidLauncher (libgdx). There will be more information listed 
below in the AndroidMonetizationSubSystem section.
