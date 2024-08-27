package technikal.task.fishmarket.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import technikal.task.fishmarket.config.SecurityConfig;
import technikal.task.fishmarket.controllers.FishController;
import technikal.task.fishmarket.exception.FilterExceptionHandler;
import technikal.task.fishmarket.imports.ClassesImports;
import technikal.task.fishmarket.services.FishService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Nikolay Boyko
 */

@ClassesImports
@AutoConfigureMockMvc
@WebMvcTest(FishController.class)
public class ControllerTest {


    @Autowired
    private MockMvc mvc;

    @Autowired
    private FilterExceptionHandler filterExceptionHandler;

    @Test
    @WithAnonymousUser
    public void testGetMethod_nonAuthorized() throws Exception { //should pass since auth for this method is disabled
        mvc.perform(get("/fish"))
                .andExpect(status().isOk());
    }
}
