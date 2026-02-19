# 🚀 Guida Professionale Android: Architettura MVVM + Repository + Offline-First

Questa guida ti accompagnerà passo dopo passo nella creazione di un'app Android moderna utilizzando **Kotlin**, **Jetpack Compose**, **Hilt**, **Retrofit** e **Room**. 

L'obiettivo è costruire una struttura **"Offline-First"**: l'app mostra i dati dal database locale (velocità) e si aggiorna in background dalla rete (freschezza).

---

## 🛠️ Fase 1: Fondamenta e Dependency Injection (DI)

Prima di tutto, configura il progetto per supportare l'iniezione delle dipendenze con **Hilt**. Senza DI, dovresti creare manualmente ogni oggetto, rendendo il codice fragile.

1.  **Classe Application:** Crea una classe che estende `Application` e annotala con `@HiltAndroidApp`. Questo è il "motore" che avvia Hilt.
2.  **MainActivity:** Annotala con `@AndroidEntryPoint` per permettere a Hilt di iniettare i ViewModel.

---

## 📦 Fase 2: Definizione dei Modelli (Data vs Domain)

Non usare mai i dati grezzi delle API direttamente nell'interfaccia utente. Separa i modelli in due tipi:

1.  **DTO (Data Transfer Object):** Rappresenta il JSON delle API.
    ```kotlin
    @Serializable
    data class CharacterDto(@SerialName("name") val name: String, @SerialName("image") val image: String)
    ```
2.  **Domain Model:** La versione pulita usata dall'UI.
    ```kotlin
    data class Character(val name: String, val imageUrl: String)
    ```

---

## 🌐 Fase 3: Il Network Layer (Retrofit)

Crea un'interfaccia per definire le chiamate HTTP. Retrofit si occuperà di tutto il resto.

```kotlin
interface RickAndMortyApi {
    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): CharacterResponse
}
```

---

## 💾 Fase 4: Il Local Storage Layer (Room)

Per il supporto offline, crea una tabella (`Entity`) e un punto di accesso (`Dao`).

```kotlin
@Entity(tableName = "characters")
data class CharacterEntity(@PrimaryKey val id: Int, val name: String, val imageUrl: String)

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters")
    fun getAllCharacters(): Flow<List<CharacterEntity>> // Usiamo Flow per aggiornamenti in tempo reale!
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)
}
```

---

## 🔌 Fase 5: Il Repository (Il Cervello del Caching)

Il Repository è l'unica fonte di verità (**SSOT**). Coordina Rete e Database.

**Logica Professionale:**
1.  Espone un `Flow` dal database locale. L'UI "osserva" questo flusso.
2.  Metodo `refresh()`: Scarica i dati dalle API e li salva nel DB.
3.  Appena salvati nel DB, il `Flow` del punto 1 emette automaticamente i nuovi dati all'UI.

```kotlin
class CharacterRepositoryImpl(private val api: RickAndMortyApi, private val dao: CharacterDao) {
    // L'UI osserva questo:
    val characters = dao.getAllCharacters().map { entities -> entities.toDomain() }

    // Questo aggiorna i dati:
    suspend fun refresh() {
        val remoteData = api.getCharacters(1)
        dao.insertAll(remoteData.toEntities())
    }
}
```

---

## 🧠 Fase 6: ViewModel e Gestione dello Stato (UI State)

Il ViewModel gestisce lo stato dell'UI usando una `Sealed Class`. Questo evita stati incoerenti (es. mostrare errore e caricamento insieme).

```kotlin
sealed class UiState {
    object Loading : UiState()
    data class Success(val data: List<Character>) : UiState()
    data class Error(val msg: String) : UiState()
}

@HiltViewModel
class MyViewModel @Inject constructor(private val repo: CharacterRepository) : ViewModel() {
    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state = _state.asStateFlow()

    init {
        // 1. Osserva il DB locale
        viewModelScope.launch { repo.characters.collect { data -> _state.value = UiState.Success(data) } }
        // 2. Prova il refresh
        viewModelScope.launch { repo.refresh() }
    }
}
```

---

## 🎨 Fase 7: Interfaccia Utente (Jetpack Compose)

L'UI è una funzione pura dello stato. Usiamo `collectAsStateWithLifecycle` per essere sicuri di non consumare risorse quando l'app è in background.

```kotlin
@Composable
fun MyScreen(viewModel: MyViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is UiState.Loading -> CircularProgressIndicator()
        is UiState.Success -> LazyColumn { /* Mostra la lista */ }
        is UiState.Error -> Text("Errore!")
    }
}
```

---

## 🛣️ Fase 8: Navigazione

Configura un `NavHost` centralizzato. Usa rotte stringa per passare parametri (es: `"character/{id}"`).

---

## 💡 Riepilogo del Flusso (Developer Path)

1.  **Configura Hilt:** Assicurati che le dipendenze siano iniettate correttamente.
2.  **Crea i Modelli:** Definisce la struttura dati (Data e Domain).
3.  **Implementa Room:** Prepara il database per la persistenza.
4.  **Implementa Retrofit:** Prepara le chiamate di rete.
5.  **Crea il Repository:** Unisci i due mondi e gestisci la logica di caching.
6.  **Crea il ViewModel:** Esponi lo stato all'interfaccia.
7.  **Crea l'UI Compose:** Disegna la schermata reagendo allo stato del ViewModel.
8.  **Collega tutto:** Usa il NavGraph per completare l'esperienza utente.

**Perché farlo?** Questa struttura separa le responsabilità (**SOC**), rende il codice testabile e garantisce che l'app funzioni sempre, anche in galleria o in aereo! ✈️
