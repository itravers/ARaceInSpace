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
  <img src="http://i.imgur.com/WJPY8Zc.png">
</p>

### MonetizationController
<p align="center" alt="MonetizationController">
  <img src="http://i.imgur.com/ztzewo5.png">
</p>


### GameAd
The GameAd is the base ad type. All Banner, Interstitial and Reward Ads
should be extended from this base object. GameAd provides the ability
to easily check if ads have been loaded or are currently showing.
Because GameAd does not have any knowledge of the actual implementation
of the specific Ad. We leave the actual loading and showing of the
ads to implemented sub classes.

