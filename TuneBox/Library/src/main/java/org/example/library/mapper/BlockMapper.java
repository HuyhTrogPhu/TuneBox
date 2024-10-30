package org.example.library.mapper;

import org.example.library.dto.BlockDto;
import org.example.library.model.Block;
import org.example.library.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BlockMapper {

    public BlockDto toDto(Block block) {
        if (block == null) {
            return null;
        }

        BlockDto blockDto = new BlockDto();
        blockDto.setId(block.getId());

        // Chỉ ánh xạ ID và tên người dùng từ blocker và blocked
        blockDto.setBlockerId(block.getBlocker().getId());
        blockDto.setBlockedId(block.getBlocked().getId());
        blockDto.setCreateBlock(block.getCreateBlock());

        return blockDto;
    }

    public Block toEntity(BlockDto blockDto) {
        if (blockDto == null) {
            return null;
        }

        Block block = new Block();
        block.setId(blockDto.getId());
        // Lưu ý: Cần phải tìm kiếm User dựa vào ID
        block.setBlocker(new User(blockDto.getBlockerId()));
        block.setBlocked(new User(blockDto.getBlockedId()));
        block.setCreateBlock(blockDto.getCreateBlock());

        return block;
    }
    public List<BlockDto> toDTOList(List<Block> blocks) {
        return blocks.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}

