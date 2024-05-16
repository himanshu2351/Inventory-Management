package com.inventory.management.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.management.dto.enums.OrderStatus;
import com.inventory.management.dto.enums.ProductStatus;
import com.inventory.management.dto.requests.InventoryRestoreRequest;
import com.inventory.management.dto.requests.PlaceOrder;
import com.inventory.management.dto.requests.ReturnOrderRequest;
import com.inventory.management.dto.requests.UpdateOrderStatus;
import com.inventory.management.dto.responses.*;
import com.inventory.management.model.Customer;
import com.inventory.management.model.Order1;
import com.inventory.management.model.Product;
import com.inventory.management.repository.CustomerRepository;
import com.inventory.management.repository.OrderRepository;
import com.inventory.management.repository.ProductRepository;
import com.inventory.management.service.InventoryService;

import static com.inventory.management.dto.enums.ErrorCodeErrorMsg.*;
import static com.inventory.management.utils.Constants.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public ListProducts viewAllProducts() {

        ListProducts listProducts = new ListProducts();
        try {
            List<Product> products = productRepository.findAll();
            listProducts.setProducts(products);
            listProducts.setStatus(SUCCESS);
//            return listProducts;

        } catch (Exception ex) {
            listProducts.setStatus(FAILED);
            listProducts.setErrCode(APPLICATION_ERROR.getErrCode());
            listProducts.setErrMsg(APPLICATION_ERROR.getErrMsg());
        }
        return listProducts;
    }

    @Override
    @Transactional
    public OrderPlacedResponse orderPlace(PlaceOrder placeOrder) {
        OrderPlacedResponse orderPlacedResponse = new OrderPlacedResponse();
        try {
            Optional<Customer> customer = customerRepository.findById(placeOrder.getCustId());

            
            if (!customer.isPresent()) {
                orderPlacedResponse.setErrCode(INVALID_CUSTOMER.getErrCode());
                orderPlacedResponse.setErrMsg(INVALID_CUSTOMER.getErrMsg());
                orderPlacedResponse.setMessage(ORDER_PLACED_FAILED);
                return orderPlacedResponse;
            }

            Optional<Product> productOptional = productRepository.findById(placeOrder.getProductId());

            if (!productOptional.isPresent()) {
                orderPlacedResponse.setErrCode(INVALID_PRODUCT.getErrCode());
                orderPlacedResponse.setErrMsg(INVALID_PRODUCT.getErrMsg());
                orderPlacedResponse.setMessage(ORDER_PLACED_FAILED);
                return orderPlacedResponse;
            }

            Product product = productOptional.get();

            if (product.getQuantity() < placeOrder.getProductQuantity()) {

                orderPlacedResponse.setErrCode(INVALID_PRODUCT_QUANTITY.getErrCode());
                orderPlacedResponse.setErrMsg(INVALID_PRODUCT_QUANTITY.getErrMsg());
                orderPlacedResponse.setMessage(ORDER_PLACED_FAILED);
                return orderPlacedResponse;
            }

            Order1 order = createOrder(placeOrder, customer, product);

            float newQuantity = product.getQuantity() - placeOrder.getProductQuantity();
            ProductStatus productStatus = ProductStatus.valueOf(product.getStatus());

            if (newQuantity == 0) {
                productStatus = ProductStatus.OUT_OF_STOCK;
            }

            productRepository.updateProductQuantity(newQuantity, product.getId(), product.getVersion(), product.getVersion() + 1, productStatus.toString());
            order = orderRepository.save(order);
            orderPlacedResponse.setOrderID(order.getOrder_id());
            orderPlacedResponse.setMessage(ORDER_PLACED_SUCCESS);

            return orderPlacedResponse;

        } catch (Exception ex) {
            orderPlacedResponse.setErrCode(APPLICATION_ERROR.getErrCode());
            orderPlacedResponse.setErrMsg(APPLICATION_ERROR.getErrMsg());
            orderPlacedResponse.setMessage(ORDER_PLACED_FAILED);
            return orderPlacedResponse;
        }
    }

    @Override
    public OrderStatusResponse getOrderStatus(long orderId) {

        OrderStatusResponse orderStatusResponse = new OrderStatusResponse();
        try {
            Optional<Order1> optionalOrder = orderRepository.findById(orderId);

            if (!optionalOrder.isPresent()) {
                orderStatusResponse.setErrCode(INVALID_ORDER_ID.getErrCode());
                orderStatusResponse.setErrMsg(INVALID_ORDER_ID.getErrMsg());
                return orderStatusResponse;
            }
            orderStatusResponse.setOrderStatus(OrderStatus.valueOf(optionalOrder.get().getStatus()));
            orderStatusResponse.setOrderId(String.valueOf(optionalOrder.get().getOrder_id()));
            return orderStatusResponse;
        } catch (Exception ex) {
            orderStatusResponse.setErrCode(APPLICATION_ERROR.getErrCode());
            orderStatusResponse.setErrMsg(APPLICATION_ERROR.getErrMsg());
            return orderStatusResponse;
        }
    }

    @Override
    public OrderStatusResponse updateOrderStatus(UpdateOrderStatus updateOrderStatus) {

        OrderStatusResponse orderStatusResponse = new OrderStatusResponse();
        try {
            Optional<Order1> optionalOrder = orderRepository.findById(updateOrderStatus.getOrderId());

            if (!optionalOrder.isPresent()) {
                orderStatusResponse.setErrCode(INVALID_ORDER_ID.getErrCode());
                orderStatusResponse.setErrMsg(INVALID_ORDER_ID.getErrMsg());
                return orderStatusResponse;
            }
            Order1 order = optionalOrder.get();
            order.setStatus(updateOrderStatus.getOrderStatus().toString());
            order = orderRepository.save(order);
            orderStatusResponse.setOrderStatus(OrderStatus.valueOf(order.getStatus()));
            orderStatusResponse.setOrderId(String.valueOf(order.getOrder_id()));
            return orderStatusResponse;
        } catch (Exception ex) {
            orderStatusResponse.setErrCode(APPLICATION_ERROR.getErrCode());
            orderStatusResponse.setErrMsg(APPLICATION_ERROR.getErrMsg());
            return orderStatusResponse;
        }
    }

    @Override
    @Transactional
    public ReturnOrderResponse returnOrder(ReturnOrderRequest returnOrderRequest) {

        ReturnOrderResponse returnOrderResponse = new ReturnOrderResponse();
        try {

            Optional<Order1> optionalOrder = orderRepository.findById(returnOrderRequest.getOrderId());

            if (!optionalOrder.isPresent()) {
                returnOrderResponse.setErrCode(INVALID_ORDER_ID.getErrCode());
                returnOrderResponse.setErrMsg(INVALID_ORDER_ID.getErrMsg());
                return returnOrderResponse;
            }
            Order1 order = optionalOrder.get();

            Optional<Product> productOptional = productRepository.findById(order.getProductId());

            if (!productOptional.isPresent()) {
                returnOrderResponse.setErrCode(INVALID_PRODUCT.getErrCode());
                returnOrderResponse.setErrMsg(INVALID_PRODUCT.getErrMsg());
                return returnOrderResponse;
            }

            Product product = productOptional.get();

            float newQuantity = product.getQuantity() + order.getQuantity();


            productRepository.updateProductQuantity(newQuantity, product.getId(), product.getVersion(), product.getVersion() + 1, ProductStatus.AVAILABLE.toString());
            order.setStatus(OrderStatus.ORDER_RETURN.toString());
            orderRepository.save(order);
            returnOrderResponse.setMessage(ORDER_RETURN_SUCCESS);
            return returnOrderResponse;
        } catch (Exception ex) {
            returnOrderResponse.setErrCode(APPLICATION_ERROR.getErrCode());
            returnOrderResponse.setErrMsg(APPLICATION_ERROR.getErrMsg());
            returnOrderResponse.setMessage(ORDER_RETURN_FAILED);
            return returnOrderResponse;
        }
    }

    @Override
    public InventoryRestoreResponse restoringInventory(InventoryRestoreRequest inventoryRestoreRequest) {
        InventoryRestoreResponse inventoryRestoreResponse = new InventoryRestoreResponse();
        try {
            for (Product product : inventoryRestoreRequest.getProducts()) {

                if (product.getQuantity() > 0) {
                    if (product.getId() == 0) {
                        product.setVersion(0);
                        product.setStatus(ProductStatus.AVAILABLE.toString());
                        productRepository.save(product);
                    } else {
                        updateInventoryProduct(product);
                    }
                }
            }

            inventoryRestoreResponse.setMessage(SUCCESS);
            return inventoryRestoreResponse;
        } catch (Exception ex) {
            inventoryRestoreResponse.setMessage(FAILED);
            return inventoryRestoreResponse;
        }

    }

    @Transactional
    private void updateInventoryProduct(Product newProduct) {

        try {
            Optional<Product> productOptional = productRepository.findById(newProduct.getId());

            if (!productOptional.isPresent()) {
                throw new Exception();
            }

            Product product = productOptional.get();
            float newQuantity = product.getQuantity() + newProduct.getQuantity();
            productRepository.updateProductQuantity(newQuantity, product.getId(), product.getVersion(), product.getVersion() + 1, ProductStatus.AVAILABLE.toString());

        } catch (Exception ex) {

        }
    }

    private Order1 createOrder(PlaceOrder placeOrder, Optional<Customer> customer, Product product) {
        Order1 order = new Order1();
        order.setCustomerId(customer.get().getCust_id());
        order.setDate(new Date(System.currentTimeMillis()));
        order.setPrice(String.valueOf(product.getPrice() * placeOrder.getProductQuantity()));
        order.setStatus(OrderStatus.ORDER_PLACED.toString());
        order.setQuantity(placeOrder.getProductQuantity());
        order.setProductId(product.getId());
        return order;
    }
}
