package org.ga.chess.controller;
import org.ga.chess.model.Admin;
import org.ga.chess.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/{email}")
    public ResponseEntity<?> getAdminByEmail(@PathVariable String email) {
        return adminService.getAdmin(email);
    }

    @GetMapping("/id-{id}")
    public ResponseEntity<?> getAdminById(@PathVariable Long id) {
        return adminService.getAdmin(id);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editAdmin(@RequestBody Admin admin) {
        return adminService.editAdmin(admin);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAdmin() {
        return adminService.deleteAdmin();
    }

    @PutMapping("/promote/{email}")
    public ResponseEntity<?> promotePlayerToAdmin(@PathVariable String email) {
        return adminService.promotePlayer(email);
    }

    @PutMapping("/deactivate/{email}")
    public ResponseEntity<?> deactivatePlayerByEmail(@PathVariable String email) {
        return adminService.deactivatePlayer(email);
    }

    @PutMapping("/deactivate/id-{id}")
    public ResponseEntity<?> deactivatePlayerById(@PathVariable Long id) {
        return adminService.deactivatePlayer(id);
    }

    @PutMapping("/activate/{email}")
    public ResponseEntity<?> activatePlayerByEmail(@PathVariable String email) {
        return adminService.activatePlayer(email);
    }

    @PutMapping("/activate/id-{id}")
    public ResponseEntity<?> activatePlayerById(@PathVariable Long id) {
        return adminService.activatePlayer(id);
    }

    @DeleteMapping("/deletePlayer/{email}")
    public ResponseEntity<?> deletePlayerByEmail(@PathVariable String email) {
        return adminService.deletePlayer(email);
    }

    @DeleteMapping("/deletePlayer/id-{id}")
    public ResponseEntity<?> deletePlayerById(@PathVariable Long id) {
        return adminService.deletePlayer(id);
    }
}

