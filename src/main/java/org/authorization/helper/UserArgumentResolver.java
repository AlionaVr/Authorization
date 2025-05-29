package org.authorization.helper;

import org.authorization.model.User;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == User.class &&
               parameter.hasParameterAnnotation(FromQuery.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        String username = webRequest.getParameter("user");
        String password = webRequest.getParameter("password");

        User user = new User(username, password); // 1. сначала создаём

        if (binderFactory != null) {
            WebDataBinder binder = binderFactory.createBinder(webRequest, user, "user");
            binder.validate(); // 2. потом валидируем
            if (binder.getBindingResult().hasErrors()) {
                throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
            }
        }
        return user;
    }
}
