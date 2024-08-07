package com.sergeineretin.weatherviewer.controllers;

import com.sergeineretin.weatherviewer.Utils;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import org.thymeleaf.ITemplateEngine;

public class BaseController extends HttpServlet {
    protected ITemplateEngine templateEngine;
    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        this.templateEngine = Utils.buildTemplateEngine(servletContext);
    }
}
