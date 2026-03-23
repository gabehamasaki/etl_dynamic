package dev.hamasakis.etl.controllers;

import dev.hamasakis.etl.dtos.UpdateUserRequest;
import dev.hamasakis.etl.dtos.UserResponse;
import dev.hamasakis.etl.models.User;
import dev.hamasakis.etl.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> index(@PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        log.info("Fetching users page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<UserResponse> usersPage = userRepository.findAll(pageable)
                .map(UserResponse::fromModel);

        return ResponseEntity.ok(usersPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> view(@PathVariable UUID id){
        return userRepository.findById(id)
                .map(UserResponse::fromModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable UUID id, @RequestBody UpdateUserRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    if (request.name() != null) {
                        user.setName(request.name());
                    }
                    if (request.email() != null) {
                        user.setName(request.email());
                    }
                    if (request.role() != null) {
                        user.setRole(request.role());
                    }
                    if (request.active() != null) {
                        user.setActive(request.active());
                    }

                    User updatedUser = userRepository.save(user);
                    return ResponseEntity.ok(UserResponse.fromModel(updatedUser));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
