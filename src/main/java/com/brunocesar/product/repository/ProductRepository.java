package com.brunocesar.product.repository;

import com.brunocesar.product.model.Category;
import com.brunocesar.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    @RestResource(path = "names", rel = "names")
    Page<Product> findByNameContains(@Param("name") String name, Pageable pageable);

    @RestResource(path = "categories", rel = "categories")
    Page<Product> findByCategory(@Param("category") Category category, Pageable pageable);

}
