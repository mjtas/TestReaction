# Reaction Time Tester
A console-based reaction time testing game built with modern Java design patterns and enterprise-level architecture.
## Features
### Core Functionality
- Measure your reaction speed across multiple rounds
- Comprehensive performance tracking and reporting
- Customisable game parameters through Builder pattern
- Complete game session tracking with timestamps

### Features
- Clean state management with proper transitions
- Asynchronous Reporting (non-blocking statistics generation)
- Thread-Safe Operations (concurrent data structures where appropriate)
- Built-in logging for debugging and monitoring
- Data classes following immutability principles

### Architecture
- State, Builder, Strategy patterns
- Single responsibility, Open/closed, Dependency inversion
- Functional Programming: Streams, Optional, CompletableFuture
- Separation of concerns, dependency injection
- Package organisation and modularity

### Getting Started
#### Prerequisites
- Java 11+ - Download from Oracle or OpenJDK
- Gradle 7.0+ or Maven 3.6+ - For building the project
- JLine 3.21.0 - For enhanced console input (automatically handled by build tools)

#### Installation
- Clone the repository
```
git clone git@github.com:mjtas/TestReaction.git
cd TestReaction
```
#### Build and Run

- Using Gradle:
```
./gradlew build
./gradlew run
```
- Alternative: using Maven
```
mvn clean compile
mvn exec:java -Dexec.mainClass="com.reactionmachine.ReactionMachineApp"
```
- Alternative: Direct Java Execution
```
javac -cp "lib/*" src/main/java/com/reactionmachine/*.java src/main/java/com/reactionmachine/*/*.java
java -cp "lib/*:src/main/java" com.reactionmachine.ReactionMachineApp
```
### How to Play

- Run the application to see the welcome screen
- Insert Coin: Press SPACE + ENTER to start a new game
- Press GO: Press ENTER to begin the reaction test
- Watch for the display to change from "Wait..." to "0.00"
- Press ENTER as quickly as possible when you see the timer start
- Repeat for 3 rounds to get your average reaction time
- Your performance is automatically tracked and displayed

#### Controls

SPACE + ENTER: Insert coin
ENTER: Go/Stop action
ESC + ENTER: Exit application

### Configuration
Customise game behavior using the GameConfiguration.Builder:
```
javaGameConfiguration config = new GameConfiguration.Builder()
.maxAttempts(5)              // Number of reaction tests per game
.timeoutSeconds(15.0)        // Timeout for coin insertion state
.delayRange(200, 500)        // Random delay range (ms)
.maxReactionTime(3.0)        // Maximum reaction time before timeout
.build();

EnhancedReactionController controller = new EnhancedReactionController(config);
```

### Statistics & Reporting
The application provides comprehensive statistics:
- Individual Session Data: Reaction times, averages, best times
- Overall Performance across all games played
- Standard deviation, min/max values
- Generate detailed reports without blocking gameplay

Example Statistics Output
```
=== REACTION TIME STATISTICS ===
Generated: 2024-01-15T10:30:45

Games Played: 5
Total Reactions: 15
Average Time: 0.742 seconds
Best Time: 0.234 seconds
Worst Time: 1.456 seconds
Standard Deviation: 0.234
```

### Testing
Run the comprehensive test suite:
```
# Using Gradle
./gradlew test

# Using Maven
mvn test

# Run specific test class
./gradlew test --tests EnhancedReactionControllerTest
``` 
Test Coverage:
- Unit Tests: Individual component testing
- Integration Tests: Full game flow validation
- Edge Case Tests: Boundary conditions and error scenarios
- Configuration Tests: Builder pattern validation