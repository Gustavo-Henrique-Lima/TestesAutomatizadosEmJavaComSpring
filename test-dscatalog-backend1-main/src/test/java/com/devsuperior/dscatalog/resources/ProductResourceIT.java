package com.devsuperior.dscatalog.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.tests.ProductFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private Long existsId;
	private Long nonExistsId;
	private Long countTotalProducts;
	private ProductDTO entityDto;

	@BeforeEach
	void setUp() throws Exception {
		existsId = 1L;
		nonExistsId = 777L;
		countTotalProducts = 25L;
		entityDto = ProductFactory.createProductDTO();
	}

	@Test
	public void findAllShouldReturnSortedPageWhenSortByName() throws Exception {
		ResultActions results = mockMvc
				.perform(get("/products?page=0&size=12&sort=name,asc").accept(MediaType.APPLICATION_JSON));
		results.andExpect(status().isOk());
		results.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
		results.andExpect(jsonPath("$.content").exists());
		results.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		results.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
		results.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
	}

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(entityDto);
		String expectedName = entityDto.getName();
		String expectedDescription = entityDto.getDescription();
		ResultActions results = mockMvc.perform(put("/products/{id}", existsId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		results.andExpect(status().isOk());
		results.andExpect(jsonPath("$.id").exists());
		results.andExpect(jsonPath("$.name").value(expectedName));
		results.andExpect(jsonPath("$.description").value(expectedDescription));
	}

	@Test
	public void updateShouldNotFoundWhenIdDoesNotExists() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(entityDto);
		ResultActions results = mockMvc.perform(put("/products/{id}", nonExistsId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		results.andExpect(status().isNotFound());
	}
}