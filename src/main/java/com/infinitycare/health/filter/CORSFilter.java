package com.infinitycare.health.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CORSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ((HttpServletResponse)response).addHeader("Access-Control-Allow-Methods", "*");
        ((HttpServletResponse)response).addHeader("Access-Control-Allow-Credentials", "true");
        //((HttpServletResponse)response).addHeader("Access-Control-Expose-Headers", "");
        //((HttpServletResponse)response).addHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        ((HttpServletResponse)response).addHeader("Access-Control-Allow-Origin", "https://infinity-care.herokuapp.com");

        ((HttpServletResponse)response).addHeader("Access-Control-Allow-Headers", "*");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
