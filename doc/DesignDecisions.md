For Milestone 1, some of the most notable data structures found in group 19's project include Linked lists, 
Hashmaps, and 2 dimensional arrays.

In order to populate the game with Plant and Zombie entities, the EntityFactory class utilizes two private Hashmaps and an enumerated
entity type of either Plant or Zombie. The setup of the EntityFactory class is perfect for a Hashmap's key-value pair, as in this case
each type of Plant/Zombie entity (the 'value') is associated with a distinct String (the 'key'). As a result, should additional entities
be added in future, they can be included in EntityFactory's register() method, ensuring new Plant/Zombie types can be added simply and easily.
In this way, the EntityFactory can be entirely responsible for the creation of new Plants and Zombies, casting objects as needed and calling
their respective constructors from within its makeEntity method. A HashMap was chosen rather than a TreeMap because we do not care about the subclasses being ordered, and elements will never need to be removed from the Map. 

However, once these entities have been created, they need to be placed on a board, and their locations (along with their personal fields 
such as Hp and cooldown) need to be monitored. An easy way that the Board class keeps track of all relevant entities is by creating a
2 dimensional array of tile objects, each of which can hold an occupant (an entity). This data structure choice not only ensures that no
two entities will occupy the same space on the board at a given time, but also makes it easy to determine what is on a tile by simply 
indexing that location of the array via an x and y coordinate. 

Finally, linked lists were used in the Level class, where a private class Wave records the current turn and number of Zombies on the board.
In the game, hordes of Zombies spawn throughout a given level, and a Linked list is a simple way to implement the game's pattern of 
continous, consecutive waves. By using an iterator to continually increment the current wave, the Level class is responsible for determining
when more Zombies should spawn and for keeping track of how many are on the board at a given time, while also making sure more Zombies don't
spawn at inopportune or unfair times. 

Though Milestone 1 primarily deals with the 'Model' of the MVC implementation, a view and controller are currently in place and function as
their respective names imply: the ASCIIView class primarily deals with converting and drawing the board and its entities, and outputting them
to the console for the player to see using Strings and characters to represent tiles, Plants and Zombies. Additionally, ASCIIView can announce
information to the player, informing them when a change has been made, or something should be brought to their attention. 

Conversely, the Controller in charge of the game as a whole, and is the central hub where the game is physically run. It is here that a new
board is created, the ASCIIView art is drawn and invoked, the Level is recorded and the number of zombies kept track of and slowly decremented
until a player has beaten that level. The Controller identifies whether or not a game is in progress, has been won or lost, or if the player
is currently in a menu. It also allows players to plant on a select tile, or specify what is currently occupying a given tile. Finally, it
is responsible for advancing the turn, or action, of each individual Plant/Zombie on the board, updating their respective hitpoints, positions
and cooldowns. 
