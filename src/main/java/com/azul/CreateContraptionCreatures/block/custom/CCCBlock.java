package com.azul.CreateContraptionCreatures.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class CCCBlock extends Block {

    public CCCBlock(Settings settings) {
        super(settings);
    }

	@Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return makeShape();
    }

	public static VoxelShape makeShape()
	{
		return VoxelShapes.union(
			VoxelShapes.cuboid(0.125, 0.5, 0.125, 0.875, 0.6875, 0.875),
			VoxelShapes.cuboid(0, 0, 0, 1, 0.5, 1),
			VoxelShapes.cuboid(0, 0.6875, 0, 1, 1, 1)
		);
	}
}
