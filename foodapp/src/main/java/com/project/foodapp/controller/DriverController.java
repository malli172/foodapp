package com.project.foodapp.controller;
import com.project.foodapp.model.Customer;
import com.project.foodapp.model.Driver;
import com.project.foodapp.model.Order;
import com.project.foodapp.service.DriverServiceImpl;
import com.project.foodapp.service.OrderServiceImpl;
import com.project.foodapp.exception.OrderAlreadyDeliverException;
import com.project.foodapp.exception.OrderAlreadyFinishException;
import com.project.foodapp.exception.OrderNotExistException;
import com.project.foodapp.exception.OrderNotFinishedException;
import com.project.foodapp.exception.PasswordNotMatchException;
import com.project.foodapp.exception.UserAlreadyExistException;
import com.project.foodapp.exception.UserNotExistException;

import java.util.List;
import java.util.Optional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/driver")
public class DriverController {

  private final DriverServiceImpl driverService;
  private final OrderServiceImpl orderService;

  @Autowired
  public DriverController(DriverServiceImpl driverService, OrderServiceImpl orderService) {
    this.driverService = driverService;
    this.orderService = orderService;
  }

  @GetMapping(path = "{id}")
  public Driver getDriverById(@PathVariable("id") String id)
      throws UserNotExistException {
    return driverService.getUser(id)
        .orElseThrow(() -> new UserNotExistException("User doesn't exist"));
  }

  @PostMapping(path = "/login")
  public Driver loginDriver(@RequestBody String jsonUser)
      throws UserNotExistException, PasswordNotMatchException {

    JSONObject user = new JSONObject(jsonUser);
    String userName = user.getString("userName");
    String password = user.getString("password");
    Optional<Driver> driver = driverService.getUserByName(userName);
    if (driver.isEmpty()) {
      throw new UserNotExistException("User doesn't exist");
    }
    if (!driverService.passwordMatch(driver.get().getId()+"", password)) {
      throw new PasswordNotMatchException("Password doesn't match");
    }
    return driver.get();
  }

  @PostMapping(path = "/register")
  public Driver registerDriver(@RequestBody String jsonUser)
      throws UserAlreadyExistException {

    JSONObject user = new JSONObject(jsonUser);
    String userName = user.getString("userName");
    String password = user.getString("password");
    String phoneNumber = user.getString("phoneNumber");
    String address = user.getString("address");
    String city = user.getString("city");
    String state = user.getString("state");
    String zip = user.getString("zip");
    Driver driver = driverService
        .addUser(userName, password, phoneNumber, address, city, state, zip);
    if (driver == null) {
      throw new UserAlreadyExistException("User already exists, please login");
    }
    return driver;
  }

  @PostMapping(path = "/logout")
  public int logoutDriver() {
    System.out.println("logout the user");
    return 1;
  }

  @GetMapping(path = "/pendingOrders/{id}")
  public List<Order> getPendingOrders(@PathVariable("id") String id)
      throws UserNotExistException {
    if (driverService.getUser(id).isEmpty()) {
      throw new UserNotExistException("User doesn't exist");
    }
    if (orderService.driverGetActiveOrder(id) != null) {
      System.out.println("Driver already has an active order");
      return null;
    }
    return orderService.getAllPendingOrders();
  }

  @GetMapping(path = "/myActiveOrder/{id}")
  public Order getActiveOrder(@PathVariable("id") String id)
      throws UserNotExistException {
    if (driverService.getUser(id).isEmpty()) {
      throw new UserNotExistException("User doesn't exist");
    }
    return orderService.driverGetActiveOrder(id);
  }

  @GetMapping(path = "/myOrderHistory/{id}")
  public List<Order> getOrderHistory(@PathVariable("id") String id)
      throws UserNotExistException {
    if (driverService.getUser(id).isEmpty()) {
      throw new UserNotExistException("User doesn't exist");
    }
    return orderService.driverFindPastOrders(id);
  }

  @PostMapping(path = "/accept")
  public int acceptOrder(@RequestBody String jsonOrder)
      throws UserNotExistException, OrderNotExistException, OrderAlreadyDeliverException {
    JSONObject order = new JSONObject(jsonOrder);
    System.out.println(order);
    long Id = order.getLong("orderId");
    String orderId = String.valueOf(Id);
    String driverId = order.getString("driverId");
    if (driverService.getUser(driverId).isEmpty()) {
      throw new UserNotExistException("User doesn't exist");
    }
    int res = orderService.acceptOrder(orderId, driverId);
    if (res == -1) {
      throw new OrderNotExistException("Order doesn't exist");
    }
    if (res == 0) {
      throw new OrderAlreadyDeliverException("Order in cart or already in delivery");
    }
    return res;
  }

  @PostMapping(path = "/finish")
  public int finishOrder(@RequestBody String jsonOrder)
      throws OrderNotExistException, OrderAlreadyFinishException {
    JSONObject order = new JSONObject(jsonOrder);
    long Id = order.getLong("orderId");
    String id =String.valueOf(Id);
    int res = orderService.finishOrder(id);
    if (res == -1) {
      throw new OrderNotExistException("Order doesn't exist");
    }
    if (res == 0) {
      throw new OrderAlreadyFinishException("Order already finished");
    }
    return res;
  }

  @DeleteMapping(path = "{id}")
  public int deleterDriver(@PathVariable("id") String id)
      throws UserNotExistException, OrderNotFinishedException {
    if (orderService.driverGetActiveOrder(id) != null) {
      throw new OrderNotFinishedException("You still have active orders, please finish them first");
    }
    int res = driverService.deleteUser(id);
    if (res == -1) {
      throw new UserNotExistException("User doesn't exist");
    }
    return res;
  }

  @PostMapping(path = "/resetPassword")
  public int resetPassword(@RequestBody String jsonPassword)
      throws UserNotExistException, PasswordNotMatchException {
    JSONObject object = new JSONObject(jsonPassword);
    int cid=object.getInt("id");
    String id = cid+"";
    String oldPassword = object.getString("oldPassword");
    String newPassword = object.getString("newPassword");
    int res = driverService.updatePassword(id, oldPassword, newPassword);
    if (res == -1) {
      throw new UserNotExistException("User doesn't exist");
    }
    if (res == 0) {
      throw new PasswordNotMatchException("Password doesn't match");
    }
    return res;
  }

  @PostMapping(path = "/resetPhone")
  public int resetPhoneNumber(@RequestBody String jsonPhone)
      throws UserNotExistException {
    JSONObject object = new JSONObject(jsonPhone);
    int cid=object.getInt("id");
    String id = cid+"";
    String phoneNumber = object.getString("phoneNumber");
    int res = driverService.updatePhoneNumber(id, phoneNumber);
    if (res == -1) {
      throw new UserNotExistException("User doesn't exist");
    }
    return res;
  }

    @GetMapping("/count")
  public int getTotalNumberOfDrivers() {
    return driverService.getTotalNumberOfDrivers();
  }

  @GetMapping("/all")
  public List<Driver> getAllDrivers() {
    return driverService.getAllUsers();
  }

  @PostMapping(path = "/resetAddress")
  public int resetAddress(@RequestBody String jsonAddress)
      throws UserNotExistException {
    JSONObject object = new JSONObject(jsonAddress);
    int cid=object.getInt("id");
    String id = cid+"";
    String address = object.getString("address");
    String city = object.getString("city");
    String state = object.getString("state");
    String zip = object.getString("zip");
    int res = driverService.updateAddress(id, address, city, state, zip);
    if (res == -1) {
      throw new UserNotExistException("User doesn't exist");
    }
    return res;
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler({UserNotExistException.class, PasswordNotMatchException.class,
      UserAlreadyExistException.class, OrderNotExistException.class,
      OrderAlreadyDeliverException.class, OrderAlreadyFinishException.class,
      OrderNotFinishedException.class})
  public String handleException(Exception e) {
    return e.getMessage();
  }
}
