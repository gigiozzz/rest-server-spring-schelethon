package com.gigiozzz.framework.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.util.WebUtils;

public class GlobalRestHandlerExceptionResolver extends AbstractHandlerExceptionResolver implements InitializingBean {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private HttpMessageConverter<?>[] messageConverters = null;

	private List<HttpMessageConverter<?>> allMessageConverters = null;

	public void setMessageConverters(HttpMessageConverter<?>[] messageConverters) {
		this.messageConverters = messageConverters;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ensureMessageConverters();
	}

	@SuppressWarnings("unchecked")
	private void ensureMessageConverters() {

		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();

		// user configured values take precedence:
		if (this.messageConverters != null && this.messageConverters.length > 0) {
			converters.addAll(CollectionUtils.arrayToList(this.messageConverters));
		}

		new HttpMessageConverterHelper().addDefaults(converters);

		this.allMessageConverters = converters;
	}

	//Inner class
	//devo estendere perkè metodo protetto cazzo
    private static final class HttpMessageConverterHelper extends WebMvcConfigurationSupport {
        public void addDefaults(List<HttpMessageConverter<?>> converters) {
            addDefaultHttpMessageConverters(converters);
        }
    }
	
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,HttpServletResponse response, Object handler, Exception ex) {

        ModelAndView mav = null;
        ServletWebRequest webRequest = new ServletWebRequest(request, response);

        try {
        	RestError body = handleResponseMsgByException(ex);
        	setStatus(webRequest,body.getStatus());
        	mav = handleResponseBody(body,webRequest);
        } catch (Exception invocationEx) {
            logger.error("Handling  Exception [" + ex + "] resulted in an exception.", invocationEx);
        }

        return mav;
    }
	
	private RestError handleResponseMsgByException(Exception ex){
		if (ex instanceof HttpMediaTypeNotSupportedException) {
			return handleHttpMediaTypeNotSupportedException((HttpMediaTypeNotSupportedException)ex);
		}
		if (ex instanceof HttpMessageNotReadableException) {
			return handleHttpMessageNotReadableException((HttpMessageNotReadableException)ex);
		}
		if (ex instanceof MethodArgumentNotValidException) {
			return handleMethodArgumentNotValidException((MethodArgumentNotValidException)ex);
		}
		if (ex instanceof DataRetrievalFailureException) {
			return handleDataRetrievalFailureException((DataRetrievalFailureException)ex);
		}
		if (ex instanceof Exception) {
			return handleException((Exception)ex);
		}
		
		return null;
	}

	private void setStatus(ServletWebRequest webRequest,HttpStatus status){
		if (!WebUtils.isIncludeRequest(webRequest.getRequest())) {
            webRequest.getResponse().setStatus(status.value());
        }
	}
	
    private ModelAndView handleResponseBody(Object body, ServletWebRequest webRequest) throws ServletException, IOException {

        HttpInputMessage inputMessage = new ServletServerHttpRequest(webRequest.getRequest());

        List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
        if (acceptedMediaTypes.isEmpty()) {
            acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
        }

        MediaType.sortByQualityValue(acceptedMediaTypes);

        HttpOutputMessage outputMessage = new ServletServerHttpResponse(webRequest.getResponse());

        Class<?> bodyType = body.getClass();

        List<HttpMessageConverter<?>> converters = this.allMessageConverters;

        if (converters != null) {
            for (MediaType acceptedMediaType : acceptedMediaTypes) {
                for (HttpMessageConverter messageConverter : converters) {
                    if (messageConverter.canWrite(bodyType, acceptedMediaType)) {
                        messageConverter.write(body, acceptedMediaType, outputMessage);
                        // come diavolo fa questa cosa a funzionare ?
                        // Spring sa che ho ritornato la render ma come se la passo vuota ?
                        return new ModelAndView();
                    }
                }
            }
        }

        logger.warn("Could not find HttpMessageConverter that supports return type [" + bodyType +"] and " + acceptedMediaTypes);

        return null;
    }

    
    private RestError handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex){
    	List<ErrorMsg> errorsMsg= new ArrayList<ErrorMsg>();
    	errorsMsg.add(new ErrorMsg(null,"Unsupported content type: " + ex.getContentType()) );
    	errorsMsg.add(new ErrorMsg(null,"Supported content types: " + MediaType.toString(ex.getSupportedMediaTypes())) );
    	
    	return new RestError(HttpStatus.UNSUPPORTED_MEDIA_TYPE,HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),ex.getMessage(),errorsMsg);
    }

    
    private RestError handleHttpMessageNotReadableException(HttpMessageNotReadableException ex){
    	
    	return new RestError(HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value(),ex.getMessage());
    }
    
    private RestError handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
    	List<ErrorMsg> errorsMsg= new ArrayList<ErrorMsg>();
    	
    	List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
        
        for (FieldError fieldError : fieldErrors) {
        	errorsMsg.add(new ErrorMsg(fieldError.getField(),fieldError.getDefaultMessage()) );
        }
        for (ObjectError objectError : globalErrors) {
        	errorsMsg.add(new ErrorMsg(objectError.getObjectName(),objectError.getDefaultMessage()) );
        }
        /*
         * prima era campo, errore come mai? se lo aspetta jquerymobile?
         * 
         *         for (FieldError fieldError : fieldErrors) {
            error = fieldError.getField() + ", " + fieldError.getDefaultMessage();
            errors.add(error);
        }
        for (ObjectError objectError : globalErrors) {
            error = objectError.getObjectName() + ", " + objectError.getDefaultMessage();
            errors.add(error);
        }

         * 
         * */
		return new RestError(HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value(),ex.getMessage(),errorsMsg);
    }

    private RestError handleDataRetrievalFailureException(DataRetrievalFailureException ex){
    	return new RestError(HttpStatus.NOT_FOUND,HttpStatus.NOT_FOUND.value(),ex.getMessage());
    }

    private RestError handleException(Exception ex){
		return new RestError(HttpStatus.INTERNAL_SERVER_ERROR,HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage());
    }

    /*
    * inizio a raccogliere classi -- errori http trovati su internet
    * va testato a modo
    * okkio alcune nn devono essere classi ma stringhe ... nell'ottica framework nostro  
    * 
    // 400
    HttpMessageNotReadableException.class HttpStatus.BAD_REQUEST
    MissingServletRequestParameterException.class, HttpStatus.BAD_REQUEST
    TypeMismatchException.class, HttpStatus.BAD_REQUEST
    "javax.validation.ValidationException", HttpStatus.BAD_REQUEST
    // 404
    NoSuchRequestHandlingMethodException.class, HttpStatus.NOT_FOUND
    "org.hibernate.ObjectNotFoundException", HttpStatus.NOT_FOUND
    // 405
    HttpRequestMethodNotSupportedException.class, HttpStatus.METHOD_NOT_ALLOWED
    // 406
    HttpMediaTypeNotAcceptableException.class, HttpStatus.NOT_ACCEPTABLE
    // 409
    "org.springframework.dao.DataIntegrityViolationException", HttpStatus.CONFLICT
    // 415
    HttpMediaTypeNotSupportedException.class, HttpStatus.UNSUPPORTED_MEDIA_TYPE
    *
    *
    */
    
}
