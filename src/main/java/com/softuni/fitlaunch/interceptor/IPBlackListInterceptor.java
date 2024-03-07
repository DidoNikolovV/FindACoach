package com.softuni.fitlaunch.interceptor;

import com.softuni.fitlaunch.service.BlackListService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.View;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import java.util.Locale;
import java.util.Map;


@Component
public class IPBlackListInterceptor implements HandlerInterceptor {

    private final BlackListService blackListService;
    private final ThymeleafViewResolver thymeleafViewResolver;

    public IPBlackListInterceptor(BlackListService blackListService, ThymeleafViewResolver thymeleafViewResolver) {
        this.blackListService = blackListService;
        this.thymeleafViewResolver = thymeleafViewResolver;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = getIpAddressFromRequest(request);
        if(blackListService.isBanned(ip)) {
            View bannedView = thymeleafViewResolver.resolveViewName("banned", Locale.getDefault());
            if(bannedView != null) {
                bannedView.render(Map.of(), request, response);
            }
            return false;
        }

        return true;
    }


    private String getIpAddressFromRequest(HttpServletRequest request) {

        String ipAddress = null;

        String xffHeader = request.getHeader("X-Forwarded-For");
        if(xffHeader != null && !xffHeader.equals("unknown")) {
            int commaIndex = xffHeader.indexOf(",");
            if(commaIndex > 0) {
                ipAddress = xffHeader.substring(0, commaIndex - 1);
            }
        }

        if(ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress;

    }
}
