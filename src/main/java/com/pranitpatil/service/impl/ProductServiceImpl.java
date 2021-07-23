package com.pranitpatil.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pranitpatil.config.WarehouseManagementProperties;
import com.pranitpatil.controller.ArticleController;
import com.pranitpatil.controller.ProductController;
import com.pranitpatil.dao.ArticleRepository;
import com.pranitpatil.dao.ProductRepository;
import com.pranitpatil.dto.AvailableProduct;
import com.pranitpatil.dto.PagedResponse;
import com.pranitpatil.dto.Product;
import com.pranitpatil.dto.Products;
import com.pranitpatil.entity.Article;
import com.pranitpatil.entity.ArticleQuantity;
import com.pranitpatil.exception.NotFoundException;
import com.pranitpatil.exception.ValidationException;
import com.pranitpatil.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ObjectMapper objectMapper;
    private ProductRepository productRepository;
    private ModelMapper modelMapper;
    private WarehouseManagementProperties warehouseManagementProperties;
    private ArticleRepository articleRepository;

    @Autowired
    public ProductServiceImpl(ObjectMapper objectMapper, ProductRepository productRepository,
                              ModelMapper modelMapper, WarehouseManagementProperties warehouseManagementProperties,
                              ArticleRepository articleRepository) {
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.warehouseManagementProperties = warehouseManagementProperties;
        this.articleRepository = articleRepository;
    }

    @PostConstruct
    void loadProducts() throws IOException{
        loadProductsFromFile(warehouseManagementProperties.getProduct().getPath());
    }

    @Override
    public Product getProductById(long id) {
        com.pranitpatil.entity.Product product = productRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Product with id - " + id + " is not found."));

        return getProductDto(product);
    }

    @Override
    public void loadProductsFromFile(String path) throws IOException {
        Products products = objectMapper.readValue(new File(path), Products.class);

        List<com.pranitpatil.entity.Product> productEntities = products.getProducts()
                .stream()
                .map(p -> getProductEntity(p))
                .collect(Collectors.toList());

        productRepository.saveAll(productEntities);
    }

    private com.pranitpatil.entity.Product getProductEntity(Product p){

        com.pranitpatil.entity.Product product =  modelMapper.map(p, com.pranitpatil.entity.Product.class);

        List<ArticleQuantity> articleQuantities = new ArrayList<>();

        for (com.pranitpatil.dto.ArticleQuantity aq: p.getArticleQuantities()){
            articleQuantities.add(new ArticleQuantity(product, aq.getArticleId(), aq.getQuantity()));
        }

        product.setArticles(articleQuantities);

        return product;
    }

    private Product getProductDto(com.pranitpatil.entity.Product product){

        Product productDto = modelMapper.map(product, Product.class);

        List<com.pranitpatil.dto.ArticleQuantity> articleQuantities = new ArrayList<>();
        for(ArticleQuantity articleQuantity : product.getArticles()){
            com.pranitpatil.dto.ArticleQuantity articleQuantityDto = modelMapper.map(
                    articleQuantity, com.pranitpatil.dto.ArticleQuantity.class);

            Link articleLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ArticleController.class)
                    .getArticleById(articleQuantity.getArticleid())).withRel("artcle");

            articleQuantityDto.add(articleLink);
            articleQuantities.add(articleQuantityDto);
        }

        productDto.setArticleQuantities(articleQuantities);

        return productDto;
    }

    @Override
    public PagedResponse<AvailableProduct> getAllAvailableProducts(Pageable pageable) {

        Page<com.pranitpatil.entity.Product> page = productRepository.findAll(pageable);
        List<AvailableProduct> availableProducts = new ArrayList<>();

        for(com.pranitpatil.entity.Product product : page.getContent()){
            AvailableProduct availableProduct = getAvailableProduct(product);

            Link productLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductController.class)
                    .getProduct(availableProduct.getId())).withRel("product");
            availableProduct.add(productLink);

            availableProducts.add(availableProduct);
        }

        PagedResponse<AvailableProduct> pagedResponse = new PagedResponse<>();
        pagedResponse.setEntity(availableProducts);
        pagedResponse.setPageNumber(page.getNumber());
        pagedResponse.setTotalItems(page.getTotalElements());
        pagedResponse.setTotalPages(page.getTotalPages());

        return pagedResponse;
    }

    private AvailableProduct getAvailableProduct(com.pranitpatil.entity.Product product) {
        int productQuantity = Integer.MAX_VALUE;
        for(ArticleQuantity articleQuantity : product.getArticles()){
            int totalArtQantity = getArticleStock(articleQuantity);

            int maxQuantity =  totalArtQantity / articleQuantity.getQuantity();
            if(maxQuantity < productQuantity){
                productQuantity = maxQuantity;
            }
        }

        return new AvailableProduct(product.getId(), product.getName(), product.getPrice(),
                productQuantity);
    }

    /**
     * Get available article stock.
     * If article is not found then returns ZERO
     * @param articleQuantity
     * @return
     */
    private int getArticleStock(ArticleQuantity articleQuantity) {
        return articleRepository.findById(articleQuantity.getArticleid()).orElse(new Article()).getStock();
    }

    @Override
    public Product saveProduct(Product product) {
        com.pranitpatil.entity.Product productEntity =  productRepository.save(getProductEntity(product));
        return getProductDto(productEntity);
    }

    @Override
    @Transactional
    public void sellProduct(long productId, int quantity) {
        com.pranitpatil.entity.Product product = productRepository.findById(productId).
                orElseThrow(() -> new NotFoundException("Product with id - " + productId + " is not found."));

        if(quantity > getAvailableProduct(product).getAvailableQuantity()){
            throw new ValidationException("Not enough products available");
        }

        for(ArticleQuantity articleQuantity : product.getArticles()){
            Article article = articleRepository.findById(articleQuantity.getArticleid())
                    .orElseThrow(() -> new NotFoundException("Article with id - " + articleQuantity.getArticleid() + " is not found."));

            article.setStock(article.getStock() - (articleQuantity.getQuantity() * quantity));
            articleRepository.save(article);
        }
    }
}
