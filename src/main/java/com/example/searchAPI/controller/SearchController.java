package com.example.searchAPI.controller;

import com.example.searchAPI.dto.ProductDTO;
import com.example.searchAPI.entity.Product;
import com.example.searchAPI.services.SearchService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    SearchService searchService;

//  For getting all the products stored in database
    @RequestMapping(method = RequestMethod.GET, value = "/getAll")
    public ResponseEntity<?> getAll(){
        List<Product> products = searchService.findAll();
        ArrayList<ProductDTO> searchDTOList= new ArrayList<>();
        for (Product prod: products) {
            ProductDTO productDTO = new ProductDTO();
            BeanUtils.copyProperties(prod, productDTO);
            searchDTOList.add(productDTO);
        }
        return new ResponseEntity<>(searchDTOList, HttpStatus.OK);
    }

//  For getting all the products dyanamically depends on user input in search
    @RequestMapping(method = RequestMethod.GET, value = "/get/{pName}")
    public ResponseEntity<?> get(@PathVariable("pName") String pName){

        List<Product> products = searchService.findLike(pName);
        ArrayList<ProductDTO> productDTOList = new ArrayList<>();
        for (Product prod: products) {
            ProductDTO productDTO = new ProductDTO();
            BeanUtils.copyProperties(prod, productDTO);
            productDTOList.add(productDTO);
        }
        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }

//  For getting all the product depends on product name
    @RequestMapping(method = RequestMethod.GET, value = "/getByPname/{pName}")
    public ResponseEntity<?> getByPname(@PathVariable("pName") String pName){

        Product product = searchService.findByPName(pName);
        ProductDTO productDTO = new ProductDTO();
            if(product == null){
                return new ResponseEntity<String>("", HttpStatus.OK);
            }
            BeanUtils.copyProperties(product, productDTO);
            return new ResponseEntity<ProductDTO>(productDTO, HttpStatus.OK);
    }
//  For getting all the products depends on product category
    @RequestMapping(method = RequestMethod.GET, value = "/getByCategory/{category}")
    public ResponseEntity<?> getByCategory(@PathVariable("category") String category){
        Product product = searchService.findByPCategory(category);
        ProductDTO productDTO = new ProductDTO();
        if(product == null){
            return new ResponseEntity<String>("", HttpStatus.OK);
        }
        BeanUtils.copyProperties(product, productDTO);
        return new ResponseEntity<ProductDTO>(productDTO, HttpStatus.OK);
    }

//  For getting all the products depends on product brand
    @RequestMapping(method = RequestMethod.GET, value = "/getByBrand/{brand}")
    public ResponseEntity<?> getByBrand(@PathVariable("brand") String brand){
        Product product = searchService.findByPBrand(brand);
        ProductDTO productDTO = new ProductDTO();
        if(product == null){
            return new ResponseEntity<String>("", HttpStatus.OK);
        }
        BeanUtils.copyProperties(product, productDTO);
        return new ResponseEntity<ProductDTO>(productDTO, HttpStatus.OK);
    }

//  For adding a product int database by catalogue API
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public ResponseEntity<String> addOrUpdate(@RequestBody ProductDTO productDTO){
        System.out.println(productDTO);
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        Product productCreated = searchService.save(product);
        return new ResponseEntity<String>(productCreated.getpName(),HttpStatus.CREATED);
    }

//  For getting a product depends on productId for fetching details in front end
    @RequestMapping(method = RequestMethod.GET, value = "/getOne/{productId}")
    public ResponseEntity<?> getOne(@PathVariable("productId") String productId){
        Product product = searchService.findOne(productId);
        ProductDTO productDTO = new ProductDTO();
        if(product == null){
            return new ResponseEntity<String>("", HttpStatus.OK);
        }
        BeanUtils.copyProperties(product, productDTO);
        return new ResponseEntity<ProductDTO>(productDTO, HttpStatus.OK);
    }

//  For getting a list of products depends on list of productIds
    @RequestMapping(method = RequestMethod.POST, value = "/getList")
    public ResponseEntity<?> getProducts(@RequestBody List<String> listOfPid){
        List<Product> products = searchService.findByPId(listOfPid);
        System.out.println(listOfPid);
        ArrayList<ProductDTO> productDTOList = new ArrayList<>();
        for (Product ser: products) {
            ProductDTO productDTO = new ProductDTO();
            BeanUtils.copyProperties(ser, productDTO);
            productDTOList.add(productDTO);
        }
        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }

//  For getting all the latest items added in product database with limit of 20 to show on home page
    @RequestMapping(value = "/latest", method = RequestMethod.GET)
    public ResponseEntity<List<Product>> getRecent(@RequestParam(value = "size", required = false, defaultValue = "20") Integer size){
        if (size > 20)
            size = 20;
        List <Product> productList = searchService.getRecent(size);
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

}
