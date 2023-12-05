package com.yoaceng.yocommerce.services;

import com.yoaceng.yocommerce.dto.ProductDTO;
import com.yoaceng.yocommerce.entities.Product;
import com.yoaceng.yocommerce.repositories.ProductRepository;
import com.yoaceng.yocommerce.services.exceptions.DatabaseException;
import com.yoaceng.yocommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Product product = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found by the given id."));
        return convertToDto(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable){
        //Page is a stream in java
        Page<Product> result = repository.findAll(pageable);
        return result.map(this::convertToDto);
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto){
        Product product = convertToEntity(dto);
        Product result = repository.save(product);
        return convertToDto(result);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto){
        try{
            Product productById = repository.getReferenceById(id);
            copyDtoToEntity(dto, productById);
            Product result = repository.save(productById);
            return convertToDto(result);
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity){
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
    }

    private ProductDTO convertToDto(Product product){
        return modelMapper.map(product, ProductDTO.class);
    }

    private Product convertToEntity(ProductDTO productDTO){
        return modelMapper.map(productDTO, Product.class);
    }

}
