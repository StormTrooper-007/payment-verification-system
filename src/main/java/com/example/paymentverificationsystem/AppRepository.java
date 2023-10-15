package com.example.paymentverificationsystem;

import com.example.paymentverificationsystem.models.OrderDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRepository extends MongoRepository<OrderDetails, String>{

}
