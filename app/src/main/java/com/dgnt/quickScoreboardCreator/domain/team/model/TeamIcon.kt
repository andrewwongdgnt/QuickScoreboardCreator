package com.dgnt.quickScoreboardCreator.domain.team.model

import androidx.annotation.DrawableRes
import com.dgnt.quickScoreboardCreator.R

enum class TeamIcon(@DrawableRes val res: Int, group: TeamIconGroup) {
    ALIEN(R.drawable.alien, TeamIconGroup.SCIFI),
    ANT(R.drawable.ant, TeamIconGroup.ANIMALS),
    AXE(R.drawable.axe, TeamIconGroup.WEAPONS),
    BEAR(R.drawable.bear, TeamIconGroup.ANIMALS),
    BOMB(R.drawable.bomb, TeamIconGroup.WEAPONS),
    BOOK(R.drawable.book, TeamIconGroup.ARTIFACTS),
    CHALICE(R.drawable.chalice, TeamIconGroup.ARTIFACTS),
    CYBORG(R.drawable.cyborg, TeamIconGroup.SCIFI),
    DINOSAUR(R.drawable.dinosaur, TeamIconGroup.ANIMALS),
    DRAGON(R.drawable.dragon, TeamIconGroup.ANIMALS),
    EAGLE(R.drawable.eagle, TeamIconGroup.ANIMALS),
    ELEPHANT(R.drawable.elephant, TeamIconGroup.ANIMALS),
    FIREBALL(R.drawable.fireball, TeamIconGroup.NATURE),
    FIST(R.drawable.fist, TeamIconGroup.WEAPONS),
    FLOWER(R.drawable.flower, TeamIconGroup.NATURE),
    GORILLA(R.drawable.gorilla, TeamIconGroup.ANIMALS),
    HATCHET(R.drawable.hatchet, TeamIconGroup.WEAPONS),
    HURRICANE(R.drawable.hurricane, TeamIconGroup.NATURE),
    KATANA(R.drawable.katana, TeamIconGroup.WEAPONS),
    KNIFE(R.drawable.knife, TeamIconGroup.WEAPONS),
    KRAKEN(R.drawable.kraken, TeamIconGroup.ANIMALS),
    LANTERN(R.drawable.lantern, TeamIconGroup.ARTIFACTS),
    LION(R.drawable.lion, TeamIconGroup.ANIMALS),
    METEORITE(R.drawable.meteorite, TeamIconGroup.SCIFI),
    MOON(R.drawable.moon, TeamIconGroup.SCIFI),
    MOUNTAIN(R.drawable.mountain, TeamIconGroup.NATURE),
    PANTHER(R.drawable.panther, TeamIconGroup.ANIMALS),
    PIRATE_SHIP(R.drawable.pirate_ship, TeamIconGroup.VEHICLES),
    PIRATE(R.drawable.pirate, TeamIconGroup.PEOPLE),
    PLANET(R.drawable.planet, TeamIconGroup.SCIFI),
    RAY_GUN(R.drawable.ray_gun, TeamIconGroup.WEAPONS),
    REPTILE(R.drawable.reptile, TeamIconGroup.ANIMALS),
    ROBOT(R.drawable.robot, TeamIconGroup.SCIFI),
    ROCKET(R.drawable.rocket, TeamIconGroup.VEHICLES),
    SAGE(R.drawable.sage, TeamIconGroup.PEOPLE),
    SHARK(R.drawable.shark, TeamIconGroup.ANIMALS),
    SHIELD(R.drawable.shield, TeamIconGroup.WEAPONS),
    SKULL(R.drawable.skull, TeamIconGroup.PEOPLE),
    SNAKE(R.drawable.snake, TeamIconGroup.ANIMALS),
    SPACESHIP(R.drawable.spaceship, TeamIconGroup.VEHICLES),
    SPARTAN(R.drawable.spartan, TeamIconGroup.PEOPLE),
    SPIDER(R.drawable.spider, TeamIconGroup.ANIMALS),
    STAR(R.drawable.star, TeamIconGroup.ARTIFACTS),
    SWORD(R.drawable.sword, TeamIconGroup.WEAPONS),
    TANK(R.drawable.tank, TeamIconGroup.VEHICLES),
    TIGER(R.drawable.tiger, TeamIconGroup.ANIMALS),
    TORNADO(R.drawable.tornado, TeamIconGroup.NATURE),
    TREE(R.drawable.tree, TeamIconGroup.NATURE),
    TSUNAMI(R.drawable.tsunami, TeamIconGroup.NATURE),
    UFO(R.drawable.ufo, TeamIconGroup.VEHICLES),
    VAMPIRE(R.drawable.vampire, TeamIconGroup.PEOPLE),
    VIKING(R.drawable.viking, TeamIconGroup.PEOPLE),
    WARLOCK(R.drawable.warlock, TeamIconGroup.PEOPLE),
    WASP(R.drawable.wasp, TeamIconGroup.ANIMALS),
    WITCH(R.drawable.witch, TeamIconGroup.PEOPLE),
    WOLF(R.drawable.wolf, TeamIconGroup.ANIMALS),
    WORM(R.drawable.worm, TeamIconGroup.ANIMALS),
}