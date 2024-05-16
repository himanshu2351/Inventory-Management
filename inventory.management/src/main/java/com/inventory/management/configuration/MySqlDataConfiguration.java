package com.inventory.management.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.inventory.management.dto.enums.ProductStatus;
import com.inventory.management.model.Customer;
import com.inventory.management.model.Product;
import com.inventory.management.repository.CustomerRepository;
import com.inventory.management.repository.ProductRepository;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.util.Optional;

@Component
public class MySqlDataConfiguration {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostConstruct
    private void postConstruct() {
        Customer customer = new Customer();
        customer.setCust_id(1);
        customer.setAddress("Bangalore");
        customer.setEmail("himanshu2351@gmail.com");
        customer.setName("Himanshu");
        customer.setPhone("8789110970");
        customer.setPassword("Shekhar");

        Optional<Customer> customer1 = customerRepository.findById(1);
        if(customer1.isEmpty()){
            customerRepository.save(customer);
        }
        for(int i=1;i<=3;i++){
            Product product = new Product();
            product.setVersion(0);
            product.setStatus(ProductStatus.AVAILABLE.toString());
            product.setDate(new Date(System.currentTimeMillis()));
            product.setPrice(i*100);
            product.setQuantity(3);
            product.setId(i);
            switch (i){
                case 1:
                    product.setName("Chicken");
                    break;
                case 2:
                    product.setName("Mutton");
                    break;
                case 3:
                    product.setName("Fish");
                    break;
            }

            Optional<Product> product1 = productRepository.findById((long) i);
            if(product1.isEmpty()){
                productRepository.save(product);
            }
        }
    }
}