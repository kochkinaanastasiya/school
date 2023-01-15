package ru.hogwarts.school.controller;

import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;

public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping
    public void uploadAvatar(@RequestParam MultipartFile avatar) throws IOException {
        avatarService.uploadAvatar(avatar);
    }

    @GetMapping("/{id}/from-db")
    public ResponseEntity<byte[]> readAvatarFromDb(@PathVariable long id){
        return readAvatar(avatarService.readAvatarFromDb(id));
    }

    @GetMapping("/{id}/from-fs")
    public ResponseEntity<byte[]> readAvatarFromFs(@PathVariable long id) throws IOException{
        return readAvatar(avatarService.readAvatarFromFs(id));
    }

    private ResponseEntity<byte[]> readAvatar(Pair<String, byte[]> pair){
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(pair.getFirst()))
                .contentLength(pair.getSecond().length)
                .body(pair.getSecond());
    }
}
