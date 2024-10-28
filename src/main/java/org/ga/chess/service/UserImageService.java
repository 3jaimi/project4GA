package org.ga.chess.service;


import lombok.Setter;
import org.ga.chess.exception.NotFoundException;
import org.ga.chess.model.User;
import org.ga.chess.model.UserImage;
import org.ga.chess.repository.IUserImageRepository;
import org.ga.chess.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;


@Service
@Setter
public class UserImageService {
    @Autowired
    private IUserImageRepository userImageRepository;

    @Autowired
    private IUserRepository userRepository;

    public ResponseEntity<?> saveUserImage(MultipartFile imageFile) {
        try {
            Blob imageBlob = new SerialBlob(imageFile.getBytes());
            UserImage userImage = new UserImage();
            userImage.setUser(UserService.getCurrentLoggedInUser());
            userImage.setImage(imageBlob);

            return new ResponseEntity<>(userImageRepository.save(userImage), HttpStatusCode.valueOf(200));
        } catch (IOException | SQLException e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
    }

    public ResponseEntity<?> getUserImageById(Long id) {
        return new ResponseEntity<>(userImageRepository.findById(id).orElseThrow(()->new NotFoundException(UserImage.class.getSimpleName())), HttpStatusCode.valueOf(400));
    }


    public ResponseEntity<?> updateUserImage(MultipartFile imageFile) {
        try {
            UserImage existingUserImage = userImageRepository.findById(UserService.getCurrentLoggedInUser().getUserImage().getId())
                    .orElseThrow(()->new NotFoundException(UserImage.class.getSimpleName()));
            Blob imageBlob = new SerialBlob(imageFile.getBytes());

            existingUserImage.setImage(imageBlob);
            return new ResponseEntity<>(userImageRepository.save(existingUserImage),HttpStatusCode.valueOf(200));
        } catch (IOException | SQLException e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
    }

    public ResponseEntity<?> deleteUserImage() {
        userImageRepository.deleteById(UserService.getCurrentLoggedInUser().getUserImage().getId());
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }


}

