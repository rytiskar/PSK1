package lt.vu.services;

import lt.vu.entities.Product;
import lt.vu.persistence.ProductsDAO;
import lt.vu.rest.contracts.ProductDto;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class ProductService {

    @Inject
    private ProductsDAO productsDAO;

    public ProductDto getProductById(Long id) {

        Product product = productsDAO.findOne(id);

        if (product == null) {
            return null;
        }

        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getProductName());
        productDto.setPrice(product.getPrice());
        productDto.setVersion(product.getVersion());

        return productDto;
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productsDAO.findAll();

        if (products == null || products.isEmpty()) {
            return Collections.emptyList();
        }


        return mapProductsToDtos(products);
    }

    @Transactional
    public void createProduct(ProductDto productData) {
        Product newProduct = new Product();
        newProduct.setProductName(productData.getName());
        newProduct.setPrice(productData.getPrice());

        productsDAO.persist(newProduct);

        productData.setId(newProduct.getId());
    }

    @Transactional
    public ProductDto updateProduct(ProductDto productData) {

        Product detached = new Product();

        detached.setId(productData.getId());
        detached.setProductName(productData.getName());
        detached.setPrice(productData.getPrice());
        detached.setVersion(productData.getVersion());

        Product updated = productsDAO.merge(detached);

        ProductDto updatedProductDto = new ProductDto();
        updatedProductDto.setId(updated.getId());
        updatedProductDto.setName(updated.getProductName());
        updatedProductDto.setPrice(updated.getPrice());
        updatedProductDto.setVersion(updated.getVersion());

        return updatedProductDto;
    }

    @Transactional
    public void deleteProduct(Long id) {

        Product existinProduct = productsDAO.findOne(id);

        if (existinProduct == null) {
            return;
        }

        productsDAO.remove(existinProduct);
    }

    private List<ProductDto> mapProductsToDtos(List<Product> products) {
        return products.stream()
                .map(product -> {
                    ProductDto dto = new ProductDto();
                    dto.setId(product.getId());
                    dto.setName(product.getProductName());
                    dto.setPrice(product.getPrice());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
