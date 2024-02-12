# Chatter AI
Chatter AI is a [Gemini AI](https://gemini.google.com/app) powered chatting app which can simulate mutual chatting behaviour. 

[APK Link (https://drive.google.com/file/d/1F-NiE28cLRZd3_tpXKRD3mdVVeL9iPMa/view?usp=sharing)](https://drive.google.com/file/d/1F-NiE28cLRZd3_tpXKRD3mdVVeL9iPMa/view?usp=sharing)

> [!NOTE]
> Chatter AI is a standalone application which handles/wraps all of necessary simulation/backend logics inside to provide a simulation of real time chatting experince.


## How to run in your local
In order to run project in your local be aware below points ->
* Android Studio Iguana | 2023.2.1 RC 1 | Build #AI-232.10227.8.2321.11379558, built on January 30, 2024
* Checkout ```master``` branch
* Add below parameters to your local.properties file.
  * ```SERVICE_ENDPOINT_BASE_URL=https://randomuser.me/```
  * ```GEMINI_API_KEY={Your_Gemini_API_Key_Here}``` -> You can follow this [link](https://ai.google.dev/tutorials/android_quickstart) to checkout Gemini API documentation.

## 3rd party lib. usages & Tech Specs
* Kotlin
* [Gemini AI](https://ai.google.dev/tutorials/android_quickstart) 
* [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
* [Kotlin DSL](https://developer.android.com/build/migrate-to-kotlin-dsl)
* [Material 3](https://m3.material.io/)
* Patterns
    - MVVM
    - MVI
    - Clean Architecture
    - Repository
* [JetPack Compose](https://developer.android.com/jetpack/compose?gclid=Cj0KCQiAjMKqBhCgARIsAPDgWlyVg8bZaasX_bdQfYrAXsuDQ6vD-2SmFcTv34Fb-jLQxgGqPD7UxKgaAso5EALw_wcB&gclsrc=aw.ds)
* [Edge to Edge UI design](https://developer.android.com/jetpack/compose/layouts/insets)
* Native/Custom Splash Screen
* Dark/Light UI Mode
* [Compose Destinations](https://github.com/raamcosta/compose-destinations) / [Documentation](https://composedestinations.rafaelcosta.xyz/)
* [Ktor Client](https://ktor.io/docs/client-supported-platforms.html)
* [Gradle Version Catalog](https://developer.android.com/build/migrate-to-catalogs)
* [ROOM DB](https://developer.android.com/training/data-storage/room)
* [DataStrore](https://developer.android.com/topic/libraries/architecture/datastore)
* [Lottie Animations](https://github.com/airbnb/lottie-android)
* [Coil](https://github.com/coil-kt/coil)
* [Timber Client logging](https://github.com/JakeWharton/timber)
* [Dependency Injection (HILT)](https://developer.android.com/training/dependency-injection/hilt-android)
* [Turbine](https://github.com/cashapp/turbine)
* [MockK](https://mockk.io/)
* Unit testing
* Instrumentation Testing
* JUnit5

## WireFrame
<img src="https://github.com/AttilaAKINCI/ChatterAI/blob/master/images/wireframe.png" width="600">

## ScreenShots

<img src="https://github.com/AttilaAKINCI/Chatter/blob/legacy/images/1.png" width="110">   <img
src="https://github.com/AttilaAKINCI/Chatter/blob/legacy/images/2.png" width="110">   <img
src="https://github.com/AttilaAKINCI/Chatter/blob/legacy/images/3.png" width="110">   <img
src="https://github.com/AttilaAKINCI/Chatter/blob/legacy/images/4.png" width="110">   <img
src="https://github.com/AttilaAKINCI/Chatter/blob/legacy/images/5.png" width="110">   <img
src="https://github.com/AttilaAKINCI/Chatter/blob/legacy/images/6.png" width="110">   <img
src="https://github.com/AttilaAKINCI/Chatter/blob/legacy/images/7.png" width="110">    

<img src="https://github.com/AttilaAKINCI/Chatter/blob/legacy/images/1.png" width="110">   <img
src="https://github.com/AttilaAKINCI/Chatter/blob/legacy/images/2.png" width="110">   <img
src="https://github.com/AttilaAKINCI/Chatter/blob/legacy/images/3.png" width="110">   <img
src="https://github.com/AttilaAKINCI/Chatter/blob/legacy/images/4.png" width="110">   <img
src="https://github.com/AttilaAKINCI/Chatter/blob/legacy/images/5.png" width="110">   <img
src="https://github.com/AttilaAKINCI/Chatter/blob/legacy/images/6.png" width="110">   <img
src="https://github.com/AttilaAKINCI/Chatter/blob/legacy/images/7.png" width="110">   


## Business Logics & UI Flow
Chatter AI consist of 4 different screeens namely *Splash*, *Login*, *Dashboard*, *Messaging*. 

General capabilities of Chatter AI:
* Creation an account
* Login to an account
* Logout from an account
* Remember last logged in user
* List chat sessions
* Match with random chat mate
* Show chat history
* Send message to chat mate
* Receive Gemini AI response as reply
* Receive chat mate's response


## App Video

          Normal Run              New User Login            Registered User          Remember User Until Logout

<img src="https://user-images.githubusercontent.com/21987335/113508875-1c0b5780-955b-11eb-83e0-6892340584b2.gif" width="200"/>  <img 
src="https://user-images.githubusercontent.com/21987335/113509029-0d717000-955c-11eb-92f0-6f870b9f6bc2.gif" width="200"/> <img 
src="https://user-images.githubusercontent.com/21987335/113509039-1b26f580-955c-11eb-9f41-982ade6b6c5c.gif" width="200"/> <img 
src="https://user-images.githubusercontent.com/21987335/113509052-267a2100-955c-11eb-9a9a-319b5cb684a9.gif" width="200"/>


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
