package com.devsuperior.dscatalog.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import com.devsuperior.dscatalog.tests.ProductFactory;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;
	private PageImpl<ProductDTO> page;
	private ProductDTO entityDto;

	@MockBean
	private ProductService service;

	@BeforeEach
	void setUp() throws Exception {
		entityDto = ProductFactory.createProductDTO();
		page = new PageImpl<>(List.of(entityDto));
		Mockito.when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);
	}

	@Test
	public void findAllShouldReturnPage() throws Exception {
		ResultActions results = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));
		results.andExpect(status().isOk());
	}
}