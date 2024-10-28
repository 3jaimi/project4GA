package org.ga.chess.controller;

import org.ga.chess.service.UserImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user-images")
public class UserImageController {

    @Autowired
    private UserImageService userImageService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadUserImage(@RequestParam("imageFile") MultipartFile imageFile) {
        return userImageService.saveUserImage(imageFile);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserImageById(@PathVariable Long id) {
        return userImageService.getUserImageById(id);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUserImage(@RequestParam("imageFile") MultipartFile imageFile) {
        return userImageService.updateUserImage(imageFile);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUserImage() {
        return userImageService.deleteUserImage();
    }
}
