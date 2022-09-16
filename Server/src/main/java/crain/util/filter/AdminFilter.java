package crain.util.filter;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * In Order to Register this filter you'll need the following expression
 * "${admin.port} != null ? ${admin.port} > 0 : false"
 */
@Slf4j
@WebFilter
public class AdminFilter extends OncePerRequestFilter {

    @Value("${admin.port}")
    private Integer adminPort;


    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        if (Objects.isNull(adminPort)) { // In case we register this without an Admin Port, we don't enforce Anything.
            filterChain.doFilter(request, response);
        }
        if (request.getServerPort() != adminPort && request.getServletPath().startsWith("/admin")) {
            if (log.isWarnEnabled()) {
                log.warn("Unexpected Access Attempt By" + request.getRemoteUser());
            }
            throw new ServletException("Invalid User Access Attempt");
        }
        filterChain.doFilter(request, response);
    }
}
