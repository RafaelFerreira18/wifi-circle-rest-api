package com.wificircle.api.controllers;

import com.wificircle.api.models.Community;
import com.wificircle.api.repositories.CommunityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/communities")
public class CommunityController {

    private final CommunityRepository communityRepository;

    public CommunityController(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    @PostMapping
    public ResponseEntity<Community> createCommunity(@RequestBody Community community) {
        try {
            Community savedCommunity = communityRepository.save(community);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCommunity);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Community>> getAllCommunities() {
        try {
            List<Community> communities = communityRepository.findAll();
            return ResponseEntity.ok(communities);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Community> getCommunityById(@PathVariable String id) {
        try {
            Optional<Community> community = communityRepository.findById(id);
            return community.map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Community> updateCommunity(@PathVariable String id, @RequestBody Community community) {
        community.setId(id);
        try {
            Optional<Community> existingCommunity = communityRepository.findById(id);
            if (existingCommunity.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Community updatedCommunity = communityRepository.save(community);
            return ResponseEntity.ok(updatedCommunity);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommunity(@PathVariable String id) {
        try {
            Optional<Community> existingCommunity = communityRepository.findById(id);
            if (existingCommunity.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            communityRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
