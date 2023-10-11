package com.example.paymentverificationsystem;

import com.example.paymentverificationsystem.models.OrderDetails;
import com.example.paymentverificationsystem.models.PaymentRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppRepository extends MongoRepository<OrderDetails, String>{

}
