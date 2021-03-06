package projektor.projector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import projektor.Projektor;
import projektor.Reference;
import projektor.helper.StackHelper;
import projektor.proxy.CommonProxy;

public class ProjectorBlock extends BlockContainer
{
    public ProjectorBlock()
    {
        super(Material.iron);
        this.setHardness(2F);
        this.setResistance(50F);
        this.setBlockName(Reference.Naming.PROJECTOR);
        this.setCreativeTab(Projektor.TAB);
    }

    public void addStacksDroppedOnBlockBreak(TileEntity tileEntity, ArrayList<ItemStack> itemStacks)
    {
        if (tileEntity instanceof ProjectorTileEntity)
        {
            ProjectorTileEntity projector = (ProjectorTileEntity) tileEntity;
            if (projector.getHasBlueprint())
            {
                itemStacks.add(projector.getSchematic());
            }
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metaData)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null)
        {
            ArrayList<ItemStack> droppedStacks = new ArrayList<ItemStack>();
            addStacksDroppedOnBlockBreak(tileEntity, droppedStacks);
            for (ItemStack itemstack : droppedStacks)
            {
                StackHelper.throwItemStack(world, itemstack, x, y, z);
            }
            super.breakBlock(world, x, y, z, block, metaData);
        }

    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new ProjectorTileEntity();
    }

    @Override
    public int getRenderType()
    {
        return CommonProxy.RENDER_ID;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float b, float c, float g)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null && !player.isSneaking())
        {
            if (!world.isRemote)
            {
                player.openGui(Projektor.INSTANCE, 0, world, x, y, z);
            }
            return true;
        }

        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase el, ItemStack is)
    {
        super.onBlockPlacedBy(world, x, y, z, el, is);
        int facing = MathHelper.floor_double(el.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        world.setBlockMetadataWithNotify(x, y, z, facing, 2);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir)
    {
        blockIcon = ir.registerIcon(Reference.IIcon.DUMMY);
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
}
