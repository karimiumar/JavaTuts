package com.umar.apps.hibernate.optimistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Async
public class OptimisticLockingServiceParallel {

    @Autowired CustomerRepository repository;

    @Transactional
    public void update(Long id) throws InterruptedException {
        Optional<Customer> customerOptional = repository.findById(id);
        Customer customer = customerOptional.get();
        Thread.sleep(5000);
        customer.setLastName("Lohan");
        repository.save(customer);
        System.out.println(Thread.currentThread().getName() + " " + customer.getVersion());
    }
}
