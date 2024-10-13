package org.example.customer.Controller;

import org.example.library.dto.BlockDto;
import org.example.library.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api/blocks")
public class BlockController {

    @Autowired
    private BlockService blockService;

    @PostMapping("/block")
    public ResponseEntity<String> blockUser(@RequestParam Long blockerId, @RequestParam Long blockedId) {
        blockService.blockUser(blockerId, blockedId);
        return ResponseEntity.ok("User blocked successfully.");
    }

    @DeleteMapping("/unblock")
    public ResponseEntity<String> unblockUser(@RequestParam Long blockerId, @RequestParam Long blockedId) {
        blockService.unblockUser(blockerId, blockedId);
        return ResponseEntity.ok("User unblocked successfully.");
    }

    @GetMapping("/is-blocked")
    public ResponseEntity<Boolean> isUserBlocked(@RequestParam Long blockerId, @RequestParam Long blockedId) {
        boolean isBlocked = blockService.isUserBlocked(blockerId, blockedId);
        return ResponseEntity.ok(isBlocked);
    }

    @GetMapping("/blocked-users")
    public ResponseEntity<List<BlockDto>> getBlockedUsers(@RequestParam Long blockerId) {
        List<BlockDto> blockedUsers = blockService.getBlockedUsers(blockerId);
        return ResponseEntity.ok(blockedUsers);
    }
}
