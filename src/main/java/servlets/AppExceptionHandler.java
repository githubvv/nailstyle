package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AppExceptionHandler extends HttpServlet {
    private static final long serialVersionUID = 1L;
    String path = "";


    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        processError(request, response);
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        processError(request, response);
    }

    private void processError(HttpServletRequest request,
                              HttpServletResponse response) throws IOException {
        Throwable throwable = (Throwable) request
                .getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");

        path = request.getContextPath() + "/errorpages/error.xhtml";

        if (throwable != null) {
            if (throwable instanceof javax.faces.application.ViewExpiredException) {
                path = path + "?statusCode=expired";
            } else {
                path = path + "?statusCode=unknown";
            }
        } else {
            if (statusCode.intValue() == 400)
                path = path + "?statusCode=400";
            else if (statusCode.intValue() == 500)
                path = path + "?statusCode=500";
            else if (statusCode.intValue() == 403)
                path = path + "?statusCode=403";
            else
                path = path + "?statusCode=unknown";
        }

    response.sendRedirect(path);
}
}