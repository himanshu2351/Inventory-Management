package com.inventory.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.inventory.management.dto.requests.InventoryRestoreRequest;
import com.inventory.management.dto.requests.PlaceOrder;
import com.inventory.management.dto.requests.ReturnOrderRequest;
import com.inventory.management.dto.requests.UpdateOrderStatus;
import com.inventory.management.dto.responses.*;
import com.inventory.management.service.InventoryService;

@RestController()
public class InventoryController {

    @Autowired
    InventoryService inventoryService;


    @GetMapping("/view/all/products")
    public ListProducts viewAllProducts() {
        return inventoryService.viewAllProducts();
    }

    @PostMapping("/place/order")
    public OrderPlacedResponse orderPlace(@RequestBody PlaceOrder placeOrder) {

        return inventoryService.orderPlace(placeOrder);
    }

    @GetMapping("/check/order/status/{orderId}")
    public OrderStatusResponse getOrderStatus(@PathVariable long orderId) {

        return inventoryService.getOrderStatus(orderId);
    }

    @PostMapping("/update/order/status")
    public OrderStatusResponse updateOrderStatus(@RequestBody UpdateOrderStatus updateOrderStatus) {

        return inventoryService.updateOrderStatus(updateOrderStatus);
    }

    @PostMapping("/return/order")
    public ReturnOrderResponse returnOrder(@RequestBody ReturnOrderRequest returnOrderRequest) {

        return inventoryService.returnOrder(returnOrderRequest);
    }

    @PostMapping("/restocking/inventory")
    public InventoryRestoreResponse restoringInventory(@RequestBody InventoryRestoreRequest inventoryRestoreRequest) {

        return inventoryService.restoringInventory(inventoryRestoreRequest);
    }


}
