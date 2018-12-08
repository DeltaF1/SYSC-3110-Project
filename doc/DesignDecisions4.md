#Design Decisions

## Level and EditableLevel
levels are used to store information about a game level, such as the what zombies to spawn when. The level editor needs to make modifications to a Level. Originally this was achieved by just having a Level stored in the model board and having the level editor call functions in board through the controller. It was realised this was a poor idea because it broke the single responsibility principle. To solve this issue, the EditableLevel class was made. It is a level that also updates views about it's contents. This means that the level editor GUI calls functions in the controller that then just has to calls functions in an EditableLevel instead of the board which makes much more sense and is a proper MVC solution.

Additionally, we made levels that are saved to disk use XML because this is what board states already do. This made writting the new code easier and made the project more consistent and maintainable.

## ZombieSpawnSettings
the ZombieSpawnSettings object simply stores the name of the type of the zombie to be spawned and the turn it is to be spawned on. This class is simple but was needed to allow a zombies settings to be the return value of a function
