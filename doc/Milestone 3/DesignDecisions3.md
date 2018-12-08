# Design Decisions

## Save files / Serialization
We decided to use XML serialization for our game states since we couldn't find any Java JSON libraries to our liking. Additionally, we want it to be easy for us to inspect levels by hand and reverse-engineer them if something is wrong. Choosings to use XML instead of Java native serialization allows for the easy creation of external level-editing tools, not only those written in Java

## Undo/Redo
We decided to re-use our serialization implementation to save the current game state for undo/redo purposes. While this generates ome memory overhead, it simplifies our model signficiantly, since we can simply create a snapshot of the state at every turn.

## Zombie Spawning
The zombie spawning system was revamped in order to allow for undo/redo, as well as allowing the spawning of different types of zombies.

## MVC
We realized that we had been following a model closer to Model-View-Presenter than Model-View-Controller, and so we adjusted our model to notify the view instead of the controller notifying the view. This was relatively simple, and merely required setting up a list of views that are registered with the model, and notifying them to update specific entities when the model was changed.
