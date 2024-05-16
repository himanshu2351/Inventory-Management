package com.inventory.management.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.inventory.management.model.Product;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRestoreRequest {

    private List<Product> products;

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
    
}
