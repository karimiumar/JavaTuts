package com.umar.apps.hibernate.optimistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class OptimisticLockingController {

    @Autowired OptimisticLockingService optimisticLockingService;
    @Autowired OptimisticLockingServiceParallel optimisticLockingServiceParallel;

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        optimisticLockingService.createCustomer(customer);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id) throws InterruptedException {
        optimisticLockingServiceParallel.update(id);
        optimisticLockingService.updateCustomer(id);
        return ResponseEntity.ok().build();
    }
}
