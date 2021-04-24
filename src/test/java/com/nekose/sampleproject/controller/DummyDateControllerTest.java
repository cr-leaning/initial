package com.nekose.sampleproject.controller;

import com.nekose.sampleproject.application.DummyApplication;
import com.nekose.sampleproject.controller.handler.ApiResponseExceptionHandler;
import com.nekose.sampleproject.domain.model.entity.DummyData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class DummyDateControllerTest {
    private static final String BASE_URI = "/v1/dummy";
    private static final String URI_GET = BASE_URI + "/{key}";

    @Mock
    private DummyApplication dummyApplication;
    @InjectMocks
    private DummyDateController target;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(ApiResponseExceptionHandler.class).build();
    }

    @Test
    void get_normal() throws Exception {
        String exceptedKey = "testKey";

        var mockDummyData = DummyData.builder()
                .key(exceptedKey)
                .name("testName")
                .value("testValue")
                .build();
        when(dummyApplication.get(any()))
                .thenReturn(mockDummyData);

        mockMvc.perform(MockMvcRequestBuilders
                .get(URI_GET, exceptedKey))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string("{\"resultInfo\":{" +
                                "\"status\":\"SUCCESS\"," +
                                "\"statusReason\":\"success\"" +
                                "}," +
                                "\"data\":{" +
                                "\"key\":\"testKey\"," +
                                "\"name\":\"testName\"," +
                                "\"value\":\"testValue\"" +
                                "}}")
                ).andReturn();

        var captor = ArgumentCaptor.forClass(String.class);
        verify(dummyApplication, times(1)).get(captor.capture());

        assertEquals(exceptedKey, captor.getValue());
    }

}
