package com.iwilkey.designa.items;

import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.utils.Utils;

import java.util.HashMap;

public abstract class ItemRecipe {

    // Recipes

    // Resources
        // Wood
        public static class SticksRecipe extends ItemRecipe {
            public SticksRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.barkResource, 2);
            }
        }
        public static class PlywoodRecipe extends ItemRecipe {
            public PlywoodRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.stickResource, 2);
            }
        }
        public static class HardwoodRecipe extends ItemRecipe {
            public HardwoodRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.plywoodResource, 2);
            }
        }
        public static class ReinforcedHardwoodRecipe extends ItemRecipe {
            public ReinforcedHardwoodRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.hardwoodResource, 2);
            }
        }
        public static class StrongwoodRecipe extends ItemRecipe {
            public StrongwoodRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.reinforcedHardwoodResource, 2);
            }
        }
        public static class ReinforcedStrongwoodRecipe extends ItemRecipe {
            public ReinforcedStrongwoodRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.strongwoodResource, 2);
            }
        }

        // Stone
        public static class GravelRecipe extends ItemRecipe {
            public GravelRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.rockResource, 2);
            }
        }
        public static class ConcreteRecipe extends ItemRecipe {
            public ConcreteRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.gravelResource, 2);
            }
        }
        public static class ReinforcedConcreteRecipe extends ItemRecipe {
            public ReinforcedConcreteRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.concreteResource, 2);
            }
        }
        public static class CondensedSlabRecipe extends ItemRecipe {
            public CondensedSlabRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.reinforcedConcreteResource, 2);
            }
        }
        public static class StrongstoneRecipe extends ItemRecipe {
            public StrongstoneRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.condensedSlabResource, 2);
            }
        }
        public static class ReinforcedStrongstoneRecipe extends ItemRecipe {
            public ReinforcedStrongstoneRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.strongstoneResource, 2);
            }
        }

        // Copper
        public static class RecycledCopperRecipe extends ItemRecipe {
            public RecycledCopperRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.copperScrapResource, 2);
            }
        }
        public static class BluestoneRecipe extends ItemRecipe {
            public BluestoneRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.recycledCopperResource, 2);
            }
        }
        public static class ReinforcedBluestoneRecipe extends ItemRecipe {
            public ReinforcedBluestoneRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.bluestoneResource, 2);
            }
        }
        public static class RomanVitriolRecipe extends ItemRecipe {
            public RomanVitriolRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.reinforcedBluestoneResource, 2);
            }
        }

        // Silver
        public static class RecycledSilverRecipe extends ItemRecipe {
            public RecycledSilverRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.silverScrapResource, 2);
            }
        }
        public static class CoinSilverRecipe extends ItemRecipe {
            public CoinSilverRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.recycledSilverResource, 2);
            }
        }
        public static class SterlingSilverRecipe extends ItemRecipe {
            public SterlingSilverRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.coinSilverResource, 2);
            }
        }
        public static class ReinforcedSterlingSilverRecipe extends ItemRecipe {
            public ReinforcedSterlingSilverRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.sterlingSilverResource, 2);
            }
        }
        public static class FineSilverRecipe extends ItemRecipe {
            public FineSilverRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.reinforcedSterlingSilverResource, 2);
            }
        }
        public static class ReinforcedFineSilverRecipe extends ItemRecipe {
            public ReinforcedFineSilverRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.fineSilverResource, 2);
            }
        }

        // Iron
        public static class RecycledIronRecipe extends ItemRecipe {
            public RecycledIronRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.ironScrapResource, 2);
            }
        }
        public static class CastIronRecipe extends ItemRecipe {
            public CastIronRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.recycledIronResource, 2);
            }
        }
        public static class ReinforcedCastIronRecipe extends ItemRecipe {
            public ReinforcedCastIronRecipe(Item item) { super(item); }
            @Override
            public void create() {
               add(Assets.castIronResource, 2);
            }
        }
        public static class SteelRecipe extends ItemRecipe {
            public SteelRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.reinforcedCastIronResource, 2);
            }
        }
        public static class ReinforcedSteelRecipe extends ItemRecipe {
            public ReinforcedSteelRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.steelResource, 2);
            }
        }

        // Carbon
        public static class GraphiteRecipe extends ItemRecipe {
            public GraphiteRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.carbonSampleResource, 2);
            }
        }
        public static class CompressedGraphiteRecipe extends ItemRecipe {
            public CompressedGraphiteRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.graphiteResource, 2);
            }
        }
        public static class WeakDiamondRecipe extends ItemRecipe {
            public WeakDiamondRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.compressedGraphite, 2);
            }
        }
        public static class DiamondRecipe extends ItemRecipe {
            public DiamondRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.weakDiamondResource, 2);
            }
        }
        public static class ReinforcedDiamondRecipe extends ItemRecipe {
            public ReinforcedDiamondRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.diamondResource, 2);
            }
        }

    // CreatableItems
        // Tools
            // Drills
            public static class SimpleDrillRecipe extends ItemRecipe {
                public SimpleDrillRecipe(Item item) { super(item); }
                @Override
                public void create() {
                    add(Assets.plywoodResource, 16);
                    add(Assets.recycledCopperResource, 8);
                    add(Assets.coinSilverResource, 2);
                }
            }

            // Wrench
            public static class WrenchRecipe extends ItemRecipe {
                public WrenchRecipe(Item item) { super(item); }
                @Override
                public void create() {
                    add(Assets.recycledCopperResource, 4);
                }
            }

            public static class LadderRecipe extends ItemRecipe {
                public LadderRecipe(Item item) { super(item); }
                @Override
                public void create() {
                    add(Assets.stickResource, 2);
                    add(Assets.barkResource, 3);
                }
            }

        public static class TorchRecipe extends  ItemRecipe {
            public TorchRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.stickResource, 1);
            }
        }

        // Storage
        public static class CrateRecipe extends ItemRecipe {
            public CrateRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.plywoodTileItem, 4);
                add(Assets.silverScrapResource, 8);
            }
        }

        // Construction
        public static class PlywoodTileRecipe extends ItemRecipe {
            public PlywoodTileRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.plywoodResource, 2);
            }
        }
        public static class HardwoodTileRecipe extends ItemRecipe {
            public HardwoodTileRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.hardwoodResource, 2);
            }
        }
        public static class ReinforcedHardwoodTileRecipe extends ItemRecipe {
            public ReinforcedHardwoodTileRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.reinforcedHardwoodResource, 2);
            }
        }
        public static class StrongwoodTileRecipe extends ItemRecipe {
            public StrongwoodTileRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.strongwoodResource, 2);
            }
        }
        public static class ReinforcedStrongwoodTileRecipe extends ItemRecipe {
            public ReinforcedStrongwoodTileRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Assets.reinforcedStrongwoodResource, 2);
            }
        }

        // Machines
            // Mech Drills
            public static class CopperMechanicalDrillRecipe extends ItemRecipe {
                public CopperMechanicalDrillRecipe(Item item) { super(item); }
                @Override
                public void create() {
                    add(Assets.recycledCopperResource, 8);
                }
            }

            // Offloader
            public static class NodeRecipe extends ItemRecipe {
                public NodeRecipe(Item item) { super(item); }
                @Override
                public void create() {
                    add(Assets.recycledCopperResource, 2);
                    add(Assets.silverScrapResource, 2);
                }
            }

            // Pipes
            public static class StonePipeRecipe extends ItemRecipe {
                public StonePipeRecipe(Item item) { super(item); }
                @Override
                public void create() {
                    add(Assets.rockResource, 2);
                }
            }

            // Assembler
            public static class AssemblerRecipe extends ItemRecipe {
                public AssemblerRecipe(Item item) { super(item); }
                @Override
                public void create() {
                    add(Assets.sterlingSilverResource, 4);
                    add(Assets.castIronResource, 2);
                }
            }


    // Class
    public Item item;
    protected HashMap<Item, String> recipe = new HashMap<Item, String>();
    public ItemRecipe(Item item) {
        this.item = item;

        if(item.getItemType() instanceof ItemType.Resource)
            ((ItemType.Resource)item.getItemType()).setItemRecipe(this);
        else if (item.getItemType() instanceof ItemType.CreatableItem)
            ((ItemType.CreatableItem)item.getItemType()).setItemRecipe(this);
        else if (item.getItemType() instanceof ItemType.PlaceableBlock.CreatableTile)
            ((ItemType.PlaceableBlock.CreatableTile)item.getItemType()).setItemRecipe(this);

        create();
    }
    public abstract void create();
    public void add(Item i, int amount) {
        String a = Utils.toString(amount);
        this.recipe.put(i, a);
    }
    public void setRecipe(HashMap<Item, String> recipe) {
        this.recipe = recipe;
    }
    public HashMap<Item, String> getRecipe() { return this.recipe; }

}
