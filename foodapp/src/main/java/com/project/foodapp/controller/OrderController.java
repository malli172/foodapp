package com.project.foodapp.controller;
import com.project.foodapp.adapter.LocalDateTimeAdapter;
import com.project.foodapp.exception.CommentAlreadyExistException;
import com.project.foodapp.exception.OrderAlreadyCheckoutException;
import com.project.foodapp.exception.OrderAlreadyDeliverException;
import com.project.foodapp.exception.OrderNotExistException;
import com.project.foodapp.model.Dish;
import com.project.foodapp.model.Order;
import com.project.foodapp.service.OrderService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/order")
public class OrderController {

  @PersistenceContext
  private EntityManager entityManager;

  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

@PostMapping(path = "/addToCart")
@Transactional
public int addOrderToCart(@RequestBody String jsonOrder) {
  Gson gson = new GsonBuilder()
      .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
      .create();

  JSONObject order = new JSONObject(jsonOrder);
  long Id = order.getLong("customerId");
  String customerId = String.valueOf(Id);
  String restaurantId = order.getString("restaurantId");
  JSONArray shopcart = order.getJSONArray("shopcart");
  List<Dish> list = new ArrayList<>();
  Map<Dish, Integer> dishCountMap = new HashMap<>(); 
  Set<Dish> dishSet = new HashSet<>(); 

  for (Object object : shopcart) {
    Dish dish = gson.fromJson(object.toString(), Dish.class);

    dishCountMap.put(dish, dishCountMap.getOrDefault(dish, 0) + 1);

    if (!dishSet.contains(dish)) {
      Dish mergedDish = entityManager.merge(dish); 
      list.add(mergedDish);
      dishSet.add(dish);
    }
  }

  
  for (Map.Entry<Dish, Integer> entry : dishCountMap.entrySet()) {
    Dish dish = entry.getKey();
    int count = entry.getValue();

    for (int i = 0; i < count - 1; i++) {
      if (!list.contains(dish)) {
        Dish mergedDish = entityManager.merge(dish); 
        list.add(mergedDish);
      }
    }
  }

  return orderService.addOrderToCart(customerId, restaurantId, list);
}


  @DeleteMapping(path = "{id}")
  public int deleteOrder(@PathVariable("id") String id)
      throws OrderNotExistException, OrderAlreadyDeliverException {
    int res = orderService.cancelOrder(id);
    if (res == 0) {
      throw new OrderNotExistException("Order doesn't exist");
    }
    if (res == -1) {
      throw new OrderAlreadyDeliverException(
          "Can't cancel order. It is either in delivery or finished");
    }
    return res;
  }

  @PostMapping(path = "/checkoutAll")
  public int checkoutUsers(@RequestBody String jsonOrders)
      throws OrderNotExistException, OrderAlreadyCheckoutException {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .create();

    JSONObject orders = new JSONObject((jsonOrders));
    JSONArray orderList = orders.getJSONArray("orders");
    List<Order> list = new ArrayList<>();
    for (Object object : orderList) {
      list.add(gson.fromJson(object.toString(), Order.class));
    }
    int res = orderService.checkoutAll(list);
    if (res == 0) {
      throw new OrderNotExistException("Order doesn't exist");
    }
    if (res == -1) {
      throw new OrderAlreadyCheckoutException("Order already checkout");
    }
    return res;
  }


  @PostMapping(path = "/addComment")
  public int addComment(@RequestBody String jsonOrder)
      throws CommentAlreadyExistException, OrderNotExistException {
    JSONObject order = new JSONObject(jsonOrder);
    int cid=order.getInt("orderId");
    String orderId = cid+""; 
    int rating = order.getInt("rating");
    String content = order.getString("content");
    int res = orderService.addComment(orderId, rating, content);
    if (res == 0) throw new CommentAlreadyExistException("Each order can only have one comment");
    if (res == -1) throw new OrderNotExistException("Order doesn't exist");
    return res;
  }

  @DeleteMapping(path = "/deleteComment/{id}")
  public int deleteComment(@PathVariable("id") String id) throws OrderNotExistException {
    int res = orderService.deleteComment(id);
    if (res == -1) throw new OrderNotExistException("Order doesn't exist");
    return res;
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler({OrderNotExistException.class, OrderAlreadyDeliverException.class,
      OrderAlreadyCheckoutException.class, CommentAlreadyExistException.class})
  public String handleException(Exception e) {
    return e.getMessage();
  }
}
