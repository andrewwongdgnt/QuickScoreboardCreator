package com.dgnt.quickScoreboardCreator.ui.common.resourcemapping

import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIconGroup

fun TeamIcon.iconRes() = when (this) {
    TeamIcon.ALIEN-> R.drawable.alien
    TeamIcon.ANT -> R.drawable.ant
    TeamIcon.AXE -> R.drawable.axe
    TeamIcon.BEAR -> R.drawable.bear
    TeamIcon.BOMB -> R.drawable.bomb
    TeamIcon.BOOK -> R.drawable.book
    TeamIcon.CHALICE -> R.drawable.chalice
    TeamIcon.CYBORG -> R.drawable.cyborg
    TeamIcon.DINOSAUR -> R.drawable.dinosaur
    TeamIcon.DRAGON -> R.drawable.dragon
    TeamIcon.EAGLE -> R.drawable.eagle
    TeamIcon.ELEPHANT -> R.drawable.elephant
    TeamIcon.FIREBALL -> R.drawable.fireball
    TeamIcon.FIST -> R.drawable.fist
    TeamIcon.FLOWER -> R.drawable.flower
    TeamIcon.GORILLA -> R.drawable.gorilla
    TeamIcon.HAMMER -> R.drawable.hammer
    TeamIcon.HATCHET -> R.drawable.hatchet
    TeamIcon.HURRICANE -> R.drawable.hurricane
    TeamIcon.KNIFE -> R.drawable.knife
    TeamIcon.KRAKEN -> R.drawable.kraken
    TeamIcon.LANTERN -> R.drawable.lantern
    TeamIcon.LION -> R.drawable.lion
    TeamIcon.METEORITE -> R.drawable.meteorite
    TeamIcon.MOON -> R.drawable.moon
    TeamIcon.MOUNTAIN -> R.drawable.mountain
    TeamIcon.PANTHER -> R.drawable.panther
    TeamIcon.PIRATE_SHIP -> R.drawable.pirate_ship
    TeamIcon.PIRATE -> R.drawable.pirate
    TeamIcon.PLANET -> R.drawable.planet
    TeamIcon.RAY_GUN -> R.drawable.ray_gun
    TeamIcon.REPTILE -> R.drawable.reptile
    TeamIcon.ROBOT -> R.drawable.robot
    TeamIcon.ROCKET -> R.drawable.rocket
    TeamIcon.SAGE -> R.drawable.sage
    TeamIcon.SHARK -> R.drawable.shark
    TeamIcon.SHIELD -> R.drawable.shield
    TeamIcon.SKULL -> R.drawable.skull
    TeamIcon.SNAKE -> R.drawable.snake
    TeamIcon.SPACESHIP -> R.drawable.spaceship
    TeamIcon.SPARTAN -> R.drawable.spartan
    TeamIcon.SPIDER -> R.drawable.spider
    TeamIcon.STAR -> R.drawable.star
    TeamIcon.SWORD -> R.drawable.sword
    TeamIcon.TANK -> R.drawable.tank
    TeamIcon.TIGER -> R.drawable.tiger
    TeamIcon.TORNADO -> R.drawable.tornado
    TeamIcon.TREE -> R.drawable.tree
    TeamIcon.TSUNAMI -> R.drawable.tsunami
    TeamIcon.UFO -> R.drawable.ufo
    TeamIcon.VAMPIRE -> R.drawable.vampire
    TeamIcon.VIKING -> R.drawable.viking
    TeamIcon.WARLOCK -> R.drawable.warlock
    TeamIcon.WASP -> R.drawable.wasp
    TeamIcon.WITCH -> R.drawable.witch
    TeamIcon.WOLF -> R.drawable.wolf
    TeamIcon.WORM -> R.drawable.worm
}

fun TeamIconGroup.titleRes() = when(this) {
    TeamIconGroup.ANIMALS -> R.string.ic_group_animals
    TeamIconGroup.ARTIFACTS -> R.string.ic_group_artifacts
    TeamIconGroup.NATURE -> R.string.ic_group_nature
    TeamIconGroup.PEOPLE -> R.string.ic_group_people
    TeamIconGroup.SCIFI -> R.string.ic_group_scifi
    TeamIconGroup.VEHICLES -> R.string.ic_group_vehicles
    TeamIconGroup.WEAPONS -> R.string.ic_group_weapons
}