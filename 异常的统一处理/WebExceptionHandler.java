

import com.alibaba.dubbo.rpc.RpcException;
import com.crn.point.exchange.domain.ErrorCode;
import com.crn.point.exchange.domain.ResponseWrapper;
import com.crn.point.exchange.exception.PointRestException;
import com.crn.point.exchange.exception.PointServiceException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * 捕获并处理Controller所有可能出现的且未被Controller方法内部消化的异常。
 */
@ControllerAdvice
public class WebExceptionHandler {

	protected final Logger logger = LogManager.getLogger(getClass());
	private final boolean isLoggerDebugEnabled = logger.isDebugEnabled();

	/**
	 * 捕获并处理Controller所有可能出现的且未被Controller方法内部消化的异常。
	 * @return String
	 */
	@ExceptionHandler(Throwable.class)
//	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	protected ResponseWrapper<Object> handleControllerException(Throwable ex) {
		return handleExceptionInternal(ex);
	}

	/**
	 * @param exception 抛出的异常
	 * @return 将异常转化为最终可以响应给REST客户端的JSON结果。
	 */
	private ResponseWrapper<Object> handleExceptionInternal(Throwable exception) {
		/**
		 * 通过拦截的异常信息转换成的输出对象
		 */
		ResponseWrapper<Object> outputDTO = new ResponseWrapper<>();
        logger.debug("捕获到的异常, 转为JSON", exception);

		if (exception instanceof HttpRequestMethodNotSupportedException) {
			HttpRequestMethodNotSupportedException e = (HttpRequestMethodNotSupportedException) exception;
			logger.debug("错误的HTTP请求方式：{}，错误信息：{}", e.getMethod(), e.getMessage());
			outputDTO.setCode(ErrorCode.ERR_ILLEGAL_HTTP_REQUEST.getCode());
			outputDTO.setMsg("错误的HTTP请求方式：" + e.getMethod());
		} else if (exception instanceof HttpMessageNotReadableException) {
			logger.debug("请求数据解析出现错误: {}", exception.getMessage(), exception);
			HttpMessageNotReadableException e = (HttpMessageNotReadableException) exception;
			if (null != e.getCause() && e.getCause() instanceof UnrecognizedPropertyException) {
				UnrecognizedPropertyException upEx = (UnrecognizedPropertyException) e.getCause();
				outputDTO.setCode(ErrorCode.ERR_REST_INPUT_VALIDATION_REJECTED.getCode());
				outputDTO.setMsg("请求中包含未能识别的参数名" + upEx.getPropertyName());
			} else if (null != e.getCause() && e.getCause() instanceof InvalidFormatException) {
				InvalidFormatException ife = (InvalidFormatException) e.getCause();
				String fieldName = null;
				List<Reference> refList = ife.getPath();
				if (null != refList && refList.size() > 0) {
					Reference ref = refList.get(0);
					fieldName = ref.getFieldName();
				}
				outputDTO.setCode(ErrorCode.ERR_REST_INPUT_VALIDATION_REJECTED.getCode());
				if (null != fieldName) {
					outputDTO.setMsg("参数" + fieldName + "的输入范围有误：" + ife.getValue());
				} else {
					outputDTO.setMsg("参数值输入范围有误：" + ife.getValue());
				}
			} else {
				outputDTO.setCode(ErrorCode.ERR_REST_INPUT_VALIDATION_REJECTED.getCode());
				outputDTO.setMsg("请求内容无法解析，请检查请求数据的格式");
			}
		} else if (exception instanceof HttpMessageConversionException) {
			outputDTO.setCode(ErrorCode.ERR_REST_INPUT_VALIDATION_REJECTED.getCode());
			outputDTO.setMsg("部分参数格式有误导致类型转换失败");
		} else if (exception instanceof HttpMediaTypeException) {
			outputDTO.setCode(ErrorCode.ERR_ILLEGAL_HTTP_REQUEST.getCode());
			outputDTO.setMsg("请求Content-Type有误");
		} else if (exception instanceof TypeMismatchException) {
			outputDTO.setCode(ErrorCode.ERR_REST_INPUT_VALIDATION_REJECTED.getCode());
			outputDTO.setMsg("请求参数数据类型有误");
		} else if (exception instanceof MissingServletRequestParameterException) {
			outputDTO.setCode(ErrorCode.ERR_REST_INPUT_VALIDATION_REJECTED.getCode());
			outputDTO.setMsg("缺少必需的请求参数");
		} else if (exception instanceof BindException) {
			outputDTO.setCode(ErrorCode.ERR_REST_INPUT_VALIDATION_REJECTED.getCode());
			outputDTO.setMsg("请求数据格式有误，数据解析失败");
		} else if (exception instanceof RpcException) {
			RpcException e = (RpcException) exception;
			if (e.isNetwork()) {
				logger.error("Dubbo调用网络异常: {}", e.getMessage(), e);
				outputDTO.setCode(ErrorCode.ERR_DUBBO_SERVICE_NETWORK_EXCEPTION.getCode());
				outputDTO.setMsg(ErrorCode.ERR_DUBBO_SERVICE_NETWORK_EXCEPTION.getMsg());
			} else if (e.isTimeout()) {
				logger.error("Dubbo调用网络超时: {}", e.getMessage(), e);
				outputDTO.setCode(ErrorCode.ERR_DUBBO_SERVICE_WAIT_HANDLING_TIMEOUT.getCode());
				outputDTO.setMsg(ErrorCode.ERR_DUBBO_SERVICE_WAIT_HANDLING_TIMEOUT.getMsg());
			} else if (e.isForbidded()) {
				logger.error("Dubbo调用失败，目标服务不存在或不可用，请检查调用的服务名称及服务版本号是否一致: {}", e.getMessage(), e);
				outputDTO.setCode(ErrorCode.ERR_DUBBO_SERVICE_FORBIDDEN_EXCEPTION.getCode());
				outputDTO.setMsg(ErrorCode.ERR_DUBBO_SERVICE_FORBIDDEN_EXCEPTION.getMsg());
			} if(ConstraintViolationException.class.isInstance(e.getCause())){
				ConstraintViolationException cve = (ConstraintViolationException)e.getCause();
				Set<ConstraintViolation<?>> violations = cve.getConstraintViolations();
				StringBuilder sb = new StringBuilder();
				for(@SuppressWarnings("rawtypes") ConstraintViolation violation : violations){
					sb.append(violation.getPropertyPath())
					.append(violation.getMessageTemplate())
					.append(",");
				}
				outputDTO.setCode(ErrorCode.ERR_REST_INPUT_VALIDATION_REJECTED.getCode());
				outputDTO.setMsg(sb.toString());
			} else {
				logger.error("Dubbo调用环节异常: {}", exception);
				outputDTO.setCode(ErrorCode.ERR_DUBBO_SERVICE_UNKNOWN_EXCEPTION.getCode());
				outputDTO.setMsg(ErrorCode.ERR_DUBBO_SERVICE_UNKNOWN_EXCEPTION.getMsg());
			}
		} else if (exception instanceof MethodArgumentNotValidException) {
			logger.debug("Controller层捕获到异常，异常信息: {}", exception.getMessage(), exception);
			outputDTO.setCode(ErrorCode.ERR_REST_INPUT_VALIDATION_REJECTED.getCode());
			outputDTO.setMsg(ErrorCode.ERR_REST_INPUT_VALIDATION_REJECTED.getMsg());
		}else if(exception instanceof PointServiceException){
			logger.debug("Controller层捕获到异常，异常信息: {}", exception.getMessage(), exception);
			outputDTO.setCode(((PointServiceException) exception).getCode());
			outputDTO.setMsg(((PointServiceException) exception).getMsg());
		}else if(exception instanceof PointRestException){
			logger.debug("Controller层捕获到异常，异常信息: {}", exception.getMessage(), exception);
			outputDTO.setCode(((PointRestException) exception).getCode());
			outputDTO.setMsg(((PointRestException) exception).getMsg());
		} else if (exception instanceof RuntimeException) {
			logger.debug("Controller层捕获到异常，异常信息: {}", exception.getMessage(), exception);
			outputDTO.setMsg(ErrorCode.ERR_UNKNOWN_ERROR.getMsg());
			outputDTO.setCode(ErrorCode.ERR_UNKNOWN_ERROR.getCode());
		} else {
			// 其他未知异常
			logger.error("REST层捕获到未知异常，异常信息: {}", exception);
			outputDTO.setCode(ErrorCode.ERR_DUBBO_SERVICE_UNKNOWN_EXCEPTION.getCode());
			outputDTO.setMsg(ErrorCode.ERR_DUBBO_SERVICE_UNKNOWN_EXCEPTION.getMsg());
		}
		return outputDTO;
	}

}
