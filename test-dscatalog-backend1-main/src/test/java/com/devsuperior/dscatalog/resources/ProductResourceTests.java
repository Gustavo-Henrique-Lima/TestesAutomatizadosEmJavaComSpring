package com.devsuperior.dscatalog.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.ProductFactory;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;
	private PageImpl<ProductDTO> page;
	private ProductDTO entityDto;
	private Long existsId;
	private Long nonExistsId;

	@MockBean
	private ProductService service;

	@BeforeEach
	void setUp() throws Exception {
		entityDto = ProductFactory.createProductDTO();
		page = new PageImpl<>(List.of(entityDto));
		existsId = 1L;
		nonExistsId = 999L;
		Mockito.when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(service.findById(existsId)).thenReturn(entityDto);
		Mockito.when(service.findById(nonExistsId)).thenThrow(ResourceNotFoundException.class);
	}

	@Test
	public void findAllShouldReturnPage() throws Exception {
		ResultActions results = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));
		results.andExpect(status().isOk());
	}

	@Test
	public void findyByIdShouldReturnProductDTOWhenIdExists() throws Exception {
		ResultActions results = mockMvc.perform(get("/products/{id}", existsId).accept(MediaType.APPLICATION_JSON));
		results.andExpect(status().isOk());
		results.andExpect(jsonPath("$.id").exists());
		results.andExpect(jsonPath("$.name").exists());
	}

	@Test
	public void findyByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		ResultActions results = mockMvc.perform(get("/products/{id}", nonExistsId).accept(MediaType.APPLICATION_JSON));
		results.andExpect(status().isNotFound());
	}
}