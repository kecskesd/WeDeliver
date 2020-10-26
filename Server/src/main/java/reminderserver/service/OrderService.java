package reminderserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reminderserver.Repositories.OrderRepository;
import reminderserver.models.Order;

import java.util.*;


@Service
public class OrderService {


    @Autowired
    OrderRepository orderRepository;

    public void create(Order order){
        orderRepository.save(order);
    }

    public void update(Order order) {
        for (Order me : orderRepository.findAll()) {
            if (order.getId().equals(me.getId())) {
                me.setStatus(order.getStatus());
                orderRepository.save(me);
            }
        }
    }

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public List<Order> findByBuyerId(Long buyerId){
        return orderRepository.findByBuyerId(buyerId);
    }

    public List<Order> findByCustomerId(Long customerId){
        return orderRepository.findByCustomerId(customerId);
    }

}
