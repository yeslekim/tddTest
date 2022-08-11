package com.example.tddTest.app.membership.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.tddTest.app.common.GlobalExceptionHandler;
import com.example.tddTest.app.enums.MembershipErrorResult;
import com.example.tddTest.app.enums.MembershipType;
import com.example.tddTest.app.membership.service.MembershipConstants;
import com.example.tddTest.app.membership.service.MembershipRequest;
import com.example.tddTest.app.membership.service.MembershipResponse;
import com.example.tddTest.app.membership.service.MembershipService;
import com.example.tddTest.exception.MembershipException;
import com.google.gson.Gson;

@ExtendWith(MockitoExtension.class)
public class MembershipControllerTest {

	@InjectMocks
	private MembershipController target;
	
	private MockMvc mockMvc;
	private Gson gson;
	
	@Mock
	private MembershipService membershipService;
	
	@BeforeEach
	public void init() {
		gson = new Gson();
		mockMvc = MockMvcBuilders.standaloneSetup(target)
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}
	
//	@Test
//	public void mockMvc가Null이아님() throws Exception {
//		assertThat(target).isNotNull();
//		assertThat(mockMvc).isNotNull();
//	}
	
	@Test
	public void 멤버십등록실패_사용자식별값이헤더에없음() throws Exception {
		
		// given
		final String url = "/api/v1/memberships";
		
		// when
		final ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.post(url)
				.content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
				.contentType(MediaType.APPLICATION_JSON)
				);
		
		// then
		resultActions.andExpect(status().isBadRequest());
	}
	
	@Test
	public void 멤버십등록실패_포인트가null() throws Exception {
		
		// given
		final String url = "/api/v1/memberships";
		
		// when
		final ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.post(url)
				.header(MembershipConstants.USER_ID_HEADER, "12345")
				.content(gson.toJson(membershipRequest(null, MembershipType.NAVER)))
				.contentType(MediaType.APPLICATION_JSON)
				);
		
		// then
		resultActions.andExpect(status().isBadRequest());
	}
	
	@Test
	public void 멤버십등록실패_포인트가음수() throws Exception {
		
		// given
		final String url = "/api/v1/memberships";
		
		// when
		final ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.post(url)
				.header(MembershipConstants.USER_ID_HEADER, "12345")
				.content(gson.toJson(membershipRequest(-1, MembershipType.NAVER)))
				.contentType(MediaType.APPLICATION_JSON)
				);
		
		// then
		resultActions.andExpect(status().isBadRequest());
	}
	
	@Test
	public void 멤버십등록실패_멤버십종류가Null() throws Exception {
		
		// given
		final String url = "/api/v1/memberships";
		
		// when
		final ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.post(url)
				.header(MembershipConstants.USER_ID_HEADER, "12345")
				.content(gson.toJson(membershipRequest(10000, null)))
				.contentType(MediaType.APPLICATION_JSON)
				);
		
		// then
		resultActions.andExpect(status().isBadRequest());
	}
	
	@Test
	public void 멤버십등록실패_MemberService에서에러Throw() throws Exception {
		
		// given
		final String url = "/api/v1/memberships";
		doThrow(new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER))
			.when(membershipService)
			.addMembership("12345", MembershipType.NAVER, 10000);
		
		// when
		final ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.post(url)
				.header(MembershipConstants.USER_ID_HEADER, "12345")
				.content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
				.contentType(MediaType.APPLICATION_JSON)
				);
		
		// then
		resultActions.andExpect(status().isBadRequest());
	}
	
	@Test
	public void 멤버십등록성공() throws Exception {
		
		// given
		final String url = "/api/v1/memberships";
		final MembershipResponse membershipResponse = MembershipResponse.builder()
				.id(-1L)
				.membershipType(MembershipType.NAVER)
				.build();
		
		doReturn(membershipResponse).when(membershipService).addMembership("12345", MembershipType.NAVER, 10000);
		
		// when
		final ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.post(url)
				.header(MembershipConstants.USER_ID_HEADER, "12345")
				.content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
				.contentType(MediaType.APPLICATION_JSON)
				);
		
		// then
		resultActions.andExpect(status().isCreated());
		
		final MembershipResponse response = gson.fromJson(resultActions.andReturn()
				.getResponse()
				.getContentAsString(StandardCharsets.UTF_8), MembershipResponse.class);
		
		assertThat(response.getMembershipType()).isEqualTo(MembershipType.NAVER);
		assertThat(response.getId()).isNotNull();
		
	}
	
	// 파라미터만 다른 테스트 케이스 작성 시 사용
	@ParameterizedTest
	@MethodSource("invaildMembershipAddParameter")
	public void 멤버십등록실패_잘못된파라미터(final Integer point, final MembershipType membershipType) throws Exception {
		
		// given
		final String url = "/api/v1/memberships";
		
		// when
		final ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.post(url)
				.header(MembershipConstants.USER_ID_HEADER, "12345")
				.content(gson.toJson(membershipRequest(point, membershipType)))
				.contentType(MediaType.APPLICATION_JSON)
				);
				
		
		// then
		resultActions.andExpect(status().isBadRequest());
		
	}
	
    private MembershipRequest membershipRequest(final Integer point, final MembershipType membershipType) {
        return MembershipRequest.builder()
                .point(point)
                .membershipType(membershipType)
                .build();
    }
    
    private static Stream<Arguments> invaildMembershipAddParameter() {
    	
    	return Stream.of(
    			Arguments.of(null, MembershipType.NAVER)
    			, Arguments.of(-1, MembershipType.NAVER)
    			, Arguments.of(1000, null)
    			);
    	
    }
}
