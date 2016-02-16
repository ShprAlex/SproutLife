# Sprout Life

Sprout Life is a model for the evolution of complex life. It extends Conway’s Game of Life, which is famous for having lifelike patterns emerge from simple rules. Sprout Life takes this emergence a step further, creating patterns that reproduce, mutate, and evolve.

In the first few minutes of the game, Sprout Life gives rise to 100s of diverse replicating organisms. We can watch as the organisms compete for space and align with their siblings. Evolution is open ended, so more advanced organisms can always emerge. In future versions it could be fun to enable competitions between organisms collected by multiple users.

[![Download and Run](https://github.com/ShprAlex/SproutLife/blob/master/readme/download.png)](https://github.com/ShprAlex/SproutLife/raw/master/lib/SproutLife.jar) 

[Download and run SproutLife.jar!](https://github.com/ShprAlex/SproutLife/raw/master/lib/SproutLife.jar)

![Sprout Life](https://github.com/ShprAlex/SproutLife/blob/master/readme/SproutLife_2016-01-27.gif)

### Motivation

There are several reasons why Sprout Life is exciting:

- **Open ended evolution** - Sprout Life emulates the rise of biological complexity. We want to see bigger more advanced organisms evolving from smaller simpler ones. Evolution of big organisms is open ended because there is no limit to how big organisms can get. Being big doesn't automatically mean being more advanced though. Big organisms need to succeed despite their size not because of it. A bigger size brings other advantages like being able to perform more calculations, which is really the property we're trying to evolve.

- **Collective behavior, not just individual fitness** - Collective behavior is the driver for evolution within Sprout Life. Cellular automata patterns are fragile and sensitive to disruption. In order for an organism to succeed it needs to be a good neighbor to its offspring, parents, and relatives.

- **A rich source of metaphor** - A better model of collective evolution is a rich source of metaphor. Beyond biology, there are strong parallels between the success of new mutations, and the success of new ideas in the startup world. Potentially even phenomena like political revolutions or the boom and bust cycles of the stock market can have light shed upon them by modelling the ebb and flow of evolution.

- **Made for speed** - Speed is of the essence in simulating evolution. The ideal is to have a beautiful story unfold in real time. Cellular automata are perfectly designed for rapid computation by computers. Getting the next state of the game is almost like adding together two binary numbers. This is what computers were made to do. With the computer as our vehicle, we can explore the evolving world of cellular automata and learn from our discoveries.

### Seeds that Sprout - the key idea

A slight change to the rules of GOL allows organisms in Sprout Life to reproduce, mutate, and evolve.

![Seed Sprout Illustration](https://github.com/ShprAlex/SproutLife/blob/master/readme/SeedSproutIllustration.png)

- **Cell** - Cells in Sprout Life are only considered as cells if the are in the "alive" state. [Conway's Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life) (GOL) uses the term "dead" cells to refer to empty coordinates. Sprout Life simply considers this as empty space. This is only a clarification of terminology, it does not change the rules.

- **Seed** - A seed is a collection of cells that we replace with a new cells. A static 2x2 block pattern is a natural choice for a seed, because lots of these blocks are produced during a typical game. Other small patterns work just as well, and better in some cases.

- **Sprout** - A sprout is a pattern that replaces a seed. An [R-Pentomino](https://www.youtube.com/watch?v=bTPN3spiq1I) is a natural choice for a sprout pattern because it is small and produces a lot of growth. Sprouting bends the rules of GOL, because we are adding cells that wouldn't have otherwise been created.

- **Organism** - An organism is a collection of cells. Organisms begin their life as a sprout, and every cell that grows from this pattern becomes part of the organism. Collisions between cells of different organisms will be discussed later.

- **Reproduction** - A seed from a parent sprouts to become a new child organism. We know the identity of the parent for each child. Thus we can support inheritance, where genes pass down from parent to child. For now every organism has a single parent. Sexual reproduction can also easily be added as a result of contact between a parent and other organisms.

![Sprout Animation](https://github.com/ShprAlex/SproutLife/blob/master/readme/SproutAnimation.gif)

- **Self imposed lifespan** - It turns out that having old organisms self-destruct is beneficial to their children. Removing all of a parent's cells from the board creates more room for its children and descendants to grow. New organisms develop in a predictable pattern allowing stability to arise, whereas older organisms get damaged by collision and mutation. It was exciting to discover that letting organisms control their own lifespan does not lead to run-away growth. Organisms often prefer to be small. The maximum lifespan is encoded as an integer value that can mutate from generation to generation. This kind of cell-death is another deviation from the rules of GOL.

- **Mutation** - There are lots of ways to implement a genetic code and mutation. An option that works well, is to have mutations be a pair of (x,y) coordinates, and a time value ((x,y),t) for each coordinate. If at age t, an organism has a mutation ((x,y),t) and it has a living cell at coordinates (x,y) then that cell is killed. Turning off a cell changes how the organism grows from that point. This is yet another deviation from GOL rules.

- **Genome** - A collection of mutations make up an organism's genome. Mutations can be added to the organism's genome, or existing mutations removed. We do this by random chance during an organism’s life. Our implementation keeps things simple and doesn't bifurcate the genetic code, so mutations have to be beneficial across all conditions. For example, a first child and a second child are born at different times with different surrounding cell configurations. In our case they both follow the same genetic blueprint, even though it's tempting to take birth order into account.

![Sprout Animation](https://github.com/ShprAlex/SproutLife/blob/master/readme/SproutLife%202016-01-29zb.gif) 

- **Visualizing the Genome** - A lucky accident helped me come up with a beautiful way to display an organism's genome. The eyes, triangles and paw-print patterns in the GIFs are actually direct representations of an organism's recent mutations. Mutations have (x,y) coordinates, and correspond to the same space as the organism. The lucky accident was that I shrunk the dimensions of those mutations, causing them to appear bunched together in the center of the organism rather than spread out over the affected cells. All the organism's mutations across different time points are displayed simultaneously. The logo-like patterns have a deep connection with the organisms behavior, they roughly outline the shape the organism will have during its life.
 
- **Rotation and Chirality** - It's important to keep track of which way the R-Pentomino, or potentially other sprout pattern, was facing when the organism was born. There are 8 ways to rotate a pattern, 4 rotations and 2 chiral mirror images for most patterns. The (x,y) coordinates for mutations must be rotated relative to how the seed pattern was oriented. It's also good to let the parent organism determine which direction the sprouted child is facing. A 2x2 block seed presents us with a tricky dilemma in that we don't know which way it's facing. We can resolve this issue by checking which way the parent was facing when it was born, and combining this with whether the seed is above, below, left, right, or otherwise oriented relative to the parent. Combining these two we can have the child born facing a direction relative to how the parent was facing when it was born.
 
![Sprout Life](https://github.com/ShprAlex/SproutLife/blob/master/readme/SproutLife%202016-01-29a.gif)

### Evolution - a race to the bottom?

The initial excitement of developing Sprout Life was that it worked! Organisms do arise, reproduce, and mutate to improve themselves. But what can we see about the direction that evolution seems to take?

- **Organisms want to be small** - It turns out that the basic implementation creates organisms that want to be smaller. Organisms start big and clumsy, and figure out a way to get small and efficient. As they get smaller, organisms are able to reproduce more quickly and increase their population within the grid.

- **Bigger can be better** - A bigger size does have advantages. An organism needs to compete for space as well as reproduce, and a greater size allows it to sarcifice cells to collisions disruptive to its neighbors.

The direction towards smallness is not straight forward, and we can still learn a lot from how new abilities are introduced and propagate through the population. Overall though, the trend is to be simple, and simple is boring. Simple is also a fixed boundary beyond which progress isn't possible.

### Overcoming simplicity - initial techniques to maintain complexity

There are a few ways we can encourage our evolutionary model to maintain complexity so as to keep things interesting.

- **Maturity to childbearing adulthood** - We can add a parameter that says that an organism can't reproduce until it hits a certain age. Age is measured in terms of the number of cycles of GOL. For instance, we can say that an organism can't have children until 20 cycles have passed. This ensures that the organisms has grown and survived during that time. Bigger organisms means more opportunity to have beneficial mutations that create interesting behavior.

- **Time between children** - We can also set a number for how much time must pass after an organism has one child before it can have another. We can control how many children an organism can have at one time. We can also control how much energy must be invested for each seed to sprout. Energy can be a function of age, size, or some other combination.

### Individual opportunity vs. collective stability - or - The tyranny of 3rd children

We want to see drama unfold in the game, and 3rd children create drama. In a world populated by asexual reproduction, each organism only needs to only have one child to maintain the population. Having two children means that for a constant population one child must die. 

By controlling energy incentives for having children, we can make having 3+ children a strong temptation. We can require a big energy investment to have the first two children, and little for each additional child. Organisms with three children will dominate because having more children means they spend less energy per child on average. 

Organisms strive for order. Stable patterns arise quickly when organisms have only two children. Having 3 children, however, makes it harder to establish a stable pattern. Now two of the three children must die to maintain the status quo. That creates more permutations of possible outcomes. It's a harder environment for our evolving organisms to optimize. 3rd children benefit their parent, but everyone ends up tripping over them. This results in chaos, a less densely populated board, and a lower population. 

So what's good for the individual can in some ways be bad for the community. In our world this is a frequent occurrence requiring government intervention. An extreme but relevant example is China's one child policy. Another parallel is curbing smoking. Competition forced bars to allow smoking, and regulation was necessary to improve the situation for the majority.

![Sprout Life](https://github.com/ShprAlex/SproutLife/blob/master/readme/SproutLife%202016-01-27t.gif)

### Bigger bodies, bigger brains

For evolution towards greater complexity, it should be the case that bigger is better than smaller. The best bigger organism should be superior to the best smaller organism. Bigger need not always be better, but a more advanced organism should be able to evolve by taking advantage of greater size. 

Bigger organisms are able to have more mutations. Mutations correspond to coordinates within the boundaries of the organism. The greater its size the more room for mutation. More mutations means a longer genetic code (which simply lists mutations). The longer the genetic code, the more sophisticated the behavior that the organism can exhibit. 

Patterns are programs not just machines. Skilled designers have created GOL patterns that perform complex tasks like [generating prime numbers](http://www.njohnston.ca/2009/08/generating-sequences-of-primes-in-conways-game-of-life/). Because of their calculating ability patterns act like programs, which means we are evolving brains and not just bodies.

In essence being bigger gives an organism a bigger brain (which is also its body), and we want an environment where bigger brains win.

![Sprout Life](https://github.com/ShprAlex/SproutLife/blob/master/readme/SproutLife%202016-01-28f.gif)

### Competitive Collision Mode - finally achieving growth

In pursuit of bigger, more sophisticated organisms it seems natural to let bigger organisms win [collisions](https://github.com/ShprAlex/SproutLife/wiki/Collisions). Bigger organisms should come out undamaged and carrying on with reproduction, while smaller organisms have some cells destroyed and may be unable to procreate. 

- **Competitive mode - survival of the biggest** - Competitive mode maintains the basic [B3/S23](https://en.wikipedia.org/wiki/Life-like_cellular_automaton) rule of GOL with the following difference. A cell survives if it has exactly 2 or 3 friends (with "friends" defined above). A cell survives more than 3 neighbors, as long as its organism is the biggest one of all the neighbors. If the cell has 2 friends and a neighbor from a bigger organism it dies, even though it has a total or 3 neighbors. Similarly for birth, to be born in an empty space, a cell needs to have exactly 3 would be friends, with other neighbors being ignored as long as the cell being born belongs to the biggest organism adjacent to the space.

- **Biggest among more than 8 neighbors** - Checking more than 8 neighbors to see if the cell belong to the biggest organism gives an extra bonus to the winner, and an extra penalty to the loser. For a greater effect we check a 5x5 square around each cell to see if a bigger organism occupies one of the 25 cells. If it does then the cell dies or isn't born.

- **Respecting relatives** - In competitive mode, cells do not compete with related organisms. A relationship as distant as first cousins (organisms that share a grandparent) will not destroy the cells of the other even if their size metrics differ.
 
- **Finally growth, slow growth** - With all these measures in place, we finally get a noticeable impact when competitive collision mode is turned on. Even so, growth is slow. It can take 1,000 generations, which takes a 1 minute, for a change in organism size to occur. Growth is easier to achieve in organisms that are already big. Organisms that have become optimized to be small through the application of cooperative mode can resist growing larger even after competitive mode is on for a long time.

- **Growth is exciting** - Really though, growth is exciting. Evolution towards growth is open ended. There is no limit to how large an organism can be, and no limit to the range of behavior it can exhibit. **_A glider gun used as a real gun to disable enemies?_** This is a real possibility, that I believe I've seen happen.

- **Next steps after achieving growth** - [Small bigger biggest](https://github.com/ShprAlex/SproutLife/wiki/Small-Bigger-Biggest) - Having achieved growth we see the emergence of interesting behavior with cycles of 3 species engaged in rock-paper-scissors style competition. Please see the wiki for more details.

### Wiki & Documentation

* [Sprout Life Wiki](https://github.com/ShprAlex/SproutLife/wiki) - For more details and documentation please see the Wiki!



