For Milestone 2, the two most prominent changes are the addition of Unit testing and the inclusion of GUI elements in the View of the game.

Initially, the [ASCIIView](../src/mainPackage/ASCIIView.java) class was exactly what it sounded like: a class that contained several 
methods of physically drawing a board and assorted splash art using ASCII characters. With the new version of the game needing an updated 
visual component, a new interface View(..src/mainPackage/View.java) was created to incorporate drawing the board, announcing information 
to the player and creating the Menu, Game over and Win screens. Next, the [GraphicsView](../src/mainPackage/GraphicsView.java) class was 
created, and incorporates thejava.awt and javax.swing libraries studied in lectures and labs to create a more user-friendly interface that 
goes beyond entering and interacting primarily with text areas. This new class still draws and updates the board as needed, but now uses a 
series of buttons to act as input from the player, with additional image icons to replace the simple Xs and blanks of Milestone 1. These 
buttons range from plant buttons that a player can select and place on the board, to board buttons that each manage a tile on the board's 
grid. When a user clicks a board button, a [Controller] (../src/mainPackage/Controller.java) method informs the user about what is on that 
tile. If the user clicks a plant button followed by a board button, the Controller instead attempts to plant that respective entity on the 
selected tile, so long as it is unoccupied.

Naturally, with changes in the view, so too must the Controller change. Specifically, a new class [StringUtils](..src/mainPackage/StringUtils.java) 
was created to replace the parseText method in the Controller to optimize cohesion and reorganize structure. Furthermore, the Controller 
now properly references View instead of ASCIIView, and the Controller was modified to accommodate for this shift by making several of its 
methods public and making inputs for those methods simpler, as text-based entry was no longer required. However, the essence of the 
controller remains, as it is still responsible for advancing the turns of each entity on the board, and for keeping track of the zombie 
count for the current level.

In terms of unit testing, a separate package was created containing test classes for every class not directly associated with the MVC 
model. These classes are made up of a series of test methods, each with a purpose of ensuring a specific method in its respective main 
class is behaving properly. This ranges from checking constructor values to confirming setting/getter methods work, but the central test 
class is most notably [TestBoard](../src/tests/TestBoard.java) The Board class, responsible for monitoring the position of every entity 
on top of the total sun the player has accumulated, needed to be thoroughly checked to make sure placing, moving and removing entities 
functioned as desired, in addition to spending and adding sun to/from the player's total.

Finally, the necessary Class Diagrams were recreated to accommodate for the changes made in milestone 2. For the following iteration,
most if not all Known Issues will ideally be resolved, as currently Sunflowers are invincible and there is not yet a level editor or
means to load/save progress to a file. For more details on upcoming changes, consult the [ReadMe's](../README.md) roadmap section.
