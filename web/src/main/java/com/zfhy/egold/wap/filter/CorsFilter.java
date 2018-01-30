package com.zfhy.egold.wap.filter;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Order(1)
@WebFilter(filterName = "crosFilter", urlPatterns = "/*")
public class CorsFilter implements Filter {

    @Value("${CROS_ALLOWORIGIN}")
    private String allowOrigin;

    @Value("${CROS_ALLOWMETHODS}")
    private String allowMethods;

    @Value("${CROS_ALLOWCREDENTIALS}")
    private String allowCredentials;

    @Value("${CROS_ALLOWHEADERS}")
    private String allowHeaders;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {


    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (StringUtils.isNotEmpty(this.allowOrigin)) {
            if (Objects.equals("*", this.allowOrigin)) {
                
                response.setHeader("Access-Control-Allow-Origin", "*");
            } else {
                
                List<String> allowOriginList = Arrays.asList(allowOrigin.split(","));
                if (CollectionUtils.isNotEmpty(allowOriginList)) {
                    String currentOrigin = request.getHeader("Origin");
                    if (allowOriginList.contains(currentOrigin)) {
                        
                        response.setHeader("Access-Control-Allow-Origin", currentOrigin);
                    }
                }
            }

        }

        if (StringUtils.isNotEmpty(allowMethods)) {

            
            response.setHeader("Access-Control-Allow-Methods", allowMethods);
        }
        if (StringUtils.isNotEmpty(allowCredentials)) {
            
            response.setHeader("Access-Control-Allow-Credentials", allowCredentials);
        }
        if (StringUtils.isNotEmpty(allowHeaders)) {
            
            response.setHeader("Access-Control-Allow-Headers", allowHeaders);
        }
        /*if (StringUtil.isNotEmpty(exposeHeaders)) {
            
            response.setHeader("Access-Control-Expose-Headers", exposeHeaders);
        }*/
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
