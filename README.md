# Sprout Life

Sprout Life introduces the concept of organisms to Conway's Game of Life. A slight change to the rules allows organisms to reproduce and to mutate. Now we can watch first hand as organisms evolve.

From their evolution we can see that there is a trade-off between individual benefit and collective stability. Paradoxically, it turns out that greater reproductive ability and longer life span do not help an organism and it's descendants to thrive.

![Sprout Life](https://github.com/ShprAlex/SproutLife/blob/master/resources/images/SproutLife_2016-01-27.gif)

###About me

Hi, I'm [Alex Shapiro](https://twitter.com/shpralex). I became obsessed with creating Sprout Life in December 2015. 

My main motivation is to hack on something that's relatively small and self-contained. GOL can be written in 100 lines, and I thought that Sprout Life would not be much longer. It's turned out a bit bigger than I thought, but it's still a fun toy project. I'm also excited that it main purpose is to look cool and be fun to talk about.

I hope that others get involved with this project as well. Rather than collaborating directly, I see this as a theme that others can innovate on. I decided to start with a Java implementation because it's the language I'm most familiar with for designing the application. I think that current processors are fast enough to have Sprout Life run in a web browser using Javascript.

One future possibility is that organisms evolved separately on different machines can be brought together to compete on a central server. That is assuming that others are as passionate and inspired as I am.

### Genetic algorithms and collective behavior

Sprout Life is exciting, because it presents a truly open ended model for genetic evolution. As far as I'm aware, most genetic algorithms work on a fixed set of parameters. In the case of GOL and Sprout Life, part of the code is embedded in the body of the pattern/organism. The bigger the organism the bigger the code, allowing it to grow to an unbounded complexity. 

It is also an excellent model of both evolution and collective behavior. There are strong parallels between the success of genes, and the success of ideas in the startup world which I'm a part of by day.

### Seeds that Sprout - the key idea

![Seed Sprout Illustration](https://github.com/ShprAlex/SproutLife/blob/master/resources/images/SeedSproutIllustration.png)

- **Seed** - A seed is a collection of cells that we replace with a new cells. A static 2x2 block pattern is a natural choice for a seed, because lots of these blocks are produced during a typical game. This was my first choice for a seed pattern. I've since found that other small patterns work just as well, and better in some casees.

- **Sprout** - A sprout is a pattern that replaces a seed. An [R-Pentomino](https://www.youtube.com/watch?v=bTPN3spiq1I) is a natural choice for a sprout pattern becase it is small and produces a lot of growth. Sprouting bends the rules of GOL, because we are adding cells that wouldn't have otherwise been created.

- **Organism** - An organism is a collection of cells. Organisms begin their life as a sprout, and every cell that grows from this pattern becomes part of the organism. Collisions betwen cells of different organisms will be discussed later.

- **Reproduction** - An organism is formed when we replace parent cells from one organism with child cells of a new organism. We thus create the possibility of inheritance. Every organism has a single parent (although sexual reproduction can easily be added as a result of contact between a parent and other organisms).

- **Self imposed lifespan** - It turns out that having old organisms destroy and remove their cells from the board is beneficial to the children because it craetes more room for them to grow. It was also exciting to discover that lettign organisms control their own lifespan, through an integer value that can mutate from generation to generation does not lead to run-away growth. Organisms often prefer to be small. This kind of cell-death is another deviation from the rules of GOL.

- **Mutation** - There are lots of ways to implement a genetic code and mutation. An option that I've come up with, and that works well, is to have a collection of (x,y) coordinates, and a time value ((x,y),t) for each coordinate. If at time t, an organism has a mutation (x,y), and it has a living cell at those coordinates, than that cell is killed. Turning off a cell changes how the organism grows from that point. Mutations can be added to the genetic code, or existing mutations removed during an organisms life (or prior to birth). In theory, every time an organism is born it gets to live it's life from the begining, and thus beneficial mutations will propogate. What's tricky is taht a child may be born in a different enviroment than it's parent. For instance a second child is born in a different environment than a first child. Rather than being concerned with these things, every organism treats it's genetic code the same way.


