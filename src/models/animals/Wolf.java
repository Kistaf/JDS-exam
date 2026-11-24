package models.animals;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import models.structures.Carcass;
import models.structures.WolfDen;
import utils.CollectionUtils;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Wolf extends Animal implements DynamicDisplayInformationProvider {
    private WolfPack boundPack;
    private final int atkDmg = 10;
    private final double MAX_ENERGY = 100;
    private final double MAX_AGE = 500;
    private final double REPRODUCTION_ENERGY_MIN = MAX_ENERGY * 0.8;
    private  final double REPRODUCTION_ENERGY = MAX_ENERGY * 0.25;
    private final double SLEEPING_ENERGY = MAX_ENERGY * 0.01;

    public Wolf(){
        boundPack = null;
        energy = MAX_ENERGY * 0.85;
        age = 0;
        this.imageKey = "wolf";
    }

    public Wolf(WolfPack wolfPack){
        boundPack = wolfPack;
        boundPack.addWolf(this);
        energy = MAX_ENERGY * 0.85;
        age = 0;
        this.imageKey = "wolf";
    }

    @Override
    protected void performDayTasks(World world) {
        if (boundPack.getWolfDen() == null) { performMakeDen(world); }
        else if(isOutsideWolfDen()) { performSurfaceDayTasks(world); }
        else { performWolfDenDayTasks(world); }
    }

    @Override
    protected void performNightTasks(World world) {
        if (boundPack.getWolfDen() == null) { performMakeDen(world); }
        else if(isOutsideWolfDen()) { performSurfaceNightTasks(world); }
        else { performWolfDenNightTasks(world); }
    }

    private void performSurfaceDayTasks(World world){
        boundPack.checkPrayStatus(world);
        Set<Animal> nearbyTargets = getNearbyAnimals(world);
        Set<Carcass> nearbyCarcasses = getNearbyCarcasses(world);

        if(preyExists() && isPreyOnMap(world)){
            performHunt(world);
        }
        else if(isHungry() && !nearbyCarcasses.isEmpty()){
            performEat(world);
        }
        else if(isHungry() && nearbyCarcasses.isEmpty()){
            performLookForCarcass(world);
        }
        else if(!nearbyTargets.isEmpty()){
            performAttack(nearbyTargets);
        }
        else if(nearbyTargets.isEmpty()){
            performLookForAnimals(world);
        }
        else {
            performMove(world);
        }
    }

    private void performSurfaceNightTasks(World world){
        boundPack.checkPrayStatus(world);
        Set<Animal> nearbyTargets = getNearbyAnimals(world);
        Set<Carcass> nearbyCarcasses = getNearbyCarcasses(world);

        if(needsSleep() || canReproduce()) {
            performReturnToDen(world);
        }
        else if(preyExists() && isPreyOnMap(world)){
            performHunt(world);
        }
        else if(isHungry() && !nearbyCarcasses.isEmpty()){
            performEat(world);
        }
        else if(isHungry() && nearbyCarcasses.isEmpty()){
            performLookForCarcass(world);
        }
        else if(!nearbyTargets.isEmpty()){
            performAttack(nearbyTargets);
        }
        else if(nearbyTargets.isEmpty()){
            performLookForAnimals(world);
        }
        else {
            performMove(world);
        }
    }

    private void performWolfDenDayTasks(World world){
        if (canReproduce() && getFertileWolfs().size() >= 1){
            CollectionUtils.randomListElement(getFertileWolfs()).performReproduction(world);
            decreaseEnergy(REPRODUCTION_ENERGY);
        }
        else if(canExitDen(world)){
            performExitDen(world);
        }
    }

    private void performWolfDenNightTasks(World world){
        performSleep();
    }

    private void performSleep(){
        increaseEnergy(SLEEPING_ENERGY);
    }

    private void performLookForAnimals(World world){
        Animal targetAnimal = detectAnimal(world, 3);

        if(targetAnimal != null){
            performMoveTowardsTarget(world, world.getLocation(targetAnimal));
        }
        else {
            performMove(world);
        }
    }

    private void performLookForCarcass(World world){
        Carcass targetCarcass = detectCarcass(world, 3);

        if(targetCarcass != null){
            performMoveTowardsTarget(world, world.getLocation(targetCarcass));
        }
        else {
            performMove(world);
        }
    }

    @Override
    protected void performEat(World world) {
        Set<Carcass> carcasses = getNearbyCarcasses(world);
        Carcass carcass = CollectionUtils.randomSetElement(carcasses);
        increaseEnergy(carcass.getEnergyValue());
        carcass.consume(world);
    }

    private void performMove(World world){
        if(world.getEmptySurroundingTiles(world.getLocation(this)).isEmpty()){
            return;
        }

        List<Wolf> wolfs = boundPack.getWolfs();

        if(wolfs.size() <= 1 || wolfs.equals(null) || isPackNearby(world)){
            performRandomMove(world);
        }
        else {
            performFollowPack(world, wolfs);
        }
    }

    private void performFollowPack(World world, List<Wolf> wolfs){
        List<Location> possibleLocs = new ArrayList<>();

        for(Wolf wolf : boundPack.getWolfs()){
            if(!boundPack.getWolfDen().getOccupyingWolfs().contains(wolf)){
                possibleLocs.add(world.getLocation(wolf));
            }
        }

        Random r = new Random();
        int randomIndex = r.nextInt(possibleLocs.size());
        Location targetLoc = possibleLocs.get(randomIndex);

        performMoveTowardsTarget(world, targetLoc);
    }

    private void performHunt(World world){
        if(isPreyNearby(world)){
            performAttack(boundPack.getPrey());
        } else {
            performMoveTowardsTarget(world, world.getLocation(boundPack.getPrey()));
        }
    }

    private void performAttack(Animal prey){
        boundPack.setPrey(prey);
        boundPack.getPrey().decreaseEnergy(atkDmg);
    }

    private void performAttack(Set<Animal> preySet){
        Animal prey = CollectionUtils.randomSetElement(preySet);
        boundPack.setPrey(prey);
        boundPack.getPrey().decreaseEnergy(atkDmg);
    }

    private void performReturnToDen(World world){
        WolfDen wolfDen = boundPack.getWolfDen();

        if(wolfDen.getOccupyingWolfs().contains(this)){
            return;
        }

        if(world.getLocation(this).equals(world.getLocation(boundPack.getWolfDen()))){
            performEnterDen(world);
        } else {
            performMoveTowardsTarget(world, world.getLocation(wolfDen));
        }

    }

    private void performEnterDen(World world){
        boundPack.getWolfDen().enter(this);
        world.remove(this);
    }

    private void performExitDen(World world){
        WolfDen wolfDen = boundPack.getWolfDen();
        wolfDen.exit(this);
        world.setTile(world.getLocation(wolfDen), this);
    }

    private void performMakeDen(World world){
        WolfDen wolfDen = new WolfDen();
        world.setTile(world.getLocation(this), wolfDen);
        boundPack.setWolfDen(wolfDen);
    }

    public void performReproduction(World world){
        decreaseEnergy(REPRODUCTION_ENERGY);
        Wolf wolf = new Wolf(boundPack);
        world.add(wolf);
        boundPack.getWolfDen().enter(wolf);
    }

    private boolean isPackNearby(World world){
        Set<Animal> nearbyWolfs = sameSpeciesNearby(world, world.getLocation(this), 2);
        if(nearbyWolfs.size() >= (boundPack.getWolfs().size() - 1) / 2 || nearbyWolfs.size() >= 3) return true;
        else return false;
    }

    private boolean isPreyNearby(World world){
        Set<Animal> nearbyTargets = sameSpeciesNearby(world, world.getLocation(this));
        if(nearbyTargets.contains(boundPack.getPrey())) return true;
        else return false;
    }

    private boolean isPreyOnMap(World world){
        return world.getEntities().get(boundPack.getPrey()) != null;
    }

    private boolean isHungry(){ return energy <= MAX_ENERGY * 0.75; }

    private boolean isOutsideWolfDen(){
        return !boundPack.getWolfDen().getOccupyingWolfs().contains(this);
    }

    private boolean canExitDen(World world){
        return world.isTileEmpty(world.getLocation(boundPack.getWolfDen()));
    }

    public boolean canReproduce(){
        return energy >= REPRODUCTION_ENERGY_MIN && age >= MAX_AGE * 0.25 && age <= MAX_AGE * 0.75;
    }

    private boolean needsSleep(){
        return energy <= MAX_ENERGY * 0.75;
    }

    private boolean preyExists(){
        return boundPack.getPrey() != null;
    }

    private Carcass detectCarcass(World world, int radius){
        return CollectionUtils.randomSetElement(getNearbyCarcasses(world, radius));
    }

    private Animal detectAnimal(World world, int radius){
        return CollectionUtils.randomSetElement(getNearbyAnimals(world, radius));
    }

    @Override
    protected double maxEnergy() {
        return MAX_ENERGY;
    }

    @Override
    protected double maxAge() {
        return MAX_AGE;
    }

    @Override
    protected void die(World world) {
        boundPack.removeWolf(this);
        super.die(world);
    }

    private Animal getRandomPrey(Set<Animal> preySet){
        List<Animal> preyList = new ArrayList<>();

        for(Animal prey : preySet){
            preyList.add(prey);
        }

        Random r = new Random();
        int randomIndex = r.nextInt(preyList.size());
        return preyList.get(randomIndex);
    }

    private Set<Animal> getNearbyAnimals(World world){
        Set<Animal> targets = new HashSet<>();

        for (Animal animal : world.getAll(Animal.class, world.getSurroundingTiles(world.getLocation(this)))) {
            if (animal != this && !boundPack.getWolfs().contains(animal)) targets.add(animal);
        }

        return targets;
    }

    private Set<Animal> getNearbyAnimals(World world, int radius){
        Set<Animal> targets = new HashSet<>();

        for (Animal animal : world.getAll(Animal.class, world.getSurroundingTiles(world.getLocation(this), radius))) {
            if (animal != this && !boundPack.getWolfs().contains(animal)) targets.add(animal);
        }

        return targets;
    }

    private Set<Carcass> getNearbyCarcasses(World world){
        Set<Carcass> carcasses = new HashSet<>();

        for (Carcass carcass : world.getAll(Carcass.class, world.getSurroundingTiles(world.getLocation(this)))) {
            carcasses.add(carcass);
        }

        return carcasses;
    }

    private Set<Carcass> getNearbyCarcasses(World world, int radius){
        Set<Carcass> carcasses = new HashSet<>();

        for (Carcass carcass : world.getAll(Carcass.class, world.getSurroundingTiles(world.getLocation(this), radius))) {
            carcasses.add(carcass);
        }

        return carcasses;
    }

    private List<Wolf> getFertileWolfs(){
        List<Wolf> fertileWolfs = new ArrayList<>();

        for(Wolf wolf : boundPack.getWolfDen().getOccupyingWolfs()){
            if(wolf.canReproduce() && !wolf.equals(this)){
                fertileWolfs.add(wolf);
            }
        }

        return fertileWolfs;
    }

    public WolfPack getBoundPack() {
        return boundPack;
    }

    public void setBoundPack(WolfPack boundPack) {
        this.boundPack = boundPack;
    }

    @Override
    protected double adultAge() {
        return this.maxAge() * 0.2;
    }

    @Override
    public DisplayInformation getInformation() {
        StringBuilder builder = new StringBuilder(this.imageKey);
        if (this.age < this.adultAge()) builder.append("-small");
        return new DisplayInformation(Color.GREEN, builder.toString(), true);
    }
}
