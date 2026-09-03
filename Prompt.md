We are building an Android app using Kotlin, Jetpack Compose for UI, Koin for dependency injection, and a strict ViewModel architecture. Generate a multi-step execution plan and file structure.

This Android app will be called FE3H Reference. It will be a reference for characters from the Nintendo game "Fire Emblem Three Houses." In the game, these characters can be recruited, have various abilities, items, and preferences. The app will have details for each of these characters. As well as a system to quickly look up material.

Some interactions in the game are timed: the user is asked a question and must respond correctly within 30 seconds or so. Thus, the search system will allow a user to type and in real time pull up potential answers. 

Screens:

Base screen:
Top Bar should have a dropdown option for Item Search

Item Search:
* Allows user to type in a Lost Item, results should be the items that match the query and a portrait of the associated character

Character selection screen: 
* serves as the first screen / Home
* has a grid of each character with their portrait and name

Character Detail screen:
* Character name at the top
* a tab bar that has the following tabs: Stats, Items, Teas

Stats tab:
* has a portrait of the character
* Lists any Crests they have (an icon that, if tapped, shows a tooltip detailing the crest's name and effect)
* Lists Proficiencies as a table of icons, for each proficiency it should be empty, show budding talent stars, show bane or show boon depending on that particular character. For example, Hubert has a budding talent in Lance, so under the lance icon there should be the three stars, he has a bane in axe, so the red down arrow should be under axe, he has a boon in Bow, so the blue up arrow should show under Bow, and he is neutral in Sword, so no icon should be in the grid below Sword.
* Base stats table
* Growth rates table
* Max stats table

Items tab:
* Lists their Lost Items, Liked Gifts, Disliked Gifts

Teas tab:
* Top bar becomes a search, typing into it filters for tea topics and final comments that match the query

* Lists favorite teas
* List tea topics they respond to
* Lists final questions and approprite responses that happen during tea. 
