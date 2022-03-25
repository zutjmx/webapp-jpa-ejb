package org.zutjmx.apiservlet.webapp.headers.controllers;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.zutjmx.apiservlet.webapp.headers.configs.ProductoServicePrincipal;
import org.zutjmx.apiservlet.webapp.headers.models.entities.Categoria;
import org.zutjmx.apiservlet.webapp.headers.models.entities.Producto;
import org.zutjmx.apiservlet.webapp.headers.services.ProductoService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet("/productos/form")
public class ProductoFormServlet extends HttpServlet {

    @Inject
    @ProductoServicePrincipal
    private ProductoService productoService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long id;
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            id = 0L;
        }
        Producto producto = new Producto();
        producto.setCategoria(new Categoria());
        if (id > 0) {
            Optional<Producto> optionalProducto = productoService.porId(id);
            if (optionalProducto.isPresent()) {
                producto = optionalProducto.get();
            }
        }

        req.setAttribute("categorias",productoService.listarCategorias());
        req.setAttribute("producto",producto);
        req.setAttribute("titulo",req.getAttribute("titulo") + ": Formulario Productos");
        getServletContext().getRequestDispatcher("/form-producto.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /*Connection connection = (Connection) req.getAttribute("connection");
        ProductoService productoService = new ProductoServiceJdbcImpl(connection);*/

        String nombre = req.getParameter("nombre");
        String sku = req.getParameter("sku");
        String fechaCadena = req.getParameter("fecha_registro");

        Integer precio;
        try {
            precio = Integer.valueOf(req.getParameter("precio"));
        } catch (NumberFormatException e) {
            precio = 0;
        }

        Long categoriaId;
        try {
            categoriaId = Long.valueOf(req.getParameter("categoria"));
        } catch (NumberFormatException e) {
            categoriaId = 0L;
        }

        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaCadena, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            fecha = null;
        }

        Long id;
        try {
            id = Long.valueOf(req.getParameter("id"));
        } catch (NumberFormatException e) {
            id = null;
        }

        Map<String, String> errores = new HashMap<>();
        if (nombre == null || nombre.isBlank()) {
            errores.put("nombre","El nombre es requerido");
        }
        if (sku == null || sku.isBlank()) {
            errores.put("sku","El sku es requerido");
        }
        if (fechaCadena == null || fechaCadena.isBlank()) {
            errores.put("fecha_registro","La fecha es requerida");
        }
        if (precio.equals(0)) {
            errores.put("precio","El precio es requerido");
        }
        if (categoriaId.equals(0L)) {
            errores.put("categoria","La categoria es requerida");
        }

        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setSku(sku);
        producto.setFechaRegistro(fecha);

        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);
        producto.setCategoria(categoria);

        if (errores.isEmpty()) {
            productoService.guardar(producto);
            resp.sendRedirect(req.getContextPath() + "/productos");
        } else {
            req.setAttribute("errores",errores);
            req.setAttribute("categorias",productoService.listarCategorias());
            req.setAttribute("producto",producto);
            req.setAttribute("titulo",req.getAttribute("titulo") + ": Formulario Productos");
            getServletContext().getRequestDispatcher("/form-producto.jsp").forward(req,resp);
        }
    }
}
