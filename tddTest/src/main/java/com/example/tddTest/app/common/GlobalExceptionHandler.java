package com.example.tddTest.app.common;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.tddTest.app.enums.MembershipErrorResult;
import com.example.tddTest.exception.MembershipException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/*
 * @RestControllerAdvice란?
 * @ExceptionHandler, @ModelAttribute, @InitBinder 가 적용된 메서드들을 
 * AOP를 적용해 컨트롤러 단에 적용하기 위해 고안된 애너테이션
 * 모든 컨트롤러에서 해당 Exception이 발생하였을 때 전역적으로 이를 잡고 처리
 */
@Slf4j
@RestControllerAdvice	
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

	/*
	 * [ handleMethodArgumentNotValid ]
	 * @Valid에서 예외가 발생할 경우에 처리하게 되는데, 발생한 에러들을 갖고 있는 MethodArgumentNotValidException에서 에러 메세지들을 얻어, 이를 메세지로 반환하도록 하고 있다.
	 * 만약 에러가 발생할 경우 다음과 같은 포맷으로 에러 메세지가 반환되는 것이다
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

		final List<String> errorList = ex.getBindingResult()
				.getAllErrors()
				.stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage)
				.collect(Collectors.toList());
		
		log.warn("Invalid DTO Parameter errors : {}", errorList);
		
		return this.makeErrorResponseEntity(errorList.toString());
	}
	
	private ResponseEntity<Object> makeErrorResponseEntity(final String errorDescription) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), errorDescription));
	}
	
	// 작성한 MembershipException 처리
	@ExceptionHandler({MembershipException.class})
	public ResponseEntity<ErrorResponse> handleRestApiException(final MembershipException exception) {
		log.warn("MembershipException occur: ", exception);
		
		return this.makeErrorResponseEntity(exception.getErrorResult());
	}
	
	// 직접 작성한 MembershipException 외 exception 처리
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
        log.warn("Exception occur: ", exception);
        return this.makeErrorResponseEntity(MembershipErrorResult.UNKNOWN_EXCEPTION);
    }
	
    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final MembershipErrorResult errorResult) {
        return ResponseEntity.status(errorResult.getHttpStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
    }
	
    @Getter
    @RequiredArgsConstructor
    static class ErrorResponse {
        private final String code;
        private final String message;
    }
}
