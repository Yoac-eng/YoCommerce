package com.yoaceng.yocommerce.services;

import com.yoaceng.yocommerce.dto.ProductDTO;
import com.yoaceng.yocommerce.entities.Product;
import com.yoaceng.yocommerce.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Product product = repository.findById(id).get();
        return convertToDto(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable){
        //Page is a stream in java
        Page<Product> result = repository.findAll(pageable);
        return result.map(x -> convertToDto(x));
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto){
        Product product = convertToEntity(dto);
        Product result = repository.save(product);
        return convertToDto(result);
    }

    private ProductDTO convertToDto(Product product){
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        return productDTO;
    }

    private Product convertToEntity(ProductDTO productDTO){
        Product product = modelMapper.map(productDTO, Product.class);
        return product;
    }
}
