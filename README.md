# Battleship

The battleship game involves two players. At beginning, each player can place five ships on their ocean grid. During the game, each player take turns firing shots to try to hit opponent's ships. The game ends when one player sink 5 ships of the other player.  

## How to run

1. Download the zip. Unzip in a folder.
2. Open the folder in an Java code editor(IntelliJ, Eclipse...)
3. Run 2 instances of `Client.java`, and run 1 instance of `Server.java`. Make sure your localhost doesn't not take port `1216` .
4. Game may start.

## Game Instruction

- Start placing ship by clicking menubar Game &rarr; Play. 
  - Enter the row and column number and hit Place button.
  - View the console for status message. e.g. which ship is about to be placed.
- After all ship have been placed, server can be connected by clicking menubar Game &rarr; Connect
- After both player connected, the Attack button is enabled. Whoever first connect to the server would move first.
- Upon a successful "hit", the player is able to attack again.
  - ...until a "miss", or a "ship sinking" is detected after the attack
  - The result of the attack is indicated using the grid's color:
  - :large_blue_diamond: Blue: Miss
  - :white_circle: White: Unexplored...
  - :green_heart: Green: Your ship's position
  - :red_circle: Red: Hit
  - :black_circle: Black: ship sink.
- When all ship of a player is sunk, the game will end. 
