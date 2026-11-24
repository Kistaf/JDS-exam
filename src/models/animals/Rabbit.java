package models.animals;

import interfaces.Consumable;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import models.plants.Grass;
import models.structures.RabbitHole;
import utils.CollectionUtils;
import utils.LocationUtils;
import utils.NumberUtils;

import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Rabbit extends Animal implements DynamicDisplayInformationProvider {
    private RabbitHole boundRabbitHole;
    private Object nightMovementTarget;
    private Object dayMovementTarget;
    private Location sleepingSpot;
    private final double MIN_ENERGY = 10;
    private final double BASE_ENERGY = 20;
    private final double MAX_AGE = 30;
    private final double REPRODUCTION_ENERGY_MIN = 10;

    public Rabbit() {
        this.energy = BASE_ENERGY;
        this.imageKey = "rabbit";
    }

    @Override
    protected void performDayTasks(World world) {
        if (!world.isOnTile(this)) return;
        this.nightMovementTarget = null;
        Location currentLoc = currentLocation(world);

        Random random = new Random();
        int roll = random.nextInt(0, 10);

        if (roll % 2 == 0) {
            Location target = this.computeRandomTargetLocation(world, List.of(Rabbit.class));
            if (target == null) return;
            Location nextMove = LocationUtils.greedyBest(world, currentLoc, target);
            if (nextMove == null) return;
            this.performMoveTowardsTarget(world, nextMove);
        } else if (roll % 3 == 0) {
            Location grassTarget = this.computeRandomTargetLocation(world, List.of(Grass.class));
            if (grassTarget == null) this.performRandomMove(world);
            else this.performMoveTowardsTarget(world, grassTarget);
        } else this.performRandomMove(world);

        this.attemptReproduction(world, currentLoc);
        this.performEat(world);
    }

    @Override
    protected void performNightTasks(World world) {
        if (!world.isOnTile(this)) return;
        this.dayMovementTarget = null;
        Location currentLoc = currentLocation(world);

        if (isStandingOnRabbitHole(world)) {
            RabbitHole hole = (RabbitHole) world.getNonBlocking(currentLoc);
            this.enterRabbitHole(world, hole);
            return;
        }

        boolean shouldDigRabbitHole = NumberUtils.chancePercentage(10);
        if (this.boundRabbitHole == null && shouldDigRabbitHole) {
            this.attemptRabbitHoleDigging(world);
            if (this.boundRabbitHole != null) return;
        }


        // determine target for the night
        if (this.nightMovementTarget == null) {
            if (this.boundRabbitHole != null) {
                this.nightMovementTarget = this.boundRabbitHole;
            } else {
                Set<RabbitHole> holes = CollectionUtils.allEntitiesOfType(world, RabbitHole.class);
                if (!holes.isEmpty()) {
                    this.nightMovementTarget = CollectionUtils.randomSetElement(holes);
                }
            }
        }

        // no rabbit holes on the map
        if (this.nightMovementTarget == null) {
            this.performRandomMove(world);
        } else {
            Location targetLoc = world.getLocation(this.nightMovementTarget);
            Location nextLoc = this.computeNextBestTile(world, currentLoc, targetLoc);
            if (nextLoc == null) return;
            world.move(this, nextLoc);
            world.setCurrentLocation(nextLoc);
        }

    }

    private Location currentLocation(World world) {
        return world.getLocation(this);
    }

    private Location computeNextBestTile(World world, Location from, Location to) {
        Location bestNextTile = LocationUtils.greedyBest(world, from, to);
        if (bestNextTile == null || !world.isTileEmpty(bestNextTile)) return null;
        return bestNextTile;
    }


    private boolean isStandingOnRabbitHole(World world) {
        Location currentLoc = world.getLocation(this);
        return world.getNonBlocking(currentLoc) instanceof RabbitHole;
    }


    public void enterRabbitHole(World world, RabbitHole hole) {
        hole.enter(this);
        world.remove(this);
    }

    public void exitRabbitHole(World world, Location loc) {
        world.setTile(loc, this);
        world.setCurrentLocation(loc);
    }


    @Override
    public void performEat(World world) {
        if (this.energy >= this.maxEnergy()) return;

        Location currentLoc = world.getCurrentLocation();
        Object nonBlockingObj = world.getNonBlocking(currentLoc);
        if (nonBlockingObj == null) return;
        if (nonBlockingObj instanceof Consumable food) {
            double energyGain = food.getEnergyValue();
            if (!food.consumableBy().contains(Rabbit.class)) return;
            food.consume(world);
            this.increaseEnergy(energyGain);
        }
    }

    @Override
    protected double maxEnergy() {
        return Math.max(this.MIN_ENERGY, this.BASE_ENERGY - this.age);
    }

    @Override
    protected double maxAge() {
        return this.MAX_AGE;
    }

    @Override
    protected double adultAge() {
        return this.maxAge() * 0.20;
    }


    private void attemptRabbitHoleDigging(World world) {
        Location currentLoc = world.getLocation(this);
        if (world.containsNonBlocking(currentLoc)) return;
        boolean shouldDig = NumberUtils.chancePercentage(40);
        if (!shouldDig) return;
        this.performRabbitHoleDigging(world, currentLoc);
    }

    public void performRabbitHoleDigging(World world, Location currentLoc) {
        RabbitHole rabbitHole = new RabbitHole();
        world.setTile(currentLoc, rabbitHole);
        this.boundRabbitHole = rabbitHole;
        this.enterRabbitHole(world, rabbitHole);
    }

    public void attemptReproduction(World world, Location location) {
        Random random = new Random();
        if (energy < 0.5 * this.maxEnergy() || random.nextDouble() >= 0.10 || this.energy < this.REPRODUCTION_ENERGY_MIN) return;

        Set<Animal> closeByRabbits = this.sameSpeciesNearby(world, location);
        Rabbit mate = (Rabbit) CollectionUtils.randomSetElement(closeByRabbits);
        if (mate == null || mate == this || mate.energy < this.REPRODUCTION_ENERGY_MIN) return;

        Rabbit baby = new Rabbit();
        Location babyLoc = LocationUtils.randomCompletelyFreeTile(world);
        world.setTile(babyLoc, baby);

        this.decreaseEnergy(5);
        mate.decreaseEnergy(5);
    }


    @Override
    public DisplayInformation getInformation() {
        StringBuilder builder = new StringBuilder(this.imageKey);
        if (this.age >= this.adultAge()) builder.append("-large");
        else builder.append("-small");
        return new DisplayInformation(Color.GREEN, builder.toString(), true);
    }

}
