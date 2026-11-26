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
import java.util.Set;

public class Rabbit extends Animal implements DynamicDisplayInformationProvider {
    private RabbitHole boundRabbitHole;
    private Object nightMovementTarget;
    private Object dayMovementTarget;
    private final double MIN_ENERGY = 10;
    private final double BASE_ENERGY = 30;
    private final double MAX_AGE = 50;
    private int reproductionCooldown = 30;

    public Rabbit() {
        this.energy = BASE_ENERGY;
        this.imageKey = "rabbit";
    }

    @Override
    public void act(World world) {
        this.reproductionCooldown = Math.max(0, this.reproductionCooldown - 1);
        super.act(world);
    }

    @Override
    protected void performDayTasks(World world) {
        if (!world.isOnTile(this)) return;
        this.nightMovementTarget = null;

        if (this.dayMovementTarget == null) {
            boolean searchForGrass = NumberUtils.chancePercentage(60);
            if (searchForGrass) {
                Set<Grass> grassSet = CollectionUtils.allEntitiesOfType(world, Grass.class);
                Grass grassTarget = CollectionUtils.randomSetElement(grassSet);
                if (grassTarget == null) return;
                this.dayMovementTarget = grassTarget;
            } else {
                Set<Rabbit> rabbitSet = CollectionUtils.allEntitiesOfType(world, Rabbit.class);
                Rabbit mateTarget = CollectionUtils.randomSetElement(rabbitSet);
                if (mateTarget == null) return;
                this.dayMovementTarget = mateTarget;
            }
        }

        Location currentLoc = currentLocation(world);

        try {
            Location targetLoc = world.getLocation(this.dayMovementTarget);
            Location nextLoc = this.computeNextBestTile(world, currentLoc, targetLoc);
            if (nextLoc == null) return;
            world.move(this, nextLoc);
            world.setCurrentLocation(nextLoc);
        } catch (IllegalArgumentException e) {
            // target was eaten
            this.performRandomMove(world);
        }

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


    public void performRabbitHoleDigging(World world, Location currentLoc) {
        RabbitHole rabbitHole = new RabbitHole();
        world.setTile(currentLoc, rabbitHole);
        this.boundRabbitHole = rabbitHole;
        this.enterRabbitHole(world, rabbitHole);
    }

    public void enterRabbitHole(World world, RabbitHole hole) {
        hole.enter(this);
        world.remove(this);
    }

    public void exitRabbitHole(World world, Location loc) {
        world.setTile(loc, this);
        world.setCurrentLocation(loc);
    }

    public void reproduce() {
        this.decreaseEnergy(5);
        this.reproductionCooldown = 30;
    }

    public boolean canReproduce() {
        return this.MIN_ENERGY <= (this.energy - this.reproductionEnergyRequired()) && this.reproductionCooldown == 0;
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


    public void sleepRecovery() {
        double regainedEnergy = this.sleepingEnergyRegain();
        this.increaseEnergy(regainedEnergy);
    }

    private double reproductionEnergyRequired() {
        return this.maxEnergy() * 0.2;
    }

    private double sleepingEnergyRegain() {
        return this.maxEnergy() * 0.01;
    }


    private void attemptRabbitHoleDigging(World world) {
        Location currentLoc = world.getLocation(this);
        if (world.containsNonBlocking(currentLoc)) return;
        boolean shouldDig = NumberUtils.chancePercentage(40);
        if (!shouldDig) return;
        this.performRabbitHoleDigging(world, currentLoc);
    }

    @Override
    public DisplayInformation getInformation() {
        StringBuilder builder = new StringBuilder(this.imageKey);
        if (this.age >= this.adultAge()) builder.append("-large");
        else builder.append("-small");
        return new DisplayInformation(Color.GREEN, builder.toString(), true);
    }

}
