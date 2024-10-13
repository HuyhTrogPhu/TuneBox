package org.example.library.service.implement;

import org.example.library.dto.BlockDto; // Thay đổi import cho DTO mới
import org.example.library.mapper.BlockMapper; // Thay đổi import cho mapper mới
import org.example.library.model.Block; // Thay đổi import cho entity mới
import org.example.library.model.User;
import org.example.library.repository.BlockRepository; // Thay đổi import cho repository mới
import org.example.library.repository.UserRepository;
import org.example.library.service.BlockService; // Thay đổi import cho service mới
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BlockServiceImpl implements BlockService {

    @Autowired
    private UserRepository userRepository; // Sử dụng UserRepository
    @Autowired
    private BlockRepository blockRepository; // Sử dụng BlockRepository
    @Autowired
    private BlockMapper blockMapper; // Sử dụng BlockMapper

    @Override
    public void blockUser(Long blockerId, Long blockedId) {
        // Lấy user từ repository
        User blocker = userRepository.findById(blockerId)
                .orElseThrow(() -> new RuntimeException("Blocker not found."));
        User blocked = userRepository.findById(blockedId)
                .orElseThrow(() -> new RuntimeException("Blocked user not found."));

        // Kiểm tra nếu user đã block targetUser trước đó chưa
        if (blockRepository.existsByBlocker_IdAndBlocked_Id(blockerId, blockedId)) {
            throw new RuntimeException("User is already blocked.");
        }

        // Tạo bản ghi block mới
        Block block = new Block();
        block.setBlocker(blocker); // Sử dụng đối tượng User
        block.setBlocked(blocked); // Sử dụng đối tượng User
        block.setCreateBlock(LocalDateTime.now()); // Hoặc LocalDateTime.now()

        blockRepository.save(block);
    }

    @Override
    public void unblockUser(Long blockerId, Long blockedId) {
        // Tìm kiếm bản ghi block và xóa nó
        Block block = blockRepository.findByBlocker_IdAndBlocked_Id(blockerId, blockedId)
                .orElseThrow(() -> new RuntimeException("Block record not found."));
        blockRepository.delete(block);
    }

    @Override
    public boolean isUserBlocked(Long blockerId, Long blockedId) {
        return blockRepository.existsByBlocker_IdAndBlocked_Id(blockerId, blockedId);
    }

    @Override
    public List<BlockDto> getBlockedUsers(Long blockerId) {
        List<Block> blockedUsers = blockRepository.findByBlocker_Id(blockerId);
        return blockMapper.toDTOList(blockedUsers); // Sử dụng mapper để convert entity sang DTO
    }
}
