/*
 * Skytils - Hypixel Skyblock Quality of Life Mod
 * Copyright (C) 2020-2024 Skytils
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package gg.skytils.skytilsmod.events.impl

import gg.skytils.skytilsmod.Skytils.Companion.mc
import gg.skytils.skytilsmod.events.SkytilsEvent
import gg.skytils.skytilsmod.events.impl.PlayerSentMessageEvent.Companion.starts

/**
 * Event for when a player sends a message.
 *
 * @param type where the message is from. Currently won't work with Private Messages.
 * @param player the player that sent the message
 * @param message the message
 * @param rankColor the color of the rank
 * @param level the skyblock level, only exists when "Show Skyblock Level in Chat" is on and only on [MessageType.All]
 * @param rankText the text for the rank, is null when the player is unranked
 *
 * @author TakoTheDev
 */
data class PlayerSentMessageEvent(
    val type: MessageType,
    val player: String,
    val message: String,
    val rankColor: String,
    val level: Int? = null,
    val rankText: String? = null
) : SkytilsEvent() {
    fun isOurPlayer(): Boolean {
        return player == mc.thePlayer?.name
    }

    fun getNameWithRank(): String {
        return "$rankColor${if (rankText != null) "$rankText " else ""}$player"
    }

    companion object {
        // val privateSentMessage = "§dTo §r"

        val starts = mapOf(
            MessageType.All to listOf("§r"),
            MessageType.Party to listOf("§r§9Party §8> ", "§r§9P §8> "),
            MessageType.Guild to listOf("§r§2Guild > ", "§r§2G > "),
            // Has been removed as there is no real reason to track it, and it adds uneccessary complexity
            // MessageType.Private to listOf("§dFrom §r", privateSentMessage),
            MessageType.Coop to listOf("§r§bCo-op > ")
        )

        val playerMessageRegex = Regex("^(?:§r§8\\[§r§.(?<skyblockLevel>\\d+)§r§.] )?(?:(?<emblem>§r§..) )?(?<from>${starts.values.flatten().join()})?(?<rankColor>§.)(?:(?<rankText>\\[[^]]+]) )?(?<player>\\w+)(?: (?<guildRankColor>§.)(?<guildRankText>\\[[^]]+]))?(?:§.)+: (?<message>.+)§r$")

        private fun List<String>.join(): String {
            return this.joinToString("|")
        }
    }
}

enum class MessageType {
    All,
    Party,
    Guild,

    // Private,
    Coop;

    companion object {
        fun fromText(text: String): MessageType? {
            return starts.entries.find { it.value.contains(text) }?.key
        }
    }
}
