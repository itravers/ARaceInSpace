Monetization SubSystem
======================
The Monetization SubSystem is designed to the game to have
cross platform in-game advertisements and in-app purchases.

Put Monetization UML HERE
<p align="center" alt="A mockup, not a screenshot">
  <img src="http://i.imgur.com/EeOmFO3.png">
</p>

### AdsController


### GameAd
The GameAd is the base ad type. All Banner, Interstitial and Reward Ads
should be extended from this base object. GameAd provides the ability
to easily check if ads have been loaded or are currently showing.
Because GameAd does not have any knowledge of the actual implementation
of the specific Ad. We leave the actual loading and showing of the
ads to implemented sub classes.

