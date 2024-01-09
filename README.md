# Chatter - Under maintanence -> Jetpack Compose migration
Chatter is a chatting sample app which can simulate mutual chatting behaviour. 

## UI
Chatter application consist of 3 different fragments and 1 root activity. Activity holds a container layout in order to manage fragments which will be controlled by navigation component.

[APK Link (https://drive.google.com/file/d/1838a1BVOCnpyOPRwZ-nEfeo7MPxBUnuF/view?usp=sharing)](https://drive.google.com/file/d/1838a1BVOCnpyOPRwZ-nEfeo7MPxBUnuF/view?usp=sharing)

Fragments :
* SplashFragment
* LoginFragment
* ChatDashboardFragment

## App Video

          Normal Run              New User Login            Registered User          Remember User Until Logout

<img src="https://user-images.githubusercontent.com/21987335/113508875-1c0b5780-955b-11eb-83e0-6892340584b2.gif" width="200"/>  <img 
src="https://user-images.githubusercontent.com/21987335/113509029-0d717000-955c-11eb-92f0-6f870b9f6bc2.gif" width="200"/> <img 
src="https://user-images.githubusercontent.com/21987335/113509039-1b26f580-955c-11eb-9f41-982ade6b6c5c.gif" width="200"/> <img 
src="https://user-images.githubusercontent.com/21987335/113509052-267a2100-955c-11eb-9a9a-319b5cb684a9.gif" width="200"/>


## 3rd party lib. usages & Tech Specs
* Patterns
    - MVVM design pattern
    - Repository pattern for data management
* JetPack Libs
    - Navigation Component
* Retrofit
* Kotlin Coroutines
* Room Database 
* LiveData
* Glide image loading 
* Lottie animation Lib.
* Fragment Transition Animations
* Moshi Json handler
* Timber Client logging
* Dependency Injection (HILT) 
* DataBinding, ViewBinding
* Visitor Parttern for Recycler View
* RecyclerView with List Adapter and DiffUtil
* Single Activity multiple Fragments approach
* Unit testing samples & HILT integrations for testing
* MockK library for unit testing
* Junit5
* Thruth (assertions)

## MVVM Design
In this example general architecture is depends on MVVM Design Pattern in order to separate layers. 
Additionally, Repository Pattern is applied for data management and Visitor Pattern is applied for multi view recycler view.
Necessary abstractions are applied so as to ensure injections and polymorphic usages of classes.

UI, ViewModel, Repository layers are separated and they communicate each other with the help of event base informer(Resource.kt). 

## Error Handling 
Chatter makes network requests in order to acquire some user dependent chat history. Network related or device related errors can be occurred during runtime. 

For network requests HTTP responses between 200 and 299 indicates that successful communication, between 400 and 599
indicates error which has to be handled.

Repository layer handles that errors and informs ViewModel Layer with Resource.kt type. ViewModel Layer is informs UI layer if it necessary.

## Testing
For unit tests, I created some utility testing classes. MockK and JUnit5 is used for unit testing. Junit4 is used for integration testing

Context dependent tests are placed under androidTest folder, Independent class and utility tests are placed 
under test folder.

## Application UI Flow

#### SplashFragment
Chatter opens with splash screen fragment which controls user related data in order to determine where tha app navigate next. 
If a user logged in before, Chatter remembers that user until he/she sign out and navigates automatically to ChatDashboardFragment.
If there is not any user sessions on the phone, SplashFragment navigates user to LoginFragment.

Navigation is made with fragment transition animations.(Chatter logo moves around screen so as to give a better user expreience.)

#### LoginFragment
If user signed out or first app lunch, LoginFragment takes place. Username field is protected with "Not Empty" and "Min 2 Character" validations. 
After proper username input, user can be signed up(for first time login users) or login(for previously logged in users) the app.

After Login User is navigated to ChatDashboardFragment.

#### ChatDashboardFragment
ChatDashboardFragment is the main part of the application. Features and instructions is listed below:
* Using Log Out button, user can be sign out. App navigates loginFragment through Splah Screen
* Using plus button, user can simulate to get a random message from outsider.
* In tiled background, user can interact with his chat history.
* Using bottom chat window, User can send some message to active thread. 

#### WireFrame
<img src="https://github.com/AttilaAKINCI/Chatter/blob/legacy/images/wireframe.png" width="600">

#### ScreenShots
<img src="https://github.com/AttilaAKINCI/Chatter/blob/legacy/images/1.png" width="200">   <img
src="https://github.com/AttilaAKINCI/Chatter/blob/legacy/images/2.png" width="200">   <img
src="https://github.com/AttilaAKINCI/Chatter/blob/legacy/images/3.png" width="200">   <img
src="https://github.com/AttilaAKINCI/Chatter/blob/legacy/images/4.png" width="200">   <img
src="https://github.com/AttilaAKINCI/Chatter/blob/legacy/images/5.png" width="200">   <img
src="https://github.com/AttilaAKINCI/Chatter/blob/legacy/images/6.png" width="200">   <img
src="https://github.com/AttilaAKINCI/Chatter/blob/legacy/images/7.png" width="200">    


# License

The code is licensed as:

```
Copyright 2021 Attila Akıncı

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
