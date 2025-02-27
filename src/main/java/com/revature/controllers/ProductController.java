package com.revature.controllers;

import com.revature.annotations.Authorized;
import com.revature.dtos.ProductInfo;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Authorized
    @GetMapping
    public ResponseEntity<List<Product>> getInventory() {
        return ResponseEntity.ok(productService.findAll());
    }

    @Authorized
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") int id) {
        Optional<Product> optional = productService.findById(id);

        if(!optional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optional.get());
    }

    @Authorized
    @PutMapping
    public ResponseEntity<Product> upsert(@RequestBody Product product, HttpSession session) {
        User u= (User) session.getAttribute("user");
        if(u.getRole().toString()=="Admin") {
            return ResponseEntity.ok(productService.save(product));
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @Authorized
    @PatchMapping
    public ResponseEntity<List<Product>> purchase(@RequestBody List<ProductInfo> metadata) { 	
    	List<Product> productList = new ArrayList<Product>();
    	
    	for (int i = 0; i < metadata.size(); i++) {
    		Optional<Product> optional = productService.findById(metadata.get(i).getId());

    		if(!optional.isPresent()) {
    			return ResponseEntity.notFound().build();
    		}

    		Product product = optional.get();

    		if(product.getQuantity() - metadata.get(i).getQuantity() < 0) {
    			return ResponseEntity.badRequest().build();
    		}
    		
    		product.setQuantity(product.getQuantity() - metadata.get(i).getQuantity());
    		productList.add(product);
    	}
        
        productService.saveAll(productList, metadata);

        return ResponseEntity.ok(productList);
    }

    @Authorized
    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") int id, HttpSession session) {
        Optional<Product> optional = productService.findById(id);
        User u= (User) session.getAttribute("user");
        if (u.getRole().toString()=="Admin") {
            if (!optional.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            productService.delete(id);

            return ResponseEntity.ok(optional.get());
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    //Show Featured Products.
    @Authorized
    @GetMapping("/featured")
    public ResponseEntity<List<Product>> getFeaturedProducts((@PathVariable("id") int id) {
        return ResponseEntity.ok(productService.getFeaturedProducts());
    }
    
    //Add A Featured Product.
    @Authorized
    @PutMapping("/add-featured/{id}")
    public ResponseEntity<Product> addFeaturedProduct((@PathVariable("id") int id) {
        if(ProductIsNotFeatured(id))
        {
            return ResponseEntity.ok(productService.addFeaturedProduct());
        }
        else if(!ProductIsNotFeatured(id))
        {
            return ResponseEntity.internalServerError();    
        }
    }
    
    //Remove a Featured Product.
    @Authorized
    @PutMapping("/remove-featured/{id}")
    public ResponseEntity<Product> removeFeaturedProduct((@PathVariable("id") int id) {
        
        if(ProductIsNotFeatured(id))
        {
            return ResponseEntity.internalServerError();   
        }
        else if(!ProductIsNotFeatured(id))
        {
            return ResponseEntity.ok(productService.removeFeaturedProduct())    
        }
    }
    
    

    @Authorized
    @GetMapping("/sale")
    public ResponseEntity<List<Product>> getProductsOnSale() {
        return ResponseEntity.ok(productService.getProductsOnSale());
    }
}
