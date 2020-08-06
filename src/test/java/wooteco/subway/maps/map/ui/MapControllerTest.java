package wooteco.subway.maps.map.ui;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import wooteco.subway.maps.map.application.MapService;
import wooteco.subway.maps.map.dto.MapResponse;
import wooteco.subway.maps.map.dto.PathResponse;

@SpringBootTest
public class MapControllerTest {
	@MockBean
	MapService mapService;

	MockMvc mockMvc;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext) {
		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.addFilter(new CharacterEncodingFilter("UTF-8", true))
			.build();
	}

	@DisplayName("경로 조회")
	@Test
	void getPath() throws Exception {
		given(mapService.findPath(anyLong(), anyLong(), any())).willReturn(new PathResponse());

		mockMvc.perform(get("/paths?source=1&target=2&type=DISTANCE")
			.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
            .andDo(print());
	}

	@Test
	void findMap() {
		MapService mapService = mock(MapService.class);
		MapController controller = new MapController(mapService);

		ResponseEntity<MapResponse> entity = controller.findMap();

		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		verify(mapService).findMap();
	}
}
