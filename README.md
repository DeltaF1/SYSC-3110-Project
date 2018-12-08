# SYSC-3110-Project
_Plants vs. Zombies Clone for SYSC 3110 Fall 2018_
GitHub URL:https://github.com/DeltaF1/SYSC-3110-Project

## Authors
Ehren:
- Added save state loading/saving
- Added sequence diagrams
- Updated manual

Trevor:
- Added numerous test cases
- Created Level and EditableLevel
- Created level editor GUI
- Added sequence diagrams
- Added new design desisions

Aldous:
- Integrated the Level system into the existing board spawning system
- Fixed undo/redo with serialization
- Added level selection screen
- Added class diagrams
- Fixed up test conflicts

Sam:
- Was unresponive for the duration of Milestone 4

## Known issues

Known issues can be found at https://github.com/DeltaF1/SYSC-3110-Project/issues

- Turns are offset by 1
- Tests are wonky
- Saving in main menu creates corupt save file

## Changes

### Milestone 4
- Added a level system to make zombie spawning unique per level
- Added a level loading screen
- Added game saving/loading

### Milestone 3
- Implemented XML serilazation, and used this capability to implement undo/redo
- Added new zombie + plant types
- Added a more robust zombie spawning system

### Milestone 2

- Added GUI layer on top of existing text-based skeleton via View interface and GraphicsView class
- Created separate package for Unit testing (Model)
- Updated Diagrams to match new content

## Roadmap

### To be met by November 11th, 2018:

- GUI layer on top of text-based skeleton
- Unit testing (Model)
- Extraneous changes to UML/data structures

### To be met by November 25th, 2018:

- Complete implementation
- Unlimited undo/redo actions available
- Multiple unique entities

### To be met by December 7th, 2018:

- Save and load features
- Game level builder(XML)

OPTIONAL: Real-time version / port to android



