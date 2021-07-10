# SproutLife

SproutLife simulates the evolution of complex life. It extends Conway’s Game of Life, which is famous for having lifelike behavior arise from simple rules. SproutLife takes this emergence a step further defining organisms that mutate and reproduce.

Run SproutLife on your desktop to see evolution in action. Observe the intricate geometric patterns that form, or dive deep to explore the data about how individual competition and collective fitness interact.

![SproutLife](https://github.com/ShprAlex/SproutLife-Gallery/blob/master/ShprAlex/SproutLife%202019-11-13%20zig%20swimmers.gif)

## Compiling and Running

[![Download and Run](https://github.com/ShprAlex/SproutLife/blob/master/resources/images/download.png)](https://github.com/ShprAlex/SproutLife/releases)

You can download and run SproutLife with `Java 1.8`

[Download SproutLife](https://github.com/ShprAlex/SproutLife/releases) executable SproutLife.jar file, or the source code.

Run `SproutLife.jar` by opening the file on your desktop, or using the following command line:

 `java -jar -Xmx2g SproutLife.jar`

**Compile and Run:**

[Download maven](https://maven.apache.org/download.cgi) and follow the [maven installation instructions](https://maven.apache.org/install.html)

`mvn package` to build an executable jar

`mvn exec:java` to run the application

![SproutLife](https://github.com/ShprAlex/SproutLife-Gallery/blob/master/ShprAlex/SproutLife%202019-10-27e.gif)

## Motivation

The inspiration for SproutLife was to create an open ended genome with unlimited potential for evolution.

The initial success was that this was possible.

Anticlimactically, it turns out that an open ended genome in a turing complete environment does not automatically lead to some kind of transcendent evolutionary product. The solutions that emerge are still limited by the simple problems they are tasked with.

It is the evolutionary journey, rather than a specific destination where SproutLife can be most informative.

In particular, the process of "collapse" is an interesting topic for investigation. From Covid to the popping of stock market bubbles, political upheaval, and even global warming we are surrounded by real and potential falling of the established order. SproutLife also exhibits this kind of behavior and can let us understand how to quantify and perhaps predict it.

## Seeds that Sprout - the key idea

A slight change to the rules of GOL allows organisms in SproutLife to reproduce, mutate, and evolve.

![Seed Sprout Illustration](https://github.com/ShprAlex/SproutLife-Gallery/blob/master/ShprAlex/SeedSproutIllustration.png)

- **Cell** - Cells in SproutLife are only considered as cells if the are in the "alive" state. [Conway's Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life) (GOL) uses the term "dead" cells to refer to empty coordinates. SproutLife simply considers this as empty space.

- **Seed** - A seed is a collection of cells that we replace with a new cells. A static 2x2 block pattern is a natural choice for a seed, because lots of these blocks are produced during a typical game. Other small patterns work just as well, and better in some cases.

- **Sprout** - A sprout is a pattern that replaces a seed. An [R-Pentomino](https://www.youtube.com/watch?v=bTPN3spiq1I) is a natural choice for a sprout pattern because it is small and produces a lot of growth.

- **Organism** - An organism is a collection of cells. Organisms begin their life as a sprout, and every cell that grows from this pattern becomes part of the organism. Collisions between cells of different organisms will be discussed later.

- **Reproduction** - A seed from a parent sprouts to become a new child organism. We know the identity of the parent for each child. Thus we can support inheritance, where genes pass down from parent to child. For now every organism has a single parent. Sexual reproduction can also easily be added as a result of contact between a parent and other organisms.

![Sprout Animation](https://github.com/ShprAlex/SproutLife-Gallery/blob/master/ShprAlex/SproutAnimation.gif)

- **Self imposed lifespan** - It turns out that having old organisms self-destruct is beneficial to their children. Removing all of a parent's cells from the board creates more room for its children and descendants to grow. New organisms develop in a predictable pattern allowing stability to arise, whereas older organisms get damaged by collision and mutation. It was exciting to discover that letting organisms control their own lifespan does not lead to run-away growth. Organisms often prefer to be small. The maximum lifespan is encoded as an integer value that can mutate from generation to generation.

- **Mutation** - There are lots of ways to implement a genetic code and mutation. An option that works well, is to have mutations be a pair of (x,y) coordinates, and a time value ((x,y),t) for each coordinate. If at age t, an organism has a mutation ((x,y),t) and it has a living cell at coordinates (x,y) then that cell is killed. Turning off a cell changes how the organism grows from that point.

- **Genome** - A collection of mutations make up an organism's genome. Mutations can be added to the organism's genome, or existing mutations removed. We do this by random chance during an organism’s life. Mutations have to be beneficial across all conditions. For example a first child and a second child are born at different times with different surrounding cell configurations. In our case they both follow the same genetic blueprint, even though it's tempting to take birth order into account.

![Sprout Animation](https://github.com/ShprAlex/SproutLife-Gallery/blob/master/ShprAlex/SproutLife%202016-01-29zb.gif)

- **Visualization Layer** - To make the simulation easier to follow we add a visualization layer on top of the grid of cells. We track the paths between the coordinates where an organism was born to where its children are born to where their children are born and connect them by lines. We color the lines based on the angle between them. This allows us see what's happening even when the game runs at many cycles a second.

    The eyes and paw-print shapes in the GIFs are a direct representation of an organism's genome. Mutations have (x,y) coordinates, and correspond to the same space as the organism. The visualization shrinks the dimensions of those mutations making them look like a connected shape in the center of the organism. Recent mutations at every age ((x,y),t) are displayed simultaneously.
 
- **Rotation and Reflection** - It's important to keep track of which way the R-Pentomino, or potentially other sprout pattern, was facing when the organism was born. There are 8 ways to rotate a pattern, 4 rotations and 2 mirror images for most patterns. The (x,y) coordinates for mutations must be rotated relative to how the seed pattern was oriented. For reproduction, A 2x2 block seed presents us with a dilemma that we don't immediately know which way it's facing. Other seed patterns are easier to work with because they have a distinct shape under rotation and reflection so that we can directly replace a seed with a correspondingly oriented sprout.
 
![SproutLife](https://github.com/ShprAlex/SproutLife-Gallery/blob/master/ShprAlex/SproutLife%202016-01-29a.gif)

## Evolution - a race to the bottom?

The initial excitement of developing SproutLife was that it worked! Organisms do arise, reproduce, and mutate to improve themselves. But what can we see about the direction that evolution seems to take?

- **Organisms want to be small** - It turns out that the basic implementation creates organisms that want to be smaller. Organisms start big and clumsy, and figure out a way to get small and efficient. As they get smaller, organisms are able to reproduce more quickly and increase their population within the grid.

The direction towards smallness is not straight forward, and we can still see how new behaviors propagate through the population. Overall though, the smaller the organisms get the less room there is for improvement resulting in a limit to further progress.

![SproutLife](https://github.com/ShprAlex/SproutLife-Gallery/blob/master/ShprAlex/SproutLife%202016-01-27t.gif)

## Competitive Collision Mode - finally achieving growth

A long series of experiments led to the development of a competitive collision mode.

- **Competitive mode - survival of the biggest** - Competitive mode maintains the basic [B3/S23](https://en.wikipedia.org/wiki/Life-like_cellular_automaton) rule of GOL with the following difference. If a cell from a smaller organism has a neighbor from a bigger organism the smaller organism's cell dies even if it had a total or 2 or 3 neighbors.

- **Competitive target age** - After discovering that organisms naturally want to get smaller it was tempting to find a way to add just a bit of a competitive edge to have organisms grow larger. The idea was that a slight incentive would lead to an open-ended system where organisms would continuously find creative ways to get bigger. Finding such a balanced competitive rule proved difficult though, there always seemed to be either runaway growth or decay. Instead the concept of a target-age was introduced where organisms get points for their size only before reaching that age while still being able to control their own lifespan and even live longer. This turns out to be effective with the simulation never settling for millions of game cycles even for small competitive target ages.

![SproutLife](https://github.com/ShprAlex/SproutLife-Gallery/blob/master/ShprAlex/SproutLife%202019-11-14%20blue%20luminescence.gif)

## Next Steps

SproutLife is an open source project. Everyone is encouraged to contribute and explore these next steps.

### Game Design

- **Cell Walls** - It would be interesting to see the rise of cell walls and the resulting ratcheting of attack and defense dynamics. So far this is something that's proved elusive. Perhaps an improved collision rule that's more sophisticated than just having the biggest organism win would incentivize cell wall formation.

- **Music / Sonification** - The regular timing patterns of organisms being born can naturally map to notes. The organism frequently synchronize, and it would make sense for simple ratios to exist in their birth cycles as well. It would be fun to add a way to let users listen, not just see what's going on.

- **Stats and Charts** - Things as simple as average lifespan over time become interesting when one examines multi-species ecosystems and the complex rebirth patterns of individual organisms. The current rudimentary statistics panel could be greatly expanded to display all this information.

- **Sex and Viruses** - The current simulation only has asexual reproduction. It would be interesting to study the effects of enabling gene transfer. There could be different rules about when gene transfer is allowed. Organisms could even have individual criteria for accepting gene transfer that could also evolve, so we can see if that reflects biological patterns.

![SproutLife](https://github.com/ShprAlex/SproutLife-Gallery/blob/master/ShprAlex/SproutLife%202019-12-01%20octopolis5.gif)

### Areas for Investigation

- **Speed of evolution vs. board size** - Do organisms on bigger boards evolve faster? Do we see the same patterns as with evolution on continents vs. isolated islands? Does the "breakout rooms" approach of periods of isolation followed by coming together lead to the fastest evolution of all?

- **Mutation resistance** - SproutLife makes it easy to see the evolution of mutation resistance where organisms that initially don't survive high mutation rates can evolve to tolerate it if the mutation rate is gradually increased. High mutation rates naturally lead to smaller organisms with smaller genomes, but even so these organisms eventually can find ways to get big. What can we learn about survival in high mutation environments?

- **Spotting fads vs. revolutions** - The simulation shows how greedy strategies come and go while truly advantageous developments ultimately succeed. Can we distinguish between the two? How long into the emergence of a new trend does it take to say whether it will succeed of fail.

- **Competition and collapse** - A theory is that radical mutations don't just happen. Usually new mutants have disadvantages as well as advantages and it takes time for them to evolve away their rough edges. This means that existing organisms can block radical new mutations by staying in sync and not giving space for new mutations to develop. However as dominant organisms compete with one another they create cracks for new mutants to take hold. Is this true? What about when a small, fast reproducing organisms emerge, does their existence create a balanced external threat that prevents big organisms from competing with each other? When does the system tip too far so that the big organisms collapse and small ones take over, and when does this cycle ultimately lead to stable or unstable end states.

- **Stable, unstable, and diverse ecosystems** - Like simple 1d cellular automata, SproutLife ecosystems can exhibit stable, chaotic, and diverse end states. What are the conditions where only a single dominant species will ever emerge? When will every generation look unpredictable despite reproduction? Is there an analogy between the bifurcation of a logistic function and the emergence of multiple species?

![SproutLife](https://github.com/ShprAlex/SproutLife-Gallery/blob/master/ShprAlex/SproutLife%202020-04-03%20spaceship.gif)

[See more animations and genomes in the gallery](https://github.com/ShprAlex/SproutLife-Genome-Gallery/)
