package cdw.helper.controllers;

import cdw.helper.dto.SuccessResponseDTO;
import cdw.helper.dto.UserUpdationDTO;
import cdw.helper.entities.User;
import cdw.helper.services.adminservices.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Class that holds Admin Controller Operations
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Fetch all users from the system
     * @return
     */
    @GetMapping
    public ResponseEntity<List<User>> fetchAllUsers() {
        List<User> users = adminService.fetchAllUsers();
        return ResponseEntity.ok().body(users);
    }

    /**
     * Approves a resident and helper/ technicians
     * @return
     */
    @PatchMapping("/users/{id}/approval")
    public ResponseEntity<SuccessResponseDTO> approveUser(@PathVariable("id") Long id) {
        String verificationStatus = adminService.verifyUser(id);

        SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setStatus(HttpStatus.OK);
        successResponseDTO.setBody(verificationStatus);

        return ResponseEntity.ok().body(successResponseDTO);
    }

    /**
     * Updates a user based on the updateUserDTO
     * @return
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<SuccessResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserUpdationDTO userUpdationDTO) {
        adminService.updateUser(id, userUpdationDTO);

        SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setStatus(HttpStatus.OK);
        successResponseDTO.setBody("Updated successfully !");

        return ResponseEntity.ok().body(successResponseDTO);
    }

    /**
     * Deletes a user
     * @param id
     * @return
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
