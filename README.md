# Sprout Life

Sprout Life introduces the concept of organisms to Conway's Game of Life. A slight change to the rules allows organisms to reproduce and to mutate. Now we can watch first hand as organisms evolve.

From their evolution we can see that there is a trade-off between individual benefit and collective stability. Paradoxically, it turns out that greater reproductive ability and longer life span do not always help an organism and its descendants to thrive.

![Sprout Life](https://github.com/ShprAlex/SproutLife/blob/master/resources/images/SproutLife_2016-01-27.gif)

###About me

Hi, I'm [Alex Shapiro](https://twitter.com/shpralex). I've been playing around with ideas about artificial life for over 20 years, since I first started programming. I've spent a large part of my career building a network visualization product, which was inspired by thinking about congnition. Cellular automata has always been a source of inspiration. The possibility of creating life within the machine, to have simple rules give rise to something that goes beyond its programming, to understand the real world by learning from a simulation, I think this have been the dream of many technologists including myself.

I had the insight that gave rise to Sprout Life in December 2015. Since then I've been obsessed with developing the project and seeing where it led. GOL can be written in 100 lines, and I thought that Sprout Life would not be much longer. It's turned out a bit bigger than I thought, but it's still a fun toy project.

I started with a Java implementation because my initial goal was to quickly refile the design, and java is language I'm most familiar with. Popularizing the application may be better done in Javascript. I think that current processors are fast enough to run Sprout Life using Javascript in a web browser.

I hope that others get involved with this project as well. In particular, I hope that Sprout Life appeals to programmers for the same reason that GOL does - it is relatively small and self contained and produces results that look cool and are fun to talk about. 

### Genetic algorithms and collective behavior

Sprout Life is exciting because presents a truly open ended model for genetic evolution. As far as I'm aware, most genetic algorithms work on a fixed set of parameters. The machine can optimize parameter values, but it can't really create something new. In the case of GOL and Sprout Life, part of the code is embedded in the body of the pattern/organism. The bigger the organism the bigger the code, allowing it to grow to an unbounded complexity. 

It is collective behavior, not just individual fitness, that is the driver for evolution within Sprout Life. Cellular automata patterns are sensitive to disruption. In order for an organism to succeed it needs to be a good neighbor to its offspring, parents, and relatives.

A better model of collective evolution is a rich source of metaphor. Beyond biology, there are strong parallels between the success of genes, and the success of ideas in the startup world which I'm a part of by day. Potentially even phenomena like political revolutions or the boom and bust cycles of the stock market can have light shed on them by looking at what happens to the population as a new capability is discovered.

### Seeds that Sprout - the key idea

![Seed Sprout Illustration](https://github.com/ShprAlex/SproutLife/blob/master/resources/images/SeedSproutIllustration.png)

- **Cell** - Cells in Sprout Life are only considered as cells if the are in the "alive" state, as defined in [Conway's Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life) (GOL). What GOL calls "dead" cells, Sprout Life simply considers as empty space. This is only a clarification of terminology, it dos not change the rules.

- **Seed** - A seed is a collection of cells that we replace with a new cells. A static 2x2 block pattern is a natural choice for a seed, because lots of these blocks are produced during a typical game. Other small patterns work just as well, and better in some cases.

- **Sprout** - A sprout is a pattern that replaces a seed. An [R-Pentomino](https://www.youtube.com/watch?v=bTPN3spiq1I) is a natural choice for a sprout pattern because it is small and produces a lot of growth. Sprouting bends the rules of GOL, because we are adding cells that wouldn't have otherwise been created.

- **Organism** - An organism is a collection of cells. Organisms begin their life as a sprout, and every cell that grows from this pattern becomes part of the organism. Collisions between cells of different organisms will be discussed later.

- **Reproduction** - A seed from a parent sprouts to become a new child organism. We know the identity of the parent for each child. Thus we can support inheritance, where genes pass down from parent to child. For now every organism has a single parent. Sexual reproduction can also easily be added as a result of contact between a parent and other organisms.

![Sprout Animation](https://github.com/ShprAlex/SproutLife/blob/master/resources/images/SproutAnimation.gif)

- **Self imposed lifespan** - It turns out that having old organisms self-destruct is beneficial to their children. Removing all of a parent's cells from the board creates more room for its children and descendants to grow. New organisms develop in a predictable pattern allowing stability to arise, whereas older organisms get damaged by collision and mutation. It was exciting to discover that letting organisms control their own lifespan does not lead to run-away growth. Organisms often prefer to be small. The maximum lifespan is encoded as an integer value that can mutate from generation to generation. This kind of cell-death is another deviation from the rules of GOL.

- **Mutation** - There are lots of ways to implement a genetic code and mutation. An option that works well, is to have mutations be a pair of (x,y) coordinates, and a time value ((x,y),t) for each coordinate. If at time t, an organism has a mutation ((x,y),t) and it has a living cell at coordinates (x,y) then that cell is killed. Turning off a cell changes how the organism grows from that point. This is yet another deviation from GOL rules.

- **Genome** - A collection of mutations make up an organism's genome. Mutations can be added to the organism's genome, or existing mutations removed during an organism’s life (or prior to birth). In theory, every time an organism is born it gets to live through identical circumstances. If it’s lucky and does everything right it reproduces and thrives, if not, it dies childless. Thus beneficial mutations will propagate. What's tricky is that a child may be born in a different environment than its parent. For instance a second child is born in a different environment than a first child (and the parent may have been a first child). Rather than being concerned with these things, every organism treats its genetic code the same way.
 
- **Visualizing the Genome** - A lucky accident helped me come up with a beautiful way to display an organism's genome. The triangle and paw-print patterns in the GIF above are actually direct representations of an organism's recent mutations. Mutations have (x,y) coordinates, and correspond to the same space as the organism. The lucky accident was that I accidentally shrunk the dimensions of those mutations, causing them to appear bunched together in the center of the organism rather than spread out over its body. All the mutations across different time points are displayed at the same time, except for older mutations which would be identical for organisms that are all relatives of each other.
 
- **Rotation and Orientation** - It's important to keep track of which way the R-Pentomino, or potentially other sprout pattern, was facing when the organism was born. There are 8 ways to rotate a pattern, 4 rotations and 2 mirror images for most patterns. The (x,y) coordinates for mutations must be relative to how the seed pattern was oriented. It's also good to let the parent organism determine which direction the sprouted child is facing. A 2x2 block seed presents us with a tricky dilemma in that we don't know which way it's facing. We can resolve this issue by checking which way the parent was facing when it was born, and combining this with whether the seed is above, below, left, right, or otherwise oriented relative to the parent. Combining these two we can have the child born facing a direction relative to how the parent was born.
 
![Sprout Life](https://github.com/ShprAlex/SproutLife/blob/master/resources/images/SproutLife%202016-01-29a.gif)

### Evolution - a race to the bottom?

The initial excitement of developing Sprout Life was that it worked! Organisms do arise, reproduce, and mutate to improve themselves. But what can we see about the direction that evolution seems to take?

- **Organisms want to be small** - It turns out that the basic implementation creates organisms that want to be smaller. Organisms start big and clumsy, and figure out a way to get small and efficient. As they get smaller, organisms are able to reproduce more quickly and increase their population within the grid.

- **Bigger can be better** - A bigger size does have advantages. An organism needs to compete as well as reproduce, and a greater size allows it to put more energy into collisions disruptive to its neighbors.

The direction towards smallness is not straight forward, and we can still learn a lot from how new abilities are introduced and propagate through the population. Overall though, the trend is to be simple, and simple is boring. Simple is also a fixed boundary beyond which progress isn't possible.

### Overcoming simplicity - initial techniques to maintain complexity

There are a few ways we can encourage our evolutionary model to maintain complexity so as to keep things interesting.

- **Maturity to child-bearing adulthood** - We can add a parameter that says that an organism can't reproduce until it hits a certain age. Age is measured in terms of the number of cycles of GOL. For instance, we can say that an organism can't have children until 20 cycles have passed. This ensures that the organisms has grown and survived during that time. Bigger organisms means more opportunity to have beneficial mutations that create interesting behavior.

- **Time between children** - We can also set a number for how much time must pass after an organism has one child before it can have another. We can control how many children an organism can have at one time. We can also control how much energy must be invested for each seed to sprout. Energy can be a function of age, size, or some other combination.

![Sprout Life](https://github.com/ShprAlex/SproutLife/blob/master/resources/images/SproutLife%202016-01-27t.gif)

### Collisions - "competitive mode" lets organisms grow bigger

To get organisms to grow, we need to add another incentive. A "competitive" mode makes it so that when organisms collide, the cells touched on the smaller organism are destroyed. Tweaking these parameters finally resulted in organisms growing bigger. Ultimately, bigger is better under the right circumstances, which means there is no bound to how sophisticated an organism can be in order to more effectively survive and reproduce.

Will write more about this later, check out the code!

![Sprout Life](https://github.com/ShprAlex/SproutLife/blob/master/resources/images/SproutLife%202016-01-28f.gif)

