# Sprout Life

Sprout Life introduces the concept of organisms to Conway's Game of Life. A slight change to the rules allows organisms to reproduce and to mutate. Now we can watch first hand as organisms evolve.

From their evolution we can see that there is a trade-off between individual benefit and collective stability. Paradoxically, it turns out that greater reproductive ability and longer life span do not always help an organism and it's descendants to thrive.

![Sprout Life](https://github.com/ShprAlex/SproutLife/blob/master/resources/images/SproutLife_2016-01-27.gif)

###About me

Hi, I'm [Alex Shapiro](https://twitter.com/shpralex). I became obsessed with creating Sprout Life in December 2015. 

My main motivation is to hack on something that's relatively small and self-contained. GOL can be written in 100 lines, and I thought that Sprout Life would not be much longer. It's turned out a bit bigger than I thought, but it's still a fun toy project. It's appeal is that it is a game, a hobby project meant to look cool and be fun to talk about.

I hope that others get involved with this project as well. I decided to start with a Java implementation because it's the language I'm most familiar with for designing the application. I think that current processors are fast enough to have Sprout Life run in a web browser using Javascript.

One future possibility is that organisms evolved separately on different machines can be brought together to compete on a central server. That is assuming that others are as inspired by it as I am.

### Genetic algorithms and collective behavior

Sprout Life is exciting, because it presents a truly open ended model for genetic evolution. As far as I'm aware, most genetic algorithms work on a fixed set of parameters. In the case of GOL and Sprout Life, part of the code is embedded in the body of the pattern/organism. The bigger the organism the bigger the code, allowing it to grow to an unbounded complexity. 

It is also an excellent model of both evolution and collective behavior. There are strong parallels between the success of genes, and the success of ideas in the startup world which I'm a part of by day.

### Seeds that Sprout - the key idea

![Seed Sprout Illustration](https://github.com/ShprAlex/SproutLife/blob/master/resources/images/SeedSproutIllustration.png)

- **Seed** - A seed is a collection of cells that we replace with a new cells. A static 2x2 block pattern is a natural choice for a seed, because lots of these blocks are produced during a typical game. Other small patterns work just as well, and better in some cases.

- **Sprout** - A sprout is a pattern that replaces a seed. An [R-Pentomino](https://www.youtube.com/watch?v=bTPN3spiq1I) is a natural choice for a sprout pattern because it is small and produces a lot of growth. Sprouting bends the rules of GOL, because we are adding cells that wouldn't have otherwise been created.

- **Organism** - An organism is a collection of cells. Organisms begin their life as a sprout, and every cell that grows from this pattern becomes part of the organism. Collisions between cells of different organisms will be discussed later.

- **Reproduction** - A seed from a parent sprouts to become a new child organism. We know the identity of the parent for each child. Thus we can support inheritance, where genes pass down from parent to child. For now every organism has a single parent. Sexual reproduction can also easily be added as a result of contact between a parent and other organisms.

![Sprout Animation](https://github.com/ShprAlex/SproutLife/blob/master/resources/images/SproutAnimation.gif)

- **Self imposed lifespan** - It turns out that having old organisms destroy and remove their cells from the board is beneficial to the children. When a parent's cells are removed, it creates more room for children and descendants to grow. It was exciting to discover that letting organisms control their own lifespan does not lead to run-away growth. Organisms often prefer to be small. The maximum lifespan is encoded as an integer value that can mutate from generation to generation. This kind of cell-death is another deviation from the rules of GOL.

- **Mutation** - There are lots of ways to implement a genetic code and mutation. An option that works well, is to have mutations be a pair of (x,y) coordinates, and a time value ((x,y),t) for each coordinate. If at time t, an organism has a mutation ((x,y),t) and it has a living cell at coordinates (x,y) then that cell is killed. Turning off a cell changes how the organism grows from that point. This is yet another deviation from GOL rules.

- **Genome** - A collection of mutations make up an organism's genome. Mutations can be added to the organism's genome, or existing mutations removed during an organism’s life (or prior to birth). In theory, every time an organism is born it gets to live its life from the beginning. If its lucky and does everything right it reproduces and thrives, if not, it dies childless. Thus beneficial mutations will propagate. What's tricky is that a child may be born in a different environment than its parent. For instance a second child is born in a different environment than a first child (and the parent may have been a first child). Rather than being concerned with these things, every organism treats its genetic code the same way.
 
- **Visualizing the Genome** - A lucky accident helped me come up with a beautiful way to display an organism's genome. The triangle and paw-print patterns in the GIF above are actually direct representations of an organism's recent mutations. Mutations have (x,y) coordinates, and correspond to the same space as the organism. The lucky accident was that I accidentally shrunk the dimensions of those mutations, causing them to appear bunched together in the center of the organism rather than spread out over its body. All the mutations across different time points are displayed at the same time, except for older mutations which would be identical for organisms that are all relatives of each other.
 
- **Rotation and Orientation** - It's important to keep track of which way the R-Pentomino, or potentially other sprout pattern, was facing when the organism was born. There are 8 ways to rotate a pattern, 4 rotations and 2 mirror images for most patterns. The (x,y) coordinates for mutations must be relative to how the seed pattern was oriented. It's also good to let the parent organism determine which direction the sprouted child is facing. A 2x2 block seed presents us with a tricky dilemma in that we don't know which way it's facing. We can resolve this issue by checking which way the parent was facing when it was born, and combining this with whether the seed is above, below, left, right, or otherwise oriented relative to the parent. Combining these two we can have the child born facing a direction relative to how the parent was born.
 
### Results - what are they?

There are some exciting, and some disappointing, things we can see when we run Sprout Life. The initial excitement is that there is in fact evolution. The potential disappointment is that most evolution is in the wrong direction! Organisms want to evolve to be simpler. They start big and figure out a way to get small and therefore increase their population.

To get organisms to grow, we need to add another incentive. A "competitive" mode makes it so that when organisms collide, the cells touched on the smaller organism are destroyed. Tweaking these parameters finally resulted in organisms growing bigger. Ultimately, bigger is better under the right circumstances, which means there is no bound to how sophisticated an organism can be in order to more effectively survive and reproduce.

Will write more about this later, check out the code!

#####Cool blinkers

![Sprout Life](https://github.com/ShprAlex/SproutLife/blob/master/resources/images/SproutLife%202016-01-27t.gif)

#####Centipede looking guys

![Sprout Life](https://github.com/ShprAlex/SproutLife/blob/master/resources/images/SproutLife%202016-01-29a.gif)

#####Different competing gene lines

![Sprout Life](https://github.com/ShprAlex/SproutLife/blob/master/resources/images/SproutLife%202016-01-28f.gif)
