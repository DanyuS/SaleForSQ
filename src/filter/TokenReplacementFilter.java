package filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class TokenReplacementFilter
 */


@WebFilter(urlPatterns="/*", filterName="TokenReplacementFilter",
		initParams={@WebInitParam( name  ="token.name", value="version"),
		@WebInitParam(name="token.value",value="3.0")
		}
)

public class TokenReplacementFilter implements Filter {
	private String replToken, replValue;

    /**
     * Default constructor. 
     */
    public TokenReplacementFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		BufferedResponse resWrapper = new BufferedResponse((HttpServletResponse)response);
		chain.doFilter(request, resWrapper);
	//	System.out.println("Token filter "+resWrapper.getOutputType()+BufferedResponse.OT_WRITER);
		if (resWrapper.getOutputType() == BufferedResponse.OT_WRITER) {
			String resBody = new String(resWrapper.toByteArray(),resWrapper.getCharacterEncoding());
			if (resWrapper.getContentType().equals("text/html;charset=utf-8")) {
			//对字节数组进行处理：用replValue替换@replToken@
				resBody = resBody.replaceAll("@" + replToken + "@", replValue);
			//调整响应的content-Length
				response.setContentLength(resBody.length());
			}
			PrintWriter writer = response.getWriter();
			writer.println(resBody);//将数组写入实际响应对象中
		} else if (resWrapper.getOutputType() == BufferedResponse.OT_OUTPUT_STREAM) {
			//不替换
			ServletOutputStream out = response.getOutputStream();
			out.write(resWrapper.toByteArray());
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		replToken = fConfig.getInitParameter("token.name");
		replValue = fConfig.getInitParameter("token.value");
		if (null == replToken) {
			throw new ServletException("TokenReplacementFilter named " + fConfig.getFilterName() +" missing token.name init parameter.");
		}
		if (null == replValue) {
			throw new ServletException("TokenReplacementFilter named " + fConfig.getFilterName() +" missing token.value init parameter.");
		}
	}

}
