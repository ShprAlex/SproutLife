# SproutLife

SproutLife simulates the evolution of complex life. It extends Conway’s Game of Life, which is famous for having lifelike patterns emerge from simple rules. SproutLife takes this emergence a step further by defining organism that mutate and reproduce.

Enabling reproduction sets in motion a world that's both alien and familiar. Organisms begin to compete for space to survive and align in battle formations with their siblings.

Run SproutLife of your desktop to see evolution in action. Observe the intricate geometric patterns that form, or dive deep to explore the data about how individual and collective fitness interact.

![SproutLife](https://github.com/ShprAlex/SproutLife/blob/gallery/SproutLife%202019-10-27e.gif)

### Compiling and Running

[![Download and Run](https://github.com/ShprAlex/SproutLife/blob/master/resources/images/download.png)](https://github.com/ShprAlex/SproutLife/releases)

You can compile and run SproutLife with `Java 1.8`

[Download SproutLife](https://github.com/ShprAlex/SproutLife/releases) executable SproutLife.jar file, or the source code.

Run `SproutLife.jar` by opening the file on your desktop, or using the following command line:

 `java -jar -Xmx2g SproutLife.jar`

**Compile and Run:**

[Download maven](https://maven.apache.org/download.cgi) and follow the [maven installation instructions](https://maven.apache.org/install.html)

`mvn package` to build an executable jar

`mvn exec:java` to run the application

### Motivation

There are several reasons why SproutLife is exciting:


- **Collective behavior, not just individual fitness** - SproutLife uniquely models the evolution of collective behavior. Cellular automata patterns are fragile and sensitive to disruption. In order for an organism to succeed it needs to be a good neighbor to its offspring, parents, and relatives. Organisms survive and compete by forming patterns, and in turn those patterns themselves compete and combine into more complex structures.

- **A rich source of metaphor** - Evolution is a rich source of metaphor. Beyond biology, there are strong parallels between the success of new mutations, and the success of new ideas in the startup world. Potentially even phenomena like political revolutions or the boom and bust cycles of the stock market can have light shed upon them by modelling the ebb and flow of evolution.

- **Made for speed** - Speed is of the essence in simulating evolution. The ideal is to have a beautiful story unfold in real time. Cellular automata are perfectly designed for rapid computation by computers. Getting the next state of the game is almost like adding together two binary numbers. This is what computers were made to do. With the computer as our vehicle, we can explore the evolving world of cellular automata and learn from our discoveries.

### Seeds that Sprout - the key idea

A slight change to the rules of GOL allows organisms in SproutLife to reproduce, mutate, and evolve.

![Seed Sprout Illustration](https://github.com/ShprAlex/SproutLife/blob/gallery/SeedSproutIllustration.png)

- **Cell** - Cells in SproutLife are only considered as cells if the are in the "alive" state. [Conway's Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life) (GOL) uses the term "dead" cells to refer to empty coordinates. SproutLife simply considers this as empty space.

- **Seed** - A seed is a collection of cells that we replace with a new cells. A static 2x2 block pattern is a natural choice for a seed, because lots of these blocks are produced during a typical game. Other small patterns work just as well, and better in some cases.

- **Sprout** - A sprout is a pattern that replaces a seed. An [R-Pentomino](https://www.youtube.com/watch?v=bTPN3spiq1I) is a natural choice for a sprout pattern because it is small and produces a lot of growth.

- **Organism** - An organism is a collection of cells. Organisms begin their life as a sprout, and every cell that grows from this pattern becomes part of the organism. Collisions between cells of different organisms will be discussed later.

- **Reproduction** - A seed from a parent sprouts to become a new child organism. We know the identity of the parent for each child. Thus we can support inheritance, where genes pass down from parent to child. For now every organism has a single parent. Sexual reproduction can also easily be added as a result of contact between a parent and other organisms.

![Sprout Animation](https://github.com/ShprAlex/SproutLife/blob/gallery/SproutAnimation.gif)

- **Self imposed lifespan** - It turns out that having old organisms self-destruct is beneficial to their children. Removing all of a parent's cells from the board creates more room for its children and descendants to grow. New organisms develop in a predictable pattern allowing stability to arise, whereas older organisms get damaged by collision and mutation. It was exciting to discover that letting organisms control their own lifespan does not lead to run-away growth. Organisms often prefer to be small. The maximum lifespan is encoded as an integer value that can mutate from generation to generation.

- **Mutation** - There are lots of ways to implement a genetic code and mutation. An option that works well, is to have mutations be a pair of (x,y) coordinates, and a time value ((x,y),t) for each coordinate. If at age t, an organism has a mutation ((x,y),t) and it has a living cell at coordinates (x,y) then that cell is killed. Turning off a cell changes how the organism grows from that point.

- **Genome** - A collection of mutations make up an organism's genome. Mutations can be added to the organism's genome, or existing mutations removed. We do this by random chance during an organism’s life. Mutations have to be beneficial across all conditions. For example a first child and a second child are born at different times with different surrounding cell configurations. In our case they both follow the same genetic blueprint, even though it's tempting to take birth order into account.

![Sprout Animation](https://github.com/ShprAlex/SproutLife/blob/gallery/SproutLife%202016-01-29zb.gif)

- **Visualizing the Genome** - A lucky accident resulted in a beautiful way to display an organism's genome. The eyes, triangles and paw-print patterns in the GIFs are actually direct representations of an organism's recent mutations. Mutations have (x,y) coordinates, and correspond to the same space as the organism. The lucky accident was shrinking the dimensions of those mutations, causing them to appear bunched together in the center of the organism rather than spread out over the affected cells. All the organism's mutations across different time points are displayed simultaneously. The logo-like patterns have a deep connection with the organisms behavior, they roughly outline the shape the organism will have during its life.
 
- **Rotation and Reflection** - It's important to keep track of which way the R-Pentomino, or potentially other sprout pattern, was facing when the organism was born. There are 8 ways to rotate a pattern, 4 rotations and 2 mirror images for most patterns. The (x,y) coordinates for mutations must be rotated relative to how the seed pattern was oriented. It's also good to let the parent organism determine which direction the sprouted child is facing. A 2x2 block seed presents us with a tricky dilemma in that we don't know which way it's facing. We can resolve this issue by checking which way the parent was facing when it was born, and combining this with whether the seed is above, below, left, right, or otherwise oriented relative to the parent. Combining these two we can have the child born facing a direction relative to how the parent was facing when it was born.
 
![SproutLife](https://github.com/ShprAlex/SproutLife/blob/gallery/SproutLife%202016-01-29a.gif)

### Evolution - a race to the bottom?

The initial excitement of developing SproutLife was that it worked! Organisms do arise, reproduce, and mutate to improve themselves. But what can we see about the direction that evolution seems to take?

- **Organisms want to be small** - It turns out that the basic implementation creates organisms that want to be smaller. Organisms start big and clumsy, and figure out a way to get small and efficient. As they get smaller, organisms are able to reproduce more quickly and increase their population within the grid.

The direction towards smallness is not straight forward, and we can still learn a lot from how new abilities are introduced and propagate through the population. Overall though, the trend is to be simple, and simple is boring. Simple is also a fixed boundary beyond which progress isn't possible.

![SproutLife](https://github.com/ShprAlex/SproutLife/blob/gallery/SproutLife%202016-01-27t.gif)

### Overcoming simplicity - initial techniques to maintain complexity

There are a few ways we can encourage our evolutionary model to maintain complexity so as to keep things interesting.

- **Maturity to childbearing adulthood** - We can add a parameter that says that an organism can't reproduce until it hits a certain age. Age is measured in terms of the number of cycles of GOL. For instance, we can say that an organism can't have children until 20 cycles have passed. This ensures that the organisms has grown and survived during that time. Bigger organisms means more opportunity to have beneficial mutations that create interesting behavior.

- **Time between children** - We can also set a number for how much time must pass after an organism has one child before it can have another. We can control how many children an organism can have at one time. We can also control how much energy must be invested for each seed to sprout. Energy can be a function of age, size, or some other combination.

![SproutLife](https://github.com/ShprAlex/SproutLife/blob/gallery/SproutLife%202016-01-28f.gif)

### Competitive Collision Mode - finally achieving growth

In pursuit of bigger, more sophisticated organisms it seems natural to let bigger organisms win [collisions](https://github.com/ShprAlex/SproutLife/wiki/Collisions). [Bigger organisms](https://github.com/ShprAlex/SproutLife/wiki/Competitive-Mode) should come out undamaged and carrying on with reproduction, while smaller organisms have some cells destroyed and may be unable to procreate.

- **Competitive mode - survival of the biggest** - Competitive mode maintains the basic [B3/S23](https://en.wikipedia.org/wiki/Life-like_cellular_automaton) rule of GOL with the following difference. A cell survives if it has exactly 2 or 3 friends (with "friends" defined above). A cell survives more than 3 neighbors, as long as its organism is the biggest one of all the neighbors. If the cell has 2 friends and a neighbor from a bigger organism it dies, even though it has a total or 3 neighbors. Similarly for birth, to be born in an empty space, a cell needs to have exactly 3 would be friends, with other neighbors being ignored as long as the cell being born belongs to the biggest organism adjacent to the space.

- **Biggest among more than 8 neighbors** - Checking more than 8 neighbors to see if the cell belong to the biggest organism gives an extra bonus to the winner, and an extra penalty to the loser. For a greater effect we check a 5x5 square around each cell to see if a bigger organism occupies one of the 25 cells. If it does then the cell dies or isn't born.

- **Respecting relatives** - In competitive mode, cells do not compete with related organisms. A relationship as distant as first cousins (organisms that share a grandparent) will not destroy the cells of the other even if their size metrics differ.
 
- **Finally growth, slow growth** - With all these measures in place, we finally get a noticeable impact when competitive collision mode is turned on. Growth is exciting. Evolution towards growth is open ended. There is no limit to how large an organism can be, and no limit to the range of behavior it can exhibit.

### Wiki & Documentation

* [SproutLife Wiki](https://github.com/ShprAlex/SproutLife/wiki) - For more details and documentation please see the Wiki!



