package com.bluit.tourgatronomico.controller;

import com.bluit.tourgatronomico.model.ResenaLike;
import com.bluit.tourgatronomico.repository.ResenaLikeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/resenas")
public class ResenaLikeController {

    private final ResenaLikeRepository likeRepo;

    public ResenaLikeController(ResenaLikeRepository likeRepo) {
        this.likeRepo = likeRepo;
    }

    @GetMapping("/{resenaId}/likes")
    public ResponseEntity<?> getLikes(@PathVariable Long resenaId, @RequestParam(required = false) Long usuarioId) {
        long total = likeRepo.countByResenaId(resenaId);
        boolean liked = false;
        if (usuarioId != null) {
            liked = likeRepo.existsByResenaIdAndUsuarioId(resenaId, usuarioId);
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("total", total);
        resp.put("likedByMe", liked);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/{resenaId}/likes")
    public ResponseEntity<?> toggleLike(@PathVariable Long resenaId, @RequestParam Long usuarioId) {
        boolean exists = likeRepo.existsByResenaIdAndUsuarioId(resenaId, usuarioId);
        if (exists) {
            likeRepo.deleteByResenaIdAndUsuarioId(resenaId, usuarioId);
        } else {
            likeRepo.save(new ResenaLike(resenaId, usuarioId));
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("total", likeRepo.countByResenaId(resenaId));
        resp.put("likedByMe", !exists);
        return ResponseEntity.ok(resp);
    }
}
