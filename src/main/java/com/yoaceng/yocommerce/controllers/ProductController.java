package com.yoaceng.yocommerce.controllers;

import com.yoaceng.yocommerce.dto.ProductDTO;
import com.yoaceng.yocommerce.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Default controller to handle Product related requests.
 *
 * @author Cayo Cutrim
 * @since 28/11/2023
 */
@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductService service;

    /**
     * Recupera uma lista paginada de produtos.
     *
     * @param pageable Objeto {@link Pageable} que contém as informações de paginação e ordenação.
     *                 Este parâmetro é automaticamente configurado com base nos parâmetros de requisição,
     *                 como 'page', 'size' e 'sort'.
     *
     * @return Uma {@link ResponseEntity} contendo uma página de {@link ProductDTO}.
     *         Retorna um objeto {@link Page} com os produtos encontrados, juntamente com as informações
     *         de paginação e status HTTP OK (200).
     */
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable){
        Page<ProductDTO> dto = service.findAll(pageable);
        return ResponseEntity.ok(dto);
    }

    /**
     * Busca um recurso pelo seu ID.
     *
     * @param id O identificador único do produto a ser buscado. Este valor é extraído da URL.
     *
     * @return Uma {@link ResponseEntity} contendo {@link ProductDTO} com os detalhes do produto
     *         e o status HTTP OK (200) se o produto for encontrado.
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
        ProductDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Insere um novo recurso no sistema.
     * Este método recebe um objeto ProductDTO, valida-o e o insere no sistema.
     * Uma URI representando o local do produto recém-criado é gerada e retornada na resposta.
     *
     * @param newProductDTO {@link ProductDTO} O objeto DTO de produto a ser inserido. A anotação {@code @Valid}
     * indica que o objeto deve ser validado antes da inserção.
     *
     * @return Uma {@link ResponseEntity} contendo o {@link ProductDTO} do produto inserido e a URI do recurso criado.
     */
    @PostMapping
    public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO newProductDTO){
        newProductDTO = service.insert(newProductDTO);
        //URI da requisição atual para ser retornado com status code 201
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newProductDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(newProductDTO);
    }

    /**
     * Atualiza um recurso existente.
     * Este método recebe um ID e um DTO de produto. Ele chama o serviço de atualização
     * para atualizar o produto correspondente ao ID fornecido com as informações
     * no DTO. Após a atualização, retorna um {@link ResponseEntity} contendo o DTO atualizado.
     *
     * @param id O ID do Recurso a ser atualizado. Deve ser um {@link Long} válido.
     * @param dto O {@link ProductDTO} contendo as informações atualizadas do produto.
     *            Deve ser validado antes de ser passado para este método.
     * @return Um {@link ResponseEntity<ProductDTO>} contendo o produto atualizado.
     * @throws IllegalArgumentException se o ID ou DTO fornecido não for válido ou se
     *                                  o produto com o ID especificado não existir.
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id,@Valid @RequestBody ProductDTO dto){
        dto = service.update(id, dto);
        return ResponseEntity.ok(dto);
    }

    /**
     * Exclui um recurso especificado pelo ID.
     * Este método utiliza um serviço para remover o recurso identificado pelo ID fornecido.
     * Após a exclusão bem-sucedida, retorna uma resposta HTTP sem conteúdo (204 No Content).
     *
     * @param id o identificador único do recurso a ser excluído.
     * @return Um {@link ResponseEntity<Void>} contendo uma resposta vazia indicando que o recurso foi excluído com sucesso.
     */
    @DeleteMapping (value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long idx){
        service.delete(idx);
        return ResponseEntity.noContent().build();
    }
}
