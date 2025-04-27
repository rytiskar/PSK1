package lt.vu.services;

import lt.vu.mybatis.model.Product;
import lt.vu.persistence.MyBatisProductsDAO;
import lt.vu.rest.contracts.ProductDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MyBatisProductService {

    @Inject
    private MyBatisProductsDAO myBatisProductsDAO;

    public List<ProductDto> getAllProducts()
    {
        List<Product> products = myBatisProductsDAO.findAll();

        if (products == null || products.isEmpty()) {
            throw new NotFoundException("No products found.");
        }

        return products.stream()
                .map(product -> {
                    ProductDto dto = new ProductDto();
                    dto.setId(product.getId());
                    dto.setName(product.getProductname());
                    dto.setPrice(product.getPrice());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void createProduct(ProductDto productData) {

        Product newProduct = new Product();

        newProduct.setProductname(productData.getName());
        newProduct.setPrice(productData.getPrice());

        myBatisProductsDAO.persist(newProduct);
    }
}
