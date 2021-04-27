package com.umar.apps.hibernate.optimistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class OptimisticLockingService {

    @Autowired CustomerRepository repository;

    public ResponseEntity<?> createCustomer(Customer customer) {
        repository.save(customer);
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<?> updateCustomer(Long id) {
        Optional<Customer> customerOptional = repository.findById(id);
        customerOptional.get().setFirstName("Lindsey");
        repository.save(customerOptional.get());
        System.out.println(Thread.currentThread().getName() + " update first name " + customerOptional.get().getVersion());
        return ResponseEntity.ok().build();
    }
}
