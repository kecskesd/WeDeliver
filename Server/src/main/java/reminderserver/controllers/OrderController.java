package reminderserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reminderserver.models.Order;
import reminderserver.service.OrderService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    //definovanie REST volani pre order
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Order> create(@RequestBody final Order order){
        orderService.create(order);
        return orderService.getAll();
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Order> update(@RequestBody final Order order){
        orderService.update(order);
        return orderService.getAll();
    }

    @GetMapping
    public List<Order> getAll(){
        return orderService.getAll();
    }

    @GetMapping(path = "buyerId/{buyerId}")
    public List<Order> findByBuyerId(@PathVariable("buyerId") Long buyerId){
        return orderService.findByBuyerId(buyerId);
    }

    @GetMapping(path = "customerId/{customerId}")
    public List<Order> findByCustomerId(@PathVariable("customerId") Long customerId){
        return orderService.findByCustomerId(customerId);
    }
}
