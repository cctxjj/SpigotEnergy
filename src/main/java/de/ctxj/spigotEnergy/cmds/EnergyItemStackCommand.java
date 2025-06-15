package de.ctxj.spigotEnergy.cmds;

import de.ctxj.spigotEnergy.objects.abstr.EnergyItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EnergyItemStackCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(!player.isOp()) {
                player.sendMessage(ChatColor.RED + "Du hast keine Berechtigung, diesen Befehl zu nutzen.");
                return false;
            }
            switch(args.length) {
                case 1 -> {
                    if(args[0].equalsIgnoreCase("add")) {
                        if(EnergyItemStack.isEnergyItemStack(player.getInventory().getItemInMainHand())) {
                            player.sendMessage(ChatColor.RED + "Das Item ist bereits als EnergyItem registriert.");
                            return false;
                        }
                        if(player.getInventory().getItemInMainHand().getType() == Material.AIR) {
                            player.sendMessage(ChatColor.RED + "Du musst ein Item in der Hand halten, um es als EnergyItem zu registrieren.");
                            return false;
                        }
                        if(!player.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
                            player.sendMessage(ChatColor.RED + "Das Item muss einen Namen haben, um es als EnergyItem zu registrieren.");
                            return false;
                        }

                        EnergyItemStack.addEnergyItemToConfig(player.getInventory().getItemInMainHand());
                        player.sendMessage(ChatColor.GREEN + "Du hast ein EnergyItem hinzugefügt.");
                        ItemStack item = player.getInventory().getItemInMainHand();
                        ItemMeta meta = item.getItemMeta();
                        if (meta != null) {
                            List<String> lore = meta.getLore();
                            if (lore == null) {
                                lore = new ArrayList<>();
                            } else {
                                lore.removeIf(line -> ChatColor.stripColor(line).startsWith("Energy:"));
                            }
                            List<String> newLore = new ArrayList<>();
                            newLore.add(""); // Leerzeile vor der Energy-Zeile
                            newLore.add(ChatColor.GREEN + "Energy: 0");
                            newLore.add(""); // Leerzeile nach der Energy-Zeile
                            newLore.addAll(lore); // Bestehende Lore anhängen
                            meta.setLore(newLore);
                            item.setItemMeta(meta);
                        }
                    } else if(args[0].equalsIgnoreCase("remove")) {
                        try {
                            EnergyItemStack.removeEnergyItemFromConfig(player.getInventory().getItemInMainHand());
                            player.sendMessage(ChatColor.GREEN + "Du hast einen EnergyItemStack entfernt.");
                        } catch (Exception e) {
                            player.sendMessage(ChatColor.RED + "Das Item ist nicht als EnergyItem registriert.");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Unbekannter Befehl: " + args[0]);
                    }
                }
                case 2 -> {
                    if (args[0].equalsIgnoreCase("setenergy")) {
                        try {
                            int amount = Integer.parseInt(args[1]);
                            if (amount < 0) {
                                player.sendMessage(ChatColor.RED + "Die Energie muss eine positive Zahl sein.");
                                return false;
                            }
                            ItemStack item = player.getInventory().getItemInMainHand();
                            if (item.getType() == Material.AIR) {
                                player.sendMessage(ChatColor.RED + "Du musst ein Item in der Hand halten, um die Energie zu setzen.");
                                return false;
                            }
                            if (!EnergyItemStack.isEnergyItemStack(item)) {
                                player.sendMessage(ChatColor.RED + "Das Item ist kein gültiges EnergyItem.");
                                return false;
                            }
                            EnergyItemStack energyItemStack = new EnergyItemStack(item);
                            energyItemStack.setEnergy(amount);
                            player.sendMessage(ChatColor.GREEN + "Die Energie des Items wurde auf " + amount + " gesetzt.");
                        } catch (NumberFormatException e) {
                            player.sendMessage(ChatColor.RED + "Bitte gib eine gültige Zahl für die Energie an.");
                        } catch (Exception e) {
                            player.sendMessage(ChatColor.RED + "Ein Fehler ist aufgetreten. Bitte prüfe deine Eingabe.");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Unbekannter Befehl: " + args[0]);
                    }
                }
                default -> {
                    player.sendMessage(ChatColor.RED + "Nutze /energyitem [add/remove] oder /energyitem setMaxEnergy [amount] oder /energyitem setEnergy [amount]");
                    return false;
                }
            }
        }
        return false;
    }
}
