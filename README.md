Wordle / Absurdle / Happy / Mixed
A Java/Swing word game suite:

Wordle — classic fixed-answer play.

Absurdle — adversarial: feedback is chosen to keep the largest set of answers alive.

Happy mode — adaptive difficulty that targets ~50% player win-rate by tuning the game based on recent results.

Mixed mode — starts like Absurdle, then switches to normal Wordle (picking a fixed answer) based on a chosen strategy.

Includes optional rules: Hard Mode and Real Words Only.

Quick Start
Requirements: JDK 17+ (JavaSE), Swing (bundled).
Run the main class edu.wm.cs.cs301.f2024.wordle.controller.Wordle.

Modes
Select with the game mode flag 

--mode wordle    
--mode absurdle 
--mode mixed     
--mode happy     
If no mode is provided, the default is Wordle.

Wordle
Fixed secret word chosen at start. Standard feedback.

Absurdle
No fixed secret at first. Feedback is picked to maximize the remaining candidate set, making you work harder.

Happy (adaptive)
Tries to keep players “happy” by aiming for ~50% win-rate. You can set a threshold that influences how aggressively difficulty adjusts.

Mixed
Begins like Absurdle, then commits to a single Wordle answer based on a switch strategy:

-ssn <N> — SwitchAfterNGuesses: switch to Wordle after N guesses.

-sst <T> — SwitchWhenWordListIsBelowThreshold: switch when the live candidate list drops below T.

-ssr — SwitchRandomly: switch at a random time.

Rules (optional)
Hard Mode
--hard
Enforces that each guess must be consistent with all previous feedback (greens stay in place, yellows must be used, etc.).

Real Words Only
--words-only
Restricts input to a curated dictionary. 

