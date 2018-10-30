# Really Terrible Plants VS Zombies


When you start the game you will be greeted with a splash screen. Type the command `start` and press enter to start the game.  

Once the game begins you will have 150 sun points. If you don't plant a sunflower using these points you will almost certainly lose.  
To plant a sunflower type `place sunflower 0 0` and press enter. A [x] symbol will appear in the top left of the screen. This is your sunflower.  

To advance the game to the next state, enter the command `done`.  Zombies (represented by a [z] symbol) should appear on the right of the map.  

Now that you understand how the game works, here are a list of commands: 

## Start
`start`
Starts the game
Only works on the home screen

## Place
`place <plantName> <x> <y>`  
Places a plant at the specified location

* **plantName**: name of the plant you're placing. options include:
	* _sunflower_ (a plant that produces 50 sun every turn)
	* _proj_ (a plant that shoots any zombie standing in front of it)
* **x**: x coordinate to place the plant
* **y**: y coordinate to place the plant

## Info
`info <x> <y>`
Retrieves information about the entity at the specified location

* **x**: x coordinate to inspect
* **y**: y coordinate to inspect

## Done
`done`
Completes your turn