Absolutely. I’d structure this as a **data-driven reference app**, rather than putting character-specific information directly into Compose screens. That will make adding/correcting FE3H data dramatically easier and will also make the timed Lost Item search extremely fast.

The key architectural rule I’d use is:

> **Compose renders state. ViewModels own UI state and user interactions. Use cases own application behavior. Repositories own data access. Data models contain the FE3H data.**

## 1. High-level architecture

```text
                    ┌──────────────────────┐
                    │      Compose UI      │
                    │ Screens / Components │
                    └──────────┬───────────┘
                               │ Events
                               ▼
                    ┌──────────────────────┐
                    │     ViewModels       │
                    │  State + UI logic    │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │      Use Cases       │
                    │ Application behavior │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │    Repositories      │
                    └───────┬───────┬──────┘
                            │       │
                ┌───────────┘       └────────────┐
                ▼                                ▼
        ┌───────────────┐                ┌───────────────┐
        │ Static Game   │                │ Future Local  │
        │ Data / JSON   │                │ Database      │
        └───────────────┘                └───────────────┘
```

For the initial version, **I would not introduce Room unless you have a reason to persist user-created data**. FE3H's character/item/tea information is essentially static reference data, so bundled JSON is simpler and extremely fast.

Room can be added later for things such as favorites, notes, custom filters, or progress tracking.

---

# 2. Recommended project structure

I'd start with a single `app` module but organize it strongly by architectural layer/feature.

```text
FE3HReference/
│
├── app/
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           │
│           ├── java/com/.../fe3hreference/
│           │   │
│           │   ├── FE3HReferenceApplication.kt
│           │   │
│           │   ├── di/
│           │   │   ├── AppModule.kt
│           │   │   ├── DataModule.kt
│           │   │   ├── DomainModule.kt
│           │   │   └── ViewModelModule.kt
│           │   │
│           │   ├── navigation/
│           │   │   ├── AppNavHost.kt
│           │   │   ├── Routes.kt
│           │   │   └── NavigationExtensions.kt
│           │   │
│           │   ├── data/
│           │   │   ├── model/
│           │   │   │   ├── Character.kt
│           │   │   │   ├── CharacterStats.kt
│           │   │   │   ├── Proficiency.kt
│           │   │   │   ├── Crest.kt
│           │   │   │   ├── Item.kt
│           │   │   │   ├── Gift.kt
│           │   │   │   ├── LostItem.kt
│           │   │   │   ├── Tea.kt
│           │   │   │   ├── TeaTopic.kt
│           │   │   │   └── TeaQuestion.kt
│           │   │   │
│           │   │   ├── repository/
│           │   │   │   └── FE3HRepositoryImpl.kt
│           │   │   │
│           │   │   └── source/
│           │   │       ├── FE3HDataSource.kt
│           │   │       └── JsonFE3HDataSource.kt
│           │   │
│           │   ├── domain/
│           │   │   ├── repository/
│           │   │   │   └── FE3HRepository.kt
│           │   │   │
│           │   │   └── usecase/
│           │   │       ├── GetCharactersUseCase.kt
│           │   │       ├── GetCharacterUseCase.kt
│           │   │       ├── SearchLostItemsUseCase.kt
│           │   │       └── SearchTeaResponsesUseCase.kt
│           │   │
│           │   ├── feature/
│           │   │   │
│           │   │   ├── characterlist/
│           │   │   │   ├── CharacterListScreen.kt
│           │   │   │   ├── CharacterListViewModel.kt
│           │   │   │   ├── CharacterListUiState.kt
│           │   │   │   └── components/
│           │   │   │       └── CharacterGridItem.kt
│           │   │   │
│           │   │   ├── characterdetail/
│           │   │   │   ├── CharacterDetailScreen.kt
│           │   │   │   ├── CharacterDetailViewModel.kt
│           │   │   │   ├── CharacterDetailUiState.kt
│           │   │   │   │
│           │   │   │   └── components/
│           │   │   │       ├── CharacterHeader.kt
│           │   │   │       ├── CharacterTabs.kt
│           │   │   │       ├── StatsTab.kt
│           │   │   │       ├── ItemsTab.kt
│           │   │   │       ├── TeasTab.kt
│           │   │   │       ├── CrestList.kt
│           │   │   │       ├── ProficiencyTable.kt
│           │   │   │       ├── BaseStatsTable.kt
│           │   │   │       ├── GrowthRatesTable.kt
│           │   │   │       └── MaxStatsTable.kt
│           │   │   │
│           │   │   ├── itemsearch/
│           │   │   │   ├── ItemSearchScreen.kt
│           │   │   │   ├── ItemSearchViewModel.kt
│           │   │   │   ├── ItemSearchUiState.kt
│           │   │   │   └── components/
│           │   │   │       ├── ItemSearchBar.kt
│           │   │   │       └── ItemSearchResult.kt
│           │   │   │
│           │   │   └── teasearch/
│           │   │       ├── TeaSearchViewModel.kt
│           │   │       └── TeaSearchUiState.kt
│           │   │
│           │   └── ui/
│           │       ├── theme/
│           │       │   ├── Color.kt
│           │       │   ├── Theme.kt
│           │       │   └── Type.kt
│           │       │
│           │       └── components/
│           │           ├── FE3HTopBar.kt
│           │           ├── SearchTopBar.kt
│           │           ├── Portrait.kt
│           │           ├── EmptyState.kt
│           │           └── Tooltip.kt
│           │
│           └── res/
│               ├── drawable/
│               ├── mipmap/
│               ├── values/
│               └── raw/
│                   └── fe3h_data.json
│
├── build.gradle.kts
├── settings.gradle.kts
└── gradle/
```

---

# 3. Data model first

This should be the foundation of the project.

I'd avoid designing the data models around the screens. Design them around the **game's concepts**.

For example:

```kotlin
data class Character(
    val id: CharacterId,
    val name: String,
    val portrait: Int,
    val crests: List<Crest>,
    val proficiencies: Map<ProficiencyType, ProficiencyStatus>,
    val stats: CharacterStats,
    val lostItems: List<Item>,
    val likedGifts: List<Item>,
    val dislikedGifts: List<Item>,
    val favoriteTeas: List<Tea>,
    val teaTopics: List<TeaTopic>,
    val teaQuestions: List<TeaQuestion>
)
```

Then:

```kotlin
enum class ProficiencyType {
    SWORD,
    LANCE,
    AXE,
    BOW,
    GAUNTLET,
    REASON,
    FAITH,
    AUTHORITY,
    RIDING,
    FLYING,
    ARMOR
}
```

And importantly:

```kotlin
sealed interface ProficiencyStatus {
    data object Neutral : ProficiencyStatus
    data object Boon : ProficiencyStatus
    data object Bane : ProficiencyStatus
    data object BuddingTalent : ProficiencyStatus
}
```

That makes the UI extremely simple.

```kotlin
ProficiencyIcon(
    proficiency = ProficiencyType.LANCE,
    status = character.proficiencies[ProficiencyType.LANCE]
)
```

The UI shouldn't have to know that "three stars means budding talent."

---

# 4. Stats model

I'd make the stat system explicit rather than using arbitrary strings.

```kotlin
enum class StatType {
    HP,
    STR,
    MAG,
    DEX,
    SPD,
    LCK,
    DEF,
    RES,
    CHA
}
```

Then:

```kotlin
data class CharacterStats(
    val base: Map<StatType, Int>,
    val growthRates: Map<StatType, Int>,
    val maximum: Map<StatType, Int>
)
```

This lets the same reusable component render all three tables.

```text
                 HP   STR   MAG   DEX   SPD ...
Base             29    30    10    20    15
Growth           55%   45%   20%   35%   30%
Maximum          78    63    35    48    42
```

---

# 5. Build the static data pipeline

This is one of the most important parts of the project.

I'd make the app's data live in JSON rather than Kotlin source.

Something conceptually like:

```json
{
  "characters": [
    {
      "id": "hubert",
      "name": "Hubert",
      "portrait": "hubert",
      "crests": [],
      "proficiencies": {
        "sword": "neutral",
        "lance": "neutral",
        "axe": "bane",
        "bow": "boon",
        "reason": "boon",
        "faith": "bane",
        "authority": "boon"
      }
    }
  ]
}
```

Then the application code doesn't need to change when you discover:

> "Oops, Hubert's growth rate is wrong."

You change the data, not the application.

### Better still

I'd actually separate the raw data:

```text
res/raw/
    characters.json
    items.json
    crests.json
    teas.json
```

This makes editing and validating the data substantially easier.

---

# 6. Repository layer

Define the interface in `domain`:

```kotlin
interface FE3HRepository {

    suspend fun getCharacters(): List<Character>

    suspend fun getCharacter(
        id: CharacterId
    ): Character?

    suspend fun searchLostItems(
        query: String
    ): List<LostItemResult>

    suspend fun searchTeaResponses(
        characterId: CharacterId,
        query: String
    ): List<TeaSearchResult>
}
```

The implementation can initially be:

```text
FE3HRepositoryImpl
        ↓
JsonFE3HDataSource
        ↓
JSON
```

Later:

```text
FE3HRepositoryImpl
        ↓
 ┌──────────────┐
 │ JSON         │
 │ Room         │
 │ Remote API   │
 └──────────────┘
```

without requiring the ViewModels to change.

---

# 7. Koin dependency injection

I'd keep DI deliberately boring.

```text
FE3HReferenceApplication
        │
        ├── DataModule
        │     ├── FE3HDataSource
        │     └── FE3HRepository
        │
        ├── DomainModule
        │     └── UseCases
        │
        └── ViewModelModule
              ├── CharacterListViewModel
              ├── CharacterDetailViewModel
              ├── ItemSearchViewModel
              └── TeaSearchViewModel
```

For example:

```kotlin
val dataModule = module {
    single<FE3HDataSource> {
        JsonFE3HDataSource(get())
    }

    single<FE3HRepository> {
        FE3HRepositoryImpl(get())
    }
}
```

And:

```kotlin
val viewModelModule = module {
    viewModel {
        CharacterListViewModel(get())
    }

    viewModel { params ->
        CharacterDetailViewModel(
            characterId = params.get(),
            getCharacter = get()
        )
    }

    viewModel {
        ItemSearchViewModel(
            searchLostItems = get()
        )
    }
}
```

---

# 8. Strict ViewModel architecture

I'd enforce a very specific pattern throughout the project.

### UI

```kotlin
@Composable
fun CharacterListScreen(
    viewModel: CharacterListViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CharacterListContent(
        state = state,
        onCharacterClicked = viewModel::onCharacterClicked
    )
}
```

### ViewModel

```kotlin
class CharacterListViewModel(
    private val getCharacters: GetCharactersUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<CharacterListUiState>(
            CharacterListUiState.Loading
        )

    val uiState = _uiState.asStateFlow()

    fun onCharacterClicked(id: CharacterId) {
        // navigation event
    }
}
```

### Important architectural rule

**Don't do this:**

```kotlin
CharacterListScreen {
    repository.getCharacters()
}
```

And don't do this either:

```kotlin
CharacterListScreen {
    if (character.name == "Hubert") {
        // ...
    }
}
```

Character-specific behavior belongs in the data/domain layers.

---

# 9. Home / Character Selection

This should be the start destination.

```text
CharacterListScreen

┌─────────────────────────────────────┐
│ FE3H Reference                   ▼  │
├─────────────────────────────────────┤
│                                     │
│ ┌───────┐ ┌───────┐ ┌───────┐      │
│ │       │ │       │ │       │      │
│ │ Felix │ │ Hubert│ │ Edel  │ ...  │
│ │       │ │       │ │ gard  │      │
│ └───────┘ └───────┘ └───────┘      │
│                                     │
└─────────────────────────────────────┘
```

Use:

```kotlin
LazyVerticalGrid
```

with a reusable:

```kotlin
CharacterGridItem
```

The grid should be driven entirely from:

```kotlin
List<Character>
```

rather than having individual character composables.

---

# 10. Top-level navigation

I'd use Navigation Compose with roughly:

```text
CharacterList
CharacterDetail/{characterId}
ItemSearch
```

The top bar's dropdown becomes a navigation mechanism rather than every screen independently reinventing it.

```kotlin
sealed class Route(val route: String) {
    data object Characters : Route("characters")
    data object ItemSearch : Route("items/search")
    data object CharacterDetail : Route("character/{id}")
}
```

---

# 11. Character Detail

I would **not create three ViewModels** for Stats, Items, and Teas.

Use one:

```text
CharacterDetailViewModel
```

because all three tabs describe the same selected character.

```text
CharacterDetailScreen
       │
       └── CharacterDetailViewModel
               │
               └── Character
                    ├── Stats
                    ├── Items
                    └── Teas
```

The tabs are simply different projections of the same state.

```kotlin
enum class CharacterDetailTab {
    STATS,
    ITEMS,
    TEAS
}
```

---

# 12. Stats UI

I'd make each section an independent composable:

```text
StatsTab
 ├── CharacterPortrait
 ├── CrestList
 │    └── CrestIcon
 ├── ProficiencyTable
 │    └── ProficiencyCell
 ├── BaseStatsTable
 ├── GrowthRatesTable
 └── MaxStatsTable
```

This gives you a particularly clean proficiency implementation.

For Hubert:

```text
             Sword   Lance   Axe   Bow   ...
             ─────   ─────   ───   ───
Status          ·       ·      ↓     ↑
```

The cell simply receives:

```kotlin
ProficiencyStatus
```

and renders:

```text
Neutral          nothing
BuddingTalent    ★★★
Bane             ↓
Boon             ↑
```

No character-specific logic in the UI.

---

# 13. Crest tooltip

I'd make the crest itself a reusable component:

```kotlin
CrestIcon(
    crest = crest,
    onClick = ...
)
```

Clicking it updates local UI state:

```kotlin
var selectedCrest by remember { mutableStateOf<Crest?>(null) }
```

The tooltip/popover is presentation state, so **this is one of the few cases where state can appropriately live in the Composable**.

The actual crest data does not.

```text
Crest
 ├── name
 ├── description
 ├── effect
 └── icon
```

---

# 14. Items tab

This one should be almost trivial once the data model exists.

```text
ItemsTab
 │
 ├── Lost Items
 │    └── ItemList
 │
 ├── Liked Gifts
 │    └── ItemList
 │
 └── Disliked Gifts
      └── ItemList
```

I'd use the same `ItemRow` for all three.

---

# 15. The important part: Lost Item search

This deserves special treatment because of the **30-second gameplay constraint**.

The user should not have to:

```text
type → press search → wait → results
```

Instead:

```text
typing
   ↓
debounced query
   ↓
instant local search
   ↓
results
```

And because the data set is tiny, **there is absolutely no reason to hit a network or database for every keystroke.**

Load all lost items into memory when the app starts.

Then:

```text
"fe"
 ↓
"Felix's ..."
"Feather..."
...
```

### ViewModel

Conceptually:

```kotlin
fun onQueryChanged(query: String) {
    _uiState.update {
        it.copy(
            query = query,
            results = searchLostItems(query)
        )
    }
}
```

For this app, even a straightforward case-insensitive `contains()` search will probably be fast enough.

But I'd design the use case so that we can later improve it to:

```text
exact match
↓
prefix match
↓
substring match
↓
fuzzy match
```

For example, typing:

```text
con
```

could immediately find:

```text
Constance's Personal Item
```

even if the user doesn't remember the exact name.

---

# 16. Lost Item search result

The result should immediately establish:

```text
┌───────────────────────────────────────┐
│ [portrait]   Tattered Overcoat        │
│              Dimitri                  │
└───────────────────────────────────────┘
```

I'd make the entire result clickable.

Clicking it should take the user directly to:

```text
CharacterDetail/Dimitri
```

That makes the search useful beyond merely answering:

> "Whose item is this?"

---

# 17. Tea search

Tea is slightly different because the search lives inside Character Detail.

I'd have:

```text
CharacterDetailViewModel
        │
        ├── character
        ├── selectedTab
        └── teaSearchQuery
```

When Teas is selected:

```text
┌──────────────────────────────────────┐
│ 🔍 favorite tea / search...          │
├──────────────────────────────────────┤
│ Favorite Teas                        │
│ ...                                  │
│                                      │
│ Tea Topics                           │
│ ...                                  │
│                                      │
│ Final Questions                      │
│ ...                                  │
└──────────────────────────────────────┘
```

The search should filter:

* tea topics
* final questions
* appropriate responses

I'd preserve the category structure rather than returning one giant undifferentiated list.

For example:

```text
Query: "knight"

Tea Topics
  • Knights

Final Questions
  • "Do you have any thoughts on knights?"
      → Appropriate response
```

---

# 18. UI state design

I'd strongly recommend sealed UI states for screens that can load/fail.

For example:

```kotlin
sealed interface CharacterListUiState {

    data object Loading : CharacterListUiState

    data class Success(
        val characters: List<Character>
    ) : CharacterListUiState

    data class Error(
        val message: String
    ) : CharacterListUiState
}
```

For search, something simpler:

```kotlin
data class ItemSearchUiState(
    val query: String = "",
    val results: List<LostItemResult> = emptyList()
)
```

No need to over-engineer it.

---

# 19. Execution plan

I'd build this in the following order.

## Phase 1 — Project skeleton

**Goal:** Empty app with architecture established.

1. Create Android project.
2. Configure Kotlin.
3. Configure Compose.
4. Add Koin.
5. Add Navigation Compose.
6. Add lifecycle Compose.
7. Establish package structure.
8. Create `Application` class.
9. Configure Koin.
10. Create navigation skeleton.

**Deliverable:**

```text
App launches
    ↓
Character List placeholder
    ↓
Character Detail placeholder
    ↓
Item Search placeholder
```

---

# Phase 2 — Domain/data model

Create:

```text
Character
CharacterStats
StatType
ProficiencyType
ProficiencyStatus
Crest
Item
LostItem
Gift
Tea
TeaTopic
TeaQuestion
```

Then establish IDs:

```kotlin
@JvmInline
value class CharacterId(val value: String)

@JvmInline
value class ItemId(val value: String)
```

This is preferable to passing arbitrary strings around.

---

# Phase 3 — Populate FE3H data

Create:

```text
characters.json
items.json
crests.json
teas.json
```

Build the JSON parser.

Then write repository tests verifying things such as:

```text
Hubert exists
Hubert has Lance = BuddingTalent
Hubert has Axe = Bane
Hubert has Bow = Boon
Hubert has Sword = Neutral
```

This is where I'd spend a surprising amount of effort.

**The app's value is ultimately the correctness and usability of its data.**

---

# Phase 4 — Character selection

Implement:

```text
CharacterListViewModel
CharacterListScreen
CharacterGridItem
```

Get all characters displaying correctly.

Then clicking one navigates to:

```text
CharacterDetail/{id}
```

---

# Phase 5 — Character detail

Implement the shell:

```text
Character name
Stats | Items | Teas
```

Then implement Stats in this order:

1. Portrait
2. Crests
3. Proficiency table
4. Base stats
5. Growth rates
6. Maximum stats

This gives you the hardest UI component—the proficiency matrix—early.

---

# Phase 6 — Items

Implement:

```text
Lost Items
Liked Gifts
Disliked Gifts
```

Make sure all of these are data-driven.

---

# Phase 7 — Lost Item search

This is the first feature I'd consider **MVP-critical**.

Implement:

```text
ItemSearchViewModel
ItemSearchScreen
ItemSearchBar
ItemSearchResult
SearchLostItemsUseCase
```

Requirements:

* search begins immediately on typing
* case insensitive
* no submit button required
* results update in real time
* character portrait displayed
* character result is clickable
* search works offline
* search is effectively instantaneous

Then test it with actual phone hardware.

---

# Phase 8 — Tea data/UI

Implement:

```text
Favorite Teas
Tea Topics
Final Questions
Responses
```

Then add:

```text
Tea search
```

Search should update as the user types.

---

# Phase 9 — Navigation polish

At this point:

```text
Home
 ↓
Character
 ├── Stats
 ├── Items
 └── Teas

Top bar
 ↓
Item Search
 ↓
Character
```

Make navigation behavior consistent.

In particular, make sure returning from a character to search doesn't unnecessarily reload everything.

---

# Phase 10 — Visual polish

Only after the data and functionality are solid:

* FE3H-inspired typography
* spacing
* portrait framing
* crest presentation
* proficiency icons
* stat tables
* animations
* dark/light theme
* responsive layouts
* accessibility
* tablet/landscape layout

I would **not** spend much time making it beautiful before the data model is proven.

---

# Phase 11 — Testing

I'd have three testing layers.

### Domain tests

```text
SearchLostItemsUseCaseTest
CharacterRepositoryTest
TeaSearchTest
```

### ViewModel tests

```text
CharacterListViewModelTest
CharacterDetailViewModelTest
ItemSearchViewModelTest
```

Especially:

```text
query = ""
→ all items / appropriate initial state

query = "hub"
→ Hubert-related result

query = "HUB"
→ same results

query = "xyz"
→ no results
```

### Compose UI tests

Test important user flows:

```text
Launch
→ character grid
→ tap Hubert
→ Stats
→ verify proficiency indicators

Launch
→ Item Search
→ type item
→ tap result
→ correct character
```

---

# 20. One architectural change I'd strongly recommend

Although you called this a "strict ViewModel architecture," I would **not** interpret that as "every piece of state must live in a ViewModel."

Use this division:

### ViewModel state

Things representing application state:

* selected character
* search query
* search results
* selected tab
* loaded character data
* loading/error states

### Compose state

Purely ephemeral presentation state:

* crest tooltip currently open
* dropdown currently expanded
* pressed/expanded UI element
* animation state

That keeps the ViewModels from becoming giant bags of UI trivia.

---

# 21. Suggested final architecture

The finished application should conceptually look like this:

```text
                         FE3H Reference
                               │
                  ┌────────────┴────────────┐
                  │                         │
             Character List             Item Search
                  │                         │
                  │                    SearchLostItems
                  │                         │
                  ▼                         ▼
           CharacterDetail             CharacterId
                  │
        ┌─────────┼─────────┐
        │         │         │
       Stats     Items     Teas
        │         │         │
   ┌────┼────┐    │    ┌───┼────────┐
   │    │    │    │    │   │        │
 Crests Prof Stats Lost Gifts  Topics Questions
       /Boon
       /Bane
       /Budding
```

And underneath all of it:

```text
                         ViewModels
                             │
                         Use Cases
                             │
                         Repository
                             │
                       Static Data
                             │
                  ┌──────────┴──────────┐
                  │                     │
             characters.json       items.json
                  │
              teas.json
                  │
             crests.json
```

## MVP milestone

I'd define **MVP** as:

> **Open app → see all characters → select character → inspect Stats/Items/Teas → use Item Search to identify a character in real time.**

Everything else—favorites, notes, advanced fuzzy search, filtering, fancy animations, etc.—can come afterward.

FE3H data and assets are available from https://github.com/thierrylee/FE3H-AssetsDB/tree/master . 

