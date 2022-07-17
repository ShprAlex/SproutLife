# SproutLife

SproutLife simulates the evolution of complex life. It extends Conway’s Game of Life, which is famous for having lifelike behavior arise from simple rules. SproutLife takes this emergence a step further defining organisms that mutate and reproduce.

Run SproutLife on your desktop to see evolution in action. Observe the intricate geometric patterns that form, or dive deep to explore the data about how individual competition and collective fitness interact.

![SproutLife](https://github.com/ShprAlex/SproutLife-Gallery/blob/master/ShprAlex/SproutLife%202019-11-13%20zig%20swimmers.gif)

## Compiling and Running

[![Download and Run](https://github.com/ShprAlex/SproutLife/blob/master/resources/images/download.png)](https://github.com/ShprAlex/SproutLife/releases)

**Download and Run:**

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

The inspiration for SproutLife was to create an open ended genome on top of the Turing-complete foundation of Conway's Game of Life. The initial success was that this was possible. After much experimentation it also seems likely that SproutLife can generate truly complex, hierarchical, and even multicellular structures.

Simulating artificial life is exciting because it may show us something about life's advances, and it may also be useful for studying life's flaws.

From new the emergence of Covid, to new technologies, and new political ideologies we are immersed in the ups and downs of trends caused by evolving living agents. SproutLife also exhibits this kind of behavior and may help us understand how to quantify and perhaps predict it.

## Seeds that Sprout - the key idea

A slight change to the rules of [Conway's Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life) (GOL) allows organisms in SproutLife to reproduce, mutate, and evolve.

![Seed Sprout Illustration](https://github.com/ShprAlex/SproutLife-Gallery/blob/master/ShprAlex/SeedSproutIllustration.png)

- **Cell** - Cells in SproutLife are coordinates on a grid that are in the "alive" state. GOL uses the term "dead" cells to refer to empty coordinates. SproutLife simply considers this as empty space.

- **Seed** - A seed is a collection of cells that we replace with a new cells. A static 2x2 block pattern is a natural choice for a seed, because lots of these blocks are produced in GOL. Other small patterns work just as well, and better in some cases.

- **Sprout** - A sprout is a pattern that replaces a seed. An [R-Pentomino](https://www.youtube.com/watch?v=bTPN3spiq1I) is a natural choice for a sprout pattern because it is small and produces a lot of growth.

- **Organism** - An organism is a collection of cells. Organisms begin their life as a sprout, and every cell that grows from this pattern becomes part of the organism. Collisions between cells of different organisms will be discussed later.

- **Reproduction** - A seed from a parent sprouts to become a new child organism. Thus we know the identity of the parent for each child. This enables implementing inheritance, where genes pass down from parent to child. For now every organism has a single parent, meaning that reproduction is asexual, but there are relevant ways to implement sexual reproduction as well.

![Sprout Animation](https://github.com/ShprAlex/SproutLife-Gallery/blob/master/ShprAlex/SproutAnimation.gif)

- **Self imposed lifespan** - It turns out that having old organisms self-destruct is beneficial to their children. Removing all of a parent's cells from the board creates more room for its children and descendants to grow. New organisms develop in a predictable pattern allowing stability to arise, whereas older organisms get damaged by collision and mutation. It was exciting to discover that letting organisms control their own lifespan does not lead to run-away growth. Organisms naturally prefer to be small. The maximum lifespan is encoded as an integer value that can mutate from generation to generation.

- **Genes** - There are lots of ways to implement a genes and mutation. An option that works well, is to have genes be a pair of (x,y) coordinates, and a time value ((x,y),t) for each coordinate. If at age t, an organism has a gene ((x,y),t) and it has a living cell at coordinates (x,y) then that cell is killed. Turning off a cell changes how the organism grows from that point.

- **Genome and Mutation** - A collection of genes make up an organism's genome. Mutations can add genes to the organism's genome, or existing genes can be removed. We do this by random chance during an organism’s life. All children inherit and follow the same genome even though they are born at different times with different surrounding cell configurations.

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

## Hierarchical organization - A theory about what's possible

What kind of behavior can a simple 2D cellular-automata based simulation generate? Surprisingly even such a basic setup can potentially produce multicellular life.

The forces in SproutLife are simple. Each organism has a competitive score and some 2D growth/reproduction pattern. Those patterns may be be stable, or they may have multiple phases where the competitive score alternates across generations. An organism strain with a high competitive score on average can have an Achilles heel where it loses to another strain with a low but consistent average competitive score.

Rock-paper-scissors type nontransitive comparisons where 3 organism strains [swirl around as they eat each other](https://www.youtube.com/watch?v=TvZI6Xc0J1Y) is nothing new. SproutLife already exhibits this kind of behavior, which comes from the prior mentioned property that organisms with a higher score can loose to those with a lower one.

There is however an evolutionary leap that only a 2D (or higher) spatially embedded cellular automata model can take -- a step that goes beyond the rock-paper-scissors cycle and leads to the formation of symbiotic hierarchical structures.

![SproutLife](https://github.com/ShprAlex/SproutLife-Gallery/blob/master/ShprAlex/SproutLife%202019-12-01%20octopolis5.gif)

## Hierarchical organization - Islands

Take a system of 3 organisms where the 1st strongest can defeat the 2nd, the 2nd can defeat the 3rd, and the 3rd can defeat the 1st. For instance think of people, cats, and mice, where people are in danger from mouse infestations. There is a way for the 1st type of organism to minimize the effects of the 3rd; and that is for the 1st type of organism to form multiple disjoint islands within the protective cover of the 2nd.

The formation of islands of 1st strongest organisms within the 2nd weaker ones requires some kind of evolved self-restraint on the part of the 1st organisms where they don't completely destroy the 2nd. It is not a stable balance whereby mutations will always cause the 1st organisms to lose self control and annihilate the 2nd ones, however the balance is maintained by the 3rd type or organism which will kill off continuous regions of 1st type of organism that emerge beyond the 2nd layer.

Furthermore, symbiotic islands of a 1st and 2nd organisms can defeat a 4th kind of organism that's weaker than the 1st but stronger than the 2nd. When the 4th organism strain breaches the 2nd organism type to touch an island of the 1st type, the 1st organism strain can spread and destroy all of the 4th.

If symbiotic island structures can have a competitive advantage over single organism structures, then it's also possible that the islands themselves will form internal islands of their own. That is because islands are also competing against each other, and can also evolve rock-paper-scissors relationships eventually leading to collaboration and hierarchies. Thus ultimately the simulation will produce deeper nesting with concentric island hierarchies engaging in both internal and external competition.

SproutLife seems to have all the ingredients to exhibit this kind of [symbiogenesis](https://en.wikipedia.org/wiki/Symbiogenesis). It may simply be a matter of running the simulation for longer on a larger grid for systems of islands to emerge and be detected and recorded.

![SproutLife](https://github.com/ShprAlex/SproutLife-Gallery/blob/master/ShprAlex/SproutLife%202021-01-03%20slow%20bugs.gif)

It's likely that the evolution of nested symbiotic hierarchies is a foundational concept for the mathematics of life. A more elaborate real world physics model could present life with greater challenges and opportunities resulting in the sensory, metabolic, and motile functions we admire. However perhaps even a stripped down 2D physics engine can still produce structures and behaviors we find familiar, that in complexity (though not in purpose) are are as advanced as what we see in the world around us.

[Check out the SproutLife playlist on youtube](https://www.youtube.com/watch?v=H2tXsnzVTaw&list=PLX8XVhHfSZ0tWGyDDoFEOEiCmmhkKYLDf)

[See more animations and genomes in the SproutLife Gallery](https://github.com/ShprAlex/SproutLife-Gallery/)
