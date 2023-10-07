package com.example.paymentverificationsystem.controller;


import com.example.paymentverificationsystem.services.MyService;
import com.example.paymentverificationsystem.models.PaymentRequest;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
//@CrossOrigin(origins = "http://localhost:5173")
public class HomeController {

    @Autowired
    private final APIContext apiContext;

    private final MyService myService;
    @GetMapping("/home")
    public String home(){
        try{
            myService.sendMessageToWebSocket("Hello from 8080");
            return "Hello Home";
        }catch(Exception ex){
           return ex.getMessage();
        }
    }

    @GetMapping("/secured")
    public String secured(){
        return "secured page";
    }

    @PostMapping("/pay")
    public ResponseEntity<Map<String, String>> pay(@RequestBody PaymentRequest paymentRequest) {
        Map<String, String> response = new HashMap<>();

        // Create payment amount and set transaction details
        Amount amount = new Amount();
        amount.setCurrency("EUR");
        amount.setTotal(String.valueOf(paymentRequest.getAmount()));

        Transaction transaction = new Transaction();
        transaction.setDescription(paymentRequest.getDescription());
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:5173/cancel");
        redirectUrls.setReturnUrl("http://localhost:5173/success");
        payment.setRedirectUrls(redirectUrls);

        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")){
        try {
                Payment createdPayment = payment.create(apiContext);
                for (Links link : createdPayment.getLinks()) {
                    if (link.getRel().equalsIgnoreCase("approval_url")) {
                        response.put("approval_url", link.getHref());
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }
                }
            } catch (PayPalRESTException e) {
                System.out.println(e.getMessage());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }


        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/complete")
    public ResponseEntity<String> completePayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = Payment.get(apiContext, paymentId);
            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerId);
            Payment executedPayment = payment.execute(apiContext, paymentExecution);

            if (executedPayment.getState().equalsIgnoreCase("approved")) {
                return new ResponseEntity<>("Payment Successful!", HttpStatus.OK);
            }

        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Payment Failed!", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Payment Failed!", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancelPayment() {
        return new ResponseEntity<>("Payment Canceled!", HttpStatus.OK);
    }

}
