package lt.vu.services;

import lt.vu.entities.Product;
import lt.vu.persistence.ProductsDAO;
import lt.vu.rest.contracts.ProductDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
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

        return productDto;
    }

}
