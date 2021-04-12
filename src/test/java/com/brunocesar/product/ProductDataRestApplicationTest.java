package com.brunocesar.product;

import com.brunocesar.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductDataRestApplicationTest {

    private static final String API_URL = "/api";
    private static final String PRODUCT_URL = API_URL + "/products";

    private static final String CREATE_PRODUCT_PAYLOAD = "{\"name\": \"Desmodur\", \"price\": 10.23, \"currency\": \"EUR\", \"category\": \"PCS\"}";
    private static final String CREATE_PRODUCT_WITH_DESCRIPTION_PAYLOAD = "{\"name\": \"Desmodur\", \"description\": \"Desmodur Description\", \"price\": 35.67, \"currency\": \"EUR\", \"category\": \"PCS\"}";
    private static final String UPDATE_PRODUCT_PAYLOAD = "{\"name\": \"MX50\", \"price\": 5.55, \"currency\": \"USD\", \"category\": \"CAS\"}";
    private static final String UPDATE_PRODUCT_WITH_DESCRIPTION_PAYLOAD = "{\"name\": \"MX50\", \"description\": \"MX50 Description\", \"price\": 5.55, \"currency\": \"USD\", \"category\": \"CAS\"}";

    @Autowired
    private MockMvc mockMVC;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    public void deleteAllBeforeTests() {
        repository.deleteAll();
    }

    @Test
    public void shouldReturnRepositoryIndex() throws Exception {
        mockMVC
            .perform(get(API_URL))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._links.products").exists())
            .andExpect(jsonPath("$._links.profile").exists());
    }

    @Test
    public void shouldCreateProduct() throws Exception {
        mockMVC
            .perform(post(PRODUCT_URL).content(CREATE_PRODUCT_PAYLOAD))
            .andExpect(status().isCreated())
            .andExpect(header().string(HttpHeaders.LOCATION, containsString("products/")));
    }

    @Test
    public void shouldRetrieveProduct() throws Exception {
        var mvcResult = mockMVC
            .perform(post(PRODUCT_URL).content(CREATE_PRODUCT_PAYLOAD))
            .andExpect(status().isCreated())
            .andReturn();

        var location = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);

        mockMVC
            .perform(get(location))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Desmodur"))
            .andExpect(jsonPath("$.description").value(is(nullValue())))
            .andExpect(jsonPath("$.price").value(10.23))
            .andExpect(jsonPath("$.currency").value("EUR"))
            .andExpect(jsonPath("$.category").value("PCS"));
    }

    @Test
    public void shouldRetrieveProductWithDescription() throws Exception {
        var mvcResult = mockMVC
            .perform(post(PRODUCT_URL).content(CREATE_PRODUCT_WITH_DESCRIPTION_PAYLOAD))
            .andExpect(status().isCreated())
            .andReturn();

        var location = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);

        mockMVC
            .perform(get(location))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Desmodur"))
            .andExpect(jsonPath("$.description").value("Desmodur Description"))
            .andExpect(jsonPath("$.price").value(35.67))
            .andExpect(jsonPath("$.currency").value("EUR"))
            .andExpect(jsonPath("$.category").value("PCS"));
    }

    @Test
    public void shouldUpdateProduct() throws Exception {
        var mvcResult = mockMVC
            .perform(post(PRODUCT_URL).content(CREATE_PRODUCT_PAYLOAD))
            .andExpect(status().isCreated())
            .andReturn();

        var location = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);

        mockMVC
            .perform(put(location).content(UPDATE_PRODUCT_PAYLOAD))
            .andExpect(status().isNoContent());

        mockMVC
            .perform(get(location))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("MX50"))
            .andExpect(jsonPath("$.description").value(is(nullValue())))
            .andExpect(jsonPath("$.price").value(5.55))
            .andExpect(jsonPath("$.currency").value("USD"))
            .andExpect(jsonPath("$.category").value("CAS"));
    }

    @Test
    public void shouldUpdateProductWithDescription() throws Exception {
        var mvcResult = mockMVC
            .perform(post(PRODUCT_URL).content(CREATE_PRODUCT_WITH_DESCRIPTION_PAYLOAD))
            .andExpect(status().isCreated())
            .andReturn();

        var location = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);

        mockMVC
            .perform(put(location).content(UPDATE_PRODUCT_WITH_DESCRIPTION_PAYLOAD))
            .andExpect(status().isNoContent());

        mockMVC
            .perform(get(location))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("MX50"))
            .andExpect(jsonPath("$.description").value("MX50 Description"))
            .andExpect(jsonPath("$.price").value(5.55))
            .andExpect(jsonPath("$.currency").value("USD"))
            .andExpect(jsonPath("$.category").value("CAS"));
    }

    @Test
    public void shouldPartiallyUpdateProduct() throws Exception {
        var mvcResult = mockMVC
            .perform(post(PRODUCT_URL).content(CREATE_PRODUCT_PAYLOAD))
            .andExpect(status().isCreated())
            .andReturn();

        var location = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);

        mockMVC
            .perform(patch(location).content("{\"price\": 12.65}"))
            .andExpect(status().isNoContent());

        mockMVC
            .perform(get(location))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Desmodur"))
            .andExpect(jsonPath("$.description").value(is(nullValue())))
            .andExpect(jsonPath("$.price").value(12.65))
            .andExpect(jsonPath("$.currency").value("EUR"))
            .andExpect(jsonPath("$.category").value("PCS"));
    }

    @Test
    public void shouldUpdateWithDescriptionProduct() throws Exception {
        var mvcResult = mockMVC
            .perform(post(PRODUCT_URL).content(CREATE_PRODUCT_WITH_DESCRIPTION_PAYLOAD))
            .andExpect(status().isCreated())
            .andReturn();

        var location = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);

        mockMVC
            .perform(patch(location).content("{\"description\": \"Desmodur Description\"}"))
            .andExpect(status().isNoContent());

        mockMVC
            .perform(get(location))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Desmodur"))
            .andExpect(jsonPath("$.description").value("Desmodur Description"))
            .andExpect(jsonPath("$.price").value(35.67))
            .andExpect(jsonPath("$.currency").value("EUR"))
            .andExpect(jsonPath("$.category").value("PCS"));
    }

    @Test
    public void shouldDeleteProduct() throws Exception {
        var mvcResult = mockMVC.perform(
            post(PRODUCT_URL).content(CREATE_PRODUCT_PAYLOAD))
            .andExpect(status().isCreated())
            .andReturn();

        var location = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);
        mockMVC.perform(delete(location)).andExpect(status().isNoContent());

        mockMVC.perform(get(location)).andExpect(status().isNotFound());
    }

}
