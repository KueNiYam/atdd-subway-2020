package wooteco.subway.maps.map.documentation;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.web.context.WebApplicationContext;

import wooteco.subway.common.documentation.Documentation;
import wooteco.subway.maps.map.application.MapService;
import wooteco.subway.maps.map.dto.PathResponse;
import wooteco.subway.maps.map.ui.MapController;
import wooteco.subway.maps.station.dto.StationResponse;

@WebMvcTest(controllers = {MapController.class})
public class PathDocumentation extends Documentation {
	@Autowired
	private MapController mapController;
	@MockBean
	private MapService pathService;

	@BeforeEach
	public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
		super.setUp(context, restDocumentation);
	}

	@Test
	void getPath() {
		List<StationResponse> stationResponses = Arrays.asList(new StationResponse(1L, "잠실역"),
			new StationResponse(2L, "잠실나루역"));
		when(pathService.findPath(anyLong(), anyLong(), any(), any())).thenReturn(
			new PathResponse(stationResponses, 3, 3, 1250));

		given().log().all().
			when().
			get("/paths?source={source}&target={target}&type={type}", 1L, 2L, "DISTANCE").
			then().log().all().
			apply(getPathDocument()).extract();
	}

	private RestDocumentationResultHandler getPathDocument() {
		return document("paths/get",
			requestParameters(
				parameterWithName("source").description("시작 역 id"),
				parameterWithName("target").description("도착 역 id"),
				parameterWithName("type").description("경로 타입")
			),
			responseFields(
				fieldWithPath("stations").type(JsonFieldType.ARRAY).description("시작 역부터 도착 역 경로"),
				fieldWithPath("duration").type(JsonFieldType.NUMBER).description("시작 역부터 도착 역까지 가는 시간"),
				fieldWithPath("distance").type(JsonFieldType.NUMBER).description("시작 역부터 도착 역까지 거리"),
				fieldWithPath("fare").type(JsonFieldType.NUMBER).description("시작 역부터 도착 역까지 예상 요금"),
				fieldWithPath("stations.[].id").type(JsonFieldType.NUMBER).description("지하철 역의 아이디"),
				fieldWithPath("stations.[].name").type(JsonFieldType.STRING).description("지하철 역의 이름")
			)
		);
	}
}
