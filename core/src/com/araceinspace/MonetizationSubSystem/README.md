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
below in the AndroidMonetizationSubSystem section. The 
AndroidMonetizationController is THE Monetization Controller 
for the AndroidLauncher. Any and all monetization functions 
will be conducted through this controller for the android 
system. Any System in the rest of the app will ONLY deal with 
any monetization by utilizing the monetization controller.

### GameAd
A GameAd is an abstract object that needs to be implemented by a 
specific system. It provides functionality to track if an ad is 
loading, or showing. It also tracks id’s.
<p align="center" alt="GameAd">
  <img src="http://i.imgur.com/TnLGYXW.png">
</p>

#### AndroidBannerAd
The AndroidBannerAd implements GameAds and is used to show and 
display Banner Ads on the AndroidLauncher. This is an AdMob ad. The 
AndroidBannerAd interfaces with the Admob banner ads. It is tied to 
a specific view that is created in the launcher.

#### AndroidInterstitialAd
AndroidInterstitialAd implements GameAds and is used to show and 
display Interstitial Ads on the AndroidLauncher. This is an AdMob ad.
Similar to the AndroidBannerAd the AndroidInterstitialAd also 
interfaces with Admobs ads. You load an interstitial ad, and then 
after you see that it is loaded, you display it.

#### AdColonyAndroidRewardAd
The AdColonyAndroidRewardAd implements GameAds and is used to 
show and display reward Ads on the AndroidLauncher. This class 
varies a bit from the other GameAds, and doesn’t use ID, and 
several methods from GameAds() The AdColonyAndroidRewardAd interfaces 
with with AdColony displaying a reward ad, and then after the ad is 
watch a callback gets called rewarding the user. The ad must be set 
up in the AdColony console and activated with a zone before it starts 
showing ads.

### ToastInterface
Android has the ability to toast, which is a little text popup 
that displays messages to the users. We implement a ToastInterface 
so we can send a toast to it. If we want to make a toast for another 
type of app other than AndroidLauncher we will have to also make the 
underlying graphics for it.

### PlayPurchaseManager
PlayPurchaseManager interfaces with Google Play’s in-app purchases. 
The underlying google play system only has managed items and 
subscriptions. However here we split up managed items into 
consumable and non-consumable items. The only difference between 
them is that we will never consume the non-consumable items. That 
means whenever the game is reloaded we will be able to see that the 
player has this item. It’s good for permanently unlocking in-app 
features.
<p align="center" alt="PlayPurchaseManager">
  <img src="http://i.imgur.com/v1zRe60.png">
</p>

#### PurchasableItem
A PurchasableItem represents an item purchasable in-app. It defines 
the type of item, such as consumable, non-consumable and a 
subscription. The MonetizationController has a buyItem() method 
that takes a PurchasableItem as a parameter.

### MyFirebaseMessagingService
While integrating Admob we found that it was pretty simple to 
implement FireBase analytics to track what happens in the app. 
Then we found that it was possible to send messages from the 
FireBase Console. So this class listens for any messages from 
Firebase, and will display a toast on any device that is currently 
running the app.
