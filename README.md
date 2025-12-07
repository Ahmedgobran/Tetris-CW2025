# Tetris Game - COMP2042 Coursework Assignment - Ahmed Abdelhalim (20580127)

## Table of Contents

1. [GitHub Repository](#1-github-repository)
2. [Compilation Instructions](#2-compilation-instructions)
3. [Implemented and Working Properly](#3-implemented-and-working-properly)
4. [Implemented but Not Working Properly](#4-implemented-but-not-working-properly)
5. [Features Not Implemented](#5-features-not-implemented)
6. [New Java Classes](#6-new-java-classes)
7. [Modified Java Classes](#7-modified-java-classes)
8. [Unexpected Problems](#8-unexpected-problems)

---

## 1. GitHub Repository

**Repository URL:** https://github.com/Ahmedgobran/Tetris-CW2025.git

The project follows a comprehensive **Model-View-Controller (MVC)** architecture with clear separation of concerns across distinct packages:

- **Controller Package** (`com.comp2042.controller`): Game control logic and input handling
- **Model Package** (`com.comp2042.model`): Game state, business logic, and board management
- **View Package** (`com.comp2042.view`): UI components and rendering systems
- **Util Package** (`com.comp2042.util`): Utility classes and helper functions

---

## 2. Compilation Instructions

### Prerequisites

- **Java Development Kit (JDK):** Version 21 or higher
- **Apache Maven:** Version 3.6 or higher
- **JavaFX:** Version 21.0.6 (managed via Maven)
- **IDE (Optional):** IntelliJ IDEA, Eclipse, or NetBeans with JavaFX support

### Step-by-Step Compilation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Ahmedgobran/Tetris-CW2025.git
   cd [Project Directory]
   ```

2. **Verify Java Installation**
   ```bash
   java -version
   # Should display: java version "21.x.x" or higher
   ```

3. **Verify Maven Installation**
   ```bash
   mvn -version
   # Should display: Apache Maven 3.6.x or higher
   ```

4. **Clean Previous Builds**
   ```bash
   mvn clean
   ```

5. **Download Dependencies**
   ```bash
   mvn dependency:resolve
   ```

6. **Run Unit Tests**
   ```bash
   mvn test
   ```

7. **Run the Application**
   ```bash
   mvn javafx:run
   ```

### Alternative Compilation Method (IDE Setup)

1. Right click `pom.xml`
2. Click "Add as Maven Project" (Reload All Maven Projects)
3. Select Run/Debug Configurations or edit configuration
4. Click Modify options (Alt+M)
5. Java - Add VM options
6. Enter this into VM options:
   ```
   --module-path
   "PATH_TO_JAVAFX_SDK_LIB"
   --add-modules
   javafx.controls,javafx.fxml,javafx.media
   ```
7. Select Main class and run (`src/main/java/com/comp2042/Main.java`)

### Key Dependencies

All dependencies are managed through Maven's `pom.xml`:

- **JavaFX Controls** (21.0.6)
- **JavaFX FXML** (21.0.6)
- **JavaFX Media** (21.0.6)
- **JUnit Jupiter** (5.12.1)
- **Maven Compiler Plugin** (3.13.0)
- **JavaFX Maven Plugin** (0.0.8)

---

## 3. Implemented and Working Properly

### 1. Ghost Piece (Shadow Preview)

**Description:** Visual aid showing where the current piece will land

**Implementation:**
- Added `getShadowYPosition()` method to `Board` interface and `TetrisBoard`
- Updated `ViewData` class to transport shadow position from model to view
- Initially implemented in `GameViewController`, later optimized by replacing `GridPane` with JavaFX `Group` for better performance

**Files Modified:**
- `src/main/java/com/comp2042/model/board/Board.java`
- `src/main/java/com/comp2042/model/board/TetrisBoard.java`
- `src/main/java/com/comp2042/model/state/ViewData.java`
- `src/main/java/com/comp2042/controller/GameViewController.java`

**Rationale:** Standard Tetris quality-of-life feature that reduces frustrating misdrops, especially with Hard Drop mechanic, which is the next feature I implemented.

---

### 2. Hard Drop Mechanic

**Description:** Instantly locks a piece at its lowest valid position

**Implementation:**
- Added `hardDrop()` method to `Board` interface and `TetrisBoard` implementation
- Mapped Space Bar to hard drop action in `GameInputHandler`
- Integrated scoring system: 2 points per line dropped (vs. 1 point for soft drop)
- Added `onHardDropEvent` to `AbstractGameController` and `InputEventListener` interface

**Files Modified:**
- `src/main/java/com/comp2042/model/board/TetrisBoard.java`
- `src/main/java/com/comp2042/controller/gamemode/NormalModeController.java`
- `src/main/java/com/comp2042/controller/GameInputHandler.java`

**Rationale:** Essential for good players, adds high risk high reward gameplay (speed for points).

---

### 3. Next Piece Preview

**Description:** Displays the upcoming brick that will spawn next

**Implementation:**
- Created `NextPieceRenderer` class to handle brick preview rendering
- Dynamically centers next piece in preview box using bounding box calculations
- Integrated into sidebar panel for clear visibility
- Supports multiple next pieces display (next and hold)
- Updates automatically when current piece locks

**Files Modified:**
- `src/main/java/com/comp2042/view/renderers/NextPieceRenderer.java` (New)
- `src/main/java/com/comp2042/view/renderers/GameRenderer.java`
- `src/main/resources/gameLayout.fxml`

**Rationale:** Essential Tetris feature allowing players to plan ahead and make strategic decisions about piece placement.

---

### 4. Sidebar Panel & Layout Restructuring

**Description:** Dedicated sidebar displaying critical game information

**Implementation:**
- Changed root container from `Pane` to `HBox` for horizontal split layout
- Created `VBox` sidebar which has "Next Piece" preview, Score display, and control buttons
- Linked FXML components directly to `GameViewController` for dynamic updates

**Files Modified:**
- `src/main/resources/gameLayout.fxml`
- `src/main/java/com/comp2042/controller/GameViewController.java`

**Rationale:** Industry-standard Tetris layout providing clear space for game feedback without obstructing gameplay.

---

### 5. Core Dimension Fix

**Description:** Corrected fundamental matrix initialization error

**Implementation:**
- Fixed `TetrisBoard` matrix initialization from `[width][height]` to `[height][width]`
- Adjusted game controllers to respect correct dimension order
- Ensured alignment between logical grid and visual representation

**Files Modified:**
- `src/main/java/com/comp2042/model/board/TetrisBoard.java`
- `src/main/java/com/comp2042/controller/gamemode/NormalModeController.java`

**Rationale:** Critical bug fix preventing two wrongs make a right principle and ensuring proper game and brick matrix logic.

---

### 6. Line Clear Animation & SFX

**Description:** Visual and audio feedback when rows are cleared

**Implementation:**
- Created dedicated `LineClearAnimation` class using JavaFX `SequentialTransition`
- Implemented Flash In (300ms) → Pause (200ms) → Flash Out (500ms) sequence
- Added callback system to synchronize visual effects with game state updates
- Enhanced `MatrixOperations.checkRemoving` to collect cleared row indices
- Updated `ClearRow` class to store and expose cleared row data

**Files Modified:**
- `src/main/java/com/comp2042/view/LineClearAnimation.java` (New)
- `src/main/java/com/comp2042/util/MatrixOperations.java`
- `src/main/java/com/comp2042/model/state/ClearRow.java`
- `src/main/java/com/comp2042/controller/GameViewController.java`

**Rationale:** Provides essential player feedback without blocking game loop; maintains state consistency.

---

### 7. Invisible Blocks Challenge Mode

**Description:** Time-based difficulty mode where locked blocks periodically disappear

**Implementation:**
- Created `InvisibleBlocksBoard` with dual-board architecture (logical + render boards)
- Implemented timer system: blocks disappear x seconds after reveal phase
- Added countdown notification system (3-2-1 warning) before disappearance
- Created specialized `ChallengeModeController` with 2x score multiplier for lines cleared, hard drop, and soft drop
- Implemented background refresh on every game tick for real time updates

**Files Modified:**
- `src/main/java/com/comp2042/model/board/InvisibleBlocksBoard.java` (New)
- `src/main/java/com/comp2042/controller/gamemode/ChallengeModeController.java` (New)
- `src/main/java/com/comp2042/view/NotificationPanel.java`
- `src/main/java/com/comp2042/controller/GameViewController.java`

**Rationale:** Tests player memory and spatial awareness; separation of logical/render boards maintains accurate collision detection.

---

### 8. Level Selection Screen

**Description:** Intermediate screen for choosing between Normal and Challenge modes

**Implementation:**
- Created `LevelSelectionController` and `levelSelection.fxml`
- Updated `MainMenuController` to route Play button to level selection
- Enables distinct initialization for each game mode

**Files Modified:**
- `src/main/java/com/comp2042/controller/LevelSelectionController.java` (New)
- `src/main/resources/levelSelection.fxml` (New)
- `src/main/java/com/comp2042/controller/MainMenuController.java`

**Rationale:** Scalable navigation flow supporting multiple game modes.

---

### 9. Centralized Scene Management

**Description:** Utility class for FXML loading and scene transitions

**Implementation:**
- Created `SceneLoader` class with reusable methods: `openSettings()`, `openMainMenu()`, `openLevelSelection()`
- Refactored all controllers to use centralized loading logic
- Eliminated repetitive try-catch blocks and path resolution code

**Files Modified:**
- `src/main/java/com/comp2042/util/SceneLoader.java` (New)
- `src/main/java/com/comp2042/controller/MainMenuController.java`
- `src/main/java/com/comp2042/controller/PauseMenuController.java`
- `src/main/java/com/comp2042/controller/GameViewController.java`

**Rationale:** Adheres to DRY principles; makes FXML path changes easier to maintain.

---

### 10. Pause Menu Integration

**Description:** Fully functional pause overlay

**Implementation:**
- Created `PauseMenuController` handling Resume, Restart, Settings, and Main Menu options
- Designed `pauseMenu.fxml` with CSS integration and custom png images
- Updated `GameViewController` with `resumeGameFromPause()` to manage timeline and input focus

**Files Modified:**
- `src/main/java/com/comp2042/controller/PauseMenuController.java` (New)
- `src/main/resources/pauseMenu.fxml` (New)
- `src/main/java/com/comp2042/controller/GameViewController.java`

**Rationale:** Standard game feature improving usability and providing access to options without quitting.

---

### 11. Input Handling Refactoring (GameInputHandler)

**Description:** Extracted keyboard input logic from `GameViewController`

**Implementation:**
- Created dedicated `GameInputHandler` class
- Moved `setOnKeyPressed` event listener from main controller
- Added ESC key support for instant Main Menu return

**Files Modified:**
- `src/main/java/com/comp2042/controller/GameInputHandler.java` (New)
- `src/main/java/com/comp2042/controller/GameViewController.java`

**Rationale:** Adheres to Single Responsibility Principle; simplifies key binding modifications.

---

### 12. Controls Menu Overhaul

**Description:** Visual controls display using keyboard key images

**Implementation:**
- Created `ControlsController` and `controlsPanel.fxml`
- Replaced plain text with high-quality visual assets
- Reorganized resources into dedicated `Images/` directory
- Simplified `MainMenuController` by removing legacy scroll pane logic

**Files Modified:**
- `src/main/java/com/comp2042/controller/ControlsController.java` (New)
- `src/main/resources/controlsPanel.fxml` (New)
- `src/main/java/com/comp2042/controller/MainMenuController.java`

**Rationale:** Professional presentation consistent with game's graphical style.

---

### 13. Transparent Pause Menu Overlay

**Description:** Pause menu overlays active game board instead of scene switching

**Implementation:**
- Updated `pauseGame()` to load pause menu FXML on top of game scene
- Removed scene-swapping logic from `PauseMenuController`
- Game state remains visible but frozen in background

**Files Modified:**
- `src/main/java/com/comp2042/controller/GameViewController.java`
- `src/main/java/com/comp2042/controller/PauseMenuController.java`

**Rationale:** Maintains visual continuity; better UX allowing players to see board state while paused.

---

### 14. Persistent High Score System

**Description:** Local storage of top 10 scores

**Implementation:**
- Created `HighScoreManager` for serialization/deserialization to `highscores.txt`
- Developed `HighScoreController` and `highScores.fxml` for leaderboard visualization
- Updated `NormalModeController` and `ChallengeModeController` to save scores on Game Over
- Integrated navigation via `MainMenuController` and `SceneLoader`

**Files Modified:**
- `src/main/java/com/comp2042/util/HighScoreManager.java` (New)
- `src/main/java/com/comp2042/controller/HighScoreController.java` (New)
- `src/main/resources/highScores.fxml` (New)
- `src/main/java/com/comp2042/controller/gamemode/NormalModeController.java`
- `src/main/java/com/comp2042/controller/gamemode/ChallengeModeController.java`

**Rationale:** Critical for game repetition and player retention; separates persistence logic from UI (MVC pattern).

---

### 15. Architectural Restructuring (MVC)

**Description:** Reorganized entire project structure to strictly adhere to MVC pattern

**Implementation:**
- Split packages into granular layers:
  - `com.comp2042.model`: Game logic and state entities (including bricks in `model.bricks`)
  - `com.comp2042.view`: Rendering logic and custom UI components
  - `com.comp2042.controller`: JavaFX controllers and user input
  - `com.comp2042.util`: Shared utilities
- Moved rendering classes into separate `view/renderers/` sub-package

**Files Modified:**
- Entire `src/main/java/com/comp2042/` directory structure

**Rationale:** Improves code navigability, enforces separation of concerns, makes codebase easier to scale and test.

---

### 16. Code Modernization

**Description:** Refactored data structures to align with Java best practices

**Implementation:**
- Converted immutable data carriers to Java record types (`ClearRow`, `DownData`, `NextShapeInfo`, `ViewData`, `MoveEvent`)
- Replaced "magic numbers" with named constants in `GameViewController` and `InvisibleBlocksBoard`
- Renamed `SimpleBoard` to `TetrisBoard` for clarity

**Files Modified:**
- `src/main/java/com/comp2042/model/state/` (Converted to Records)
- `src/main/java/com/comp2042/model/event/MoveEvent.java`
- `src/main/java/com/comp2042/model/board/TetrisBoard.java`

**Rationale:** Reduces technical debt; records simplify data model; removing magic numbers improves readability.

---

### 17. Comprehensive Unit Testing

**Description:** JUnit testing suite covering critical game mechanics

**Implementation:**
- Core logic tests: `MatrixOperations`, `BrickRotator`, Brick data structures
- Board mechanics: `TetrisBoard`, `InvisibleBlocksBoard`
- Data integrity: `GameSettingsTest`, `ScoreTest`
- Additional tests: `LevelManagerTest`, `DataImmutabilityTest`, `HoldBlockTest`, `GameInputHandlerTest`

**Test Files:**
- `src/test/java/com/comp2042/model/board/TetrisBoardTest.java`
- `src/test/java/com/comp2042/model/board/InvisibleBoardTest.java`
- `src/test/java/com/comp2042/util/MatrixOperationsTest.java`
- `src/test/java/com/comp2042/model/bricks/BrickRotatorTest.java`
- `src/test/java/com/comp2042/model/bricks/BrickTest.java`
- `src/test/java/com/comp2042/model/LevelManagerTest.java`
- `src/test/java/com/comp2042/model/ScoreTest.java`
- `src/test/java/com/comp2042/model/DataImmutabilityTest.java`
- `src/test/java/com/comp2042/model/board/HoldBlockTest.java`
- `src/test/java/com/comp2042/model/ShadowLogicTest.java`
- `src/test/java/com/comp2042/model/BoardStateTest.java`
- `src/test/java/com/comp2042/util/HighScoreManagerTest.java`
- `src/test/java/com/comp2042/util/GameSettingsTest.java`
- `src/test/java/com/comp2042/controller/GameInputHandlerTest.java`

**Test Coverage:** 55 test methods across 14 test classes

**Rationale:** Prevents regressions during refactoring; verifies edge cases automatically.

---

### 18. Progression System (Leveling)

**Description:** Level-based difficulty increase in Normal Mode

**Implementation:**
- Created `LevelManager` tracking progress: level increases every 1000 points
- Speed increases with each level (faster block falling)
- Updated `gameLayout.fxml` and `GameViewController` to display Level label (hidden in Challenge Mode)
- Added `showLevelUp()` animation to `NotificationPanel` (gold text with zoom effect)
- Implemented score multiplier: Base Score × Current Level for line clears

**Files Modified:**
- `src/main/java/com/comp2042/model/LevelManager.java` (New)
- `src/main/java/com/comp2042/controller/GameViewController.java`
- `src/main/java/com/comp2042/controller/gamemode/NormalModeController.java`

**Rationale:** Provides tangible progression and challenge; keeps gameplay engaging for longer sessions.

---

### 19. Game Loop Encapsulation

**Description:** Decoupled time-based logic from UI controller

**Implementation:**
- Created dedicated `GameLoop` class managing JavaFX `Timeline`
- Replaced loose boolean flags with `GameStatus` enum (`PLAYING`, `PAUSED`, `GAME_OVER`)
- Centralized start, stop, pause, and speed adjustment logic

**Files Modified:**
- `src/main/java/com/comp2042/util/GameLoop.java` (New)
- `src/main/java/com/comp2042/model/GameStatus.java` (New)
- `src/main/java/com/comp2042/controller/GameViewController.java`

**Rationale:** Adheres to SRP; makes speed control easier for leveling system; ensures consistent state management.

---

### 20. View Facade Pattern (GameRenderer)

**Description:** Facade pattern wrapping all rendering components

**Implementation:**
- Created `GameRenderer` class consolidating `BoardRender`, `ActivePieceRenderer`, `NextPieceRenderer`, `ShadowRender`, `LineClearAnimation`
- `GameViewController` now delegates all drawing to single entry point

**Files Modified:**
- `src/main/java/com/comp2042/view/renderers/GameRenderer.java` (New)
- `src/main/java/com/comp2042/controller/GameViewController.java`

**Rationale:** Simplifies controller by hiding rendering complexity; improves maintainability.

---

### 21. Enhanced Game Over Screen

**Description:** Full-screen Game Over overlay with dimming effect

**Implementation:**
- Refactored game over UI to use FXML definition in `gameLayout.fxml`
- Removed `GameOverPanel` Java class (replaced with FXML)
- Applied background dimming for clear session end signal
- Moved game over overlay to root `StackPane` to fix layering issues

**Files Modified:**
- `src/main/resources/gameLayout.fxml`
- `src/main/java/com/comp2042/view/GameUIManager.java`

**Rationale:** Provides unmistakable feedback; focuses attention on final score and options.

---

### 22. Hold Piece Mechanic

**Description:** Standard Tetris "Hold" functionality for storing pieces

**Implementation:**
- Updated `AbstractBoard` with `holdBrick()` method handling swap logic
- Implemented one-swap-per-turn restriction (resets after piece locks)
- Extended `ViewData` record to carry held brick state
- Added "HOLD" panel to `gameLayout.fxml`
- Updated `GameRenderer` with secondary `NextPieceRenderer` instance for held piece
- Mapped 'H' key in `GameInputHandler` to trigger hold action

**Files Modified:**
- `src/main/java/com/comp2042/model/board/AbstractBoard.java`
- `src/main/java/com/comp2042/view/renderers/GameRenderer.java`
- `src/main/java/com/comp2042/controller/GameInputHandler.java`
- `src/main/resources/gameLayout.fxml`

**Test Coverage:**
- `src/test/java/com/comp2042/model/board/HoldBlockTest.java` (New)

**Rationale:** Adds strategic depth; allows saving useful pieces for difficult situations; increases skill ceiling.

---

### 23. Advanced Refactoring (Template Method Pattern)

**Description:** Applied Template Method pattern to board logic

**Implementation:**
- Pulled up `mergeBrickToBackground` and `clearRows` methods into `AbstractBoard`
- Introduced protected hook methods (`onAfterMerge`, `onAfterClear`)
- `InvisibleBlocksBoard` overrides hooks for visibility toggling
- `TetrisBoard` now uses base implementation only

**Files Modified:**
- `src/main/java/com/comp2042/model/board/AbstractBoard.java`
- `src/main/java/com/comp2042/model/board/InvisibleBlocksBoard.java`
- `src/main/java/com/comp2042/model/board/TetrisBoard.java`

**Rationale:** Ensures identical core logic across game modes; reduces bug risk; subclasses only define differences.

---

### 24. Type Safety & Constants (Enums)

**Description:** Replaced magic numbers with enums and constants

**Implementation:**
- Introduced `BrickType` Enum for brick identification
- Updated `BrickColor` to use `EnumMap` for O(1) lookups
- Extracted hardcoded layout values into named constants (e.g., `VERTICAL_LAYOUT_OFFSET`)

**Files Modified:**
- `src/main/java/com/comp2042/model/bricks/BrickType.java` (New)
- `src/main/java/com/comp2042/view/BrickColor.java`
- `src/main/java/com/comp2042/view/renderers/ActivePieceRenderer.java`

**Rationale:** Compile-time safety; prevents errors from invalid IDs; self-documenting code.

---

### 25. Dependency Injection Architecture

**Description:** Replaced Singleton pattern with Dependency Injection

**Implementation:**
- Removed static `getInstance()` methods from `AudioManager`, `GameSettings`, `HighScoreManager`
- Updated `Main` class to instantiate services at startup
- Injected services into controllers via `SceneLoader`
- Updated unit tests for fresh service instances per test

**Files Modified:**
- `src/main/java/com/comp2042/Main.java`
- `src/main/java/com/comp2042/util/SceneLoader.java`
- `src/main/java/com/comp2042/controller/GameViewController.java`

**Rationale:** Eliminates global state; improves testability; makes dependencies explicit; allows easier mocking.

---

### 26. UI Logic Extraction (GameUIManager)

**Description:** Extracted UI overlay logic into dedicated manager

**Implementation:**
- Created `GameUIManager` handling Pause Menu, Game Over screen, and Notifications
- Resolved z-index issues using `StackPane` for proper layering
- Removed overlay logic from main controller

**Files Modified:**
- `src/main/java/com/comp2042/view/GameUIManager.java` (New)
- `src/main/java/com/comp2042/controller/GameViewController.java`

**Rationale:** Adheres to SRP; reduces controller complexity; simplifies UI navigation debugging.

---

### 27. Settings Panel

**Description:** User preferences management for audio and visual settings

**Implementation:**
- Created `SettingsController` and `settingsPanel.fxml`
- Integrated audio volume controls and ghost piece toggle
- Persisted settings via `GameSettings` class

**Files Modified:**
- `src/main/java/com/comp2042/controller/SettingsController.java` (New)
- `src/main/resources/settingsPanel.fxml` (New)
- `src/main/java/com/comp2042/util/GameSettings.java`

**Rationale:** Allows players to customize their gaming experience; settings persist across sessions.

---

### 28. Wall Kick 

**Description:** Allows pieces to rotate even when adjacent to walls or the floor

**Implementation:**
- Updated rotateLeftBrick() in AbstractBoard with offset testing logic
- Implemented sequence of checks:
1. Default rotation (0,0)
2. Kick Right (+1,0) - Fixes Left Wall collision
3. Kick Left (-1,0) - Fixes Right Wall collision
4. Floor Kick (0,-1) - Fixes Bottom collision
- Automatically applies the first valid offset found

**Files Modified:**
- `src/main/java/com/comp2042/model/board/AbstractBoard.java`

**Rationale:** Prevents pieces from getting stuck against walls; improves gameplay fluidity and responsiveness; matches modern Tetris standards.

---

## 4. Implemented but Not Working Properly

Implemented score notifications but not dynamic (should be dynamic) :
While the internal scoring system correctly calculates points (including the x2 multiplier for Challenge Mode and bonuses for Hard Drops) 
the visual notification pop-ups (e.g., "+50") display only the base score value. The notifications do not currently reflect the actual amount added to the player's total score after multipliers are applied, leading to inconsistent visual feedback.

---

## 5. Features Not Implemented

### 1. Power-ups and Special Blocks

**Reason for not implementing:**
- Fundamentally changes game balance and core Tetris mechanics
- Each power-up requires extensive individual testing and coding
- Difficult to maintain fair gameplay
- Additional UI elements needed for power-up display

**Examples Considered:**
- Bomb blocks, lightning strikes, time freeze
- Unbreakable blocks, ice blocks, explosive chains

---

### 2. Gravity Board Flipping Mode

**Reason for not implementing:**
- Fundamental physics engine rewrite required for dynamic gravity directions
- Buggy gravity roatation when block rotates
- Complex rotation logic adjustments when gravity changes (pieces fall left/right/up)
- Collision detection system overhaul needed for non-downward movement
- Significant testing required for all 4 gravity directions × 7 brick types = 28 unique behaviors
- Visual indicators and UI changes needed to show current gravity direction
- Need an overhaul to game over rules (when board flips triggering game over should be on top not bottom)

---

## 6. New Java Classes

### Controller Package (`com.comp2042.controller`)

#### 1. LevelSelectionController
- **Purpose:** Controls level selection screen navigation between Normal and Challenge modes
- **Location:** `src/main/java/com/comp2042/controller/LevelSelectionController.java`
- **Key Responsibilities:**
  - Handles button interactions for mode selection
  - Initializes appropriate game mode controllers
  - Manages scene transitions from main menu to game

#### 2. PauseMenuController
- **Purpose:** Manages pause menu overlay functionality
- **Location:** `src/main/java/com/comp2042/controller/PauseMenuController.java`
- **Key Responsibilities:**
  - Handles Resume, Restart, Settings, and Main Menu buttons
  - Coordinates with `GameViewController` for game state management
  - Manages audio settings during pause

#### 3. HighScoreController
- **Purpose:** Displays high score leaderboard
- **Location:** `src/main/java/com/comp2042/controller/HighScoreController.java`
- **Key Responsibilities:**
  - Renders top 10 scores from `HighScoreManager`
  - Provides back navigation to main menu
  - Formats score display with rankings

#### 4. ControlsController
- **Purpose:** Displays control scheme with visual keyboard assets
- **Location:** `src/main/java/com/comp2042/controller/ControlsController.java`
- **Key Responsibilities:**
  - Loads and displays keyboard key images
  - Shows current control mappings
  - Provides intuitive control reference for players

#### 5. GameInputHandler
- **Purpose:** Centralized keyboard input processing
- **Location:** `src/main/java/com/comp2042/controller/GameInputHandler.java`
- **Key Responsibilities:**
  - Maps keyboard inputs to game commands
  - Handles Arrow Keys, WASD, H (hold), P/ESC (pause)
  - Respects game state (blocks input when paused/game over)

#### 6. SettingsController
- **Purpose:** Manages user preferences and settings UI
- **Location:** `src/main/java/com/comp2042/controller/SettingsController.java`
- **Key Responsibilities:**
  - Handles audio volume controls
  - Manages ghost piece visibility toggle
  - Persists settings via `GameSettings`

### Game Mode Controllers (`com.comp2042.controller.gamemode`)

#### 7. AbstractGameController
- **Purpose:** Abstract base class for game mode controllers
- **Location:** `src/main/java/com/comp2042/controller/gamemode/AbstractGameController.java`
- **Key Responsibilities:**
  - Centralizes shared logic (movement, rotation handling)
  - Provides template methods for mode-specific behavior
  - Reduces code duplication between Normal and Challenge modes

#### 8. NormalModeController (renamed from GameController)
- **Purpose:** Controls Normal Mode gameplay
- **Location:** `src/main/java/com/comp2042/controller/gamemode/NormalModeController.java`
- **Key Responsibilities:**
  - Manages standard Tetris game loop
  - Handles leveling and speed progression
  - Implements score multiplier: Base Score × Current Level
  - Integrates audio triggers for gameplay events

#### 9. ChallengeModeController (renamed from GameControllerChallenge)
- **Purpose:** Controls Challenge Mode gameplay with invisible blocks
- **Location:** `src/main/java/com/comp2042/controller/gamemode/ChallengeModeController.java`
- **Key Responsibilities:**
  - Forces background refresh on every tick for visibility timer
  - Implements 2x score multiplier
  - Manages countdown notifications

### Model Package (`com.comp2042.model`)

#### 10. InvisibleBlocksBoard
- **Purpose:** Board implementation for Challenge Mode
- **Location:** `src/main/java/com/comp2042/model/board/InvisibleBlocksBoard.java`
- **Key Responsibilities:**
  - Dual-board architecture (logical + render boards)
  - Timer system for block visibility toggling
  - Countdown calculation for UI notifications

#### 11. AbstractBoard
- **Purpose:** Abstract base class extracting common board logic
- **Location:** `src/main/java/com/comp2042/model/board/AbstractBoard.java`
- **Key Responsibilities:**
  - Provides shared movement, rotation, and collision detection
  - Defines template methods for brick merging and line clearing
  - Implements hook methods for subclass-specific behavior

#### 12. TetrisBoard (renamed from SimpleBoard)
- **Purpose:** Standard Tetris board implementation
- **Location:** `src/main/java/com/comp2042/model/board/TetrisBoard.java`
- **Key Responsibilities:**
  - Implements standard Tetris rules
  - Extends `AbstractBoard` with default hook implementations

#### 13. LevelManager
- **Purpose:** Manages level progression system
- **Location:** `src/main/java/com/comp2042/model/LevelManager.java`
- **Key Responsibilities:**
  - Tracks score thresholds for level ups
  - Calculates speed multipliers
  - Notifies controllers of level changes

#### 14. BrickType (Enum)
- **Purpose:** Type-safe brick identification
- **Location:** `src/main/java/com/comp2042/model/bricks/BrickType.java`
- **Values:** `I_BRICK`, `J_BRICK`, `L_BRICK`, `O_BRICK`, `S_BRICK`, `T_BRICK`, `Z_BRICK`

#### 15. GameStatus (Enum)
- **Purpose:** Type-safe game state management
- **Location:** `src/main/java/com/comp2042/model/GameStatus.java`
- **Values:** `PLAYING`, `PAUSED`, `GAME_OVER`

### View Package (`com.comp2042.view`)

#### 16. LineClearAnimation
- **Purpose:** Handles line clear visual effects
- **Location:** `src/main/java/com/comp2042/view/LineClearAnimation.java`
- **Key Responsibilities:**
  - Creates flash animation sequence (`FadeTransition`)
  - Provides callback system for state synchronization
  - Dynamically scales based on board width

#### 17. GameUIManager
- **Purpose:** Manages UI overlays (pause menu, game over, notifications)
- **Location:** `src/main/java/com/comp2042/view/GameUIManager.java`
- **Key Responsibilities:**
  - Handles `StackPane` layering for proper z-index
  - Coordinates pause menu display/hiding
  - Manages notification panel animations

#### 18. BrickColor
- **Purpose:** Centralized color mapping for bricks
- **Location:** `src/main/java/com/comp2042/view/BrickColor.java`
- **Key Responsibilities:**
  - Maps `BrickType` enum to JavaFX `Paint` objects using `EnumMap`
  - Provides O(1) color lookups
  - Generates shadow colors for ghost pieces

### View Renderers Package (`com.comp2042.view.renderers`)

#### 19. GameRenderer
- **Purpose:** Facade pattern consolidating all rendering components
- **Location:** `src/main/java/com/comp2042/view/renderers/GameRenderer.java`
- **Key Responsibilities:**
  - Wraps `BoardRender`, `ActivePieceRenderer`, `NextPieceRenderer`, `ShadowRender`
  - Provides single `render()` entry point
  - Simplifies `GameViewController` by hiding rendering complexity

#### 20. ActivePieceRenderer
- **Purpose:** Handles visualization of falling bricks
- **Location:** `src/main/java/com/comp2042/view/renderers/ActivePieceRenderer.java`
- **Key Responsibilities:**
  - Renders current brick with full opacity
  - Removes previous brick positions
  - Works with `RectangleRenderer` for styling

#### 21. NextPieceRenderer
- **Purpose:** Displays upcoming brick preview
- **Location:** `src/main/java/com/comp2042/view/renderers/NextPieceRenderer.java`
- **Key Responsibilities:**
  - Dynamically centers next piece in preview box
  - Calculates bounding box for accurate offsets
  - Handles multiple next piece displays (next and hold)

#### 22. BoardRender
- **Purpose:** Handles rendering of the static game board background
- **Location:** `src/main/java/com/comp2042/view/renderers/BoardRender.java`
- **Key Responsibilities:**
  - Renders locked blocks on the game board
  - Updates board display when blocks are placed

#### 23. ShadowRender
- **Purpose:** Renders ghost piece shadow preview
- **Location:** `src/main/java/com/comp2042/view/renderers/ShadowRender.java`
- **Key Responsibilities:**
  - Displays semi-transparent shadow indicating landing position
  - Respects ghost piece visibility setting

### Util Package (`com.comp2042.util`)

#### 24. SceneLoader
- **Purpose:** Centralized FXML loading and scene management
- **Location:** `src/main/java/com/comp2042/util/SceneLoader.java`
- **Key Responsibilities:**
  - Provides reusable methods for scene transitions
  - Handles FXML path resolution and error handling
  - Injects service dependencies into controllers

#### 25. GameLoop
- **Purpose:** Encapsulates game timing and animation logic
- **Location:** `src/main/java/com/comp2042/util/GameLoop.java`
- **Key Responsibilities:**
  - Manages JavaFX `Timeline` lifecycle
  - Controls game speed adjustments for leveling
  - Handles pause/resume state transitions

#### 26. AudioManager
- **Purpose:** Centralized audio system management
- **Location:** `src/main/java/com/comp2042/util/AudioManager.java`
- **Key Responsibilities:**
  - Background music control with volume management
  - Sound effect playback for game events
  - Mute/unmute functionality

#### 27. HighScoreManager
- **Purpose:** Manages persistent high score data
- **Location:** `src/main/java/com/comp2042/util/HighScoreManager.java`
- **Key Responsibilities:**
  - Serializes/deserializes top 10 scores to `highscores.txt`
  - Maintains sorted score rankings
  - Validates and filters score entries

#### 28. GameSettings
- **Purpose:** Manages user preferences and settings persistence
- **Location:** `src/main/java/com/comp2042/util/GameSettings.java`
- **Key Responsibilities:**
  - Stores audio volume preferences
  - Manages ghost piece visibility setting
  - Provides settings persistence

---

## 7. Modified Java Classes

### Controller Package

#### 1. GameViewController (renamed from GuiController)

**Original State:** Was very messy handling multiple things at once not acting as a "GameViewController"

**Major Refactoring:**
- Extracted specialized components:
  - `GameRenderer`: Facade for all rendering operations
  - `GameInputHandler`: Keyboard input processing
  - `GameUIManager`: UI overlay management

**Current Responsibilities:**
- Orchestrates specialized UI component managers
- Manages game state (pause, game over)
- Handles FXML-injected elements
- Coordinates level transitions and notifications
- Manages sound system integration
- Scene and stage reference management
- Score binding with properties
- Game timer lifecycle control

**Files Modified:**
- `src/main/java/com/comp2042/controller/GameViewController.java`

**Rationale:** Transitioned from God Object anti-pattern to Delegation pattern; improved testability, maintainability, and readability.

---

#### 2. MainMenuController

**Changes:**
- Enabled Settings button (previously disabled in FXML)
- Integrated saved audio preferences from `GameSettings`
- Added background music startup with persisted volume
- Implemented button press SFX triggers
- Added `openSettings()` method with callback for scene restoration
- Ensured proper game controller instantiation after loading game layout

**Files Modified:**
- `src/main/java/com/comp2042/controller/MainMenuController.java`

**Rationale:** Enhanced audio initialization and settings navigation; improved scene management consistency.

### Model Package

#### 3. TetrisBoard (formerly SimpleBoard)

**Changes:**
- Fixed matrix initialization from `[width][height]` to `[height][width]`
- Changed spawn position from Y=19 (middle) to Y=1 (top with buffer)
- Implemented `hardDrop()` method with distance calculation
- Added `getShadowYPosition()` for ghost piece calculation
- Optimized shadow calculation by moving `MatrixOperations.copy()` outside loop
- Implemented `holdBrick()` swap logic with one-swap-per-turn restriction
- Updated `getViewData()` to include ghost position and held brick data

**Files Modified:**
- `src/main/java/com/comp2042/model/board/TetrisBoard.java`

**Rationale:** Critical bug fixes, performance optimizations, and support for new gameplay mechanics.

---

#### 4. Board (Interface)

**Changes:**
- Converted from concrete class to interface
- Added method signatures: `hardDrop()`, `getShadowYPosition()`, `holdBrick()`, `getHoldBrickShape()`
- Defined contract for all board implementations

**Files Modified:**
- `src/main/java/com/comp2042/model/board/Board.java`

**Rationale:** Better OOP design allowing multiple board implementations (Normal vs. Challenge Mode).

---

#### 5. MatrixOperations

**Changes:**
- Fixed `checkOutOfBound()` boundary bug allowing negative Y coordinates
- Improved row-shifting logic in `checkRemoving()` for consistent line clearing
- Updated `checkRemoving()` to collect cleared row indices into `List<Integer>`
- Enhanced `merge()` method with better error handling

**Files Modified:**
- `src/main/java/com/comp2042/util/MatrixOperations.java`

**Rationale:** Critical bug fixes preventing crashes and data loss; enhanced data model for animation system.

---

#### 6. Score

**Changes:**
- Added property binding for UI updates (`IntegerProperty`)
- Implemented level multiplier system (used in `NormalModeController`)
- Added bonus calculation methods

**Files Modified:**
- `src/main/java/com/comp2042/model/Score.java`

**Rationale:** Enhanced scoring mechanics; automatic UI synchronization via property binding.

---

#### 7. ViewData

**Changes:**
- Added `ghostYPosition` field for shadow preview
- Changed `nextBrickData` from `int[][]` to `List<int[][]>` for multiple next pieces
- Added `holdBrickData` field for hold mechanic
- Converted to Java record for immutability

**Files Modified:**
- `src/main/java/com/comp2042/model/state/ViewData.java`

**Rationale:** Support for new visual features; ensures data immutability following modern Java practices.

---

#### 8. ClearRow

**Changes:**
- Added `clearedRowIndices` field (`List<Integer>`)
- Implemented getter method for row data
- Converted to Java record for immutability

**Files Modified:**
- `src/main/java/com/comp2042/model/state/ClearRow.java`

**Rationale:** Provides precise spatial information for animation system without breaking MVC separation.

---

#### 9. BrickRotator

**Changes:**
- Package relocation to `model.bricks`
- Implemented wall kick improvements
- Added rotation state tracking for enhanced collision detection
- Better documentation with Javadoc

**Files Modified:**
- `src/main/java/com/comp2042/model/bricks/BrickRotator.java`

**Rationale:** Better organization; smoother rotation mechanics matching modern Tetris implementations.

### View Package

#### 10. NotificationPanel

**Changes:**
- Added `showCountdown()` method for Challenge Mode timer warnings
- Implemented distinct Red/Zoom animation for countdown (vs. standard score notifications)
- Enhanced animation system with auto-dismiss functionality
- Added `showLevelUp()` method with gold text and zoom animation
- Added multiple notification types support

**Files Modified:**
- `src/main/java/com/comp2042/view/NotificationPanel.java`

**Rationale:** Improved UX by providing necessary feedback for visibility state changes in Challenge Mode and level progression.

### Resource Files

#### 11. gameLayout.fxml

**Changes:**
- Added score label element and level display
- Added timer display area for Challenge Mode
- Implemented ghost piece layer
- Added pause menu overlay container
- Implemented "HOLD" panel for hold mechanic
- Added "NEXT" panels for upcoming pieces
- Updated layout from single `Pane` to `HBox` with sidebar
- Replaced `GameOverPanel` Java class with FXML definition

**Files Modified:**
- `src/main/resources/gameLayout.fxml`

**Rationale:** Support for rich game interface with all new UI elements properly structured.

---

#### 12. window_style.css

**Changes:**
- Added enhanced visual effects (shadows, gradients)
- Defined animation keyframes for notifications
- Implemented button hover states with color transitions
- Added gradient backgrounds for panels
- Enhanced font styling and sizing
- Added rounded corner styles for modern aesthetics
- Added game over box styling

**Files Modified:**
- `src/main/resources/window_style.css`

**Rationale:** Professional appearance consistent with modern game design standards.

### Brick Classes

#### 13. All Brick Classes (IBrick, JBrick, LBrick, OBrick, SBrick, TBrick, ZBrick)

**Changes:**
- Enhanced color system with vibrant, distinct colors
- Improved rotation matrices for smoother rotations
- Package restructuring to `model.bricks`
- Updated to use `BrickType` enum instead of raw integers
- Consistent code formatting and documentation
- Better adherence to interface contracts

**Files Modified:**
- `src/main/java/com/comp2042/model/bricks/[All brick classes]`

**Rationale:** Visual improvements; better organization following package structure refactoring.

---

## 8. Unexpected Problems

### 1. Core Dimension Bug Causing Index Out-of-Bounds Errors

**Problem Description:** Game crashed frequently with `ArrayIndexOutOfBoundsException` during collision detection and block placement, especially when board wasn't perfectly square.

**Root Cause:** `TetrisBoard` matrix was incorrectly initialized as `[width][height]` instead of the standard `[height][width]` (rows × columns) convention.

**Investigation Process:**
1. Added extensive logging to collision detection methods
2. Used debugger to inspect array dimensions during crashes
3. Discovered mismatch between logical grid and visual representation
4. Found inconsistent dimension ordering in game controllers

**Resolution:**
- Corrected `TetrisBoard` array initialization to `[height][width]`
- Updated all matrix access patterns throughout codebase
- Adjusted game controller dimension handling to maintain consistency
- Added validation checks to prevent future dimension mismatches

**Impact:**
- Eliminated random crashes during gameplay
- Stable collision detection across all board configurations
- Better understanding of matrix conventions in game development

**Lessons Learned:** Always follow standard conventions (rows first, columns second) to prevent subtle but critical bugs.

---

### 2. Ghost Piece Performance Degradation

**Problem Description:** Frame rate dropped significantly when ghost piece feature was enabled, especially with complex board states containing many placed blocks.

**Performance Analysis:**
- Profiled application using Java VisualVM
- Identified `calculateGhostY()` as hot spot consuming of frame time
- Found redundant calculations occurring every frame regardless of piece movement
- Discovered excessive memory allocation from repeated `MatrixOperations.copy()` calls

**Optimization Strategy:**
1. **Caching:** Stored calculated ghost positions and only recalculated on piece movement
2. **Lazy Evaluation:** Implemented dirty flag system to skip calculations when piece hasn't moved
3. **Early Exit:** Added collision detection that stops checking immediately upon finding lowest valid position
4. **Memory Pooling:** Moved `MatrixOperations.copy()` outside the calculation loop (was creating ~60 copies per second, reduced to ~2-3 per piece movement)

**Resolution:** Refactored `TetrisBoard.getShadowYPosition()` to:
```java
// Before: Copy board matrix inside while loop (60+ times per second)
// After: Copy once outside loop, reuse for all collision checks
int[][] tempMatrix = MatrixOperations.copy(currentGameMatrix); // MOVED OUTSIDE
while (!collision) {
    // Use same tempMatrix for all checks
}
```

**Results:**
- Reduced CPU usage during active gameplay
- Eliminated frame drops during ghost piece rendering

**Lessons Learned:** Always profile before optimizing; seemingly simple features can have significant performance impacts when called frequently.

---

### 3. Scoring Logic Triggered by Key Press Instead of Actual Movement

**Problem Description:** Players could accumulate points by repeatedly pressing the Down Arrow key even when the piece couldn't move further down, leading to score exploitation and unfair gameplay.

**Investigation Process:**
1. Noticed discrepancy between visual movement and score increases
2. Traced score increment calls in game controllers
3. Discovered `onDownEvent()` adding score on every key press event
4. Found missing validation of actual brick movement success

**Root Cause:** Score was incremented based on raw `onDownEvent()` trigger rather than successful `moveBrickDown()` execution result.

**Resolution:** Refactored scoring logic to check `DownData` return value:
```java
// Before
onDownEvent() {
    score.addScore(1); // Always added
    board.moveBrickDown();
}

// After   
onDownEvent() {
    DownData result = board.moveBrickDown();
    if (result.moved) { // Only add if actually moved
        score.addScore(1);
    }
}
```

**Impact:**
- Fair scoring system consistent with Tetris rules
- Players only rewarded for successful movements
- Eliminated score exploitation possibility

**Lessons Learned:** Always validate state changes before awarding points; separate input events from actual game state modifications.

---

### 4. FXML Button Icon Rendering Issues

**Problem Description:** Pause and Restart button icons (PNG files) either:
- Didn't display at all
- Appeared distorted/stretched
- Failed to resize according to FXML dimensions
- Lost quality when scaled

**Investigation Process:**
1. Verified PNG files loaded correctly (file paths were valid)
2. Tested different FXML sizing approaches (`fitWidth`, `fitHeight`, `preserveRatio`)
3. Discovered FXML-defined sizes not applying to `ImageView` children
4. Found JavaFX rendering quirks with FXML-embedded images

**Attempted Solutions:**
1. **FXML-only approach:** Set dimensions directly in `gameLayout.fxml` (failed - ignored at runtime)
2. **CSS styling:** Applied size constraints via `window_style.css` (partially worked but inconsistent)
3. **Inline styles:** Used style attributes in FXML (caused maintainability issues)

**Final Resolution:** Defined `ImageView` directly in `gameLayout.fxml` with proper nesting, eliminating need for programmatic setup.

**Impact:**
- Clean, consistent button appearance
- Proper icon scaling across window sizes
- Error handling prevents UI breaking if images missing

**Lessons Learned:** JavaFX FXML has quirks with image rendering; FXML-first approach preferred when functional.

---

## Comprehensive Testing Documentation

### Test Coverage Summary

**Total Tests:** 55 test methods across 14 test classes

### Test Structure

All tests follow JUnit 5 framework with comprehensive assertions and parameterized tests for edge cases.

**Test Location:** `src/test/java/com/comp2042/`

### Test Files

- `src/test/java/com/comp2042/model/board/TetrisBoardTest.java`
- `src/test/java/com/comp2042/model/board/InvisibleBoardTest.java`
- `src/test/java/com/comp2042/util/MatrixOperationsTest.java`
- `src/test/java/com/comp2042/model/bricks/BrickRotatorTest.java`
- `src/test/java/com/comp2042/model/bricks/BrickTest.java`
- `src/test/java/com/comp2042/model/LevelManagerTest.java`
- `src/test/java/com/comp2042/model/ScoreTest.java`
- `src/test/java/com/comp2042/model/DataImmutabilityTest.java`
- `src/test/java/com/comp2042/model/board/HoldBlockTest.java`
- `src/test/java/com/comp2042/model/ShadowLogicTest.java`
- `src/test/java/com/comp2042/model/BoardStateTest.java`
- `src/test/java/com/comp2042/util/HighScoreManagerTest.java`
- `src/test/java/com/comp2042/util/GameSettingsTest.java`
- `src/test/java/com/comp2042/controller/GameInputHandlerTest.java`


completed by Ahmed Mohamed Abdelhamid Gobran Abdelhalim - 20580127
