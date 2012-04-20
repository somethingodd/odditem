/* This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package info.somethingodd.OddItem;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class Listener implements org.bukkit.event.Listener {
    private OddItemBase oddItemBase;

    public Listener(OddItemBase oddItemBase) {
        this.oddItemBase = oddItemBase;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 128; y++) {
                    Block block = chunk.getBlock(x, y, z);
                    for (ItemStack itemStack : SpoutItem.getCustomBlocks().keySet()) {
                        if (block.getTypeId() == itemStack.getTypeId() && block.getData() == itemStack.getData().getData()) {
                            SpoutManager.getMaterialManager().overrideBlock(block, SpoutItem.getCustomBlocks().get(itemStack));
                        }
                    }
                }
            }
        }
    }
}