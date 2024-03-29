README for Breakout Variation:
By: Jesse A. Jones

Added Features:
	Added a controllable paddle that moves back 
	and forth based on holding A or D key.

	The movement of the paddle is influenced by the level and ball speed
	in the sense that the paddle moves faster based on the level and ball speed.
	This will be further elaborated later.

	Added a ball that starts with a random range velocity vector
	that bounces around and hits stuff and causes the player to die
	when it hits the ground of the game.

    The game was scaled up to 800 by 800 from 800 by 600 in order
    to have more room for game elements.

	Added breakable bricks with varying amounts of health and varying models
	based on their health. Described more later.

	When the ball collides with the paddle or bricks, it gets 0.3 percent faster, 
	making the level get harder with time due to the ball getting overall faster.

	The ball also potentially starts faster based
	on the level the player is currently in.

	Added a death mechanic caused by the ball hitting the bottom of the screen.

	Lives were also added. The player starts with 3 lives and
	loses a life with a death. If the player runs out of lives, it's game over
	and the player has to restart.

	The collision system was greatly improved with quite the robust collision detection
	and response system that removes the old problems of the ball getting stuck in stuff
	and moving at faster than light speeds in walls occasionally.

	Levels were added. There's three levels. These levels affect the health of the bricks.

	Bricks have a health mechanic added. Bricks can have health ranging from 1 to 6.
	Also, for each health the brick has a specific texture associated like so:
		1 -> dark blue
		2 -> turquoise
		3 -> green
		4 -> yellow
		5 -> orange
		6 -> red
	When a brick is hit by the ball, it loses at least one health 
	and the color of the brick adjusts based on the updated health value.
	If the brick's health reaches 0, it explodes 
	and is gone until a level change, game victory, or game over.

	Each level also has some difficulty scaling going on.
	For one, the ball starts with a slightly faster velocity potentially.

	For two, the brick health increase rate becomes more frequent, morphing from
	brick health increasing every 30 bricks, to every 20, to every 10,
	meaning that on level one, the first 30 bricks have 1 health and the next 30 have 2.
	On level two the first 20 bricks have 1 health, the next 20 2, and the last 20, 3.
	Lastly, level 3 has bricks increasing from a health of 1 all the way to 6 with the
	health increasing every row.

	The paddle speed also has some interesting speed 
	scaling going on based on the level.
	When the player holds down A or D, the paddle moves at 10 pixels per frame
	times 1.1 to the power of the level - 1 plus the natural log of the magnitude
	of the ball's velocity. This means that the paddle gets faster with each level
	and gets faster as the ball gets faster. This helps compensate for the gradual
	speed increase of the ball.

	Set the terminal to dark theme, so it's readable, made it bigger,
	and added several cheat codes to it that can be explained
	using the "?" command in the terminal.
	The added codes are:
        die -> Instantly kills player and causes them to lose a life.
        exit -> Exits the game completely.
        setlevel -> Takes in a level number and if it's a valid
            number in range 1 to 3, it sets the level to that number.
            Otherwise, tells player it's not a valid level
            or displays usage string.
        winlevel -> Causes every brick in the level to explode,
            winning the level for the player.
        losegame -> Causes the player to instantly lose the game,
            entering the game over state causing the player to
            need to restart.
        addlife -> Adds a life to the lives counter.
        wingame -> The opposite of losegame: causes the player
            to instantly win the game, causing them to need to
            restart afterwards.
        godmode -> Makes it so the ball hitting the bottom of the
            screen doesn't trigger a death state, making the player
            immortal until it's disabled
            by typing "godmode" into the terminal again.

    On the topic of the HUD terminal, the edge case was fixed where if the player
    typed backspace enough the game would crash.
    There was also an empty line edge case that was fixed.

	Added a variety of custom text that indicates the game state.
	Examples include the "any key press" prompt, the game over message, etc.

	Added a variety of *custom sounds* for mechanics like the explosions,
	ball bounces, and announcements that go with the game messages.

	Replaced given ball with custom-made ball based on plasma noise render.

	Removed a lot of old unused assets. 

	Removed bounce counter which was replaced by the lives counter.

	Added game victory when the player wins level 3 by destroying all bricks existing.

	After a game over, game victory, and the start, the player is prompted for input and the game
	starts at level 1 with fresh data.

	Before the credits sequence, a splash screen fades in indicating the game name and author. 
	Skip-able by pressing the ESC key.

	The ball is affected by a very light downwards gravity field
	which prevents the ball from getting stuck in a permanent side to side motion cycle.
	For instance if the ball was heading side to side with velocity 600, 0, gravity would
	slowly decrease the y component, bringing it down slowly to the paddle.
 
Avoiding Broken Ball Movement:
	It's easy to avoid broken ball movement: just play the game.
	If the ball gets caught in a side to side motion, 
	the gravitational field acting on it pulls it down slowly, 
	making it immune to getting stuck in a side to side loop. 

External Resources Used:
	ballHitSound.wav came from Xtreme Pong, but I made the sound effect for it,
	so it's still owned by me.

	explosionSoundCustom.wav was made for Xtreme Pong originally also
	but was made by me, so it's fine.

	All custom images and text were made
	by me in the open source image editor Gimp.

	All custom sounds were made by me
	in the open source audio editing program Audacity.

	No new external assets were added beyond the ones mentioned.
	Certainly nothing requiring attribution.

	The music was kept because it's nice chill music
	that is already given credit in the credits.