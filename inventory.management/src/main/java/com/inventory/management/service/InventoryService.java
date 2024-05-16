package com.inventory.management.service;

import com.inventory.management.dto.requests.InventoryRestoreRequest;
import com.inventory.management.dto.requests.PlaceOrder;
import com.inventory.management.dto.requests.ReturnOrderRequest;
import com.inventory.management.dto.requests.UpdateOrderStatus;
import com.inventory.management.dto.responses.*;

public interface InventoryService {
    ListProducts viewAllProducts();

    OrderPlacedResponse orderPlace(PlaceOrder placeOrder);

    OrderStatusResponse getOrderStatus(long orderId);

    OrderStatusResponse updateOrderStatus(UpdateOrderStatus updateOrderStatus);

    ReturnOrderResponse returnOrder(ReturnOrderRequest returnOrderRequest);

    InventoryRestoreResponse restoringInventory(InventoryRestoreRequest inventoryRestoreRequest);
}
